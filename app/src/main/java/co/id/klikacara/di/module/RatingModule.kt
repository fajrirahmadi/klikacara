package co.id.klikacara.di.module

import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.presenter.RatingPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class RatingModule {

    @Provides
    fun provideRatingPresenter(
        view: OrderContract.RatingView
    ): RatingPresenter {
        return RatingPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    }
}