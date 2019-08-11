package co.id.klikacara.product.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.KlikMenu
import co.id.klikacara.`object`.adapter.ProductAdapter
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.product.presenter.ProductListPresenter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_product_list.*
import org.parceler.Parcels
import javax.inject.Inject

class ProductListFragment : BaseFragment(), ProductContract.ProductListView {

    @Inject
    lateinit var productListPresenter: ProductListPresenter

    private val productListAdapter = FastItemAdapter<ProductAdapter>()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    lateinit var menuChoosed: KlikMenu

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_product_list, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureProductAdapter()
        configureBackButton()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ViewHelper.hideView(addProductButton)
        menuChoosed = Parcels.unwrap<KlikMenu>(activity?.intent?.extras?.getParcelable(BuildConfig.klikMenuDb))
        titleProduct.text =
            StringHelper.getStringBuilderToString(menuChoosed.mitraType?.description, " - ", menuChoosed.name)
        descriptionProduct.text = menuChoosed.description
        productListPresenter.getListProductByCategory(menuChoosed.key!!)
    }

    private fun configureProductAdapter() {
        configureGridItemAdapter(productListAdapter, productRecycleView, 2)
        productListAdapter.withOnClickListener { _, _, item, _ ->
            val intent = getIntent(activity!!, ProductDetailActivity::class.java)
            intent.putExtra(BuildConfig.productDb, Parcels.wrap(item.product))
            showActivity(intent)
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
}