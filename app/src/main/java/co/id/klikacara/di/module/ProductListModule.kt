package co.id.klikacara.di.module

import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.product.presenter.ProductListPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class ProductListModule {

    @Provides
    fun provideProductListPresenter(
        view: ProductContract.ProductListView
    ): ProductListPresenter {
        return ProductListPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance(), FirebaseStorage.getInstance())
    }
}