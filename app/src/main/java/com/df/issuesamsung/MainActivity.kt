package com.df.issuesamsung

import android.app.Dialog
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.os.Message
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        webView =findViewById(R.id.webView)
        initWebView();

    }

    private fun initWebView() {
        webView.apply {
            clearCache(true)
            clearHistory()

            settings.setSupportZoom(false)
            settings.displayZoomControls = false
            settings.builtInZoomControls = false
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            settings.loadsImagesAutomatically = true
            settings.allowContentAccess = false
            settings.defaultTextEncodingName = "UTF-8"
            settings.mediaPlaybackRequiresUserGesture = false
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            settings.textZoom = 100

            webViewClient = WebClient()

        }
        webView.setInitialScale(100)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)


        webView.loadUrl("http://192.168.0.42:3000/ModuleTest?moduleId=P15MA20185&resumePageUId=P15MA20185-5&resourceType=1")

    }

    inner class ChromeClient: WebChromeClient() {


        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            val newWebView = WebView(this@MainActivity)
            newWebView.webViewClient = WebViewClient()
            newWebView.settings.javaScriptEnabled = true

            // 새 Dialog 생성 및 설정
            val dialog = Dialog(this@MainActivity).apply {
                setContentView(newWebView)
                window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                show()
            }

            // 새 WebView 설정
            val transport = resultMsg?.obj as WebView.WebViewTransport
            transport.webView = newWebView
            resultMsg.sendToTarget()

            newWebView.webChromeClient = object : WebChromeClient() {
                override fun onCloseWindow(window: WebView?) {
                    // Dialog를 닫을 때의 처리
                    dialog.dismiss()
                }
            }

            return true

        }
    }
    inner class WebClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return false
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            handler?.cancel()
            finish()
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
        }
    }

}
