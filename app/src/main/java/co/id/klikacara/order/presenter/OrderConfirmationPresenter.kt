package co.id.klikacara.order.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.Order
import co.id.klikacara.order.contract.OrderContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderConfirmationPresenter(
    private val view: OrderContract.OrderConfirmationView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
) {
    fun createOrder(order: Order) {
        view.showProgressDialog()
        val reference = database.collection(BuildConfig.orderDb)
        order.key = reference.document().id
        order.uid = auth.uid
        order.vendorId = order.product!!.vendorId
        reference.document(order.key!!).set(order)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    view.doOnCreateOrderSuccess(order)
                } else {
                    view.showError("Gagal melakukan order")
                }
                view.dismissProgressDialog()
            }
    }
}