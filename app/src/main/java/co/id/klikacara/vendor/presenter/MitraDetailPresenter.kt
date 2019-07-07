package co.id.klikacara.vendor.presenter

import co.id.klikacara.vendor.contract.MitraContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MitraDetailPresenter(
    private val view: MitraContract.MitraDetailView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
) {


}