package com.example.shellpass

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback

class WebViewActivity : ComponentActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        webView = findViewById(R.id.webView)
        val url = intent.getStringExtra("url")?.let { fixUrl(it) } ?: "https://example.com"

        // Enable WebView debugging
        WebView.setWebContentsDebuggingEnabled(true)

        setupWebView(url)

        // Handle back gestures and button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(url: String) {
        webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                cacheMode = WebSettings.LOAD_NO_CACHE
            }

            // Clear cookies before loading the URL
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()

            // Prevent old cache issues
            clearCache(true)
            clearHistory()
            clearFormData()

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    request?.url?.let { view?.loadUrl(it.toString()) }
                    return true
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    Toast.makeText(this@WebViewActivity, "Error loading page", Toast.LENGTH_SHORT).show()
                }
            }

            webChromeClient = WebChromeClient()

            // Make sure URL starts with https://
            val fixedUrl = if (!url.startsWith("http")) "https://$url" else url
            loadUrl(fixedUrl)
        }
    }

    private fun fixUrl(url: String): String {
        return if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }
    }
}