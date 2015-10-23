package com.myth.myarth.gradleapplication.ui.webview

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import com.myth.myarth.gradleapplication.activity.WebViewActivity
import groovy.transform.CompileStatic


@CompileStatic
class BasicWebViewClient extends WebViewClient {

    WebViewActivity webViewActivity

    BasicWebViewClient(WebViewActivity webViewActivity) {
        super()
        this.webViewActivity = webViewActivity
    }

    @Override
    void onPageFinished(WebView view, String url) {
        (view as BasicWebView).notifyPageFinished()
        webViewActivity.onPageFinished(view, url)
        super.onPageFinished(view, url)
    }

    @Override
    void onPageStarted(WebView view, String url, Bitmap favicon) {
        (view as BasicWebView).notifyPageStarted()
        webViewActivity.onPageStarted(view, url, favicon)
        super.onPageStarted(view, url, favicon)
    }

    @TargetApi(8)
    @Override
    void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        view.stopLoading()
        view.clearView()
        webViewActivity.onReceivedError()
    }

    @Override
    boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 调用系统的邮件，拨打电话
        if (url?.startsWith('mailto:') || url?.startsWith('tel:')) {
            def intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
            webViewActivity.startActivity(intent)
            return true
        }

        // 加载链接
        (view as BasicWebView).resetLoadedUrl()
        if (url && !url.startsWith('about:blank')) {
            view.loadUrl(url)
        }
        webViewActivity.shouldOverrideUrlLoading(view, url)

        false
    }

}
