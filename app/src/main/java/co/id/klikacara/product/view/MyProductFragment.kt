package co.id.klikacara.product.view

import android.content.Context
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.adapter.ProductAdapter
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.product.presenter.MyProductPresenter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_product_list.*
import org.parceler.Parcels
import javax.inject.Inject

class MyProductFragment : BaseFragment(), ProductContract.MyProductView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var productListPresenter: MyProductPresenter

    private val productListAdapter = FastItemAdapter<ProductAdapter>()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_product_list, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productListSwipe.setOnRefreshListener(this)
        configureProductAdapter()
        ViewHelper.hideView(titleProduct)
        ViewHelper.hideView(descriptionProduct)
        productListPresenter.getListProduct()
    }

    override fun onRefresh() {
        productListSwipe.isRefreshing = false
        productListPresenter.getListProduct()
    }

    private fun configureProductAdapter() {
        configureGridItemAdapter(productListAdapter, productRecycleView, 2)
        productListAdapter.withOnClickListener { _, _, item, _ ->
            val bundle = Bundle()
            bundle.putParcelable(BuildConfig.productDb, Parcels.wrap(item.product))
            navigateTo(
                R.id.myProductNavGraph,
                R.id.action_move_fromMyProductFragment_toAddProductFragment,
                bundle
            )
            true
        }
    }

    override fun doOnGetListProductSuccess(productListAdapter: ArrayList<ProductAdapter>) {
        ViewHelper.hideView(loadingProductList)
        ViewHelper.hideView(areaNoPackage)
        ViewHelper.showView(productRecycleView)
        this.productListAdapter.clear()
        this.productListAdapter.add(productListAdapter)
    }

    override fun doOnGetListProductFailed() {
        ViewHelper.hideView(loadingProductList)
        ViewHelper.showView(areaNoPackage)
        ViewHelper.hideView(productRecycleView)
    }

    @OnClick(R.id.addProductButton)
    fun onAddProductButtonClicked() {
        navigateTo(
            R.id.myProductNavGraph,
            R.id.action_move_fromMyProductFragment_toAddProductFragment
        )
    }
}