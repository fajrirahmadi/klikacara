package co.id.klikacara.di.module.view

import co.id.klikacara.createevent.contract.EventContract
import co.id.klikacara.createevent.view.DetailEventActivity
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.view.*
import co.id.klikacara.permission.contract.PermissionContract
import dagger.Binds
import dagger.Module

@Module
abstract class MainViewModule {
    @Binds
    internal abstract fun provideProfileFragment(profileFragment: ProfileActivity):
            MainContract.ProfileView

    @Binds
    internal abstract fun provideOrderFragment(orderFragment: OrderActivity):
            MainContract.OrderView

    @Binds
    internal abstract fun provideHomeFragment(homeFragment: MainActivity):
            MainContract.HomeView

    @Binds
    internal abstract fun provideProfileDetailActivity(profileDetailActivity: ProfileDetailActivity):
            MainContract.ProfileDetailView

    @Binds
    internal abstract fun provideProfileDetailPermissionActivity(profileDetailActivity: ProfileDetailActivity):
            PermissionContract.View

    @Binds
    internal abstract fun provideEventDetailActivity(detailEventActivity: DetailEventActivity):
            EventContract.DetailEventView
}