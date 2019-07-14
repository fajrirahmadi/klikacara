package co.id.klikacara.di.module

import co.id.klikacara.base.utils.SchedulersFacade
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.HomePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    fun provideHomePresenter(
        view: MainContract.HomeView,
        scheduler: SchedulersFacade
    ): HomePresenter {
        return HomePresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance(), scheduler)
    }
}