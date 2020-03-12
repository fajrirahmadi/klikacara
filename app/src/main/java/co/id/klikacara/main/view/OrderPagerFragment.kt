package co.id.klikacara.main.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.`object`.adapter.OrderListAdapter
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.order.view.OrderDetailActivity
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.content_order_by_status.*
import org.parceler.Parcels
import java.util.ArrayList

class OrderPagerFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val paymentStatus = "PAYMENT_STATUS"

        @JvmStatic
        fun newInstance(status: PaymentStatus) = OrderPagerFragment().apply {
            arguments = Bundle().apply {
                putString(paymentStatus, status.toString())
            }
        }
    }

    private val orderAdapter = FastItemAdapter<OrderListAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getInflate(inflater, R.layout.content_order_by_status, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeOrder.setOnRefreshListener(this)
        configureAdapter()
    }

    private fun configureAdapter() {
        configureItemAdapter(orderAdapter, orderRecycleView)
        orderAdapter.withEventHook(object : ClickEventHook<OrderListAdapter>() {
            override fun onBind(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?): View? {
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
    }

    fun setOrderListAdapter(orderListAdapter: ArrayList<OrderListAdapter>) {
        orderAdapter.clear()
        orderAdapter.add(orderListAdapter)
        ViewHelper.hideView(noOrderArea)
        ViewHelper.showView(orderRecycleView)
        ViewHelper.hideView(loadingOrder)
    }

    fun showNoOrderFound() {
        ViewHelper.hideView(loadingOrder)
        ViewHelper.hideView(orderRecycleView)
        ViewHelper.showView(noOrderArea)
    }


}