package co.id.klikacara.createevent.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.`object`.adapter.TextAdapter
import co.id.klikacara.base.utils.datehelper.DateHelper
import co.id.klikacara.base.utils.timehelper.TimeHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.master.contract.MasterContract
import co.id.klikacara.master.presenter.MasterAddressPresenter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_product_add_item.fakeViewBottomSheet
import kotlinx.android.synthetic.main.base_bottom_sheet_list_of_view.*
import kotlinx.android.synthetic.main.fragment_order_address.*
import kotlinx.android.synthetic.main.fragment_order_address.nextButton
import kotlinx.android.synthetic.main.fragment_order_info_pesanan.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import java.util.*
import javax.inject.Inject

class OrderAddressAndTimeFragment : BaseFragment(), MasterContract.MasterAddressView {

    @Inject
    lateinit var masterAddressPresenter: MasterAddressPresenter

    private var provinceListAdapter = FastItemAdapter<TextAdapter>()
    private var cityListAdapter = FastItemAdapter<TextAdapter>()
    private var districtListAdapter = FastItemAdapter<TextAdapter>()
    private lateinit var sheetBehaviorListOfItem: BottomSheetBehavior<*>
    private lateinit var sheetBehaviorListOfItemCallback: BottomSheetBehavior.BottomSheetCallback
    private lateinit var order: Order
    private val calendarStart = Calendar.getInstance()
    private val calendarEnd = Calendar.getInstance()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getInflate(inflater, R.layout.fragment_order_address_and_time, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBottomSheet()
        configureBackButton()
        order = Parcels.unwrap(arguments?.getParcelable(BuildConfig.orderDb))
    }

    @OnClick(R.id.nextButton)
    fun onNextButtonClicked() {
        order.postCode = posCodeEditText.text.toString()
        order.address = addressEditText.text.toString()
        when {
            StringUtils.isBlank(order.address) -> showError("Alamat acara tidak boleh kosong")
            order.province == null -> showError("Anda belum memilih provinsi")
            order.city == null -> showError("Anda belum memilih kabupaten/kota")
            order.district == null -> showError("Anda belum memilih kecamatan")
            StringUtils.isBlank(order.postCode) -> showError("Anda belum memasukkan kode pos")
            StringUtils.isBlank(startDateEditText.text.toString()) -> startDateEditText.error =
                "Anda belum memilih tanggal mulai acara"
            StringUtils.isBlank(startTimeEditText.text.toString()) -> startDateEditText.error =
                "Anda belum memilih waktu mulai acara"
            StringUtils.isBlank(endDateEditText.text.toString()) -> startDateEditText.error =
                "Anda belum memilih tanggal selesai acara"
            StringUtils.isBlank(endTimeEditText.text.toString()) -> startDateEditText.error =
                "Anda belum memilih waktu selesai acara"
            else -> {
                order.startDate = calendarStart.timeInMillis
                order.endDate = calendarEnd.timeInMillis
                val bundle = Bundle()
                bundle.putParcelable(BuildConfig.orderDb, Parcels.wrap(order))
                navigateTo(
                    R.id.orderNavGraph,
                    R.id.action_move_fromOrderAddressAndTimeFragment_toPaymentFragment,
                    bundle
                )
            }
        }
    }

    private fun configureBottomSheet() {
        sheetBehaviorListOfItem = BottomSheetBehavior.from<LinearLayout>(listOfItemBottomSheet)
        sheetBehaviorListOfItemCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        nextButton.visibility = View.GONE
                        fakeViewBottomSheet.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        fakeViewBottomSheet.visibility = View.GONE
                        nextButton.visibility = View.VISIBLE
                    }
                    else -> {
                    }
                }
            }
        }
        sheetBehaviorListOfItem.setBottomSheetCallback(sheetBehaviorListOfItemCallback)
    }

    @OnClick(R.id.provinceEditText)
    fun onProvinceClicked() {
        if (provinceListAdapter.adapterItemCount > 0) {
            setListProvince(provinceListAdapter.adapterItems)
        } else {
            masterAddressPresenter.getListProvince()
        }
    }

    @OnClick(R.id.cityEditText)
    fun onCityClicked() {
        when {
            StringUtils.isBlank(provinceEditText.text.toString()) -> showError("Anda belum memilih provinsi")
            cityListAdapter.adapterItemCount > 0 -> setListCity(cityListAdapter.adapterItems)
            else -> masterAddressPresenter.getListCityByProviceCode(order.province!!.key)
        }
    }

    @OnClick(R.id.districtEditText)
    fun onDistrictClicked() {
        when {
            StringUtils.isBlank(provinceEditText.text.toString()) -> showError("Anda belum memilih kota/kabupaten")
            districtListAdapter.adapterItemCount > 0 -> setListDistrict(districtListAdapter.adapterItems)
            else -> masterAddressPresenter.getListDistrictByCity(order.city!!.key)
        }
    }

    override fun setListProvince(listProvince: List<TextAdapter>) {
        this.provinceListAdapter = FastItemAdapter()
        this.provinceListAdapter.add(listProvince)
        configureItemAdapter(provinceListAdapter, itemRecycleView)
        provinceListAdapter.withOnClickListener { _, _, item, _ ->
            order.province = item.masterData
            provinceEditText.setText(item.masterData.name)
            cityEditText.text = null
            districtEditText.text = null
            cityListAdapter.clear()
            districtListAdapter.clear()
            sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        }
        titleTextView.text = "Pilih Provinsi"
        sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun setListCity(listMasterDataAdapter: List<TextAdapter>) {
        this.cityListAdapter = FastItemAdapter()
        this.cityListAdapter.add(listMasterDataAdapter)
        configureItemAdapter(cityListAdapter, itemRecycleView)
        cityListAdapter.withOnClickListener { _, _, item, _ ->
            order.city = item.masterData
            cityEditText.setText(item.masterData.name)
            districtEditText.text = null
            districtListAdapter.clear()
            sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        }
        titleTextView.text = "Pilih Kota/Kabupaten"
        sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun setListDistrict(listMasterDataAdapter: List<TextAdapter>) {
        this.districtListAdapter = FastItemAdapter()
        this.districtListAdapter.add(listMasterDataAdapter)
        configureItemAdapter(districtListAdapter, itemRecycleView)
        districtListAdapter.withOnClickListener { _, _, item, _ ->
            order.district = item.masterData
            districtEditText.setText(item.masterData.name)
            sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        }
        titleTextView.text = "Pilih Kecamatan"
        sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_EXPANDED
    }

    @OnClick(R.id.startDateEditText)
    fun onStartDateClicked() {
        DateHelper.getDateWithMin(
            activity!!,
            calendarStart,
            startDateEditText,
            System.currentTimeMillis()
        )
    }

    @OnClick(R.id.endDateEditText)
    fun onEndDateClicked() {
        DateHelper.getDateWithMin(
            activity!!,
            calendarEnd,
            endDateEditText,
            calendarStart.timeInMillis
        )
    }

    @OnClick(R.id.startTimeEditText)
    fun onStartTimeClicked() {
        TimeHelper.getTime(activity!!, calendarStart, startTimeEditText)
    }

    @OnClick(R.id.endTimeEditText)
    fun onEndTimeClicked() {
        TimeHelper.getTime(activity!!, calendarEnd, endTimeEditText)
    }

    @OnClick(R.id.closeButton)
    fun closeButton() {
        sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_COLLAPSED
    }

}