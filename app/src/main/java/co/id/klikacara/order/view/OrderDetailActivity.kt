package co.id.klikacara.order.view

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import butterknife.BindString
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.`object`.adapter.KeyValueAdapter
import co.id.klikacara.`object`.authentication.Role
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.`object`.constanta.DefaultConstanta
import co.id.klikacara.base.utils.ActionHelper
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.base.utils.timehelper.TimeUtils
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.presenter.OrderDetailPresenter
import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.presenter.PermissionPresenter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.adapter_bank.*
import kotlinx.android.synthetic.main.adapter_order.*
import org.apache.commons.lang3.StringUtils
import org.checkerframework.checker.signedness.qual.Constant
import org.parceler.Parcels
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import javax.inject.Inject

class OrderDetailActivity : BaseActivity(), OrderContract.OrderDetailView, PermissionContract.View {

    @Inject
    lateinit var orderDetailPresenter: OrderDetailPresenter
    @Inject
    lateinit var permissionPresenter: PermissionPresenter

    lateinit var order: Order
    private val cameraPermissionCode = 10
    private var buktiTransaksiFile: File? = null
    private val orderListAdapter = FastItemAdapter<KeyValueAdapter>()
    private lateinit var user: User

    @BindString(R.string.label_hubungi_kami_untuk_berkomunikasi_dengan_vendor)
    lateinit var labelPaymentComplete: String
    @BindString(R.string.label_kami_sedang_memverifikasi_pembayaran_anda)
    lateinit var labelPaymentVerification: String
    @BindString(R.string.label_upload_bukti_pembayaran)
    lateinit var labelUploadBuktiPembayaran: String
    @BindString(R.string.label_bukti_pembayaran)
    lateinit var labelBuktiPembayaran: String

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        configureBackButton()
        permissionPresenter.init(RxPermissions(this))
        order = Parcels.unwrap<Order>(intent?.extras?.getParcelable(BuildConfig.orderDb))
        configureItemAdapter(orderListAdapter, orderDetailRecycleView)
        showDetailOrder()
        configureImagePopUp()
        orderDetailPresenter.checkVisibilityView(order.paymentStatus)
    }

    private fun showDetailOrder() {
        orderListAdapter.clear()
        orderListAdapter.add(KeyValueAdapter("Status Pesanan", order.paymentStatus.description))
        if (order.paymentStatus == PaymentStatus.PESANAN_DIBATALKAN)
            orderListAdapter.add(KeyValueAdapter("Alasan Pembatalan", order.reason))
        orderListAdapter.add(KeyValueAdapter("Nama Acara", order.name))
        orderListAdapter.add(KeyValueAdapter("Penanggung Jawab", order.penanggungJawab))
        orderListAdapter.add(KeyValueAdapter("Alamat Acara", order.address))
        orderListAdapter.add(
            KeyValueAdapter(
                "Mulai Acara",
                TimeUtils.getDateFormated("dd/MM/yyyy HH:mm", order.startDate)
            )
        )
        orderListAdapter.add(
            KeyValueAdapter(
                "Akhir Acara",
                TimeUtils.getDateFormated("dd/MM/yyyy HH:mm", order.endDate)
            )
        )
        if (order.product != null) {
            orderListAdapter.add(KeyValueAdapter("Nama Paket", order.product!!.name))
            orderListAdapter.add(
                KeyValueAdapter(
                    "Harga Paket", StringHelper.getStringBuilderToString(
                        StringHelper.getPriceInRp(order.product!!.price),
                        "/",
                        order.product!!.paymentType!!.description
                    )
                )
            )
        }
        orderListAdapter.add(KeyValueAdapter("Total Pembelian", order.pesanan.toString()))
        orderListAdapter.add(
            KeyValueAdapter(
                "Total Pembayaran",
                StringHelper.getPriceInRp(order.amount)
            )
        )

        GlideUtils.setFotoWithUrl(this, order.bank!!.url, logoBankImageView)
        bankTitleTextView.text = order.bank!!.bankName
        bankDescriptionTextView.text = StringHelper.getStringBuilderToString(
            order.bank!!.nomorRekening, " a/n ", order.bank!!.namaRekening
        )

        if (StringUtils.isNotBlank(order.buktiTransfer)) {
            GlideUtils.setFotoFromStorage(
                this,
                StringHelper.getStringBuilderToString("order/", order.buktiTransfer),
                buktiTransaksiImageView
            )
            FirebaseStorage.getInstance()
                .reference
                .child("order/" + order.buktiTransfer)
                .downloadUrl
                .addOnSuccessListener {
                    imagePopup.initiatePopupWithPicasso(it)
                }
            ViewHelper.hideView(uploadPictureButton)
        }
    }

    override fun doOnPermissionGranted() {
        configureEasyImage()
        EasyImage.openChooserWithGallery(this, "Foto Transaksi", cameraPermissionCode)
    }

    override fun doOnPermissionRejected() {
        showError("Untuk mengupload gambar, dibutuhkan perizinan kamera")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onImagePicked(
                    imageFile: File?,
                    source: EasyImage.ImageSource?,
                    type: Int
                ) {
                    buktiTransaksiFile = imageFile?.let { image -> compressFile(image) }
                    GlideUtils.setFotoWithUrl(
                        this@OrderDetailActivity,
                        buktiTransaksiFile?.absolutePath,
                        buktiTransaksiImageView
                    )
                    ViewHelper.hideView(uploadPictureButton)
                    ViewHelper.showView(deleteButton)
                }

                override fun onImagePickerError(
                    e: Exception?,
                    source: EasyImage.ImageSource?,
                    type: Int
                ) {
                    showError(e?.message!!)
                }

                override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                    if (source == EasyImage.ImageSource.CAMERA) {
                        EasyImage.lastlyTakenButCanceledPhoto(this@OrderDetailActivity)?.delete()
                    }
                }
            })
    }

    override fun showMenuPembayaran() {
        labelUploadBuktiPembayaranTextView.text = labelUploadBuktiPembayaran
        orderDetailPresenter.checkRole()
    }

    override fun showMenuVerifyPembayaran() {
        labelUploadBuktiPembayaranTextView.text = labelBuktiPembayaran
        afterPaymentLabel.text = labelPaymentVerification
        ViewHelper.showView(
            listOf(
                buktiPemabayranArea,
                labelUploadBuktiPembayaranTextView,
                afterPaymentLabel
            )
        )
        ViewHelper.hideView(
            listOf(
                areaCountDown,
                uploadPictureButton,
                cancelButton,
                actionButton,
                detailButton
            )
        )
        orderListAdapter.set(
            0,
            KeyValueAdapter("Status Pesanan", PaymentStatus.VERIFIKASI_PEMBAYARAN.description)
        )
        orderDetailPresenter.checkRole()
    }

    override fun showMenuPaymentVerified() {
        labelUploadBuktiPembayaranTextView.text = labelBuktiPembayaran
        afterPaymentLabel.text = labelPaymentComplete
        ViewHelper.showView(
            listOf(
                buktiPemabayranArea, labelUploadBuktiPembayaranTextView,
                afterPaymentLabel
            )
        )
        ViewHelper.hideView(
            listOf(
                areaCountDown,
                uploadPictureButton,
                cancelButton,
                actionButton,
                deleteButton
            )
        )
        orderListAdapter.set(
            0,
            KeyValueAdapter("Status Pesanan", PaymentStatus.PESANAN_DITERIMA.description)
        )
        orderDetailPresenter.checkRole()
    }

    override fun setUser(user: User) {
        this.user = user
        if (PaymentStatus.VERIFIKASI_PEMBAYARAN == order.paymentStatus && Role.ADMIN == user.type) {
            actionButton.text = "Verifikasi Pembayaran"
            ViewHelper.showView(actionButton)
            ViewHelper.hideView(afterPaymentLabel)
            actionButton.setOnClickListener {
                order.paymentStatus = PaymentStatus.PESANAN_DITERIMA
                showInfoWithCancel(
                    "Apakah Anda yakin ingin mengubah status pembayaran?",
                    View.OnClickListener {
                        infoDialog.dismissDialog()
                        orderDetailPresenter.changeStatusOrder(
                            order,
                            "Telah disetujui oleh Admin",
                            PaymentStatus.PESANAN_DITERIMA
                        )
                    })
            }
        } else if (PaymentStatus.PESANAN_DITERIMA == order.paymentStatus && Role.PENGGUNA == user.type) {
            actionButton.text = "Hubungi Sekarang"
            ViewHelper.showView(actionButton)
            actionButton.setOnClickListener {
                //                showInfoWithCancel(
//                    "Apakah Anda yakin ingin menghubungi admin sekarang?",
//                    View.OnClickListener {
//                        order.paymentStatus = PaymentStatus.PESANAN_DIPROSES
//                        infoDialog.dismissDialog()
//                        orderDetailPresenter.changeStatusOrder(
//                            order,
//                            "Pesanan siap diproses oleh vendor",
//                            PaymentStatus.PESANAN_DIPROSES
//                        )
//                    })
                val message = getMassage(order.vendorChoosed)
                ActionHelper.openWa(this, DefaultConstanta.CS_PHONE, message)
            }
        } else if (PaymentStatus.PESANAN_DIPROSES == order.paymentStatus && Role.PENGGUNA == user.type) {
            actionButton.text = "Selesaikan Pesanan"
            ViewHelper.showView(actionButton)
            actionButton.setOnClickListener {
                showInfoWithCancel(
                    "Apakah Anda yakin ingin menyelesaikan pesanan?",
                    View.OnClickListener {
                        infoDialog.dismissDialog()
                        val intent = getIntent(this, RatingActivity::class.java)
                        intent.putExtra(BuildConfig.orderDb, Parcels.wrap(order))
                        showActivity(intent)
                    })
            }
        } else if (PaymentStatus.MENUNGGU_PEMBAYARAN == order.paymentStatus && Role.PENGGUNA == user.type) {
            ViewHelper.showView(
                listOf(
                    actionButton,
                    cancelButton,
                    buktiPemabayranArea,
                    labelUploadBuktiPembayaranTextView
                )
            )
            actionButton.setOnClickListener {
                if (buktiTransaksiFile == null) {
                    showError("Anda belum menambahkan bukti transaksi")
                } else {
                    showInfoWithCancel(
                        "Apakah Anda yakin ingin mengupload bukti transaksi?",
                        View.OnClickListener {
                            infoDialog.dismissDialog()
                            orderDetailPresenter.uploadFoto(buktiTransaksiFile!!, order)
                        })
                }
            }
        }
    }

    private fun getMassage(vendorChoosed: String): String {
        return StringHelper.getStringBuilderToString(
            "Hi admin, tolong follow up vendor dengan kode berikut: \n\n- ",
            StringHelper.getStringBuilderToStringFromList(vendorChoosed.split(","), "\n- "),
            "\n\nMohon informasinya segera, terima kasih"
        )
    }

    override fun showMenuOrderDone() {
        ViewHelper.showView(
            listOf(
                buktiPemabayranArea,
                labelUploadBuktiPembayaranTextView,
                afterPaymentLabel
            )
        )
        ViewHelper.hideView(
            listOf(
                areaCountDown,
                uploadPictureButton,
                cancelButton,
                actionButton,
                deleteButton
            )
        )
        orderListAdapter.set(
            0,
            KeyValueAdapter("Status Pesanan", PaymentStatus.PESANAN_SELESAI.description)
        )
        orderDetailPresenter.checkRole()
    }

    override fun showPesananDiproses() {
        ViewHelper.showView(listOf(buktiPemabayranArea, labelUploadBuktiPembayaranTextView))
        ViewHelper.hideView(
            listOf(
                areaCountDown,
                uploadPictureButton,
                cancelButton,
                actionButton,
                deleteButton
            )
        )
        orderListAdapter.set(
            0,
            KeyValueAdapter("Status Pesanan", PaymentStatus.PESANAN_DIPROSES.description)
        )
        orderDetailPresenter.checkRole()
    }

    @OnClick(R.id.uploadPictureButton)
    fun onUploadPictureButtonClicked() {
        permissionPresenter.getCameraPermission()
    }

    @OnClick(R.id.deleteButton)
    fun onDeleteButtonClicked() {
        showInfo("Yakin ingin menghapus bukti transaksi?", View.OnClickListener {
            ViewHelper.showView(uploadPictureButton)
            ViewHelper.hideView(deleteButton)
            buktiTransaksiImageView.setImageResource(0)
            buktiTransaksiFile = null
            infoDialog.dismissAllowingStateLoss()
        })
    }

    @OnClick(R.id.cancelButton)
    fun onCancelButtonClicked() {
        showInfoWithCancel("Apakah Anda yakin ingin membatalkan pesanan?", View.OnClickListener {
            infoDialog.dismissDialog()
            orderDetailPresenter.changeStatusOrder(
                order,
                "Dibatalkan oleh pengguna",
                PaymentStatus.PESANAN_DIBATALKAN
            )
        })
    }

    @OnClick(R.id.buktiTransaksiImageView)
    fun buktiTransaksiImageViewClicked() {
        if (imagePopup.isImageIsSet) {
            imagePopup.viewPopup()
        }
    }
}