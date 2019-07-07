package co.id.klikacara.base.contract

class BaseContract {
    interface View {
        fun showProgressDialog()
        fun dismissProgressDialog()
        fun showError(message: String)
        fun showInfo(message: String)
        fun showSuccess(message: String)
    }

    interface Presenter {
    }
}