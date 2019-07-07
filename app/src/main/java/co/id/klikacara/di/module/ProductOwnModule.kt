package co.id.klikacara.di.module

import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.product.presenter.MyProductPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class ProductOwnModule {

    @Provides
    fun provideProductListPresenter(
        view: ProductContract.MyProductView
    ): MyProductPresenter {
        return MyProductPresenter(view, FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    }
}