package co.id.klikacara.di.module.builder

import co.id.klikacara.di.module.AddProductModule
import co.id.klikacara.di.module.ProductDetailModule
import co.id.klikacara.di.module.ProductListModule
import co.id.klikacara.di.module.ProductOwnModule
import co.id.klikacara.di.module.view.ProductViewModule
import co.id.klikacara.product.view.AddProductFragment
import co.id.klikacara.product.view.MyProductFragment
import co.id.klikacara.product.view.ProductDetailActivity
import co.id.klikacara.product.view.ProductListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProductBuilderModule {

    @ContributesAndroidInjector(modules = [ProductViewModule::class, ProductListModule::class])
    internal abstract fun bindProductListFragment(): ProductListFragment

    @ContributesAndroidInjector(modules = [ProductViewModule::class, ProductDetailModule::class])
    internal abstract fun bindProductDetailFragment(): ProductDetailActivity

    @ContributesAndroidInjector(modules = [ProductViewModule::class, ProductOwnModule::class])
    internal abstract fun bindProductOwnFragment(): MyProductFragment

    @ContributesAndroidInjector(modules = [ProductViewModule::class, AddProductModule::class])
    internal abstract fun bindAddProductFragment(): AddProductFragment


}