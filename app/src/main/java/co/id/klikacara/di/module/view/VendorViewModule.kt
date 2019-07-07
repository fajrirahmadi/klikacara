package co.id.klikacara.di.module.view

import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.vendor.view.VendorProductFragment
import dagger.Binds
import dagger.Module

@Module
abstract class VendorViewModule {

    @Binds
    internal abstract fun provideMitraProductFragment(vendorProductFragment: VendorProductFragment):
            ProductContract.MyProductView
}