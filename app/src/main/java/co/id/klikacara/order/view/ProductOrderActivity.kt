package co.id.klikacara.order.view

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindString
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.BaseProduct
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.`object`.adapter.ProductAdapter
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.activity.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.activity_accept_vendor.*
import kotlinx.android.synthetic.main.base_appbar_primary_with_title.*
import kotlinx.android.synthetic.main.content_error.*
import kotlinx.android.synthetic.main.content_loading.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels

class ProductOrderActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    @BindString(R.string.label_product_recomended)
    lateinit var labelProductRecomended: String
    @BindString(R.string.description_search_for_vendor)
    lateinit var descriptionSearchForVendor: String

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val productAdapter = FastItemAdapter<ProductAdapter>()
    private lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_vendor)
        swipeList.setOnRefreshListener(this)
        configureBackButton()
        titleToolbar.text = labelProductRecomended
        configureAdapter()
        order = Parcels.unwrap(intent.getParcelableExtra(BuildConfig.orderDb))
        loadData()
    }

    private fun configureAdapter() {
        configureGridItemAdapter(productAdapter, listOfViewRecycleView, 3)
        productAdapter.withOnClickListener { _, _, item, _ ->
            item.isChoosed = !item.isChoosed
            productAdapter.notifyAdapterDataSetChanged()
            enableProcessButton(productAdapter)
            true
        }
    }

    private fun enableProcessButton(productAdapter: FastItemAdapter<ProductAdapter>) {
        acceptButton.isEnabled = false
        for (data in productAdapter.adapterItems) {
            if (data.isChoosed) {
                acceptButton.isEnabled = true
                break
            }
        }
    }

    override fun onRefresh() {
        swipeList.isRefreshing = false
        loadData()
    }

    private fun loadData() {
        productAdapter.clear()
        var productRecomended = order.recomendedVendor
        if (StringUtils.isNotBlank(productRecomended)) {
            productRecomended = productRecomended.replace(" ", "")
            val productKey = productRecomended.split(",")
            ViewHelper.showView(loadingArea)
            var counter = 0
            for (product in productKey) {
                database.collection(BuildConfig.productDb)
                    .document(product)
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful && it.result != null) {
                            val data = it.result!!.toObject(BaseProduct::class.java)
                            productAdapter.add(ProductAdapter(data))
                        }
                        counter++
                        if (counter == productKey.size) {
                            ViewHelper.hideView(loadingArea)
                            ViewHelper.handleVisibility(
                                productAdapter.adapterItemCount > 0,
                                contentArea
                            )
                            ViewHelper.handleVisibility(
                                productAdapter.adapterItemCount == 0,
                                errorArea
                            )
                            ViewHelper.handleVisibility(
                                productAdapter.adapterItemCount > 0,
                                bottomView
                            )
                        }
                    }
            }
        } else {
            ViewHelper.hideView(listOf(loadingArea, contentArea, bottomView))
            ViewHelper.showView(errorArea)
            labelError.text = descriptionSearchForVendor
        }
    }

    @OnClick(R.id.acceptButton)
    fun onAcceptButtonClicked() {
        showInfoWithCancel(
            "Apakah Anda yakin ingin menerima rekomendasi alat ini?",
            View.OnClickListener {
                infoDialog.dismiss()
                order.vendorChoosed = getVendorChoosed()
                changeStatusOrder(
                    order,
                    "Rekomendasi diterima",
                    PaymentStatus.MENUNGGU_PEMBAYARAN
                )
            })
    }

    private fun getVendorChoosed(): String {
        var vendorChoosed = ""
        for (data in productAdapter.adapterItems) {
            if (data.isChoosed)
                vendorChoosed += "," + data.product.key
        }
        return vendorChoosed.replaceFirst(",", "")
    }

    @OnClick(R.id.cancelButton)
    fun onCancelButtonClicked() {
        showInfoWithCancel("Apakah Anda yakin ingin membatalkan pesanan?", View.OnClickListener {
            infoDialog.dismiss()
            changeStatusOrder(order, "Dibatalkan oleh pengguna", PaymentStatus.PESANAN_DIBATALKAN)
        })
    }

    private fun changeStatusOrder(order: Order, reason: String, paymentStatus: PaymentStatus) {
        showProgressDialog()
        order.reason = reason
        order.paymentStatus = paymentStatus
        if (PaymentStatus.MENUNGGU_PEMBAYARAN == paymentStatus) {
            order.amount = 100000
            order.pesanan = 1
        }
        database.collection(BuildConfig.orderDb).document(order.key!!)
            .set(order)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (PaymentStatus.PESANAN_DIBATALKAN == paymentStatus)
                        showInfo("Berhasil membatalkan pesanan", View.OnClickListener {
                            infoDialog.dismiss()
                            finish()
                        })
                    else if (PaymentStatus.MENUNGGU_PEMBAYARAN == paymentStatus)
                        showInfo("Berhasil menerima rekomendasi alat", View.OnClickListener {
                            infoDialog.dismiss()
                            finish()
                        })
                } else {
                    if (PaymentStatus.PESANAN_DIBATALKAN == paymentStatus)
                        showInfo("Gagal membatalkan pesanan")
                    else if (PaymentStatus.MENUNGGU_PEMBAYARAN == paymentStatus)
                        showInfo("Gagal menerima rekomendasi alat")
                }
                dismissProgressDialog()
            }
    }
}