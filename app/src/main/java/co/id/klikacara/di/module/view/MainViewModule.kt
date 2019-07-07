package co.id.klikacara.di.module.view

import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.view.HomeFragment
import co.id.klikacara.main.view.OrderFragment
import co.id.klikacara.main.view.ProfileDetailActivity
import co.id.klikacara.main.view.ProfileFragment
import co.id.klikacara.permission.contract.PermissionContract
import dagger.Binds
import dagger.Module

@Module
abstract class MainViewModule {
    @Binds
    internal abstract fun provideProfileFragment(profileFragment: ProfileFragment):
            MainContract.ProfileView

    @Binds
    internal abstract fun provideOrderFragment(orderFragment: OrderFragment):
            MainContract.OrderView

    @Binds
    internal abstract fun provideHomeFragment(homeFragment: HomeFragment):
            MainContract.HomeView

    @Binds
    internal abstract fun provideProfileDetailActivity(profileDetailActivity: ProfileDetailActivity):
            MainContract.ProfileDetailView

    @Binds
    internal abstract fun provideProfileDetailPermissionActivity(profileDetailActivity: ProfileDetailActivity):
            PermissionContract.View
}