package co.id.klikacara.order.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.`object`.Rating
import co.id.klikacara.main.presenter.UserProfilePresenter
import co.id.klikacara.order.contract.OrderContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RatingPresenter(
    private val view: OrderContract.RatingView,
    auth: FirebaseAuth,
    private val database: FirebaseFirestore
) : UserProfilePresenter(view, auth, database) {

    fun submitRating(rating: Rating, order: Order) {
        view.showProgressDialog()
        val batch = database.batch()
        val orderRef = database.collection(BuildConfig.orderDb)
        val ratingRef = database.collection(BuildConfig.ratingDb)

        rating.key = ratingRef.document().id
        rating.vendorId = order.vendorId
        order.paymentStatus = PaymentStatus.PESANAN_SELESAI

        batch.set(orderRef.document(order.key!!), order)
        batch.set(ratingRef.document(rating.key!!), rating)

        batch.commit().addOnCompleteListener {
            if (it.isSuccessful) {
                view.doOnSubmitRatingSuccess()
            } else {
                view.doOnSubmitRatingFailed()
            }
            view.dismissProgressDialog()
        }
    }

}