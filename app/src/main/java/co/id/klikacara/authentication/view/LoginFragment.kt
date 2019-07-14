package co.id.klikacara.authentication.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.R
import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.LoginPresenter
import co.id.klikacara.base.utils.viewhelper.ExpandAreaTouchHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.main.view.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_authentication_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment(), AuthenticationContract.LoginView {

    @Inject
    lateinit var loginPresenter: LoginPresenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private val RC_SIGN_IN = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_authentication_login, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ExpandAreaTouchHelper.expandSingleTouchArea(forgetPasswordButton, R.dimen.margin_medium)
        loginPresenter.initAuthHelper(activity!!)
    }

    @OnClick(R.id.registerButton)
    fun registerButtonClicked() {
        navigateTo(R.id.authentication_nav_graph, R.id.action_move_fromLoginFragment_toRegisterUserActivity)
    }

    @OnClick(R.id.loginButton)
    fun loginButtonClicked() {
        loginPresenter.doLogin(emailEditText.text.toString(), passwordEditText.text.toString())
    }

    @OnClick(R.id.loginWithGoogle)
    fun loginGoogleButtonClicked() {
        loginPresenter.loginWithGoogle()
    }

    @OnClick(R.id.forgetPasswordButton)
    fun forgetPasswordButtonClicked() {
        navigateTo(R.id.authentication_nav_graph, R.id.action_move_fromLoginFragment_toForgotPasswordFragment)
    }

    override fun showEmailInvalid() {
        showError("Email tidak valid (xxxx@mail.com)")
    }

    override fun showPasswordInvalid() {
        showError("Kata sandi tidak valid (minimum 6 digit)")
    }

    override fun doOnLoginSuccess() {
        showActivityAndFinishAllActivity(getIntent(activity!!, MainActivity::class.java))
    }

    override fun doLoginWithGoogle(intent: Intent) {
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (RC_SIGN_IN == requestCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null)
                    loginPresenter.loginWithGmail(activity!!, account)
            } catch (e: ApiException) {
                showError("Gagal login menggunakan akun Google Anda")
            }

        }
    }
}