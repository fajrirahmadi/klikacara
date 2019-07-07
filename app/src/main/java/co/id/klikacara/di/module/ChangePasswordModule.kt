package co.id.klikacara.di.module

import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.ChangePasswordPresenter
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class ChangePasswordModule {

    @Provides
    fun provideChangePasswordPresenter(
        view: AuthenticationContract.ChangePasswordView
    ): ChangePasswordPresenter {
        return ChangePasswordPresenter(view, FirebaseAuth.getInstance())
    }
}