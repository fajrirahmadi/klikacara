package co.id.klikacara.main.view

import android.os.Bundle
import android.support.design.bottomnavigation.LabelVisibilityMode
import android.view.View
import android.widget.FrameLayout
import butterknife.BindView
import co.id.klikacara.R
import co.id.klikacara.base.view.activity.BaseActivity
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx

class MainActivity : BaseActivity() {

    @BindView(R.id.bottomBar)
    lateinit var bottomBar: BottomNavigationViewEx
    @BindView(R.id.frameHome)
    lateinit var homeFrame: FrameLayout
    @BindView(R.id.frameOrder)
    lateinit var orderFrame: FrameLayout
    @BindView(R.id.frameProfile)
    lateinit var profileFrame: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_main)
        configureBottomBar()
        showHomeFragment()
    }

    private fun configureBottomBar() {
        bottomBar.enableAnimation(false)
        bottomBar.isItemHorizontalTranslationEnabled = false
        bottomBar.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        bottomBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> showHomeFragment()
                R.id.bottom_order -> showOrderFragment()
                R.id.bottom_profile -> showProfileFragment()
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun showProfileFragment() {
        homeFrame.visibility = View.GONE
        orderFrame.visibility = View.GONE
        profileFrame.visibility = View.VISIBLE
    }

    private fun showOrderFragment() {
        homeFrame.visibility = View.GONE
        orderFrame.visibility = View.VISIBLE
        profileFrame.visibility = View.GONE
    }

    private fun showHomeFragment() {
        homeFrame.visibility = View.VISIBLE
        orderFrame.visibility = View.GONE
        profileFrame.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (homeFrame.visibility == View.GONE) {
            bottomBar.selectedItemId = R.id.bottom_home
        } else
            super.onBackPressed()
    }
}
