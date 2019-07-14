package co.id.klikacara.authentication.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import butterknife.BindDrawable
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.authentication.RegistrationVendor
import co.id.klikacara.`object`.authentication.Role
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.RegistrationUserPresenter
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.main.view.MainActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_authentication_registration_user.*
import org.jetbrains.anko.textColor
import org.parceler.Parcels
import javax.inject.Inject

class RegisterUserFragment : BaseFragment(), AuthenticationContract.RegisterUserView {

    @Inject
    lateinit var registrationUserPresenter: RegistrationUserPresenter

    private var isPengguna = true

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @BindDrawable(R.drawable.base_button_primary)
    lateinit var selectedButton: Drawable
    @BindDrawable(R.drawable.base_border_primary)
    lateinit var unselectedButton: Drawable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_authentication_registration_user, container)
    }

    override fun onResume() {
        super.onResume()
        changeSelectedButton()
    }

    override fun doOnSubmitRegistrationSuccess(user: User) {
        if (isPengguna) {
            registrationUserPresenter.registerUser(user, passwordEditText.text.toString())
        } else {
            val bundle = Bundle()
            val registrationVendor = RegistrationVendor()
            registrationVendor.user = user
            registrationVendor.password = passwordEditText.text.toString()
            bundle.putParcelable(BuildConfig.registrationData, Parcels.wrap(registrationVendor))
            navigateTo(
                R.id.registrationNavGraph,
                R.id.action_move_fromRegisterUserFragment_toRegisterMitraFragment,
                bundle
            )
        }
    }

    fun changeSelectedButton() {
        if (isPengguna) {
            penggunaButton.background = selectedButton
            penggunaButton.textColor = ContextCompat.getColor(activity!!, R.color.white)
            vendorButton.background = unselectedButton
            vendorButton.textColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)
            submitButton.text = "Submit"
        } else {
            vendorButton.background = selectedButton
            vendorButton.textColor = ContextCompat.getColor(activity!!, R.color.white)
            penggunaButton.background = unselectedButton
            penggunaButton.textColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)
            submitButton.text = "Selanjutnya"
        }
    }

    @OnClick(R.id.penggunaButton)
    fun onPenggunaButtonClicked() {
        isPengguna = true
        changeSelectedButton()
    }

    @OnClick(R.id.vendorButton)
    fun onVendorButtonClicked() {
        isPengguna = false
        changeSelectedButton()
    }

    @OnClick(R.id.submitButton)
    fun onSubmitButtonClicked() {
        val user = User()
        user.name = nameEditText.text.toString()
        user.email = emailEditText.text.toString()
        if (isPengguna) {
            user.type = Role.PENGGUNA
        } else {
            user.type = Role.VENDOR
        }
        registrationUserPresenter.submitRegistrationUser(
            user,
            passwordEditText.text.toString(),
            confirmPasswordEditText.text.toString()
        )
    }

    override fun doOnRegisterSuccess() {
        showSuccess("Berhasil melakukan pendaftaran", View.OnClickListener {
            infoDialog.dismissAllowingStateLoss()
            showActivityAndFinishAllActivity(getIntent(activity!!, MainActivity::class.java))
        })
    }

    override fun doOnRegisterFailed() {
        showError("Gagal melakukan pendaftaran, cobalah beberapa saat lagi")
    }
}