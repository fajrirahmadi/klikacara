package co.id.klikacara.di.module.view

import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.product.view.AddProductFragment
import co.id.klikacara.product.view.MyProductFragment
import co.id.klikacara.product.view.ProductDetailActivity
import co.id.klikacara.product.view.ProductListFragment
import dagger.Binds
import dagger.Module

@Module
abstract class ProductViewModule {
    @Binds
    internal abstract fun provideProductListFragment(productListFragment: ProductListFragment):
            ProductContract.ProductListView

    @Binds
    internal abstract fun provideProductDetailFragment(productDetailActivity: ProductDetailActivity):
            ProductContract.ProductDetailView

    @Binds
    internal abstract fun provideProductOwnFragment(myProductFragment: MyProductFragment):
            ProductContract.MyProductView

    @Binds
    internal abstract fun provideAddProductFragment(addProductFragment: AddProductFragment):
            ProductContract.AddProductView

    @Binds
    internal abstract fun provideAddProductPermissionFragment(addProductFragment: AddProductFragment):
            PermissionContract.View
}