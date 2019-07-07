package co.id.klikacara.order.contract

import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.adapter.BankAdapter
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.contract.BaseContract
import co.id.klikacara.main.contract.MainContract

interface OrderContract {

    interface OrderDetailView : BaseContract.View {
        fun showMenuPembayaran()
        fun showMenuVerifyPembayaran()
        fun showMenuOrderDone()
        fun showMenuPaymentVerified()
        fun setUser(user: User)
        fun showPesananDiproses()

    }

    interface OrderAddressView : BaseContract.View {

    }

    interface OrderDateView : BaseContract.View {
        fun setTotalAmount(totalPrice: Long)

    }

    interface OrderPaymentView : BaseContract.View {
        fun setBankListAdapter(bankListAdapter: ArrayList<BankAdapter>)
    }

    interface OrderConfirmationView : BaseContract.View {
        fun doOnCreateOrderSuccess(order: Order)
    }

    interface RatingView : MainContract.UserProfileView {
        fun doOnSubmitRatingSuccess()
        fun doOnSubmitRatingFailed()

    }

}