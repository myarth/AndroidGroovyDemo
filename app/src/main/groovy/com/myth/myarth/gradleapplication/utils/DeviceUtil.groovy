package com.myth.myarth.gradleapplication.utils

import android.app.Activity
import android.content.Context
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import com.myth.myarth.gradleapplication.common.AppContext
import groovy.transform.CompileStatic


@CompileStatic
class DeviceUtil {

    // 根据手机的分辨率从 px(像素) 的单位 转成为 dp
    static int px2dp(Context context, float pxValue) {
        (pxValue / context.resources.displayMetrics.density + 0.5f) as int
    }

    // 根据手机的分辨率从 px(像素) 的单位 转成为 sp,字体的转换
    static int px2sp(Context context, float pxValue) {
        (pxValue / context.resources.displayMetrics.scaledDensity + 0.5f) as int
    }

    // 根据手机的分辨率从 dp 的单位 转成为 px(像素)
    static int dp2px(Context context, float dpValue) {
        (dpValue * context.resources.displayMetrics.density + 0.5f) as int
    }

    // 获取DisplayMetrics，包括屏幕高宽，密度等
    static DisplayMetrics getDisplayMetrics(Activity context) {
        def dm = new DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)
        dm
    }

    // 获得屏幕宽度 px
    static int getWidth(Activity context) {
        getDisplayMetrics(context).widthPixels
    }

    // 获得屏幕高度 px
    static int getHeight(Activity context) {
        getDisplayMetrics(context).heightPixels
    }

    static String getIMSI(Context context) {
        def imsi = ''
        try {
            imsi = (context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)?.subscriberId ?: ''
        } catch (e) {
        }
        imsi
    }

    static String getIMEI(Context context) {
        def imei = ''
        try {
            imei = (context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)?.deviceId ?: ''
        } catch (e) {
        }
        imei
    }

    static String getDeviceId(Context context = AppContext.instance) {
        def strIMEI = getIMEI(context)
        if (!strIMEI) {
            strIMEI = getIMSI(context)
        }
        strIMEI ? MiscUtil.getMD5((strIMEI * 3).bytes) : ''
    }

    static String getAppVersionName() {
        AppContext.instance.packageManager.getPackageInfo(AppContext.instance.packageName, 0).versionName
    }

}
