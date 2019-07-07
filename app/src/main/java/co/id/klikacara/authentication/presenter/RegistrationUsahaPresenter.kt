package co.id.klikacara.authentication.presenter

import android.net.Uri
import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.authentication.RegistrationVendor
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.base.utils.stringhelper.StringHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class RegistrationUsahaPresenter(
    private val view: AuthenticationContract.RegisterUsahaView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) : BasePresenter() {

    fun submitRegistrationUser(user: User, password: String, confirmPassword: String) {

    }

    fun registerUser(registrationVendor: RegistrationVendor) {
        view.showProgressDialog()
        auth.createUserWithEmailAndPassword(registrationVendor.user!!.email, registrationVendor.password)
            .addOnSuccessListener {
                registrationVendor.user!!.uid = it.user.uid
                registrationVendor.mitra!!.key = it.user.uid
                view.handleOnRegisterUserSuccess(registrationVendor)
            }
            .addOnFailureListener {
                view.dismissProgressDialog()
                view.showError(it.message!!)
            }
    }

    fun uploadFoto(
        mitraPictureFile: File,
        registrationVendor: RegistrationVendor
    ) {
        view.showProgressDialog()
        val path = StringHelper.getStringBuilderToString(
            registrationVendor.user!!.uid,
            "_",
            System.currentTimeMillis().toString()
        )
        storage.reference.child(StringHelper.getStringBuilderToString(BuildConfig.userDb, "/", path))
            .putFile(Uri.fromFile(mitraPictureFile))
            .addOnSuccessListener {
                registrationVendor.user!!.url = path
                val batch = database.batch()
                val userRef = database.collection(BuildConfig.userDb).document(registrationVendor.user!!.uid!!)
                val mitraRef = database.collection(BuildConfig.mitraDb).document(registrationVendor.user!!.uid!!)
                batch.set(userRef, registrationVendor.user!!)
                batch.set(mitraRef, registrationVendor.mitra!!)
                batch.commit()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            view.doOnRegisterSuccess()
                        } else {
                            view.doOnRegisterFailed()
                        }
                        view.dismissProgressDialog()
                    }
            }
            .addOnFailureListener {
                view.doOnRegisterFailed()
                view.dismissProgressDialog()
            }
    }
}