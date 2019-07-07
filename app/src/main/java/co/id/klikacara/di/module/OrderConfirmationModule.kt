package co.id.klikacara.di.module

import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.presenter.OrderConfirmationPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class OrderConfirmationModule {

    @Provides
    fun provideOrderConfirmationPresenter(
        view: OrderContract.OrderConfirmationView
    ): OrderConfirmationPresenter {
        return OrderConfirmationPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    }
}