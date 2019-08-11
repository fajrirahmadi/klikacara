package co.id.klikacara.authentication.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.authentication.RegistrationVendor
import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.RegistrationUsahaPresenter
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.main.view.MainActivity
import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.presenter.PermissionPresenter
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_authentication_registration_usaha.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import javax.inject.Inject

class RegisterUsahaFragment : BaseFragment(), AuthenticationContract.RegisterUsahaView, PermissionContract.View {

    @Inject
    lateinit var registerUsahaPresenter: RegistrationUsahaPresenter

    @Inject
    lateinit var permissionPresenter: PermissionPresenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_authentication_registration_usaha, container)
    }

    private lateinit var registrationVendor: RegistrationVendor
    private var mitraPictureFile: File? = null
    private val cameraPermissionCode = 10

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationVendor = Parcels.unwrap(arguments?.getParcelable(BuildConfig.registrationData))
        permissionPresenter.init(RxPermissions(this))

    }

    @OnClick(R.id.submitButton)
    fun onNextButtonClicked() {
        registrationVendor.mitra!!.description = descriptionEditText.text.toString()
        registrationVendor.mitra!!.slogan = sloganEditText.text.toString()
        when {
            mitraPictureFile == null -> showError("Anda belum menambahkan foto")
            StringUtils.isBlank(registrationVendor.mitra?.description) -> showError("Anda belum menambahkan deskripsi")
            StringUtils.isBlank(registrationVendor.mitra?.slogan) -> showError("Anda belum membuat slogan, tambahin kata-kata dikit ya.")
            else -> {
                registerUsahaPresenter.registerUser(registrationVendor)
            }
        }
    }

    override fun handleOnRegisterUserSuccess(registrationVendor: RegistrationVendor) {
        registerUsahaPresenter.uploadFoto(mitraPictureFile!!, registrationVendor)
    }

    override fun doOnRegisterSuccess() {
        showSuccess("Berhasil melakukan pendaftaran", View.OnClickListener {
            infoDialog.dismissAllowingStateLoss()
            showActivityAndFinishAllActivity(getIntent(activity!!, MainActivity::class.java))
        })
    }

    override fun doOnRegisterFailed() {
        showError("Gagal melakukan pendaftaran sebagai mitra")
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
                mitraPictureFile = imageFile?.let { image -> compressFile(image) }
                GlideUtils.setFotoCircle(activity, mitraPictureFile?.absolutePath, mitraImageView)
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

    @OnClick(R.id.mitraImageView)
    fun onMitraImageClicked() {
        permissionPresenter.getCameraPermission()
    }
}