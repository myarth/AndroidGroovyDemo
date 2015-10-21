package com.myth.myarth.gradleapplication.utils

import android.content.Context
import android.widget.Toast
import com.myth.myarth.gradleapplication.activity.LoginActivity
import com.myth.myarth.gradleapplication.activity.MainActivity
import com.myth.myarth.gradleapplication.common.AppContext
import groovy.transform.CompileStatic

@CompileStatic
class UiUtil {

    static def shortToast(int resId, Context context = AppContext.instance) {
        toast(resId, Toast.LENGTH_SHORT, context)
    }

    static def longToast(int resId, Context context = AppContext.instance) {
        toast(resId, Toast.LENGTH_LONG, context)
    }

    static def shortToast(String text, Context context = AppContext.instance) {
        toast(text, Toast.LENGTH_SHORT, context)
    }

    static def longToast(String text, Context context = AppContext.instance) {
        toast(text, Toast.LENGTH_LONG, context)
    }

    private static def toast(int resId, int duration, Context context) {
        Toast.makeText(context, resId, duration).show()
    }

    private static def toast(String text, int duration, Context context) {
        Toast.makeText(context, text, duration).show()
    }

    static void showHome(Context context) {
        context.startActivity(MainActivity)
    }

    static void showLogin(Context context) {
        context.startActivity(LoginActivity)
    }

}
