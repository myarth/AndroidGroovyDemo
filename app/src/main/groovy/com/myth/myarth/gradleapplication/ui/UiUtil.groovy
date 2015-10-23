package com.myth.myarth.gradleapplication.ui

import android.content.Context
import android.widget.Toast
import com.myth.myarth.gradleapplication.activity.LoginActivity
import com.myth.myarth.gradleapplication.activity.MainActivity
import com.myth.myarth.gradleapplication.common.AppContext
import groovy.transform.CompileStatic

@CompileStatic
class UiUtil {

    static def toast(int ResId, boolean isShort = true) {
        toast(ResId, AppContext.instance, isShort)
    }

    static def toast(int ResId, Context context, boolean isShort = true) {
        int duration = isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG
        Toast.makeText(context, ResId, duration).show()
    }

    static def toast(String text, boolean isShort = true) {
        toast(text, AppContext.instance, isShort)
    }

    static def toast(String text, Context context, boolean isShort = true) {
        int duration = isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG
        Toast.makeText(context, text, duration).show()
    }

    static void showHome(Context context) {
        context.startActivity(MainActivity)
    }

    static void showLogin(Context context) {
        context.startActivity(LoginActivity)
    }

}
