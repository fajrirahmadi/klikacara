package co.id.klikacara.di.module.builder

import co.id.klikacara.authentication.view.ChangePasswordActivity
import co.id.klikacara.authentication.view.ForgotPasswordFragment
import co.id.klikacara.authentication.view.LoginFragment
import co.id.klikacara.di.module.ChangePasswordModule
import co.id.klikacara.di.module.ForgetPasswordModule
import co.id.klikacara.di.module.LoginModule
import co.id.klikacara.di.module.view.AuthenticationViewModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthenticationBuilderModule {

    @ContributesAndroidInjector(modules = [AuthenticationViewModule::class, LoginModule::class])
    internal abstract fun bindLoginFragment(): LoginFragment

    @ContributesAndroidInjector(modules = [AuthenticationViewModule::class, ChangePasswordModule::class])
    internal abstract fun bindChangePasswordActivity(): ChangePasswordActivity

    @ContributesAndroidInjector(modules = [AuthenticationViewModule::class, ForgetPasswordModule::class])
    internal abstract fun bindForgetPasswordFragment(): ForgotPasswordFragment


}