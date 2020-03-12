package co.id.klikacara.authentication.presenter

import android.util.Log
import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.MasterData
import co.id.klikacara.`object`.adapter.TextAdapter
import co.id.klikacara.authentication.contract.AuthenticationContract
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.validator.ValidatorUsecase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationMitraPresenter(
    private val view: AuthenticationContract.RegisterMitraView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val validator: ValidatorUsecase
) : BasePresenter() {

    fun getListProvince() {
        view.showProgressDialog()
        database.collection(BuildConfig.provinceDb).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val listProvince = it.result!!.toObjects(MasterData::class.java)
                    val listProvinceAdapter = ArrayList<TextAdapter>()
                    var stringData = ""
                    for (masterData in listProvince) {
                        stringData =
                            stringData + masterData.key + ", " + masterData.name + ", " +
                                    masterData.description + ", " + masterData.code + "\n"
                        listProvinceAdapter.add(TextAdapter(masterData))
                    }
                    Log.i("DATA PROVINSI", stringData)
                    view.setListProvince(listProvinceAdapter)
                }
                view.dismissProgressDialog()
            }
    }

 //   .whereEqualTo("code", code)

    fun getListCityByProviceCode(code: String) {
        view.showProgressDialog()
        database.collection(BuildConfig.cityDb)
            .whereEqualTo("code", code)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val listMasterData = it.result!!.toObjects(MasterData::class.java)
                    val listMasterDataAdapter = ArrayList<TextAdapter>()
                    var stringData = ""
                    for (masterData in listMasterData) {
                        stringData =
                            stringData + masterData.key + ", " + masterData.name + ", " +
                                    masterData.description + ", " + masterData.code + "\n"
                        listMasterDataAdapter.add(TextAdapter(masterData))
                    }
                    Log.i("DATA PROVINSI", stringData)
                    view.setListCity(listMasterDataAdapter)
                }
                view.dismissProgressDialog()
            }
    }

    //            .whereEqualTo("code", code)
    fun getListDistrictByCity(code: String) {
        view.showProgressDialog()
        database.collection(BuildConfig.districtDb)
            .whereEqualTo("code", code)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val listMasterData = it.result!!.toObjects(MasterData::class.java)
                    val listMasterDataAdapter = ArrayList<TextAdapter>()
                    var stringData = ""
                    for (masterData in listMasterData) {
                        stringData =
                            stringData + masterData.key + ", " + masterData.name + ", " +
                                    masterData.description + ", " + masterData.code + "\n"
                        listMasterDataAdapter.add(TextAdapter(masterData))
                    }
                    Log.i("DATA PROVINSI", stringData)
                    view.setListDistrict(listMasterDataAdapter)
                }
                view.dismissProgressDialog()
            }
    }


}