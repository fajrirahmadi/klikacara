package co.id.klikacara.permission.contract

import co.id.klikacara.base.contract.BaseContract


interface PermissionContract {

    interface View : BaseContract.View {
        fun doOnPermissionGranted()
        fun doOnPermissionRejected()

    }

}