package co.id.klikacara.authentication.presenter

import android.app.Activity
import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.base.utils.authhelper.AuthHelper
import co.id.klikacara.validator.ValidatorUsecase
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginPresenter(
    private val view: AuthenticationContract.LoginView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val validator: ValidatorUsecase
) : BasePresenter() {

    var authHelper: AuthHelper? = null

    fun doLogin(email: String, password: String) {
        if (!validator.isEmailValid(email))
            view.showEmailInvalid()
        else if (!validator.isPasswordValid(password))
            view.showPasswordInvalid()
        else {
            view.showProgressDialog()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        view.doOnLoginSuccess()
                    } else {
                        view.showError(it.exception?.message!!)
                    }
                    view.dismissProgressDialog()
                }
        }
    }

    fun initAuthHelper(context: Activity) {
        authHelper = AuthHelper(context)
    }

    fun loginWithGoogle() {
        val intent = Auth.GoogleSignInApi.getSignInIntent(authHelper!!.googleApiClient)
        view.doLoginWithGoogle(intent)
    }

    fun loginWithGmail(context: Activity, account: GoogleSignInAccount) {
        view.showProgressDialog()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(
            context
        ) { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                FirebaseFirestore.getInstance().collection(BuildConfig.userDb)
                    .document(user!!.uid).get()
                    .addOnSuccessListener { documentSnapshot ->
                        val userRemote = documentSnapshot.toObject(User::class.java)
                        if (userRemote == null) {
                            val newUser =
                                User(
                                    user.uid,
                                    account.displayName!!,
                                    account.email!!,
                                    ""
                                )
                            FirebaseFirestore.getInstance().collection(BuildConfig.userDb)
                                .document(newUser.uid!!).set(newUser)
                        } else {
                            FirebaseFirestore.getInstance().collection(BuildConfig.userDb)
                                .document(userRemote.uid!!)
                                .update("status", "Online")
                        }
                        view.doOnLoginSuccess()
                        view.dismissProgressDialog()
                    }
                    .addOnFailureListener { e ->
                        view.dismissProgressDialog()
                        view.showError(if (task.exception != null) task.exception?.message!! else "Tidak bisa masuk ke Klik Acara, cobalah beberapa saat lagi.")
                    }
            } else {
                view.showError(if (task.getException() != null) task.exception?.message!! else "Tidak bisa masuk ke Klik Acara, cobalah beberapa saat lagi.")
                view.dismissProgressDialog()
            }
        }
    }


}