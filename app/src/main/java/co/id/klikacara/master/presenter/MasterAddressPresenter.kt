package co.id.klikacara.master.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.MasterData
import co.id.klikacara.`object`.adapter.TextAdapter
import co.id.klikacara.master.contract.MasterContract
import com.google.firebase.firestore.FirebaseFirestore

class MasterAddressPresenter(
    private val view: MasterContract.MasterAddressView,
    private val database: FirebaseFirestore
) {
    fun getListProvince() {
        view.showProgressDialog()
        database.collection(BuildConfig.provinceDb).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val listProvince = it.result!!.toObjects(MasterData::class.java)
                    val listProvinceAdapter = ArrayList<TextAdapter>()
                    for (masterData in listProvince)
                        listProvinceAdapter.add(TextAdapter(masterData))
                    view.setListProvince(listProvinceAdapter)
                }
                view.dismissProgressDialog()
            }
    }

    fun getListCityByProviceCode(code: String) {
        view.showProgressDialog()
        database.collection(BuildConfig.cityDb)
            .whereEqualTo("code", code)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val listMasterData = it.result!!.toObjects(MasterData::class.java)
                    val listMasterDataAdapter = ArrayList<TextAdapter>()
                    for (masterData in listMasterData)
                        listMasterDataAdapter.add(TextAdapter(masterData))
                    view.setListCity(listMasterDataAdapter)
                }
                view.dismissProgressDialog()
            }
    }

    fun getListDistrictByCity(code: String) {
        view.showProgressDialog()
        database.collection(BuildConfig.districtDb)
            .whereEqualTo("code", code)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val listMasterData = it.result!!.toObjects(MasterData::class.java)
                    val listMasterDataAdapter = ArrayList<TextAdapter>()
                    for (masterData in listMasterData)
                        listMasterDataAdapter.add(TextAdapter(masterData))
                    view.setListDistrict(listMasterDataAdapter)
                }
                view.dismissProgressDialog()
            }
    }
}