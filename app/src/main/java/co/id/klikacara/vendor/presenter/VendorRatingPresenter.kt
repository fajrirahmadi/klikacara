package co.id.klikacara.vendor.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.Rating
import co.id.klikacara.`object`.adapter.RatingAdapter
import co.id.klikacara.vendor.contract.MitraContract
import com.google.firebase.firestore.FirebaseFirestore

class VendorRatingPresenter(
    private val view: MitraContract.MitraRatingView,
    private val database: FirebaseFirestore
) {

    fun loadRating(vendorId: String) {
        database.collection(BuildConfig.ratingDb)
            .whereEqualTo("vendorId", vendorId).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && !it.result!!.isEmpty) {
                    val ulasanList = it.result!!.toObjects(Rating::class.java)
                    val ulasanListAdapter = ArrayList<RatingAdapter>()
                    for (ulasan in ulasanList)
                        ulasanListAdapter.add(RatingAdapter(ulasan))
                    view.setUlasanAdapter(ulasanListAdapter)
                } else {
                    view.failedLoadRating()
                }
            }
    }
}