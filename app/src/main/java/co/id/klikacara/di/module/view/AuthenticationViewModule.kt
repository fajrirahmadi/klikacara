package co.id.klikacara.di.module.view

import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.view.*
import co.id.klikacara.permission.contract.PermissionContract
import dagger.Binds
import dagger.Module

@Module
abstract class AuthenticationViewModule {
    @Binds
    internal abstract fun provideLoginFragment(loginFragment: LoginFragment):
            AuthenticationContract.LoginView

    @Binds
    internal abstract fun provideChangePasswordActivity(changePasswordActivity: ChangePasswordActivity):
            AuthenticationContract.ChangePasswordView

    @Binds
    internal abstract fun provideForgetPasswordFragment(forgotPasswordFragment: ForgotPasswordFragment):
            AuthenticationContract.ForgetPasswordView

}