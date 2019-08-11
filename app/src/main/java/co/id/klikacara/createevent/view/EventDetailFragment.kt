package co.id.klikacara.createevent.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.util.PatternsCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.Order
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.presenter.PermissionPresenter
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_create_event.*
import kotlinx.android.synthetic.main.fragment_order_detail.emailEditText
import kotlinx.android.synthetic.main.fragment_order_detail.namaAcaraEditText
import kotlinx.android.synthetic.main.fragment_order_detail.penanggungJawabEditText
import kotlinx.android.synthetic.main.fragment_order_detail.phoneEditText
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import javax.inject.Inject

class EventDetailFragment : BaseFragment(), PermissionContract.View {

    @Inject
    lateinit var permissionPresenter: PermissionPresenter

    private val order = Order()
    private var eventBannerFile: File? = null
    private val cameraPermissionCode = 10

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_create_event, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionPresenter.init(RxPermissions(this))
    }

    @OnClick(R.id.nextButton)
    fun onNextButtonClicked() {
        if (StringUtils.isBlank(namaAcaraEditText.text.toString()))
            showError("Nama acara tidak boleh kosong")
        else if (StringUtils.isBlank(penanggungJawabEditText.text.toString()))
            showError("Penanggung jawab tidak boleh kosong")
        else if (StringUtils.isBlank(emailEditText.text.toString()))
            showError("Email penanggung jawab tidak boleh kosong")
        else if (!PatternsCompat.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches())
            showError("Email penanggung jawab tidak valid")
        else if (StringUtils.isBlank(phoneEditText.text.toString()))
            showError("Nomor Telepon Penanggung jawab tidak boleh kosong")
        else if (promosiAcaraCheckBox.isChecked && eventBannerFile == null)
            showError("Jika ingin acara Anda dipromoosikan, sebaiknya tambahkan pooster acara Anda")
        else {
            order.name = namaAcaraEditText.text.toString()
            order.penanggungJawab = penanggungJawabEditText.text.toString()
            order.email = emailEditText.text.toString()
            order.phone = phoneEditText.text.toString()
            order.isPromo = promosiAcaraCheckBox.isChecked
            order.isAllowRegister = registerAcaraCheckBox.isChecked
            if (promosiAcaraCheckBox.isChecked)
                order.posterUrl = eventBannerFile!!.absolutePath
            val bundle = Bundle()
            bundle.putParcelable(BuildConfig.orderDb, Parcels.wrap(order))
            navigateTo(
                R.id.orderNavGraph,
                R.id.action_move_fromEventDetailFragment_toOrderAddressAndTimeFragment,
                bundle
            )
        }
    }

    @OnClick(R.id.promosiAcaraCheckBox)
    fun onPromoCheckBoxClicked() {
        if (promosiAcaraCheckBox.isChecked)
            ViewHelper.showView(promosiArea)
        else
            ViewHelper.hideView(promosiArea)
    }

    override fun doOnPermissionGranted() {
        configureEasyImage()
        EasyImage.openChooserWithGallery(this, "Foto Mitra", cameraPermissionCode)
    }

    override fun doOnPermissionRejected() {
        showError("Maaf, untuk menambahkan foto Anda harus mengaktifkan fitur kamera")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                eventBannerFile = imageFile?.let { image -> compressFile(image) }
                GlideUtils.setFotoWithUrl(activity, eventBannerFile?.absolutePath, posterAcaraImageView)
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

    @OnClick(R.id.posterAcaraImageView)
    fun onPosterAcaraImageView() {
        permissionPresenter.getCameraPermission()
    }
}