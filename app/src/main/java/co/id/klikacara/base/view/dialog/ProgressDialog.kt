package co.id.klikacara.base.view.dialog

import co.id.klikacara.R


class ProgressDialog : BaseJavaDialog() {
    override fun getLayout(): Int {
        return R.layout.base_dialog_progress
    }

    override fun getTitle(): String {
        return getString(R.string.label_nyantai_bentar)
    }
}