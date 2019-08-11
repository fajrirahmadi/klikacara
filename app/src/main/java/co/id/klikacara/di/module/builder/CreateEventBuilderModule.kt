package co.id.klikacara.di.module.builder

import co.id.klikacara.createevent.view.EventDetailFragment
import co.id.klikacara.createevent.view.OrderAddressAndTimeFragment
import co.id.klikacara.di.module.*
import co.id.klikacara.di.module.view.CreateEventViewModule
import co.id.klikacara.di.module.view.OrderViewModule
import co.id.klikacara.order.view.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CreateEventBuilderModule {

    @ContributesAndroidInjector(modules = [CreateEventViewModule::class, PermissionModule::class])
    internal abstract fun bindCreateEventDetailFragment(): EventDetailFragment

    @ContributesAndroidInjector(modules = [CreateEventViewModule::class, MasterAddressModule::class])
    internal abstract fun bindOrderAddressAndTimeFragment(): OrderAddressAndTimeFragment

}