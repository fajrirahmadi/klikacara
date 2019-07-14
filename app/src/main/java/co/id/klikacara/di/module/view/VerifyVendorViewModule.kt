package co.id.klikacara.di.module.view

import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.view.profile.VerifyVendorActivity
import co.id.klikacara.permission.contract.PermissionContract
import dagger.Binds
import dagger.Module

@Module
abstract class VerifyVendorViewModule {

    @Binds
    internal abstract fun provideVerifyVendorActivity(verifyVendorActivity: VerifyVendorActivity):
            MainContract.VerifyVendorView

    @Binds
    internal abstract fun provideVerifyVendorPermissionActivity(verifyVendorActivity: VerifyVendorActivity):
            PermissionContract.View
}