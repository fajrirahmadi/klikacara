package co.id.klikacara.di.module.view

import co.id.klikacara.createevent.view.EventDetailFragment
import co.id.klikacara.createevent.view.OrderAddressAndTimeFragment
import co.id.klikacara.master.contract.MasterContract
import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.view.*
import co.id.klikacara.permission.contract.PermissionContract
import dagger.Binds
import dagger.Module

@Module
abstract class CreateEventViewModule {

    @Binds
    internal abstract fun provideCreateEventDetailPermissionActivity(createEventDetailFragment: EventDetailFragment):
            PermissionContract.View

    @Binds
    internal abstract fun provideOrderAddressFragment(orderAddressAndTimeFragment: OrderAddressAndTimeFragment):
            MasterContract.MasterAddressView
}