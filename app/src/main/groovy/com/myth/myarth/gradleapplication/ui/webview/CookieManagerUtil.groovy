package com.myth.myarth.gradleapplication.ui.webview

import android.content.Context
import android.webkit.CookieManager
import com.myth.myarth.gradleapplication.utils.DeviceUtil
import com.myth.myarth.gradleapplication.utils.SharedPreferencesUtil
import com.myth.myarth.gradleapplication.utils.Utils
import groovy.transform.CompileStatic

import java.text.SimpleDateFormat

@CompileStatic
class CookieManagerUtil {

    static def syncCookies(Context context, String url) {
        def matcher = url =~ /^\w+:\/\/([\w\d]+\.)*([\w\d]+\.[\w\d]+)(:\d+)?\/?/
        if (matcher.find() && matcher.group(2)) {
            CookieManager.instance.with {
                acceptCookie = true
                removeAllCookie()
                [
                        path     : '/; ',
                        domain   : ".${matcher.group(2)}",
                        expires  : "${expires()}",
                        version  : "${DeviceUtil.appVersionName}",
                        token    : "${SharedPreferencesUtil.getString(SharedPreferencesUtil.TOKEN, '')}",
                        anonymous: 0,
                        wifi     : "${Utils.isWifi(context) ? 1 : 0}",
                        lon      : '',
                        lat      : '',
                ].each { k, v ->
                    setCookie(url, "${k}=${v}" as String)
                }
                it
            }.flush()
        }
    }

    static String expires() {
        def sdf = new SimpleDateFormat("E,dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
        "${sdf.format(new Date() + 1)} GMT"
    }

}
