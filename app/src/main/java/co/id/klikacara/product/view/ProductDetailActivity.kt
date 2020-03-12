package co.id.klikacara.product.view

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.BaseProduct
import co.id.klikacara.`object`.adapter.BannerAdapter
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.authentication.view.AuthenticationActivity
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.order.view.OrderActivity
import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.product.presenter.ProductDetailPresenter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_product_detail.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import javax.inject.Inject

class ProductDetailActivity : BaseActivity(), ProductContract.ProductDetailView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var productDetailPresenter: ProductDetailPresenter
    lateinit var product: BaseProduct
    lateinit var mitra: Mitra
    private val productBannerAdapter = FastItemAdapter<BannerAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_product_detail)
        configureBackButton()
        productDetailPresenter.checkRole()
        productListSwipe.setOnRefreshListener(this)
        product = Parcels.unwrap(intent.extras?.getParcelable(BuildConfig.productDb))
        configureHorizontalItemAdapter(productBannerAdapter, productBannerRecycleView)
        productDetailPresenter.getVendorById(product.vendorId)
        productNameTextView.text = product.name
        productDescriptionTextView.text = product.description
        productPriceTextView.text = StringHelper.getStringBuilderToString(
            StringHelper.getPriceInRp(product.price),
            "/",
            product.paymentType!!.description
        )
        productNoteTextView.text = product.notes
        for (imageUrl in product.url.keys) {
            productBannerAdapter.add(BannerAdapter(imageUrl))
        }
        if (StringUtils.isBlank(product.notes))
            ViewHelper.hideView(areaNotes)
    }

    override fun onRefresh() {
        productListSwipe.isRefreshing = false
    }

    override fun setDataVendor(mitra: Mitra) {
        this.mitra = mitra
        vendorTextView.text = StringHelper.getStringBuilderToString(mitra.name, " - ", mitra.city!!.name)
    }

    @OnClick(R.id.orderButton)
    fun onOrderButtonClicked() {
        productDetailPresenter.openOrderActivity()
    }

    override fun userIsVendor() {
        ViewHelper.hideView(orderButton)
    }

    override fun doOpenOrderActivity() {
        val intent = getIntent(this, OrderActivity::class.java)
        intent.putExtra(BuildConfig.productDb, Parcels.wrap(product))
        intent.putExtra(BuildConfig.mitraDb, Parcels.wrap(mitra))
        showActivity(intent)
    }

    override fun showDialogLogin() {
        showInfoWithCancel("Anda harus login terlebih dahulu untuk memesan Acara", View.OnClickListener {
            showActivity(getIntent(this, AuthenticationActivity::class.java))
        })
    }
}