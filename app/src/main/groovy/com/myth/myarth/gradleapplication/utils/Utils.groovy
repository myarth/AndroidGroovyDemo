package com.myth.myarth.gradleapplication.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.myth.myarth.gradleapplication.common.AppContext
import groovy.transform.CompileStatic


@CompileStatic
class Utils {

    private static final String TAG = "Utils"

    static Bundle getMetaData(Context context) {
        def metaData = null
        try {
            metaData = context?.packageManager?.getApplicationInfo(context?.packageName, PackageManager.GET_META_DATA)?.metaData
        } catch (PackageManager.NameNotFoundException e) {
        }
        metaData
    }

    // 获取ApiKey
    static String getMetaValue(Context context, String metaKey) {
        def metaValue = null
        if (context && metaKey) {
            try {
                metaValue = getMetaData(context)?.getString(metaKey)
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        metaValue
    }


    static NetworkInfo getActiveNetworkInfo(Context context) {
        (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)?.activeNetworkInfo
    }

    static boolean isNetworkAvailable(Context context = AppContext.instance) {
        getActiveNetworkInfo(context)?.isAvailable() ?: false
    }

    static boolean isNetworkConnected(Context context = AppContext.instance) {
        def networkInfo = getActiveNetworkInfo(context)
        (networkInfo?.isAvailable() && networkInfo?.isConnected()) ?: false
    }

    static boolean isWifi(Context context = AppContext.instance) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context)
        (networkInfo?.isAvailable() && networkInfo?.isConnected() && networkInfo.type == ConnectivityManager.TYPE_WIFI) ?: false
    }

    // 设置手机网络类型，wifi，cmwap，ctwap，用于联网参数选择
    static String getNetworkType() {
        def networkInfo = getActiveNetworkInfo(AppContext.instance)
        if (!networkInfo?.isAvailable()) {
            return ''
        }

        def networkType = 'wifi'
        def networks = ['cmwap', 'uniwap', '3gwap', 'ctwap']

        def extraInfo = networkInfo.extraInfo?.trim()?.toLowerCase()
        if (networks.contains(extraInfo)) {
            networkType = extraInfo == 'ctwap' ? 'ctwap' : 'cmwap'
        }

        networkType
    }

    /**
     * 检测Sdcard是否存在
     */
    static boolean isExitsSdcard() {
        Environment.externalStorageState == Environment.MEDIA_MOUNTED
    }

    static boolean checkSdcard(Context context) {
        def exitsSdcard = isExitsSdcard()
        if (!exitsSdcard) {
            Toast.makeText(context, '请插入手机存储卡再使用本功能', Toast.LENGTH_SHORT).show()
        }
        exitsSdcard
    }

    static String getString(Context context, int resId) {
        context?.resources?.getString(resId)
    }

    static boolean isIntentSafe(Activity activity, Intent intent) {
        activity?.packageManager?.queryIntentActivities(intent, 0)?.size() > 0
    }

}
