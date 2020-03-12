package co.id.klikacara.product.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.BaseProduct
import co.id.klikacara.`object`.KlikMenu
import co.id.klikacara.`object`.adapter.ProductImageAdapter
import co.id.klikacara.`object`.adapter.TextAdapter
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.presenter.PermissionPresenter
import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.product.presenter.AddProductPresenter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.base_bottom_sheet_list_of_view.*
import kotlinx.android.synthetic.main.fragment_product_add_edit.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import javax.inject.Inject

class AddProductFragment : BaseFragment(), ProductContract.AddProductView, PermissionContract.View {

    @Inject
    lateinit var addProductPresenter: AddProductPresenter
    @Inject
    lateinit var permissionPresenter: PermissionPresenter
    private lateinit var mitra: Mitra
    private var product: BaseProduct? = null
    private val listCategoryAdapter = FastItemAdapter<TextAdapter>()
    private val paymentTypeAdapter = FastItemAdapter<TextAdapter>()
    private val listImageAdapter = FastItemAdapter<ProductImageAdapter>()
    private var cameraPermissionCode = 10
    private val uploadedImage = ArrayList<Int>()
    private var coverFile: File? = null

    private lateinit var sheetBehaviorListOfItem: BottomSheetBehavior<*>
    private lateinit var sheetBehaviorListOfItemCallback: BottomSheetBehavior.BottomSheetCallback
    private val mapImage = HashMap<String, Boolean>()
    private var isEdit = false
    private var isChange = false

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = getInflate(inflater, R.layout.fragment_product_add_edit, container)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (BottomSheetBehavior.STATE_EXPANDED == sheetBehaviorListOfItem.state) {
                    sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_COLLAPSED
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addProductPresenter.bindingCurrency(priceEditText)
        permissionPresenter.init(RxPermissions(this))
        configureBottomSheet()
        configureBackButton()
        listImageAdapter.add(ProductImageAdapter(null, false, true))
        product = Parcels.unwrap(arguments?.getParcelable(BuildConfig.productDb))
        if (product != null) {
            isEdit = true
            showProductDetail()
            addProductPresenter.getProductKategoriById(product!!.productCategory)
        } else {
            product = BaseProduct()
        }
        addProductPresenter.getMitraData()
        addProductPresenter.getPaymentTypeAdapter()
        configureAdapter()
    }

    private fun configureAdapter() {
        configureGridItemAdapter(listImageAdapter, productPictureRecycleView, 3)
        listImageAdapter.withOnClickListener { _, _, _, position ->

            if (position == listImageAdapter.adapterItemCount - 1) {
                if (listImageAdapter.adapterItemCount > 5) {
                    showInfo("Maksimum gambar adalah 5")
                } else {
                    cameraPermissionCode = 10
                    permissionPresenter.getCameraPermission()
                }
            } else {
                showInfoWithCancel("Ingin menghapus gambar ini?", View.OnClickListener {
                    infoDialog.dismissAllowingStateLoss()
                    listImageAdapter.remove(position)
                })
            }
            true
        }
        listCategoryAdapter.withOnClickListener { _, _, item, _ ->
            kategoriEditText.setText(item.klikMenu.name)
            product!!.productCategory = item.klikMenu.key!!
            sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        }
        paymentTypeAdapter.withOnClickListener { _, _, item, _ ->
            paymentTypeEditText.setText(item.paymentType.description)
            product!!.paymentType = item.paymentType
            sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        }
    }

    private fun configureBottomSheet() {
        sheetBehaviorListOfItem = BottomSheetBehavior.from<LinearLayout>(listOfItemBottomSheet)
        sheetBehaviorListOfItemCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        fakeViewBottomSheet.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        fakeViewBottomSheet.visibility = View.GONE
                    }
                    else -> {
                    }
                }
            }
        }
        sheetBehaviorListOfItem.setBottomSheetCallback(sheetBehaviorListOfItemCallback)
    }

    private fun showProductDetail() {
        kategoriEditText.isEnabled = false
        nameEditText.setText(product!!.name)
        descriptionEditText.setText(product!!.description)
        priceEditText.setText(product!!.price.toString())
        paymentTypeEditText.setText(product!!.paymentType!!.description)
        for (url in product!!.url.keys)
            listImageAdapter.add(0, ProductImageAdapter(url, true))
    }

    override fun showProductCategory(category: KlikMenu?) {
        kategoriEditText.setText(category!!.name)
    }

    override fun setMitraData(mitra: Mitra) {
        this.mitra = mitra
    }

    override fun setListProductAdapter(categoryAdapter: ArrayList<TextAdapter>) {
        this.listCategoryAdapter.clear()
        this.listCategoryAdapter.add(categoryAdapter)
    }

    override fun setPaymentTypeAdapter(paymentTypeAdapter: List<TextAdapter>) {
        this.paymentTypeAdapter.clear()
        this.paymentTypeAdapter.add(paymentTypeAdapter)
    }

    override fun doOnPermissionGranted() {
        configureEasyImage()
        EasyImage.openChooserWithGallery(this, "Foto Produk", cameraPermissionCode)
    }

    override fun doOnPermissionRejected() {
        showError("Untuk menambahkan gambar, dibutuhkan perizinan akses kamera")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                if (type == 10) {
                    isChange = true
                    listImageAdapter.add(0, ProductImageAdapter(imageFile?.absolutePath))
                } else {
                    coverFile = imageFile
                    GlideUtils.setFotoWithUrl(activity, imageFile?.absolutePath, coverImageView)
                }
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                showError(e?.message!!)
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    EasyImage.lastlyTakenButCanceledPhoto(activity)?.delete()
                }
            }
        })
    }

    private fun doSubmit() {
        product!!.name = nameEditText.text.toString().trim()
        product!!.description = descriptionEditText.text.toString().trim()
        product!!.price = StringHelper.removeDotFromFormatedValue(priceEditText.text.toString().trim()).toLong()
        product!!.notes = notesEditText.text.toString().trim()
        uploadedImage.clear()
        for ((index, item) in listImageAdapter.adapterItems.withIndex()) {
            if (!item.fromRemote && index != listImageAdapter.adapterItemCount - 1) {
                addProductPresenter.uploadImage(product!!, item.path, index, false)
            } else if (index != listImageAdapter.adapterItemCount - 1) {
                setUploadedImageUrl(item.path, index)
            }
        }
    }

    override fun setCoverUploaded(url: String) {
        product!!.cover = url
        addProductPresenter.submitProduct(product!!)
    }

    override fun setCoverFailedUploaded() {
        showInfo("Gagal mengupload cover, silahkan coba lagi")
    }

    override fun setUploadedImageUrl(url: String, index: Int) {
        mapImage[url] = true
        listImageAdapter.getItem(index).fromRemote = true
        uploadedImage.add(index)
        if (listImageAdapter.adapterItemCount - 1 == mapImage.size) {
            product!!.url = mapImage
            addProductPresenter.uploadImage(product!!, coverFile!!.absolutePath, index, true)
        }
    }

    override fun setFailedImage(index: Int) {
        uploadedImage.add(index)
        if (uploadedImage.size == listImageAdapter.adapterItemCount - 1) {
            showInfo("Beberapa file gagal di upload, ulangi kembali")
        }
    }

    override fun doOnSubmitProductSuccess() {
        showInfo("Produk berhasil ditambahkan", View.OnClickListener {
            activity!!.onBackPressed()
        })
    }

    override fun doOnSubmitProductFailed() {
        showError("Produk gagal ditambahkan")
    }

    @OnClick(R.id.paymentTypeEditText)
    fun onPaymentTypeClicked() {
        titleTextView.text = "Pilih Tipe Pembayaran"
        configureItemAdapter(paymentTypeAdapter, itemRecycleView)
        sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_EXPANDED
    }

    @OnClick(R.id.kategoriEditText)
    fun onKategoriClicked() {
        titleTextView.text = "Pilih Kategori"
        configureItemAdapter(listCategoryAdapter, itemRecycleView)
        sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_EXPANDED
    }

    @OnClick(R.id.coverImageView)
    fun onCoverImageViewClicked() {
        cameraPermissionCode = 11
        permissionPresenter.getCameraPermission()
    }

    @OnClick(R.id.deleteImageButton)
    fun onDeleteButtonClicked() {
        coverImageView.setImageResource(R.drawable.ic_add_image)
        coverFile = null
    }

    @OnClick(R.id.submitButton)
    fun onSubmitButtonClicked() {
        if (StringUtils.isBlank(kategoriEditText.text.toString()))
            showError("Anda belum memilih kategori produk/layanan")
        else if (StringUtils.isBlank(nameEditText.text.toString()))
            showError("Anda belum memasukkan nama produk")
        else if (StringUtils.isBlank(descriptionEditText.text.toString()))
            showError("Anda belum menambahkan deskripsi produk")
        else if (StringUtils.isBlank(priceEditText.text.toString()))
            showError("Anda belum menambahkan harga produk/layanan")
        else if (StringUtils.isBlank(paymentTypeEditText.text.toString()))
            showError("Anda belum memilih tipe pembayaran")
        else if (StringUtils.isBlank(priceEditText.text.toString()))
            showError("Anda belum menambahkan gambar produk/layanan")
        else if (coverFile == null)
            showError("Anda belum menambahkan cover produk")
        else if (isEdit) {
            showInfoWithCancel("Yakin ingin melakukan perubahan?", View.OnClickListener {
                infoDialog.dismissAllowingStateLoss()
                doSubmit()
            })
        } else {
            showInfoWithCancel("Yakin ingin menambahkan produk baru?", View.OnClickListener {
                infoDialog.dismissAllowingStateLoss()
                doSubmit()
            })
        }
    }
}