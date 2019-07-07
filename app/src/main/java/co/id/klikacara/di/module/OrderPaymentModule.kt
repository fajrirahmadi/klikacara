package co.id.klikacara.di.module

import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.presenter.OrderPaymentPresenter
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class OrderPaymentModule {

    @Provides
    fun provideOrderPaymentPresenter(
        view: OrderContract.OrderPaymentView
    ): OrderPaymentPresenter {
        return OrderPaymentPresenter(view, FirebaseFirestore.getInstance())
    }
}