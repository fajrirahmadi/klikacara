package co.id.klikacara.createevent.view

import android.os.Bundle
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.Participant
import co.id.klikacara.`object`.adapter.ParticipantAdapter
import co.id.klikacara.base.view.activity.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.activity_view_participant.*
import org.parceler.Parcels

class ViewParticipantActivity : BaseActivity() {

    private val participantAdapter = FastItemAdapter<ParticipantAdapter>()
    private var event: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_participant)
        event = Parcels.unwrap(intent.extras?.getParcelable(BuildConfig.eventDb))
        configureItemAdapter(participantAdapter, listOfViewRecycleView)
        initData()
    }

    private fun initData() {
        showProgressDialog()
        FirebaseFirestore.getInstance().collection(BuildConfig.eventDb)
            .whereEqualTo("eventId", event?.key)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val participantList = it.result!!.toObjects(Participant::class.java)
                    participantAdapter.clear()
                    for (data in participantList)
                        participantAdapter.add(ParticipantAdapter(data))
                }
                dismissProgressDialog()
            }
    }
}