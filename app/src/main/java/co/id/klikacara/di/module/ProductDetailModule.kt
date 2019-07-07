package co.id.klikacara.di.module

import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.product.presenter.ProductDetailPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class ProductDetailModule {

    @Provides
    fun provideProductDetailPresenter(
        view: ProductContract.ProductDetailView
    ): ProductDetailPresenter {
        return ProductDetailPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance(), FirebaseStorage.getInstance())
    }
}