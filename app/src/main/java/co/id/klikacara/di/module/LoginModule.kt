package co.id.klikacara.di.module

import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.LoginPresenter
import co.id.klikacara.validator.ValidatorUsecase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    @Provides
    fun provideLoginPresenter(
        view: AuthenticationContract.LoginView,
        useCase: ValidatorUsecase
    ): LoginPresenter {
        return LoginPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance(), useCase)
    }
}