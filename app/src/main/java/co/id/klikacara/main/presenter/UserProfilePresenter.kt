package co.id.klikacara.main.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.main.contract.MainContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class UserProfilePresenter(
    private val view: MainContract.UserProfileView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
) {
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
}