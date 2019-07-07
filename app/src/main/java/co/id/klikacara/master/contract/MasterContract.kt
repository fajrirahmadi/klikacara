package co.id.klikacara.master.contract

import co.id.klikacara.`object`.adapter.TextAdapter
import co.id.klikacara.base.contract.BaseContract

interface MasterContract {

    interface MasterAddressView : BaseContract.View {
        fun setListProvince(listProvince: List<TextAdapter>)
        fun setListCity(listMasterDataAdapter: List<TextAdapter>)
        fun setListDistrict(listMasterDataAdapter: List<TextAdapter>)
    }
}