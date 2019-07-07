package co.id.klikacara.authentication.view

import android.content.Intent
import android.os.Bundle
import co.id.klikacara.R
import co.id.klikacara.base.view.activity.BaseActivity

class AuthenticationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_authentication)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentById(R.id.loginFragment)?.onActivityResult(requestCode, resultCode, data)
    }
}