package co.id.klikacara.base.utils.viewhelper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import org.apache.commons.lang3.StringUtils

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

        fun invisbleView(view: View?) {
            if (view != null)
                view.visibility = View.INVISIBLE
        }

        fun showView(views: List<View?>) {
            for (view in views) {
                showView(view)
            }
        }

        fun hideView(views: List<View?>) {
            for (view in views) {
                hideView(view)
            }
        }

        fun isKeyboardShown(rootView: View): Boolean {
            /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
            val SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val dm = rootView.resources.displayMetrics
            /* heightDiff = rootView height - status bar height (r.top) - visible frame height (r.bottom - r.top) */
            val heightDiff = rootView.bottom - r.bottom
            /* Threshold size: dp to pixels, multiply with display density */
            return heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density
        }

        fun handleVisibility(string: String?, view: View?) {
            if (view != null) {
                if (StringUtils.isBlank(string))
                    hideView(view)
                else
                    showView(view)
            }
        }

        fun handleVisibility(boolean: Boolean, view: View?) {
            if (view != null) {
                if (boolean)
                    showView(view)
                else
                    hideView(view)
            }
        }

        @SuppressLint("ObsoleteSdkInt")
        fun getViewHeight(view: View): Int {
            val wm = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay

            val deviceWidth: Int

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                val size = Point()
                display.getSize(size)
                deviceWidth = size.x
            } else {
                deviceWidth = display.width
            }

            val widthMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST)
            val heightMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(widthMeasureSpec, heightMeasureSpec)
            return view.measuredHeight
        }

        fun isVisible(loadingArea: View?): Boolean {
            if (loadingArea != null)
                return loadingArea.visibility == View.VISIBLE
            return false
        }

        fun underlineText(view: TextView) {
            view.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }
    }
}