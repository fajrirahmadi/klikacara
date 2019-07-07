package co.id.klikacara.di.module.builder

import co.id.klikacara.di.module.HomeModule
import co.id.klikacara.di.module.OrderModule
import co.id.klikacara.di.module.ProfileDetailModule
import co.id.klikacara.di.module.ProfileModule
import co.id.klikacara.di.module.view.MainViewModule
import co.id.klikacara.main.view.HomeFragment
import co.id.klikacara.main.view.OrderFragment
import co.id.klikacara.main.view.ProfileDetailActivity
import co.id.klikacara.main.view.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainBuilderModule {

    @ContributesAndroidInjector(modules = [MainViewModule::class, ProfileModule::class])
    internal abstract fun bindProfileFragment(): ProfileFragment

    @ContributesAndroidInjector(modules = [MainViewModule::class, OrderModule::class])
    internal abstract fun bindOrderFragment(): OrderFragment

    @ContributesAndroidInjector(modules = [MainViewModule::class, HomeModule::class])
    internal abstract fun bindHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [MainViewModule::class, ProfileDetailModule::class])
    internal abstract fun bindProfileDetailActivity(): ProfileDetailActivity

}