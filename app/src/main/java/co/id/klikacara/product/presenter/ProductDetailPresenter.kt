package co.id.klikacara.product.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.product.contract.ProductContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProductDetailPresenter(
    private val view: ProductContract.ProductDetailView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) : BasePresenter() {

    fun getVendorById(vendorId: String) {
        view.showProgressDialog()
        database.collection(BuildConfig.mitraDb).document(vendorId).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val mitra = it.result!!.toObject(Mitra::class.java)
                    if (mitra != null) {
                        view.setDataVendor(mitra)
                    }
                }
                view.dismissProgressDialog()
            }
    }

    fun checkRole() {
        if (auth.currentUser != null)
            database.collection(BuildConfig.mitraDb).document(auth.uid!!)
                .get().addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val mitra = it.result!!.toObject(Mitra::class.java)
                        if (mitra != null)
                            view.userIsVendor()
                    }
                }
    }

    fun openOrderActivity() {
        if (auth.currentUser == null)
            view.showDialogLogin()
        else
            view.doOpenOrderActivity()
    }


}