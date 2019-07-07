package co.id.klikacara.vendor.contract

import co.id.klikacara.`object`.adapter.RatingAdapter
import co.id.klikacara.base.contract.BaseContract

interface MitraContract {

    interface MitraDetailView : BaseContract.View {

    }

    interface MitraRatingView : BaseContract.View {
        fun setUlasanAdapter(ulasanListAdapter: ArrayList<RatingAdapter>)
        fun failedLoadRating()

    }
}