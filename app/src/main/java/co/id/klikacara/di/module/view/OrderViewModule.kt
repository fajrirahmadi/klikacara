package co.id.klikacara.di.module.view

import co.id.klikacara.master.contract.MasterContract
import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.view.*
import co.id.klikacara.permission.contract.PermissionContract
import dagger.Binds
import dagger.Module

@Module
abstract class OrderViewModule {
    @Binds
    internal abstract fun provideOrderAddressFragment(orderAddressFragment: OrderAddressFragment):
            MasterContract.MasterAddressView

    @Binds
    internal abstract fun provideOrderDateFragment(orderDateFragment: OrderDateFragment):
            OrderContract.OrderDateView

    @Binds
    internal abstract fun provideOrderPaymentFragment(orderPaymentFragment: OrderPaymentFragment):
            OrderContract.OrderPaymentView

    @Binds
    internal abstract fun provideOrderConfirmationFragment(orderConfirmationFragment: OrderConfirmationFragment):
            OrderContract.OrderConfirmationView

    @Binds
    internal abstract fun provideOrderDetailActivity(orderDetailActivity: OrderDetailActivity):
            OrderContract.OrderDetailView

    @Binds
    internal abstract fun provideOrderDetailPermissionActivity(orderDetailActivity: OrderDetailActivity):
            PermissionContract.View

    @Binds
    internal abstract fun provideRatingActivity(ratingActivity: RatingActivity):
            OrderContract.RatingView

}