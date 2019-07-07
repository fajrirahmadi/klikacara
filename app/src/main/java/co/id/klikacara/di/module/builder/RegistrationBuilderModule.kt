package co.id.klikacara.di.module.builder

import co.id.klikacara.authentication.view.*
import co.id.klikacara.di.module.*
import co.id.klikacara.di.module.view.AuthenticationViewModule
import co.id.klikacara.di.module.view.RegistrationViewModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RegistrationBuilderModule {

    @ContributesAndroidInjector(modules = [RegistrationViewModule::class, RegisterUserModule::class])
    internal abstract fun bindRegisterUserFragment(): RegisterUserFragment

    @ContributesAndroidInjector(modules = [RegistrationViewModule::class, RegisterMitraModule::class])
    internal abstract fun bindRegisterMitraFragment(): RegisterMitraFragment

    @ContributesAndroidInjector(modules = [RegistrationViewModule::class, RegisterUsahaModule::class])
    internal abstract fun bindRegisterUsahaFragment(): RegisterUsahaFragment

}