package com.myth.myarth.gradleapplication.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.*
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.Extra
import com.arasthel.swissknife.annotations.InjectView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.ui.ProgressBarHelper
import com.myth.myarth.gradleapplication.ui.webview.BasicWebView
import com.myth.myarth.gradleapplication.ui.webview.BasicWebViewClient
import com.myth.myarth.gradleapplication.ui.webview.CookieManagerUtil
import com.myth.myarth.gradleapplication.ui.webview.JsInterface
import groovy.transform.CompileStatic

@CompileStatic
class WebViewActivity extends FragmentActivity implements ProgressBarHelper.ProgressBarClickListener {

    @InjectView
    ProgressBar web_src_loadProgress
    @InjectView
    Button btnBack
    @InjectView
    TextView textTitle
    @Extra
    String title // 打开页面的标题
    @Extra
    String sourceUrl // 打开页面时的URL

    Activity mContext
    BasicWebView mWebView
    ProgressBarHelper progressBarHelper
    ValueCallback<Uri> mUploadMessage // 上传文件信息
    String currentUrl // 点击链接后的URL

    static final int FILE_CHOOSER_RESULT_CODE = 1

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        SwissKnife.inject(this)
        SwissKnife.loadExtras(this)


        setConfigCallback(applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager)
        initView()
        loadWebViewUrl()
    }

    void initView() {
        mContext = this
        mWebView = new BasicWebView(applicationContext)
        def mWebContainer = view(R.id.layoutWebViewParent) as FrameLayout
        mWebContainer.addView(mWebView)

        def layoutLoading = layoutInflater.inflate(R.layout.layout_loading, null) as LinearLayout
        mWebContainer.addView(layoutLoading)

        progressBarHelper = new ProgressBarHelper(this, layoutLoading)
        progressBarHelper.progressBarClickListener = this

        btnBack.show()
        btnBack.onClick {
            finish()
        }
        textTitle.text = title

        mWebView.settings.with {
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            loadWithOverviewMode = true
        }
        mWebView.addJavascriptInterface(new JsInterface(), 'JsInterface')
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            void onDownloadStart(String url, String userAgent,
                                 String contentDisposition, String mimetype,
                                 long contentLength) {
                def uri = Uri.parse(url)
                def intent = new Intent(Intent.ACTION_VIEW, uri)
                mContext.startActivity(intent)
            }
        })
        mWebView.setWebViewClient(new BasicWebViewClient(this))
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    progressBarHelper.goneLoading()
                }
                web_src_loadProgress.progress = newProgress
            }

            // For Android < 3.0
            void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg
                def intent = new Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = '*/*'
                mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILE_CHOOSER_RESULT_CODE)
            }

            // For Android 3.0+
            void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooser(uploadMsg)
            }

            // For Android 4.1
            void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg)
            }
        })
    }

    void loadWebViewUrl() {
        CookieManagerUtil.syncCookies(this, sourceUrl)
        mWebView.loadUrl(sourceUrl)
        currentUrl = sourceUrl
        progressBarHelper.showLoading()
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (mUploadMessage) {
                Uri uri = !data || resultCode != RESULT_OK ? null : data.data
                mUploadMessage.onReceiveValue(uri)
                mUploadMessage = null
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy()
        setConfigCallback(null)
        mWebView?.removeAllViews()
        mWebView?.destroy()
        mWebView = null
        System.exit(0)
    }

    void setConfigCallback(WindowManager windowManager) {
        try {
            def field = WebView.getDeclaredField('mWebViewCore')
            field = field.type.getDeclaredField('mBrowserFrame')
            field = field.type.getDeclaredField('sConfigCallback')
            field.accessible = true
            def configCallback = field.get(null)
            if (configCallback) {
                field = field.type.getDeclaredField('mWindowManager')
                field.accessible = true
                field.set(configCallback, windowManager)
            }
        } catch (e) {
        }
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
