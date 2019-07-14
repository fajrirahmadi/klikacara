package co.id.klikacara.order.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.adapter.KeyValueAdapter
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.base.utils.timehelper.TimeUtils
import co.id.klikacara.base.view.dialog.BaseJavaDialog
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.presenter.OrderConfirmationPresenter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.adapter_bank.*
import kotlinx.android.synthetic.main.fragment_order_konfirmasi.*
import org.parceler.Parcels
import javax.inject.Inject

class OrderConfirmationFragment : BaseFragment(), OrderContract.OrderConfirmationView {

    @Inject
    lateinit var orderConfirmationPresenter: OrderConfirmationPresenter

    private val confirmationListAdapter = FastItemAdapter<KeyValueAdapter>()
    lateinit var order: Order
    private val orderConfirmationDialog = BaseJavaDialog()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_order_konfirmasi, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        order = Parcels.unwrap(arguments?.getParcelable(BuildConfig.orderDb))
        configureItemAdapter(confirmationListAdapter, konfirmasiRecycleView)
        initConfirmationData()
        initOrderConfirmationDialog()
    }

    private fun initOrderConfirmationDialog() {
        orderConfirmationDialog.title = "Konfirmasi Pesanan"
        orderConfirmationDialog.description = "Apakah Anda yakin ingin melakukan pemesanan?"
        orderConfirmationDialog.hideBtnCancel(false)
        orderConfirmationDialog.okClickListener = View.OnClickListener {
            orderConfirmationPresenter.createOrder(order)
            orderConfirmationDialog.dismissAllowingStateLoss()
        }
    }

    private fun initConfirmationData() {
        confirmationListAdapter.add(KeyValueAdapter("Nama Acara", order.name))
        confirmationListAdapter.add(KeyValueAdapter("Penanggung Jawab", order.penanggungJawab))
        confirmationListAdapter.add(KeyValueAdapter("Alamat Acara", order.address))
        confirmationListAdapter.add(
            KeyValueAdapter(
                "Mulai Acara",
                TimeUtils.getDateFormated("dd/MM/yyyy HH:mm", order.startDate)
            )
        )
        confirmationListAdapter.add(
            KeyValueAdapter(
                "Akhir Acara",
                TimeUtils.getDateFormated("dd/MM/yyyy HH:mm", order.endDate)
            )
        )
        confirmationListAdapter.add(KeyValueAdapter("Nama Paket", order.product!!.name))
        confirmationListAdapter.add(
            KeyValueAdapter(
                "Harga Paket", StringHelper.getStringBuilderToString(
                    StringHelper.getPriceInRp(order.product!!.price), "/", order.product!!.paymentType!!.description
                )
            )
        )
        confirmationListAdapter.add(KeyValueAdapter("Total Pembelian", order.pesanan.toString()))

        GlideUtils.setFotoWithUrl(activity, order.bank!!.url, logoBankImageView)
        bankTitleTextView.text = order.bank!!.bankName
        bankDescriptionTextView.text = StringHelper.getStringBuilderToString(
            order.bank!!.nomorRekening, " a/n ", order.bank!!.namaRekening
        )

        valueTotalBayar.text = StringHelper.getPriceInRp(order.amount)
    }

    @OnClick(R.id.submitButton)
    fun onNextButtonClicked() {
        orderConfirmationDialog.showAllowingStateLoss(childFragmentManager, "ORDER")
    }

    override fun doOnCreateOrderSuccess(order: Order) {
        showSuccess(
            "Anda berhasil melakukan order, silahkan lakukan pembayaran.",
            View.OnClickListener {
                val intent = getIntent(activity!!, OrderDetailActivity::class.java)
                intent.putExtra(BuildConfig.orderDb, Parcels.wrap(order))
                showActivityAndFinishCurent(intent)
            })
    }
}