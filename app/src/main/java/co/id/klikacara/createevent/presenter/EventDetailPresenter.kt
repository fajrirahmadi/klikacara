package co.id.klikacara.createevent.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.Participant
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.createevent.contract.EventContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.apache.commons.lang3.StringUtils

class EventDetailPresenter(
    val view: EventContract.DetailEventView,
    val auth: FirebaseAuth,
    val database: FirebaseFirestore
) : BasePresenter() {

    fun doJoin(event: Order) {
        view.showProgressDialog()
        database.collection(BuildConfig.userDb).document(auth.uid!!)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val user = it.result!!.toObject(User::class.java)
                    if (user != null) {
                        val participant = Participant()
                        participant.uid = user.uid!!
                        participant.email = user.email
                        participant.eventId = event.key!!
                        participant.expiredReward = event.endDate
                        participant.isRewardClaimed = false
                        participant.name = user.name
                        participant.rewardCode =
                            database.collection(BuildConfig.eventDb).document().id
                        joinEvent(participant)
                    }
                }
                view.dismissProgressDialog()
            }
    }

    fun checkIsUserJoin(event: Order) {
        view.showProgressDialog()
        database.collection(BuildConfig.eventDb)
            .document(StringHelper.getStringBuilderToString(event.key, "_", auth.uid!!)).get()
            .addOnCompleteListener { participants ->
                if (participants.isSuccessful && participants.result != null) {
                    val data = participants.result!!.toObject(Participant::class.java)
                    if (StringUtils.isBlank(data?.uid)) {
                        view.doOnUserNotJoined()
                    } else {
                        view.doOnUserAlreadyJoined()
                    }
                }
                view.dismissProgressDialog()
            }
    }

    fun joinEvent(participant: Participant) {
        view.showProgressDialog()
        database.collection(BuildConfig.eventDb).document(
            StringHelper.getStringBuilderToString(
                participant.eventId,
                "_",
                participant.uid
            )
        ).set(participant)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    view.doOnJoinEventSuccess()
                }
                view.dismissProgressDialog()
            }
    }

    fun checkLoginStatus() {
        if (auth.currentUser == null)
            view.showLoginDialog()
        else {
            view.showConfirmationDialog()
        }
    }

    fun getUserData() {
        if (auth.currentUser != null) {
            view.showProgressDialog()
            database.collection(BuildConfig.userDb).document(auth.uid!!)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val user = it.result!!.toObject(User::class.java)
                        if (user != null) {
                            view.setUser(user)
                        }
                    }
                    view.dismissProgressDialog()
                }
        }
    }
}