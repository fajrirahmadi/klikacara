package co.id.klikacara.authentication.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.MitraType
import co.id.klikacara.`object`.adapter.TextAdapter
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.`object`.authentication.RegistrationVendor
import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.authentication.presenter.RegistrationMitraPresenter
import co.id.klikacara.base.view.fragment.BaseFragment
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.base_bottom_sheet_list_of_view.*
import kotlinx.android.synthetic.main.fragment_authentication_registration_mitra.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import javax.inject.Inject

class RegisterMitraFragment : BaseFragment(), AuthenticationContract.RegisterMitraView {

    @Inject
    lateinit var registerMitraPresenter: RegistrationMitraPresenter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private lateinit var sheetBehaviorListOfItem: BottomSheetBehavior<*>
    private lateinit var sheetBehaviorListOfItemCallback: BottomSheetBehavior.BottomSheetCallback

    lateinit var registrationVendor: RegistrationVendor
    val mitra = Mitra()
    var mitraCategoryAdapter = FastItemAdapter<TextAdapter>()
    var provinceListAdapter = FastItemAdapter<TextAdapter>()
    var cityListAdapter = FastItemAdapter<TextAdapter>()
    var districtListAdapter = FastItemAdapter<TextAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = getInflate(inflater, R.layout.fragment_authentication_registration_mitra, container)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (BottomSheetBehavior.STATE_EXPANDED == sheetBehaviorListOfItem.state) {
                    sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_COLLAPSED
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationVendor = Parcels.unwrap(arguments?.getParcelable(BuildConfig.registrationData))
        configureBottomSheet()
        initMitraCategoryAdapter()
    }

    private fun initMitraCategoryAdapter() {
        mitraCategoryAdapter.clear()
        mitraCategoryAdapter.add(TextAdapter(MitraType.PAKET_ACARA))
        mitraCategoryAdapter.add(TextAdapter(MitraType.PERLENGKAPAN_ACARA))
        mitraCategoryAdapter.add(TextAdapter(MitraType.PENGISI_ACARA))
        mitraCategoryAdapter.withOnClickListener { _, _, item, _ ->
            kategoriEditText.setText(item.mitraType.description)
            mitra.type = item.mitraType
            sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_COLLAPSED
            true
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
                        fakeViewBottomSheet.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        fakeViewBottomSheet.visibility = View.GONE
                    }
                }
            }
        }
        sheetBehaviorListOfItem.setBottomSheetCallback(sheetBehaviorListOfItemCallback)
    }

    @OnClick(R.id.nextButton)
    fun onNextButtonClicked() {
        mitra.name = namaUsahaEditText.text.toString()
        mitra.pic = pengelolaEditText.text.toString()
        mitra.phone = phoneEditText.text.toString()
        mitra.address = addressEditText.text.toString()
        mitra.postalCode = posCodeEditText.text.toString()

        if (mitra.type == null)
            showError("Anda belum memilih kategori")
        else if (StringUtils.isBlank(mitra.name))
            showError("Nama usaha tidak boleh kosong")
        else if (StringUtils.isBlank(mitra.pic))
            showError("Penanggung jawab tidak boleh kosong")
        else if (StringUtils.isBlank(mitra.phone))
            showError("Nomor telpon mitra tidak boleh kosong")
        else if (StringUtils.isBlank(mitra.address))
            showError("Alamat tidak boleh kosong")
        else if (mitra.province == null)
            showError("Anda belum memilih provinsi")
        else if (mitra.city == null)
            showError("Anda belum memilih kabupaten/kota")
        else if (mitra.district == null)
            showError("Anda belum memilih kecamatan")
        else if (StringUtils.isBlank(mitra.postalCode))
            showError("Anda belum memasukkan kode pos")
        else if (mitra.postalCode.length < 5)
            showError("Kode pos tidak valid")
        else {
            val bundle = Bundle()
            registrationVendor.mitra = mitra
            bundle.putParcelable(BuildConfig.registrationData, Parcels.wrap(registrationVendor))
            navigateTo(
                R.id.registrationNavGraph,
                R.id.action_move_fromRegisterMitraFragment_toRegisterUsahaFragment,
                bundle
            )
        }
    }

    @OnClick(R.id.kategoriEditText)
    fun onCategoryClicked() {
        configureItemAdapter(mitraCategoryAdapter, itemRecycleView)
        titleTextView.text = "Pilih Kategori Mitra"
        sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_EXPANDED
    }

    @OnClick(R.id.provinceEditText)
    fun onProvinceClicked() {
        if (provinceListAdapter.adapterItemCount > 0) {
            setListProvince(provinceListAdapter.adapterItems)
        } else {
            registerMitraPresenter.getListProvince()
        }
    }

    @OnClick(R.id.cityEditText)
    fun onCityClicked() {
        if (StringUtils.isBlank(provinceEditText.text.toString()))
            showError("Anda belum memilih provinsi")
        else if (cityListAdapter.adapterItemCount > 0) {
            setListCity(cityListAdapter.adapterItems)
        } else {
            registerMitraPresenter.getListCityByProviceCode(mitra.province!!.key)
        }
    }

    @OnClick(R.id.districtEditText)
    fun onDistrictClicked() {
        if (StringUtils.isBlank(provinceEditText.text.toString()))
            showError("Anda belum memilih kota/kabupaten")
        else if (districtListAdapter.adapterItemCount > 0) {
            setListDistrict(districtListAdapter.adapterItems)
        } else {
            registerMitraPresenter.getListDistrictByCity(mitra.city!!.key)
        }
    }

    override fun setListProvince(listProvince: List<TextAdapter>) {
        this.provinceListAdapter = FastItemAdapter()
        this.provinceListAdapter.add(listProvince)
        configureItemAdapter(provinceListAdapter, itemRecycleView)
        provinceListAdapter.withOnClickListener { _, _, item, _ ->
            mitra.province = item.masterData
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
            mitra.city = item.masterData
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
            mitra.district = item.masterData
            districtEditText.setText(item.masterData.name)
            sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        }
        titleTextView.text = "Pilih Kecamatan"
        sheetBehaviorListOfItem.state = BottomSheetBehavior.STATE_EXPANDED
    }

}