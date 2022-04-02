package com.example.demoapp.ui.webview

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProviders
import com.example.demoapp.R
import com.example.demoapp.base.BaseActivity
import com.example.demoapp.databinding.ActivityWebviewBinding
import com.example.demoapp.datalayer.storage.AppPref
import com.example.demoapp.utils.extension.PARAM_TOKEN
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class MyWebViewActivity : BaseActivity(), View.OnClickListener {


    private lateinit var binding:ActivityWebviewBinding
    private lateinit var viewModel: WebViewModel
    private var loadUrl = ""
    private var shouldUseCookie: Boolean = false
    private var title = ""

    override fun getLayoutId(): Int? {
        return R.layout.activity_webview
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, WebViewModelFactory(this)).get(WebViewModel::class.java)

        init()
    }

    private fun init() {
        getBundleData()
        setupActionBar()
        loadWebData()
    }

    private fun getBundleData() {
        loadUrl = intent.getStringExtra("url").toString()
        shouldUseCookie = intent.getBooleanExtra("useCookie", false)
        title = intent.getStringExtra("title").toString()
    }

    private fun setupActionBar() {
        tvTitle.text = title
    }


    private fun loadWebData() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                pbLoader.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                pbLoader.visibility = View.GONE
            }
        }
        webView.clearCache(true)
        if (shouldUseCookie) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
                CookieManager.getInstance().setAcceptCookie(true)
                CookieManager.getInstance().setCookie(loadUrl, AppPref.getValue(PARAM_TOKEN, "")) {
                    webView.loadUrl(loadUrl)
                }
            } else {
                webView.loadUrl(loadUrl)
            }
        } else {
            webView.loadUrl(loadUrl)
        }


        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.builtInZoomControls = true
        webView.setInitialScale(1)
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true
    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }

    private fun backUserToPreviousScreen() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

}