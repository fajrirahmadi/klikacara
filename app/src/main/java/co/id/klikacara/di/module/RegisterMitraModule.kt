package co.id.klikacara.di.module

import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.RegistrationMitraPresenter
import co.id.klikacara.validator.ValidatorUsecase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class RegisterMitraModule {

    @Provides
    fun provideRegisterUserPresenter(
        view: AuthenticationContract.RegisterMitraView,
        useCase: ValidatorUsecase
    ): RegistrationMitraPresenter {
        return RegistrationMitraPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance(), useCase)
    }
}