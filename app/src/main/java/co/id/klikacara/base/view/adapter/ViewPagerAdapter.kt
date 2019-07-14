package co.id.klikacara.base.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

/**
 * Created by Arloji on 7/23/2017.
 */

@Suppress("DEPRECATION")
class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    fun clearFragment() {
        fragmentList.clear()
        fragmentTitleList.clear()
        notifyDataSetChanged()
    }
}