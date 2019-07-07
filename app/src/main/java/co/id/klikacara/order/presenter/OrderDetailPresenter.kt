package co.id.klikacara.order.presenter

import android.net.Uri
import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.order.contract.OrderContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class OrderDetailPresenter(
    private val view: OrderContract.OrderDetailView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    fun checkVisibilityView(paymentStatus: PaymentStatus) {
        when (paymentStatus) {
            PaymentStatus.MENUNGGU_PEMBAYARAN -> {
                view.showMenuPembayaran()
            }
            PaymentStatus.VERIFIKASI_PEMBAYARAN -> {
                view.showMenuVerifyPembayaran()
            }
            PaymentStatus.PESANAN_DITERIMA -> {
                view.showMenuPaymentVerified()
            }
            PaymentStatus.PESANAN_DIPROSES -> {
                view.showPesananDiproses()
            }
            PaymentStatus.PESANAN_SELESAI -> {
                view.showMenuOrderDone()
            }
            PaymentStatus.PESANAN_DIBATALKAN -> {
            }
        }
    }

    fun changeStatusOrder(order: Order, reason: String, paymentStatus: PaymentStatus) {
        order.reason = reason
        order.paymentStatus = paymentStatus
        database.collection(BuildConfig.orderDb).document(order.key!!)
            .set(order)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    checkVisibilityView(order.paymentStatus)
                }
            }
    }

    fun uploadFoto(
        buktiTransaksiFile: File,
        order: Order
    ) {
        view.showProgressDialog()
        val path = StringHelper.getStringBuilderToString(
            order.key,
            "_",
            System.currentTimeMillis().toString()
        )
        storage.reference.child(StringHelper.getStringBuilderToString(BuildConfig.orderDb, "/", path))
            .putFile(Uri.fromFile(buktiTransaksiFile))
            .addOnSuccessListener {
                order.buktiTransfer = path
                order.paymentStatus = PaymentStatus.VERIFIKASI_PEMBAYARAN
                database.collection(BuildConfig.orderDb).document(order.key!!)
                    .set(order)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            view.showMenuVerifyPembayaran()
                        } else {
                            view.showError("Gagal mengupload bukti transaksi")
                        }
                        view.dismissProgressDialog()
                    }
            }
            .addOnFailureListener {
                view.dismissProgressDialog()
                view.showError("Gagal mengupload bukti transaksi")
            }
    }

    fun checkRole() {
        database.collection(BuildConfig.userDb).document(auth.uid!!).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val user = it.result!!.toObject(User::class.java)
                    if (user != null) {
                        view.setUser(user)
                    }
                }
            }
    }

}