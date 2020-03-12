package co.id.klikacara.base.view.activity

import android.annotation.TargetApi
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import org.apache.commons.lang3.StringUtils

open class ConsumerWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(if (url.contains("http", true)) url else "http://$url")
        return true
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val url = request.url.toString()
        view.loadUrl(if (url.contains("http", true)) url else "http://$url")
        return true
    }
}