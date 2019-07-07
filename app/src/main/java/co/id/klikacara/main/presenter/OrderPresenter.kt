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

class OrderPresenter(
    private val view: MainContract.OrderView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
) : BasePresenter() {

    fun getOrderList() {
        database.collection(BuildConfig.orderDb)
            .whereEqualTo("uid", auth.uid!!)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && !it.result!!.isEmpty) {
                    val orderList = it.result!!.toObjects(Order::class.java)
                    val orderListAdapter = ArrayList<OrderListAdapter>()
                    for (order in orderList)
                        orderListAdapter.add(OrderListAdapter(order))
                    view.setOrderListAdapter(orderListAdapter)
                } else {
                    view.showNoOrderFound()
                }
            }
    }

    fun checkLoginStatus() {
        if (auth.currentUser == null)
            view.showNotLoginArea()
        else
            view.showLoginArea()
    }

    fun checkRole() {
        database.collection(BuildConfig.userDb).document(auth.uid!!).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val user = it.result!!.toObject(User::class.java)
                    if (user != null) {
                        when (user.type) {
                            Role.ADMIN -> {
                                getAllOrder()
                            }
                            Role.VENDOR -> {
                                getOrderByVendorId(user.uid!!)
                            }
                            Role.PENGGUNA -> {
                                getOrderList()
                            }
                            Role.AMBASADOR -> {
                            }
                        }
                    }
                }
            }
    }

    private fun getAllOrder() {
        database.collection(BuildConfig.orderDb)
            .orderBy("paymentStatus", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && !it.result!!.isEmpty) {
                    val orderList = it.result!!.toObjects(Order::class.java)
                    val orderListAdapter = ArrayList<OrderListAdapter>()
                    for (order in orderList)
                        orderListAdapter.add(OrderListAdapter(order))
                    view.setOrderListAdapter(orderListAdapter)
                } else {
                    view.showNoOrderFound()
                }
            }
    }

    private fun getOrderByVendorId(uid: String) {
        database.collection(BuildConfig.orderDb)
            .whereEqualTo("vendorId", uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && !it.result!!.isEmpty) {
                    val orderList = it.result!!.toObjects(Order::class.java)
                    val orderListAdapter = ArrayList<OrderListAdapter>()
                    for (order in orderList) {
                        if (order.paymentStatus != PaymentStatus.MENUNGGU_PEMBAYARAN &&
                            order.paymentStatus != PaymentStatus.VERIFIKASI_PEMBAYARAN &&
                            order.paymentStatus != PaymentStatus.PESANAN_DIBATALKAN
                        )
                            orderListAdapter.add(OrderListAdapter(order))
                    }
                    view.setOrderListAdapter(orderListAdapter)
                } else {
                    view.showNoOrderFound()
                }
            }
    }


}