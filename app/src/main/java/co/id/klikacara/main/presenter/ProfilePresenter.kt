package co.id.klikacara.main.presenter

import android.content.Context
import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.Bank
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.base.utils.authhelper.AuthHelper
import co.id.klikacara.main.contract.MainContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class ProfilePresenter(
    private val view: MainContract.ProfileView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val remoteConfig: FirebaseRemoteConfig
) : BasePresenter() {

    var authHelper: AuthHelper? = null

    fun checkLoginStatus() {
        if (auth.currentUser == null)
            view.showNotLoginArea()
        else
            view.showLoginArea()
    }

    fun initAuthHelper(context: Context) {
        authHelper = AuthHelper(context)
    }

    fun doLogout() {
        authHelper?.signOut()
        view.doOnLogoutSuccess()
    }

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

    fun addBank() {
        val listBank = ArrayList<Bank>()
        listBank.add(Bank(null, "Ahmad Luthfi", "BCA", "5465312880"))
        listBank.add(Bank(null, "Fajri Rahmadi", "BNI", "132133122"))
        listBank.add(Bank(null, "Fajri Rahmadi", "Jenius", "382131275"))
        val reference = database.collection(BuildConfig.bankDb)
        for (bank in listBank) {
            bank.key = reference.document().id
            reference.document(bank.key!!).set(bank)
        }
    }

    fun getAboutUrl(): String {
        return remoteConfig.getString(BuildConfig.aboutUsKey)
    }

    fun getTnC(): String {
        return remoteConfig.getString(BuildConfig.tncKey)
    }

    fun getPp(): String {
        return remoteConfig.getString(BuildConfig.ppKey)
    }

    fun getMitraData() {
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