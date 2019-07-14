package co.id.klikacara.main.view.profile

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import butterknife.OnClick
import co.id.klikacara.R
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.VerifyVendorPresenter
import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.presenter.PermissionPresenter
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_verify_vendor.*
import org.apache.commons.lang3.StringUtils
import org.jetbrains.anko.imageResource
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import javax.inject.Inject

class VerifyVendorActivity : BaseActivity(), PermissionContract.View, MainContract.VerifyVendorView {

    @Inject
    lateinit var verifyVendorPresenter: VerifyVendorPresenter
    @Inject
    lateinit var permissionPresenter: PermissionPresenter

    private var identityFile: File? = null
    private var identityUserFile: File? = null
    private var userPlaceFile: File? = null
    private var documentUsahaFile: File? = null

    private var identityCode = 90
    private var identityWithUserCode = 91
    private var userWithPlaceCode = 92
    private var dokumentCode = 93
    private var selectedCode = 0
    private var mitra: Mitra? = null

    private var fileUploaded = SparseArray<String>(4)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSave -> {
                showInfoWithCancel("Yakin ingin menyimpan perubahan?", View.OnClickListener {
                    infoDialog.dismissAllowingStateLoss()
                    submitVerify()
                })
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                when (selectedCode) {
                    identityCode -> {
                        identityFile = imageFile?.let { image ->
                            compressFile(image)
                        }
                        handleOnImagePicked(imageFile, identityImageView, identityUploadButton, deleteButton)
                    }
                    identityWithUserCode -> {
                        identityUserFile = imageFile?.let { image -> compressFile(image) }
                        handleOnImagePicked(
                            imageFile,
                            identityWithUserImageView,
                            identityWithUserUploadButton,
                            deleteIdentityWithUserButton
                        )
                    }
                    userWithPlaceCode -> {
                        userPlaceFile = imageFile?.let { image -> compressFile(image) }
                        handleOnImagePicked(
                            imageFile,
                            userWithPlaceImageView,
                            userWithPlaceUploadButton,
                            deleteUserWithPlaceButton
                        )
                    }
                    dokumentCode -> {
                        documentUsahaFile = imageFile?.let { image -> compressFile(image) }
                        handleOnImagePicked(
                            imageFile,
                            documentUsahaImageView,
                            documentUsahaUploadButton,
                            deleteDokumenUsahaButton
                        )
                    }
                }
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                showError(e?.message!!)
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    EasyImage.lastlyTakenButCanceledPhoto(this@VerifyVendorActivity)?.delete()
                }
            }
        })
    }

    private fun handleOnImagePicked(
        imageFile: File?,
        imageView: AppCompatImageView,
        uploadButton: View,
        deleteButton: View
    ) {
        GlideUtils.setFotoWithUrl(
            this,
            imageFile?.absolutePath,
            imageView
        )
        ViewHelper.hideView(uploadButton)
        ViewHelper.showView(deleteButton)
    }

    @OnClick(
        R.id.identityUploadButton,
        R.id.identityWithUserUploadButton,
        R.id.userWithPlaceUploadButton,
        R.id.documentUsahaUploadButton
    )
    fun onUploadButtonClicked(view: View) {
        when (view.id) {
            R.id.identityUploadButton -> {
                selectedCode = identityCode
            }
            R.id.identityWithUserUploadButton -> {
                selectedCode = identityWithUserCode
            }
            R.id.userWithPlaceUploadButton -> {
                selectedCode = userWithPlaceCode
            }
            R.id.documentUsahaUploadButton -> {
                selectedCode = dokumentCode
            }
        }
        permissionPresenter.getCameraPermission()
    }

    @OnClick(
        R.id.deleteButton,
        R.id.deleteIdentityWithUserButton,
        R.id.deleteUserWithPlaceButton,
        R.id.deleteDokumenUsahaButton
    )
    fun onDeleteButtonClicked(view: View) {
        when (view.id) {
            R.id.deleteButton -> {
                identityFile = null
                handleDeleteFoto(deleteButton, identityImageView)
            }
            R.id.deleteIdentityWithUserButton -> {
                identityUserFile = null
                handleDeleteFoto(deleteIdentityWithUserButton, identityWithUserImageView)
            }
            R.id.deleteUserWithPlaceButton -> {
                userPlaceFile = null
                handleDeleteFoto(deleteUserWithPlaceButton, userWithPlaceImageView)
            }
            R.id.deleteDokumenUsahaButton -> {
                documentUsahaFile = null
                handleDeleteFoto(deleteDokumenUsahaButton, documentUsahaImageView)
            }
        }
    }

    override fun setMitraData(mitra: Mitra) {
        this.mitra = mitra
    }

    fun handleDeleteFoto(button: View, imageView: AppCompatImageView) {
        showInfoWithCancel("Yakin ingin menghapus foto?", View.OnClickListener {
            infoDialog.dismissAllowingStateLoss()
            ViewHelper.hideView(button)
            imageView.imageResource = 0
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_vendor)
        permissionPresenter.init(RxPermissions(this))
        configureToolbarNoHomeAndTitle("Verifikasi Vendor")
        verifyVendorPresenter.getMitra()
    }

    override fun doOnPermissionGranted() {
        configureEasyImage()
        EasyImage.openChooserWithGallery(this, "Foto", selectedCode)
    }

    override fun doOnPermissionRejected() {
        showError("Untuk verifikasi vendor, harap memberikan izin akses kamera")
    }

    private fun submitVerify() {
        if (identityFile == null)
            showInfo("Foto kartu identitas tidak boleh kosong")
        else if (identityUserFile == null)
            showInfo("Foto identitas dan diri tidak boleh kosong")
        else if (userPlaceFile == null)
            showInfo("Foto diri dan tempat usaha tidak boleh kosong")
        else if (documentUsahaFile == null)
            showInfo("Foto dokumen usaha tidak boleh kosong")
        else {
            if (StringUtils.isBlank(fileUploaded.get(0)))
                verifyVendorPresenter.uploadFoto(identityFile!!, 0)
            if (StringUtils.isBlank(fileUploaded.get(1)))
                verifyVendorPresenter.uploadFoto(identityUserFile!!, 1)
            if (StringUtils.isBlank(fileUploaded.get(2)))
                verifyVendorPresenter.uploadFoto(userPlaceFile!!, 2)
            if (StringUtils.isBlank(fileUploaded.get(3)))
                verifyVendorPresenter.uploadFoto(documentUsahaFile!!, 3)
        }
    }

    override fun doOnSuccessSubmitVerifyVendor() {
        showInfo("Berhasil mengunggah berkas verifikasi, tim kami akan segera memproses data Anda.")
    }

    override fun doOnUploadFotoSuccess(path: String, index: Int) {
        fileUploaded.append(index, path)
        if (fileUploaded.size() == 4 && mitra != null) {
            mitra!!.identityUrl = fileUploaded.get(0)
            mitra!!.identityWithUserUrl = fileUploaded.get(1)
            mitra!!.userWithPlaceUrl = fileUploaded.get(2)
            mitra!!.documentUrl = fileUploaded.get(3)
            verifyVendorPresenter.submitUser(mitra!!)
        }
    }
}