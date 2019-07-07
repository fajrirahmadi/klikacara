package co.id.klikacara.di.module.view

import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.view.*
import co.id.klikacara.permission.contract.PermissionContract
import dagger.Binds
import dagger.Module

@Module
abstract class RegistrationViewModule {

    @Binds
    internal abstract fun provideRegisterUserFragment(registerUserFragment: RegisterUserFragment):
            AuthenticationContract.RegisterUserView

    @Binds
    internal abstract fun provideRegisterMitraFragment(registerMitraFragment: RegisterMitraFragment):
            AuthenticationContract.RegisterMitraView

    @Binds
    internal abstract fun provideRegisterUsahaFragment(registerUsahaFragment: RegisterUsahaFragment):
            AuthenticationContract.RegisterUsahaView

    @Binds
    internal abstract fun provideRegisterUsahaPermissionFragment(registerUsahaFragment: RegisterUsahaFragment):
            PermissionContract.View

}