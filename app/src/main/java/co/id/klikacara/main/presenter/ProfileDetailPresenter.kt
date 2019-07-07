package co.id.klikacara.main.presenter

import android.net.Uri
import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.main.contract.MainContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProfileDetailPresenter(
    private val view: MainContract.ProfileDetailView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) : BasePresenter() {

    fun getProfile() {
        if (auth.currentUser != null) {
            database.collection(BuildConfig.userDb).document(auth.uid!!)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val user = it.result!!.toObject(User::class.java)
                        if (user != null) {
                            view.setUserData(user)
                        }
                    }
                }
        }
    }

    fun getMitra() {
        if (auth.currentUser != null) {
            database.collection(BuildConfig.mitraDb).document(auth.uid!!)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val mitra = it.result!!.toObject(Mitra::class.java)
                        if (mitra != null) {
                            view.setMitraData(mitra)
                        }
                    }
                }
        }
    }

    fun uploadFoto(
        profileFile: File
    ) {
        view.showProgressDialog()
        val path = StringHelper.getStringBuilderToString(
            auth.uid!!,
            "_",
            System.currentTimeMillis().toString()
        )
        storage.reference.child(StringHelper.getStringBuilderToString(BuildConfig.userDb, "/", path))
            .putFile(Uri.fromFile(profileFile))
            .addOnSuccessListener {
                view.submitProfileChange(path)
            }
            .addOnFailureListener {
                view.dismissProgressDialog()
                view.showError("Gagal mengupload foto")
            }
    }

    fun submitUser(
        user: User,
        mitra: Mitra?
    ) {
        view.showProgressDialog()
        val batch = database.batch()
        val userRef = database.collection(BuildConfig.userDb)
        val mitraRef = database.collection(BuildConfig.mitraDb)

        batch.set(userRef.document(user.uid!!), user)
        if (mitra != null)
            batch.set(mitraRef.document(mitra.key!!), mitra)

        batch.commit().addOnCompleteListener {
            if (it.isSuccessful) {
                view.doOnSumbitUserSuccess()
            } else {
                view.showError("Gagal mengubah profil")
            }
            view.dismissProgressDialog()
        }
    }

}