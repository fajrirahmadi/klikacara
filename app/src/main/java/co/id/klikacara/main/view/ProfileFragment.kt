package co.id.klikacara.main.view

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindString
import butterknife.OnClick
import co.id.klikacara.R
import co.id.klikacara.`object`.adapter.ProfileMenuAdapter
import co.id.klikacara.`object`.authentication.Role
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.authentication.view.AuthenticationActivity
import co.id.klikacara.authentication.view.ChangePasswordActivity
import co.id.klikacara.authentication.view.RegistrationActivity
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.ProfilePresenter
import co.id.klikacara.product.view.MyProductActivity
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_main_profile.*
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

class ProfileFragment : BaseFragment(), MainContract.ProfileView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var profilePresenter: ProfilePresenter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @BindString(R.string.menu_edit_profile)
    lateinit var menuEditProfile: String
    @BindString(R.string.menu_product)
    lateinit var menuProduct: String
    @BindString(R.string.menu_change_password)
    lateinit var menuChangePassword: String
    @BindString(R.string.menu_about)
    lateinit var menuAbout: String
    @BindString(R.string.menu_contact_us)
    lateinit var menuContactUs: String
    @BindString(R.string.menu_privacy_policy)
    lateinit var menuPrivacyPolicy: String
    @BindString(R.string.menu_logout)
    lateinit var menuLogout: String

    private val codeEditProfile = 0
    private val codeProduct = 1
    private val codeChangePassword = 2
    private val codeAbout = 3
    private val codeContactUs = 4
    private val codePrivacyPolicy = 5
    private val codeLogout = 6
    private var isTokoAdded = false

    private val profileMenuAdapter = FastItemAdapter<ProfileMenuAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_main_profile, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeProfile.setOnRefreshListener(this)
        configureProfileMenuAdapter()
        configureToolbarNoHome(view, "Profil")
        profilePresenter.initAuthHelper(activity!!)
        profilePresenter.checkLoginStatus()
        profilePresenter.getProfile()
    }

    override fun onRefresh() {
        swipeProfile.isRefreshing = false
        profilePresenter.getProfile()
    }

    private fun configureProfileMenuAdapter() {
        configureItemAdapter(profileMenuAdapter, profileMenuRecycleView)
        profileMenuAdapter.withOnClickListener { _, _, item, _ ->
            handleOnMenuClicked(item.code)
            true
        }
        profileMenuAdapter.clear()
        profileMenuAdapter.add(ProfileMenuAdapter(R.drawable.ic_edit_profile, menuEditProfile, codeEditProfile))
        profileMenuAdapter.add(
            ProfileMenuAdapter(
                R.drawable.ic_change_password,
                menuChangePassword,
                codeChangePassword
            )
        )
        profileMenuAdapter.add(ProfileMenuAdapter(R.drawable.ic_about, menuAbout, codeAbout))
        profileMenuAdapter.add(ProfileMenuAdapter(R.drawable.ic_contact_us, menuContactUs, codeContactUs))
        //profileMenuAdapter.add(ProfileMenuAdapter(R.drawable.menu_list, menuPrivacyPolicy, codePrivacyPolicy))
        profileMenuAdapter.add(ProfileMenuAdapter(R.drawable.ic_logout, menuLogout, codeLogout))
    }

    private fun handleOnMenuClicked(code: Int) {
        when (code) {
            codeEditProfile -> {
                showActivity(getIntent(activity!!, ProfileDetailActivity::class.java))
            }
            codeProduct -> {
                showActivity(getIntent(activity!!, MyProductActivity::class.java))
            }
            codeChangePassword -> {
                showActivity(getIntent(activity!!, ChangePasswordActivity::class.java))
            }
            codeAbout -> {

            }
            codeContactUs -> {

            }
            codeLogout -> {
                profilePresenter.doLogout()
            }
        }
    }

    override fun showLoginArea() {
        loginArea.visibility = View.VISIBLE
        notLoginArea.visibility = View.GONE
        loadingProfile.visibility = View.GONE
    }

    override fun showNotLoginArea() {
        notLoginArea.visibility = View.VISIBLE
        loginArea.visibility = View.GONE
        loadingProfile.visibility = View.GONE
    }

    @OnClick(R.id.registerVendorButton)
    fun onRegisterVendorClicked() {
        showActivity(getIntent(activity!!, RegistrationActivity::class.java))
    }

    @OnClick(R.id.loginButton)
    fun onLoginButtonClicked() {
        showActivity(getIntent(activity!!, AuthenticationActivity::class.java))
    }

    override fun doOnLogoutSuccess() {
        showActivityAndFinishAllActivity(getIntent(activity!!, MainActivity::class.java))
    }

    override fun setUserData(user: User) {
        if (StringUtils.isNotBlank(user.url))
            GlideUtils.setFotoCircleFromStorage(
                activity!!,
                "user/${user.url}",
                profileImageView
            )
        else
            profileImageView.setImageResource(R.drawable.logo_klikacara)
        nameTextView.text = user.name
        roleTextView.text = user.type.description
        if (Role.PENGGUNA == user.type)
            ViewHelper.showView(registerVendorButton)
        else {
            if (!isTokoAdded) {
                profileMenuAdapter.add(1, ProfileMenuAdapter(R.drawable.ic_product, menuProduct, codeProduct))
                isTokoAdded = true
            }
            ViewHelper.hideView(registerVendorButton)
        }
    }
}