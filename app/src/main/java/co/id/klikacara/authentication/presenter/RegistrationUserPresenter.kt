package co.id.klikacara.authentication.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.validator.ValidatorUsecase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.apache.commons.lang3.StringUtils

class RegistrationUserPresenter(
    private val view: AuthenticationContract.RegisterUserView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val validator: ValidatorUsecase
) : BasePresenter() {

    fun submitRegistrationUser(user: User, password: String, confirmPassword: String) {
        if (StringUtils.isBlank(user.name))
            view.showError("Nama tidak boleh kosong")
        else if (!validator.isEmailValid(user.email))
            view.showError("Email tidak valid")
        else if (!validator.isPasswordValid(password)) {
            view.showError("Kata sandi tidak valid")
        } else if (password != confirmPassword) {
            view.showError("Kata sandi konfirmasi tidak sesuai")
        } else {
            view.doOnSubmitRegistrationSuccess(user)
        }
    }

    fun registerUser(user: User, password: String) {
        view.showProgressDialog()
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener {
                user.uid = it.user.uid
                setRemoteUserData(user)
            }
            .addOnFailureListener {
                view.dismissProgressDialog()
            }
    }

    private fun setRemoteUserData(user: User) {
        database.collection(BuildConfig.userDb).document(user.uid!!).set(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    view.doOnRegisterSuccess()
                } else {
                    view.doOnRegisterFailed()
                }
                view.dismissProgressDialog()
            }
    }
}