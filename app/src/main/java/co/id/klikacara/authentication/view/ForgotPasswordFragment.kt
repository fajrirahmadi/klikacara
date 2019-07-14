package co.id.klikacara.authentication.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.R
import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.ForgetPasswordPresenter
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.validator.ValidatorUsecase
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_authentication_forgot_password.*
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

class ForgotPasswordFragment : BaseFragment(), AuthenticationContract.ForgetPasswordView {

    @Inject
    lateinit var forgetPasswordPresenter: ForgetPasswordPresenter
    val validatorUsecase = ValidatorUsecase()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_authentication_forgot_password, container)
    }

    @OnClick(R.id.submitButton)
    fun submitButtonClicked() {
        if (StringUtils.isBlank(emailEditText.text.toString()))
            showInfo("Email tidak boleh kosong")
        else if (!validatorUsecase.isEmailValid(emailEditText.text.toString().trim()))
            showInfo("Email tidak valid")
        else {
            forgetPasswordPresenter.sendResetPasswordEmail(emailEditText.text.toString().trim())
        }
    }

    override fun doOnResetPasswordSuccess() {
        showInfo("Email reset kata sandi telah dikirimkan ke email yang Anda masukkan, silahkan cek email Anda.",
            View.OnClickListener {
                activity!!.onBackPressed()
            })
    }
}