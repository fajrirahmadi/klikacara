package co.id.klikacara.main.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.OnClick
import co.id.klikacara.R
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.`object`.authentication.Role
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.ProfileDetailPresenter
import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.presenter.PermissionPresenter
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_profile_detail.*
import org.apache.commons.lang3.StringUtils
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import javax.inject.Inject


class ProfileDetailActivity : BaseActivity(), MainContract.ProfileDetailView, PermissionContract.View {

    @Inject
    lateinit var profileDetailPresentar: ProfileDetailPresenter
    @Inject
    lateinit var permissionPresenter: PermissionPresenter
    private var profileFile: File? = null
    private val cameraPermissionCode = 10
    private lateinit var user: User
    private lateinit var mitra: Mitra

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
                    doSubmitChangeProfile()
                })
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_detail)
        permissionPresenter.init(RxPermissions(this))
        configureToolbarNoHomeAndTitle("Detail Profil")
        profileDetailPresentar.getProfile()
        profileDetailPresentar.getMitra()
    }

    override fun setUserData(user: User) {
        this.user = user
        nameEditText.setText(user.name)
        emailEditText.setText(user.email)
        if (StringUtils.isNotBlank(user.url))
            GlideUtils.setFotoCircleFromStorage(
                this,
                StringHelper.getStringBuilderToString("user/", user.url),
                mitraImageView
            )
        if (user.type == Role.VENDOR) {
            ViewHelper.showView(areaMitra)
        }
    }

    @OnClick(R.id.changeProfileButton)
    fun onChangeButtonClicked() {
        permissionPresenter.getCameraPermission()
    }

    override fun setMitraData(mitra: Mitra) {
        this.mitra = mitra
        kategoriEditText.setText(mitra.type!!.description)
        namaUsahaEditText.setText(mitra.name)
        phoneEditText.setText(mitra.phone)
        addressEditText.setText(
            StringHelper.getStringBuilderToString(
                mitra.address,
                ", ",
                mitra.district!!.name,
                ", ",
                mitra.city!!.name,
                ", ",
                mitra.province!!.name,
                ", ",
                mitra.postalCode
            )
        )
        descriptionEditText.setText(mitra.description)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                profileFile = imageFile?.let { image -> compressFile(image) }
                GlideUtils.setFotoCircle(
                    this@ProfileDetailActivity,
                    profileFile?.absolutePath,
                    mitraImageView
                )
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                showError(e?.message!!)
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    EasyImage.lastlyTakenButCanceledPhoto(this@ProfileDetailActivity)?.delete()
                }
            }
        })
    }

    override fun doOnPermissionGranted() {
        configureEasyImage()
        EasyImage.openChooserWithDocuments(this, "Foto Profil", cameraPermissionCode)
    }

    override fun doOnPermissionRejected() {
        showError("Untuk mengubah profil, harap memberikan izin akses kamera")
    }

    private fun doSubmitChangeProfile() {
        if (StringUtils.isBlank(nameEditText.text.toString().trim()))
            showError("Nama tidak boleh kosong")
        else if (user.type == Role.VENDOR && StringUtils.isBlank(namaUsahaEditText.text.toString().trim()))
            showError("Nama usaha tidak boleh kosong")
        else if (user.type == Role.VENDOR && StringUtils.isBlank(descriptionEditText.text.toString().trim()))
            showError("Deskripsi tidak boleh kosong")
        else {
            if (profileFile != null)
                profileDetailPresentar.uploadFoto(profileFile!!)
            else {
                submitProfileChange(user.url)
            }
        }
    }

    override fun submitProfileChange(path: String?) {
        this.user.url = path!!
        user.name = nameEditText.text.toString().trim()
        if (user.type == Role.VENDOR) {
            mitra.name = namaUsahaEditText.text.toString().trim()
            mitra.description = descriptionEditText.text.toString().trim()
            profileDetailPresentar.submitUser(user, mitra)
        } else {
            profileDetailPresentar.submitUser(user, null)
        }
    }

    override fun doOnSumbitUserSuccess() {
        showSuccess("Berhasil mengubah profil")
    }
}