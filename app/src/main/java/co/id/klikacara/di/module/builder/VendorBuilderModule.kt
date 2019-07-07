package co.id.klikacara.di.module.builder

import co.id.klikacara.di.module.ProductOwnModule
import co.id.klikacara.di.module.view.VendorViewModule
import co.id.klikacara.vendor.view.VendorProductFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class VendorBuilderModule {

    @ContributesAndroidInjector(modules = [VendorViewModule::class, ProductOwnModule::class])
    internal abstract fun bindMitraProductFragment(): VendorProductFragment


}