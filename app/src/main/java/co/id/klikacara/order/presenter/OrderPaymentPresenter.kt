package co.id.klikacara.order.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.Bank
import co.id.klikacara.`object`.adapter.BankAdapter
import co.id.klikacara.order.contract.OrderContract
import com.google.firebase.firestore.FirebaseFirestore

class OrderPaymentPresenter(
    private val view: OrderContract.OrderPaymentView,
    private val database: FirebaseFirestore
) {

    fun getListBank() {
        view.showProgressDialog()
        database.collection(BuildConfig.bankDb).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val bankList = it.result!!.toObjects(Bank::class.java)
                    val bankListAdapter = ArrayList<BankAdapter>()
                    for (bank in bankList)
                        bankListAdapter.add(BankAdapter(bank, false))
                    view.setBankListAdapter(bankListAdapter)
                }
                view.dismissProgressDialog()
            }
    }
}