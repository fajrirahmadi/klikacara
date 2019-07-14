package co.id.klikacara.base.view.activity

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import co.id.klikacara.R
import kotlinx.android.synthetic.main.base_activity_web_view.*

class KlikWeb : BaseActivity() {

    companion object {
        val URL = "URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity_web_view)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webView.loadUrl(intent.getStringExtra(URL))
    }
}