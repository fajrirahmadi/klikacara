package co.id.klikacara.splash

import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import co.id.klikacara.R
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.main.view.MainActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.base_activity_splash)
        Handler().postDelayed({
            this.showActivityAndFinishCurent(getIntent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }, 1000)
    }
}