package co.id.klikacara.base.view.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import co.id.klikacara.R
import kotlinx.android.synthetic.main.base_activity_web_view.*
import org.apache.commons.lang3.StringUtils

class KlikWeb : BaseActivity() {

    companion object {
        val URL = "URL"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity_web_view)
        configureBackButton()
        webView.loadUrl(intent.getStringExtra(URL))
        webView.webViewClient = object : ConsumerWebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (StringUtils.isNotBlank(url) && url!!.contains("formResponse", true)) {
                    val intentBack = intent
                    intentBack.putExtra("Success", true)
                    finishActivityForResult(intentBack, 0)
                }
            }
        }
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.lightTouchEnabled = true
        webSettings.setSupportZoom(false)
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
    }
}