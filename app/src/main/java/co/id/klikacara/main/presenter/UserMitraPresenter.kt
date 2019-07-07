package co.id.klikacara.main.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.main.contract.MainContract
import com.google.firebase.firestore.FirebaseFirestore

open class UserMitraPresenter(
    private val view: MainContract.MitraProfileView,
    private val database: FirebaseFirestore
) {
    fun getMitraById(mitraId: String) {
        database.collection(BuildConfig.mitraDb).document(mitraId)
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