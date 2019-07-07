package co.id.klikacara.di.module

import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.presenter.PermissionPresenter
import co.id.klikacara.permission.usecase.PermissionUseCase
import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.product.presenter.AddProductPresenter
import co.id.klikacara.validator.ValidatorUsecase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class AddProductModule {

    @Provides
    fun provideAddProductPresenter(
        view: ProductContract.AddProductView,
        validator: ValidatorUsecase
    ): AddProductPresenter {
        return AddProductPresenter(view, validator, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance(), FirebaseStorage.getInstance())
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