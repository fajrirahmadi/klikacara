package co.id.klikacara.createevent.view

import android.content.Intent
import android.os.Bundle
import co.id.klikacara.R
import co.id.klikacara.base.view.activity.BaseActivity

class CreateEventActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentById(R.id.eventDetailFragment)
            ?.onActivityResult(requestCode, resultCode, data)
    }
}