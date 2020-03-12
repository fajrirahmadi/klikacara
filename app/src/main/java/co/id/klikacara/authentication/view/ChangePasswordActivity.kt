package co.id.klikacara.authentication.view

import android.os.Bundle
import android.view.View
import butterknife.OnClick
import co.id.klikacara.R
import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.ChangePasswordPresenter
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.main.view.MainActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_base_authentication_change_password.*
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

class ChangePasswordActivity : BaseActivity(), AuthenticationContract.ChangePasswordView {

    @Inject
    lateinit var changePasswordPresenter: ChangePasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_authentication_change_password)
        configureBackButton()
    }

    @OnClick(R.id.submitButton)
    fun onSubmitButtonClicked() {
        when {
            StringUtils.isBlank(oldPasswordEditText.text.toString().trim()) -> showError("Kata sandi lama tidak boleh kosong")
            oldPasswordEditText.text.toString().trim().length < 6 -> showError("Minimal kata sandi lama 6 digit")
            StringUtils.isBlank(newPasswordEditText.text.toString().trim()) -> showError("Kata sandi baru tidak boleh kosong")
            newPasswordEditText.text.toString().trim().length < 6 -> showError("Minimal kata sandi baru 6 digit")
            newPasswordEditText.text.toString().trim() != confirmPasswordEditText.text.toString().trim() -> showError("Kata sandi konfirmasi tidak sesuai")
            else -> {
                showInfoWithCancel("Apakah Anda yakin ingin mengubah katasandi?", View.OnClickListener {
                    changePasswordPresenter.doChangePassword(
                        oldPasswordEditText.text.toString().trim(),
                        newPasswordEditText.text.toString().trim()
                    )
                })
            }
        }
    }

    override fun doOnChangePasswordSuccess() {
        showInfo(
            "Kata sandi berhasil diubah, silahkan login kembali menggunakan kata sandi baru Anda",
            View.OnClickListener {
                showActivityAndFinishAllActivity(getIntent(this, MainActivity::class.java))
            })
    }

    override fun doOnChangePasswordFailed() {
        showInfo("Gagal memperbaharui kata sandi")
    }
}