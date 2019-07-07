package co.id.klikacara.base.utils.viewhelper

import android.view.View

class ViewHelper {

  companion object {

    fun showView(view: View?) {
      if (view != null)
        view.visibility = View.VISIBLE
    }

    fun hideView(view: View?) {
      if (view != null)
        view.visibility = View.GONE
    }
  }
}