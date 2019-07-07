package co.id.klikacara.di.module

import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.RegistrationUsahaPresenter
import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.presenter.PermissionPresenter
import co.id.klikacara.permission.usecase.PermissionUseCase
import co.id.klikacara.validator.ValidatorUsecase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class RegisterUsahaModule {

    @Provides
    fun provideRegisterUserPresenter(
        view: AuthenticationContract.RegisterUsahaView,
        useCase: ValidatorUsecase
    ): RegistrationUsahaPresenter {
        return RegistrationUsahaPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance(), FirebaseStorage.getInstance())
    }

    @Provides
    fun providePermissionPresenter(
        view: PermissionContract.View,
        useCase: PermissionUseCase
    ): PermissionPresenter {
        return PermissionPresenter(
            view,
            useCase
        )
    }
}