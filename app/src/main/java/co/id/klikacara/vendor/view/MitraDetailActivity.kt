package co.id.klikacara.vendor.view

import android.os.Bundle
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.adapter.RatingAdapter
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.base.view.adapter.ViewPagerAdapter
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.UserMitraPresenter
import co.id.klikacara.vendor.contract.MitraContract
import co.id.klikacara.vendor.presenter.VendorRatingPresenter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_vendor.*
import org.parceler.Parcels


class MitraDetailActivity : BaseActivity(), MainContract.MitraProfileView, MitraContract.MitraRatingView {

    lateinit var user: User
    private lateinit var userMitraPresenter: UserMitraPresenter
    private lateinit var mitraRatingPresenter: VendorRatingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_vendor)
        userMitraPresenter = UserMitraPresenter(this, FirebaseFirestore.getInstance())
        mitraRatingPresenter = VendorRatingPresenter(this, FirebaseFirestore.getInstance())
        user = Parcels.unwrap<User>(intent.extras?.getParcelable(BuildConfig.userDb))
        userMitraPresenter.getMitraById(user.uid!!)
        mitraRatingPresenter.loadRating(user.uid!!)
        configureViewPager()
    }

    override fun setMitraData(mitra: Mitra) {
        mitraNameTextView.text = mitra.name
        vendorTypeTextView.text = mitra.type!!.description
    }

    private fun configureViewPager() {
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(VendorProductFragment.newInstance(user.uid!!), "Produk Saya")
        pagerAdapter.addFragment(VendorUlasanFragment.newInstance(user.uid!!), "Ulasan")
        vendorPager.adapter = pagerAdapter
        vendorTabLayout.setupWithViewPager(vendorPager)
    }

    override fun setUlasanAdapter(ulasanListAdapter: ArrayList<RatingAdapter>) {
        var rating = 0f
        for (ulasan in ulasanListAdapter) {
            rating += ulasan.rating.rating
        }
        vendorRating.rating = rating / ulasanListAdapter.size

    }

    override fun failedLoadRating() {
        vendorRating.rating = 0f
    }
}