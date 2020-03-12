package co.id.klikacara.di.module.builder

import co.id.klikacara.di.module.*
import co.id.klikacara.di.module.view.OrderViewModule
import co.id.klikacara.order.view.*
import co.id.klikacara.order.view.fragment.OrderAddressFragment
import co.id.klikacara.order.view.fragment.OrderConfirmationFragment
import co.id.klikacara.order.view.fragment.OrderDateFragment
import co.id.klikacara.order.view.fragment.OrderPaymentFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OrderBuilderModule {

    @ContributesAndroidInjector(modules = [OrderViewModule::class, MasterAddressModule::class])
    internal abstract fun bindOrderAddressFragment(): OrderAddressFragment

    @ContributesAndroidInjector(modules = [OrderViewModule::class, OrderDateModule::class])
    internal abstract fun bindOrderDateFragment(): OrderDateFragment

    @ContributesAndroidInjector(modules = [OrderViewModule::class, OrderPaymentModule::class])
    internal abstract fun bindOrderPaymentFragment(): OrderPaymentFragment

    @ContributesAndroidInjector(modules = [OrderViewModule::class, OrderConfirmationModule::class])
    internal abstract fun bindOrderConfirmationFragment(): OrderConfirmationFragment

    @ContributesAndroidInjector(modules = [OrderViewModule::class, OrderDetailModule::class])
    internal abstract fun bindOrderDetailActivity(): OrderDetailActivity

    @ContributesAndroidInjector(modules = [OrderViewModule::class, RatingModule::class])
    internal abstract fun bindRatingModuleActivity(): RatingActivity

}