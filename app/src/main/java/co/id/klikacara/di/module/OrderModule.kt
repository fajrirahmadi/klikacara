package co.id.klikacara.di.module

import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.OrderPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class OrderModule {

    @Provides
    fun provideHomePresenter(
        view: MainContract.OrderView
    ): OrderPresenter {
        return OrderPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    }
}