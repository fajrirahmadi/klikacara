package co.id.klikacara.di.module

import co.id.klikacara.createevent.contract.EventContract
import co.id.klikacara.createevent.presenter.EventDetailPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class EventDetailModule {

    @Provides
    fun provideEventDetailPresenter(
        view: EventContract.DetailEventView
    ): EventDetailPresenter {
        return EventDetailPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    }
}