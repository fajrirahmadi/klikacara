package co.id.klikacara.main.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.`object`.adapter.OrderListAdapter
import co.id.klikacara.`object`.authentication.Role
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.authentication.view.AuthenticationActivity
import co.id.klikacara.base.view.adapter.ViewPagerAdapter
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.OrderPresenter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_main_order.*
import java.util.ArrayList
import javax.inject.Inject

class OrderFragment : BaseFragment(), MainContract.OrderView {

    lateinit var pagerAdapter: ViewPagerAdapter
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getInflate(inflater, R.layout.fragment_main_order, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbarNoHome(view, "Order")
        configureAdapter()
    }

    private fun configureAdapter() {
        pagerAdapter = ViewPagerAdapter(childFragmentManager)
        orderPager.adapter = pagerAdapter
        orderTab.setupWithViewPager(orderPager, true)
        orderTab.tabMode = TabLayout.MODE_SCROLLABLE
    }

    override fun onResume() {
        super.onResume()
        if (firebaseAuth.currentUser == null)
            showNotLoginArea()
        else
            showLoginArea()
    }

    private fun showLoginArea() {
        database.collection(BuildConfig.userDb).document(firebaseAuth.uid!!).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val user = it.result!!.toObject(User::class.java)
                    if (user != null) {
                        setUserRole(user.type)
                    }
                }
            }
        areaContent.visibility = View.VISIBLE
        notLoginArea.visibility = View.GONE
    }

    private fun showNotLoginArea() {
        areaContent.visibility = View.GONE
        notLoginArea.visibility = View.VISIBLE
    }

    @OnClick(R.id.loginButton)
    fun onLoginButtonClicked() {
        showActivity(getIntent(activity!!, AuthenticationActivity::class.java))
    }

    override fun setUserRole(type: Role) {
        pagerAdapter.clearFragment()
        when (type) {
            Role.VENDOR -> {
                pagerAdapter.addFragment(
                    OrderPagerFragment.newInstance(PaymentStatus.PESANAN_DITERIMA),
                    PaymentStatus.PESANAN_DITERIMA.description
                )
                pagerAdapter.addFragment(
                    OrderPagerFragment.newInstance(PaymentStatus.PESANAN_DIPROSES),
                    PaymentStatus.PESANAN_DIPROSES.description
                )
            }
            Role.ADMIN, Role.PENGGUNA -> {
                pagerAdapter.addFragment(
                    OrderPagerFragment.newInstance(PaymentStatus.MENUNGGU_PEMBAYARAN),
                    PaymentStatus.MENUNGGU_PEMBAYARAN.description
                )
                pagerAdapter.addFragment(
                    OrderPagerFragment.newInstance(PaymentStatus.VERIFIKASI_PEMBAYARAN),
                    PaymentStatus.VERIFIKASI_PEMBAYARAN.description
                )
                pagerAdapter.addFragment(
                    OrderPagerFragment.newInstance(PaymentStatus.PESANAN_DITERIMA),
                    PaymentStatus.PESANAN_DITERIMA.description
                )
                pagerAdapter.addFragment(
                    OrderPagerFragment.newInstance(PaymentStatus.PESANAN_DIPROSES),
                    PaymentStatus.PESANAN_DIPROSES.description
                )
                pagerAdapter.addFragment(
                    OrderPagerFragment.newInstance(PaymentStatus.PESANAN_DIBATALKAN),
                    PaymentStatus.PESANAN_DIBATALKAN.description
                )
            }
            else -> {
            }
        }
        pagerAdapter.addFragment(
            OrderPagerFragment.newInstance(PaymentStatus.PESANAN_SELESAI),
            PaymentStatus.PESANAN_SELESAI.description
        )
        pagerAdapter.notifyDataSetChanged()
    }

    override fun setOrderListAdapter(orderListAdapter: ArrayList<OrderListAdapter>) {
        (pagerAdapter.getItem(0) as OrderPagerFragment).setOrderListAdapter(orderListAdapter)
    }

    override fun showNoOrderFound() {
        (pagerAdapter.getItem(0) as OrderPagerFragment).showNoOrderFound()
    }

}