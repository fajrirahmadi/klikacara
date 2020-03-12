package co.id.klikacara.search.contract

import co.id.klikacara.base.contract.BaseContract

interface SearchContract {

    interface View : BaseContract.View {
        fun searchValid(filter: String)
    }
}