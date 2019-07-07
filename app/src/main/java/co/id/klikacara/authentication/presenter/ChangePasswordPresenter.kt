package co.id.klikacara.authentication.presenter

import co.id.klikacara.authentication.contract.AuthenticationContract
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordPresenter(
    private val view: AuthenticationContract.ChangePasswordView,
    private val auth: FirebaseAuth
) {

    fun doChangePassword(oldPassword: String, newPassword: String) {
        val user = auth.currentUser
        view.showProgressDialog()
        user!!.reauthenticate(
            EmailAuthProvider.getCredential(
                user.email!!,
                oldPassword
            )
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                FirebaseAuth.getInstance().currentUser!!
                    .updatePassword(newPassword)
                    .addOnSuccessListener {
                        view.doOnChangePasswordSuccess()
                        view.dismissProgressDialog()
                    }
                    .addOnFailureListener {
                        view.doOnChangePasswordFailed()
                        view.dismissProgressDialog()
                    }
            } else {
                view.doOnChangePasswordFailed()
                view.dismissProgressDialog()
            }
        }
    }
}