package co.id.klikacara.main.view

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.adapter.OrderListAdapter
import co.id.klikacara.authentication.view.AuthenticationActivity
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.OrderPresenter
import co.id.klikacara.order.view.OrderDetailActivity
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_main_order.*
import org.parceler.Parcels
import javax.inject.Inject

class OrderFragment : BaseFragment(), MainContract.OrderView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var orderPresenter: OrderPresenter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    val orderAdapter = FastItemAdapter<OrderListAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_main_order, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeOrder.setOnRefreshListener(this)
        configureToolbarNoHome(view, "Order")
        configureAdapter()
    }

    override fun onResume() {
        super.onResume()
        orderPresenter.checkLoginStatus()
    }

    private fun configureAdapter() {
        configureItemAdapter(orderAdapter, orderRecycleView)
        orderAdapter.withEventHook(object : ClickEventHook<OrderListAdapter>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder?): View? {
                return if (viewHolder is OrderListAdapter.ViewHolder) {
                    viewHolder.detailButton
                } else null
            }

            override fun onClick(
                view: View,
                position: Int,
                fastAdapter: FastAdapter<OrderListAdapter>,
                item: OrderListAdapter
            ) {
                val intent = getIntent(activity!!, OrderDetailActivity::class.java)
                intent.putExtra(BuildConfig.orderDb, Parcels.wrap(item.order))
                showActivity(intent)
            }
        })
    }

    override fun onRefresh() {
        swipeOrder.isRefreshing = false
        orderPresenter.checkLoginStatus()
    }

    override fun setOrderListAdapter(orderListAdapter: ArrayList<OrderListAdapter>) {
        orderAdapter.clear()
        orderAdapter.add(orderListAdapter)
        ViewHelper.hideView(noOrderArea)
        ViewHelper.showView(orderRecycleView)
        ViewHelper.hideView(loadingOrder)
    }

    override fun showNoOrderFound() {
        ViewHelper.hideView(loadingOrder)
        ViewHelper.hideView(orderRecycleView)
        ViewHelper.showView(noOrderArea)
    }

    override fun showLoginArea() {
        orderPresenter.checkRole()
        loginArea.visibility = View.VISIBLE
        notLoginArea.visibility = View.GONE
        loadingOrder.visibility = View.GONE
    }

    override fun showNotLoginArea() {
        loginArea.visibility = View.GONE
        notLoginArea.visibility = View.VISIBLE
        loadingOrder.visibility = View.GONE
    }

    @OnClick(R.id.loginButton)
    fun onLoginButtonClicked() {
        showActivity(getIntent(activity!!, AuthenticationActivity::class.java))
    }

}