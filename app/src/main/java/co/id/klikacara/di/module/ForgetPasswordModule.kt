package co.id.klikacara.di.module

import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.ForgetPasswordPresenter
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class ForgetPasswordModule {

    @Provides
    fun provideForgetPasswordPresenter(
        view: AuthenticationContract.ForgetPasswordView
    ): ForgetPasswordPresenter {
        return ForgetPasswordPresenter(view, FirebaseAuth.getInstance())
    }
}