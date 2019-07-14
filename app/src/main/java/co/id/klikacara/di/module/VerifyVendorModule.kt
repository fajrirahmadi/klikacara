package co.id.klikacara.di.module

import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.VerifyVendorPresenter
import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.presenter.PermissionPresenter
import co.id.klikacara.permission.usecase.PermissionUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class VerifyVendorModule {

    @Provides
    fun provideVerifyPresenter(
        view: MainContract.VerifyVendorView
    ): VerifyVendorPresenter {
        return VerifyVendorPresenter(
            view,
            FirebaseAuth.getInstance(),
            FirebaseFirestore.getInstance(),
            FirebaseStorage.getInstance()
        )
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