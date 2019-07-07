package co.id.klikacara.vendor.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.adapter.ProductAdapter
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.product.presenter.MyProductPresenter
import co.id.klikacara.product.view.ProductDetailFragment
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_list_of_view.*
import org.parceler.Parcels
import javax.inject.Inject

class VendorProductFragment : BaseFragment(), ProductContract.MyProductView {

    @Inject
    lateinit var myProductPresenter: MyProductPresenter

    private val productListAdapter = FastItemAdapter<ProductAdapter>()

    companion object {
        @JvmStatic
        fun newInstance(userId: String) = VendorProductFragment().apply {
            arguments = Bundle().apply {
                putString(BuildConfig.userDb, userId)
            }
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_list_of_view, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureGridItemAdapter(productListAdapter, listOfViewRecycleView, 2)
        arguments?.getString(BuildConfig.userDb, "")?.let {
            myProductPresenter.getListProductByVendorId(it)
        }
        productListAdapter.withOnClickListener { _, _, item, _ ->
            val intent = getIntent(activity!!, ProductDetailFragment::class.java)
            intent.putExtra(BuildConfig.productDb, Parcels.wrap(item.product))
            showActivity(intent)
            true
        }
    }

    override fun doOnGetListProductSuccess(productListAdapter: ArrayList<ProductAdapter>) {
        ViewHelper.hideView(loadingData)
        ViewHelper.hideView(areaNoItem)
        ViewHelper.showView(listOfViewRecycleView)
        this.productListAdapter.clear()
        this.productListAdapter.add(productListAdapter)
    }

    override fun doOnGetListProductFailed() {
        ViewHelper.hideView(loadingData)
        ViewHelper.showView(areaNoItem)
        ViewHelper.hideView(listOfViewRecycleView)
        descriptionNoItem.text = "Vendor ini belum memiliki produk/layanan"
    }
}