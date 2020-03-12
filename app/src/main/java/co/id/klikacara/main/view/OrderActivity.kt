package co.id.klikacara.main.view

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindString
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.`object`.adapter.OrderListAdapter
import co.id.klikacara.`object`.authentication.Role
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.OrderPresenter
import co.id.klikacara.order.view.OrderDetailActivity
import co.id.klikacara.order.view.ProductOrderActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_list_of_view.*
import kotlinx.android.synthetic.main.base_appbar_primary_with_title.*
import kotlinx.android.synthetic.main.content_error.*
import kotlinx.android.synthetic.main.content_loading.*
import org.parceler.Parcels
import javax.inject.Inject

class OrderActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener, MainContract.OrderView {

    @Inject
    lateinit var orderPresenter: OrderPresenter

    @BindString(R.string.label_order)
    lateinit var labelOrder: String

    private val orderAdapter = FastItemAdapter<OrderListAdapter>()
    private lateinit var role: Role

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_view)
        configureBackButton()
        titleToolbar.text = labelOrder
        swipeList.setOnRefreshListener(this)
        configureItemAdapter(orderAdapter, listOfViewRecycleView)
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
                if (item.order.paymentStatus == PaymentStatus.MENCARI_VENDOR) {
                    if (role == Role.PENGGUNA) {
                        val intentToProductRecomend =
                            getIntent(this@OrderActivity, ProductOrderActivity::class.java)
                        intentToProductRecomend.putExtra(
                            BuildConfig.orderDb,
                            Parcels.wrap(item.order)
                        )
                        showActivity(intentToProductRecomend)
                    } else if (role == Role.ADMIN) {
                        val intentToSendRecomendation =
                            getIntent(this@OrderActivity, ProductOrderActivity::class.java)
                        intentToSendRecomendation.putExtra(
                            BuildConfig.orderDb,
                            Parcels.wrap(item.order)
                        )
                        showActivity(intentToSendRecomendation)
                    }

                } else {
                    val intent = getIntent(this@OrderActivity, OrderDetailActivity::class.java)
                    intent.putExtra(BuildConfig.orderDb, Parcels.wrap(item.order))
                    showActivity(intent)
                }
            }
        })
        loadData()
    }

    override fun onRefresh() {
        swipeList.isRefreshing = false
        loadData()
    }

    private fun loadData() {
        ViewHelper.showView(loadingArea)
        orderAdapter.clear()
        orderPresenter.checkRole()
    }

    override fun setOrderListAdapter(orderListAdapter: ArrayList<OrderListAdapter>) {
        ViewHelper.hideView(errorArea)
        ViewHelper.showView(listOfViewRecycleView)
        orderAdapter.add(orderListAdapter)
    }

    override fun showNoOrderFound() {
        ViewHelper.showView(errorArea)
        ViewHelper.hideView(listOfViewRecycleView)
    }

    override fun setUserRole(type: Role) {
        this.role = type
        when (type) {
            Role.VENDOR -> {
                orderPresenter.getOrderByVendorId()
            }
            Role.ADMIN -> {
                orderPresenter.getAllOrder()
            }
            Role.PENGGUNA -> {
                orderPresenter.getOrderList()
            }
            Role.AMBASADOR -> {

            }
        }
    }

    override fun showProgressDialog() {
        ViewHelper.showView(loadingArea)
    }

    override fun dismissProgressDialog() {
        ViewHelper.hideView(loadingArea)
    }
}