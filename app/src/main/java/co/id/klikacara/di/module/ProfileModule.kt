package co.id.klikacara.di.module

import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.ProfilePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @Provides
    fun provideHomePresenter(
        view: MainContract.ProfileView
    ): ProfilePresenter {
        return ProfilePresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance(), FirebaseRemoteConfig.getInstance())
    }
}