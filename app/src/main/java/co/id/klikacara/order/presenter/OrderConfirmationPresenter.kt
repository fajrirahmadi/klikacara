package co.id.klikacara.order.presenter

import android.net.Uri
import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.BaseProduct
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.order.contract.OrderContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class OrderConfirmationPresenter(
    private val view: OrderContract.OrderConfirmationView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    fun createOrder(order: Order) {
        view.showProgressDialog()
        val batch = database.batch()
        val reference = database.collection(BuildConfig.orderDb)
        val promoId = reference.document().id
        val searchVendorId = reference.document().id
        order.uid = auth.uid
        if (order.product != null)
            order.vendorId = order.product!!.vendorId
        if (order.searchForVendor) {
            order.key = searchVendorId
            order.paymentStatus = PaymentStatus.MENCARI_VENDOR
            batch.set(reference.document(searchVendorId), order)
        }
        if (order.isPromo) {
            order.key = promoId
            order.searchForVendor = false
            order.paymentStatus = PaymentStatus.PESANAN_SELESAI
            batch.set(reference.document(promoId), order)
        }
        batch.commit()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    view.doOnCreateOrderSuccess(order)
                } else {
                    view.showError("Gagal melakukan order")
                }
                view.dismissProgressDialog()
            }
    }

    fun uploadImage(order: Order) {
        view.showProgressDialog()
        val path = StringHelper.getStringBuilderToString(
            auth.uid,
            "_",
            order.name.replace(" ", ""),
            "_",
            System.currentTimeMillis().toString()
        )
        storage.reference.child(
            StringHelper.getStringBuilderToString(
                BuildConfig.orderDb,
                "/",
                path
            )
        )
            .putFile(Uri.fromFile(File(order.posterUrl)))
            .addOnSuccessListener {
                it.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { url ->
                        view.dismissProgressDialog()
                        order.posterUrl = url.toString()
                        createOrder(order)
                    }.addOnFailureListener {
                        view.dismissProgressDialog()
                    }
            }
            .addOnFailureListener {
                view.dismissProgressDialog()
            }
    }
}