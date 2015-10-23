package com.myth.myarth.gradleapplication.ui.webview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import com.myth.myarth.gradleapplication.utils.Utils
import groovy.transform.CompileStatic
import org.codehaus.groovy.classgen.asm.MethodCaller

import java.lang.reflect.Method

@CompileStatic
class BasicWebView extends WebView implements Serializable {

    Context mContext
    int mProgress = 100
    boolean mIsLoading = false
    String mLoadedUrl
    static boolean mBoMethodsLoaded = false

    static Method mOnPauseMethod = null
    static Method mOnResumeMethod = null
    static Method mSetFindIsUp = null
    static Method mNotifyFindDialogDismissed = null

    BasicWebView(Context context) {
        super(context)
        initBasicWebView(context)
    }

    BasicWebView(Context context, AttributeSet attrs) {
        super(context, attrs)
        initBasicWebView(context)
    }

    BasicWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr)
        initBasicWebView(context)
    }

    def initBasicWebView(Context context) {
        this.mContext = context
        initializeOptions()
        loadMethods()
    }

    @SuppressLint("NewApi")
    def initializeOptions() {
        settings.with {
            javaScriptEnabled = true
            loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            saveFormData = true
            savePassword = false
            defaultZoom = WebSettings.ZoomDensity.MEDIUM
            supportZoom = false
            supportMultipleWindows = false
            longClickable = true
            scrollbarFadingEnabled = true
            scrollBarStyle = SCROLLBARS_INSIDE_OVERLAY
            drawingCacheEnabled = true
            cacheMode = Utils.isNetworkAvailable() ? LOAD_DEFAULT : LOAD_CACHE_ELSE_NETWORK
        }
        CookieManager.instance.acceptCookie = true
    }

    def loadMethods() {
        if (!mBoMethodsLoaded) {
            mOnPauseMethod = internalGetMethod('onPause')
            mOnResumeMethod = internalGetMethod('onResume')
            mSetFindIsUp = internalGetMethod('setFindIsUp', Boolean.TYPE)
            mNotifyFindDialogDismissed = internalGetMethod('notifyFindDialogDismissed')
            mBoMethodsLoaded = true
        }
    }

    Method internalGetMethod(String name, Class... parameterTypes) {
        Method method = null
        try {
            method = WebView.getMethod(name, parameterTypes)
        } catch (e) {
            Log.e(BasicWebView.name, "${MethodCaller.name}: ${e.message}" as String)
        }
        method
    }

    void doOnPause() {
        internalInvoke(mOnPauseMethod)
    }

    void doOnResume() {
        internalInvoke(mOnResumeMethod)
    }

    void doSetFindIsUp(boolean value) {
        internalInvoke(mSetFindIsUp, value)
    }

    void doNotifyFindDialogDismissed() {
        internalInvoke(mNotifyFindDialogDismissed)
    }

    void internalInvoke(Method method, Object... args) {
        try {
            method?.invoke(this, args)
        } catch (e) {
            Log.e(BasicWebView.name, "${MethodCaller.name}: ${e.message}" as String)
        }
    }

    @Override
    void loadUrl(String url) {
        mLoadedUrl = url
        super.loadUrl(url)
    }

    void resetLoadedUrl() {
        mLoadedUrl = null
    }

    void notifyPageStarted() {
        mIsLoading = true
    }

    void notifyPageFinished() {
        mProgress = 100
        mIsLoading = false
    }

}
