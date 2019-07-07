package co.id.klikacara.order.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.adapter.TextAdapter
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.master.contract.MasterContract
import co.id.klikacara.master.presenter.MasterAddressPresenter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_product_add_item.fakeViewBottomSheet
import kotlinx.android.synthetic.main.base_bottom_sheet_list_of_view.*
import kotlinx.android.synthetic.main.fragment_order_address.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import javax.inject.Inject

class OrderAddressFragment : BaseFragment(), MasterContract.MasterAddressView {

    @Inject
    lateinit var masterAddressPresenter: MasterAddressPresenter

    private var provinceListAdapter = FastItemAdapter<TextAdapter>()
    private var cityListAdapter = FastItemAdapter<TextAdapter>()
    private var districtListAdapter = FastItemAdapter<TextAdapter>()
    private lateinit var sheetBehaviorListOfItem: BottomSheetBehavior<*>
    private lateinit var sheetBehaviorListOfItemCallback: BottomSheetBehavior.BottomSheetCallback
    private lateinit var order: Order

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_order_address, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBottomSheet()
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
            else -> {
                val bundle = Bundle()
                bundle.putParcelable(BuildConfig.orderDb, Parcels.wrap(order))
                navigateTo(R.id.orderNavGraph, R.id.action_move_fromOrderAddressFragment_toOrderDateFragment, bundle)
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

}