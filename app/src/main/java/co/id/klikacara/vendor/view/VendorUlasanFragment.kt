package co.id.klikacara.vendor.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.adapter.RatingAdapter
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.vendor.contract.MitraContract
import co.id.klikacara.vendor.presenter.VendorRatingPresenter
import com.google.firebase.firestore.FirebaseFirestore
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.fragment_list_of_view.*

class VendorUlasanFragment : BaseFragment(), MitraContract.MitraRatingView {

    private val ulasanListAdapter = FastItemAdapter<RatingAdapter>()
    private lateinit var vendorRatingPresenter: VendorRatingPresenter

    companion object {
        @JvmStatic
        fun newInstance(userId: String) = VendorUlasanFragment().apply {
            arguments = Bundle().apply {
                putString(BuildConfig.userDb, userId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_list_of_view, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vendorRatingPresenter = VendorRatingPresenter(this, FirebaseFirestore.getInstance())
        configureItemAdapter(ulasanListAdapter, listOfViewRecycleView)
        arguments?.getString(BuildConfig.userDb, "")?.let {
            vendorRatingPresenter.loadRating(it)
        }
    }

    override fun setUlasanAdapter(ulasanListAdapter: ArrayList<RatingAdapter>) {
        this.ulasanListAdapter.clear()
        this.ulasanListAdapter.add(ulasanListAdapter)
        ViewHelper.hideView(loadingData)
        ViewHelper.showView(listOfViewRecycleView)
    }

    override fun failedLoadRating() {
        ViewHelper.hideView(loadingData)
        ViewHelper.showView(areaNoItem)
    }

}