package co.id.klikacara.di.module

import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.RegistrationUserPresenter
import co.id.klikacara.validator.ValidatorUsecase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class RegisterUserModule {

    @Provides
    fun provideRegisterUserPresenter(
        view: AuthenticationContract.RegisterUserView,
        useCase: ValidatorUsecase
    ): RegistrationUserPresenter {
        return RegistrationUserPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance(), useCase)
    }
}