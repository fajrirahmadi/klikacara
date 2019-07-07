package co.id.klikacara.product.view

import android.content.Intent
import android.os.Bundle
import co.id.klikacara.R
import co.id.klikacara.base.view.activity.BaseActivity

class MyProductActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_product)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentById(R.id.addEditProductFragment)
            ?.onActivityResult(requestCode, resultCode, data)
    }
}