package co.id.klikacara.main.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.`object`.adapter.OrderListAdapter
import co.id.klikacara.`object`.authentication.Role
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.main.contract.MainContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.apache.commons.lang3.StringUtils

class OrderPresenter(
    private val view: MainContract.OrderView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
) : BasePresenter() {

    fun getOrderList() {
        view.showProgressDialog()
        database.collection(BuildConfig.orderDb)
            .whereEqualTo("uid", auth.uid!!)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && !it.result!!.isEmpty) {
                    val orderList = it.result!!.toObjects(Order::class.java)
                    val orderListAdapter = ArrayList<OrderListAdapter>()
                    for (order in orderList) {
                        if (!(order.paymentStatus == PaymentStatus.PESANAN_SELESAI &&
                                    StringUtils.isBlank(order.recomendedVendor))
                        )
                            orderListAdapter.add(OrderListAdapter(order))
                    }
                    if (orderListAdapter.isNotEmpty())
                        view.setOrderListAdapter(orderListAdapter)
                    else
                        view.showNoOrderFound()
                } else {
                    view.showNoOrderFound()
                }
                view.dismissProgressDialog()
            }
    }

    fun checkRole() {
        view.showProgressDialog()
        database.collection(BuildConfig.userDb).document(auth.uid!!).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val user = it.result!!.toObject(User::class.java)
                    if (user != null) {
                        view.setUserRole(user.type)
                    }
                }
                view.dismissProgressDialog()
            }
    }

    fun getAllOrder() {
        view.showProgressDialog()
        database.collection(BuildConfig.orderDb)
            .orderBy("paymentStatus", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && !it.result!!.isEmpty) {
                    val orderList = it.result!!.toObjects(Order::class.java)
                    val orderListAdapter = ArrayList<OrderListAdapter>()
                    for (order in orderList) {
                        if (order.paymentStatus == PaymentStatus.MENCARI_VENDOR ||
                            order.paymentStatus == PaymentStatus.VERIFIKASI_PEMBAYARAN
                        )
                            orderListAdapter.add(OrderListAdapter(order))
                    }
                    if (orderListAdapter.isNotEmpty())
                        view.setOrderListAdapter(orderListAdapter)
                    else
                        view.showNoOrderFound()
                } else {
                    view.showNoOrderFound()
                }
                view.dismissProgressDialog()
            }
    }

    fun getOrderByVendorId() {
        view.showProgressDialog()
        database.collection(BuildConfig.orderDb)
            .whereEqualTo("vendorId", auth.uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && !it.result!!.isEmpty) {
                    val orderList = it.result!!.toObjects(Order::class.java)
                    val orderListAdapter = ArrayList<OrderListAdapter>()
                    for (order in orderList) {
                        if (order.paymentStatus != PaymentStatus.MENUNGGU_PEMBAYARAN &&
                            order.paymentStatus != PaymentStatus.VERIFIKASI_PEMBAYARAN &&
                            order.paymentStatus != PaymentStatus.PESANAN_DIBATALKAN &&
                            (!(order.paymentStatus == PaymentStatus.PESANAN_SELESAI &&
                                    StringUtils.isBlank(order.recomendedVendor)))
                        )
                            orderListAdapter.add(OrderListAdapter(order))
                    }
                    if (orderListAdapter.isNotEmpty())
                        view.setOrderListAdapter(orderListAdapter)
                    else
                        view.showNoOrderFound()
                } else {
                    view.showNoOrderFound()
                }
                view.dismissProgressDialog()
            }
    }


}