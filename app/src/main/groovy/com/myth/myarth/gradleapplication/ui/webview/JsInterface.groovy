package com.myth.myarth.gradleapplication.ui.webview

import android.webkit.JavascriptInterface
import com.myth.myarth.gradleapplication.common.NotObfuscateInterface
import com.myth.myarth.gradleapplication.ui.UiUtil
import groovy.transform.CompileStatic


@CompileStatic
class JsInterface implements NotObfuscateInterface {

    @JavascriptInterface
    void showMsg(String msg) {
        UiUtil.toast(msg)
    }

}
