package co.id.klikacara.createevent.contract

import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.contract.BaseContract

interface EventContract {

    interface DetailEventView : BaseContract.View {
        fun showLoginDialog()
        fun showConfirmationDialog()
        fun doOnUserAlreadyJoined()
        fun doOnJoinEventSuccess()
        fun setUser(user: User)
        fun doOnUserNotJoined()

    }
}