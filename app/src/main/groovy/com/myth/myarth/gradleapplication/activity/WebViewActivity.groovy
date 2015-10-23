package com.myth.myarth.gradleapplication.activity

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.ui.ProgressBarHelper
import com.myth.myarth.gradleapplication.ui.webview.BasicWebView
import groovy.transform.CompileStatic

@CompileStatic
class WebViewActivity extends FragmentActivity implements ProgressBarHelper.ProgressBarClickListener {

    @InjectView
    ProgressBar web_src_loadProgress

    Activity context
    BasicWebView mWebView
    ProgressBarHelper progressBarHelper
    String title // 打开页面的标题
    String sourceUrl // 打开页面时的URL
    String currentUrl // 点击链接后的URL

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        SwissKnife.inject(this)

        context = this
        mWebView = new BasicWebView(applicationContext)
        def mWebContainer = view(R.id.layoutWebViewParent) as FrameLayout
        mWebContainer.addView(mWebView)
    }

    void onPageFinished(WebView webView, String url) {
        updateUi(webView)
        showWebSrcLoadProgress()
    }

    void onPageStarted(WebView webView, String url, Bitmap favicon) {
        updateUi(webView)
        hideWebSrcLoadProgress()
    }

    void onReceivedError() {
        progressBarHelper.goneLoading()
        progressBarHelper.showNetError()
        hideWebSrcLoadProgress()
    }

    def showWebSrcLoadProgress() {
        if (web_src_loadProgress.visibility == View.GONE) {
            web_src_loadProgress.show()
        }
    }

    def hideWebSrcLoadProgress() {
        if (web_src_loadProgress.visibility == View.VISIBLE) {
            web_src_loadProgress.hide()
        }
    }

    def shouldOverrideUrlLoading(WebView webView, String url) {
        currentUrl = url
    }

    void updateUi(WebView webView) {
        // TODO
    }

    @Override
    void clickRefresh() {
        mWebView.loadUrl(currentUrl)
    }

}
