package co.id.klikacara.order.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.BaseProduct
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.base.utils.datehelper.DateHelper
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.base.utils.timehelper.TimeHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.presenter.OrderDatePresenter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_order_info_pesanan.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import java.util.*
import javax.inject.Inject

class OrderDateFragment : BaseFragment(), OrderContract.OrderDateView {

    @Inject
    lateinit var orderDatePresenter: OrderDatePresenter

    private val calendarStart = Calendar.getInstance()
    private val calendarEnd = Calendar.getInstance()
    lateinit var product: BaseProduct
    lateinit var mitra: Mitra
    lateinit var order: Order

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_order_info_pesanan, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        order = Parcels.unwrap(arguments?.getParcelable(BuildConfig.orderDb))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        product = Parcels.unwrap<BaseProduct>(activity?.intent?.extras?.getParcelable(BuildConfig.productDb))
        mitra = Parcels.unwrap<Mitra>(activity?.intent?.extras?.getParcelable(BuildConfig.mitraDb))
        productNameTextView.text = product.name
        vendorNameTextView.text = StringHelper.getStringBuilderToString(
            StringHelper.getPriceInRp(product.price), "/", product.paymentType!!.description
        )
        parameterTextView.text = product.paymentType!!.description
        orderDatePresenter.bindingText(countPackageEditText, product.price)
    }

    @OnClick(R.id.nextButton)
    fun onNextButtonClicked() {
        if (StringUtils.isBlank(startDateEditText.text.toString()))
            startDateEditText.error = "Anda belum memilih tanggal mulai acara"
        else if (StringUtils.isBlank(startTimeEditText.text.toString()))
            startDateEditText.error = "Anda belum memilih waktu mulai acara"
        else if (StringUtils.isBlank(endDateEditText.text.toString()))
            startDateEditText.error = "Anda belum memilih tanggal selesai acara"
        else if (StringUtils.isBlank(endTimeEditText.text.toString()))
            startDateEditText.error = "Anda belum memilih waktu selesai acara"
        else if (StringUtils.isBlank(countPackageEditText.text.toString()))
            startDateEditText.error = "Anda belum menambahkan jumlah pemesanan"
        else {
            order.startDate = calendarStart.timeInMillis
            order.endDate = calendarEnd.timeInMillis
            order.pesanan = countPackageEditText.text.toString().toInt()
            order.amount = order.pesanan * product.price
            val bundle = Bundle()
            bundle.putParcelable(BuildConfig.orderDb, Parcels.wrap(order))
            navigateTo(R.id.orderNavGraph, R.id.action_move_fromOrderDateFragment_toOrderPaymentFragment, bundle)
        }
    }

    @OnClick(R.id.startDateEditText)
    fun onStartDateClicked() {
        DateHelper.getDateWithMin(activity!!, calendarStart, startDateEditText, System.currentTimeMillis())
    }

    @OnClick(R.id.endDateEditText)
    fun onEndDateClicked() {
        DateHelper.getDateWithMin(activity!!, calendarEnd, endDateEditText, calendarStart.timeInMillis)
    }

    @OnClick(R.id.startTimeEditText)
    fun onStartTimeClicked() {
        TimeHelper.getTime(activity!!, calendarStart, startTimeEditText)
    }

    @OnClick(R.id.endTimeEditText)
    fun onEndTimeClicked() {
        TimeHelper.getTime(activity!!, calendarEnd, endTimeEditText)
    }

    override fun setTotalAmount(totalPrice: Long) {
        valueTotalBayar.text = StringHelper.getPriceInRp(totalPrice)
    }
}