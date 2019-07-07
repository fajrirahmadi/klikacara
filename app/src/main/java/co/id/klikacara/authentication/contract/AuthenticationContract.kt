package co.id.klikacara.authentication.contract

import android.content.Intent
import co.id.klikacara.`object`.adapter.TextAdapter
import co.id.klikacara.`object`.authentication.RegistrationVendor
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.contract.BaseContract

interface AuthenticationContract {

    interface LoginView : BaseContract.View {
        fun showEmailInvalid()
        fun showPasswordInvalid()
        fun doOnLoginSuccess()
        fun doLoginWithGoogle(intent: Intent)

    }

    interface RegisterUserView : BaseContract.View {
        fun doOnSubmitRegistrationSuccess(user: User)
        fun doOnRegisterSuccess()
        fun doOnRegisterFailed()

    }

    interface RegisterMitraView : BaseContract.View {
        fun setListProvince(listProvince: List<TextAdapter>)
        fun setListCity(listMasterDataAdapter: List<TextAdapter>)
        fun setListDistrict(listMasterDataAdapter: List<TextAdapter>)
    }

    interface RegisterUsahaView : BaseContract.View {
        fun doOnRegisterSuccess()
        fun doOnRegisterFailed()
        fun handleOnRegisterUserSuccess(registrationVendor: RegistrationVendor)
    }

    interface ForgetPasswordView : BaseContract.View {
        fun doOnResetPasswordSuccess()

    }

    interface ChangePasswordView : BaseContract.View {
        fun doOnChangePasswordSuccess()
        fun doOnChangePasswordFailed()
    }
}