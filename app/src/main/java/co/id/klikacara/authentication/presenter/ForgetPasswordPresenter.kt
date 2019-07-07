package co.id.klikacara.authentication.presenter

import co.id.klikacara.authentication.contract.AuthenticationContract
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordPresenter(
    private val view: AuthenticationContract.ForgetPasswordView,
    private val auth: FirebaseAuth
) {

    fun sendResetPasswordEmail(email: String) {
        view.showProgressDialog()
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            run {
                if (it.isSuccessful)
                    view.doOnResetPasswordSuccess()
                else
                    view.showError(it.exception?.message!!)
                view.dismissProgressDialog()
            }
        }
    }
}