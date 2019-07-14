package co.id.klikacara.main.presenter

import android.net.Uri
import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.main.contract.MainContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class VerifyVendorPresenter(
    private val view: MainContract.VerifyVendorView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) : BasePresenter() {

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
        profileFile: File,
        index: Int
    ) {
        view.showProgressDialog()
        val path = StringHelper.getStringBuilderToString(
            auth.uid!!,
            "_",
            System.currentTimeMillis().toString()
        )
        storage.reference.child(StringHelper.getStringBuilderToString(BuildConfig.kycDb, "/", path))
            .putFile(Uri.fromFile(profileFile))
            .addOnSuccessListener {
                view.doOnUploadFotoSuccess(path, index)
            }
            .addOnFailureListener {
                view.dismissProgressDialog()
                view.showError("Gagal mengupload foto")
            }
    }

    fun submitUser(
        mitra: Mitra
    ) {
        view.showProgressDialog()
        val mitraRef = database.collection(BuildConfig.mitraDb)
        mitraRef.document(mitra.key!!).set(mitra).addOnCompleteListener {
            if (it.isSuccessful) {
                view.doOnSuccessSubmitVerifyVendor()
            } else {
                view.showError("Gagal verifikasi user")
            }
            view.dismissProgressDialog()
        }
    }

}