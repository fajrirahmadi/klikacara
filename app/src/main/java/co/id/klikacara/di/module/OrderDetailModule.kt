package co.id.klikacara.di.module

import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.presenter.OrderDetailPresenter
import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.presenter.PermissionPresenter
import co.id.klikacara.permission.usecase.PermissionUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class OrderDetailModule {

    @Provides
    fun provideOrderDetailPresenter(
        view: OrderContract.OrderDetailView
    ): OrderDetailPresenter {
        return OrderDetailPresenter(
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