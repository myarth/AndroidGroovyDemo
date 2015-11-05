package com.myth.myarth.gradleapplication.utils

import android.content.Context
import android.content.SharedPreferences
import com.myth.myarth.gradleapplication.common.AppContext
import groovy.transform.CompileStatic


@CompileStatic
class SharedPreferencesUtil {

    static final String FIRST_TIME_USE = 'first-time-use'
    static final String TOKEN = 'token'

    private static SharedPreferences sp
    private static final String SP_NAME = 'gradleapplication'

    static synchronized SharedPreferences getSharedPreferences(Context context) {
        if (sp == null) {
            synchronized (SharedPreferences) {
                if (sp == null) {
                    sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                }
            }
        }
        sp
    }

    static boolean getBoolean(String key, boolean defValue, Context context = AppContext.instance) {
        def value = getSharedPreferences(context).getBoolean(key, defValue)
        value != null ? value : defValue
    }

    static void putBoolean(String key, boolean value, Context context = AppContext.instance) {
        getSharedPreferences(context).edit().putBoolean(key, value).apply()
    }

    static String getString(String key, String defValue, Context context = AppContext.instance) {
        getSharedPreferences(context).getString(key, defValue)
    }

    static void putString(String key, String value, Context context = AppContext.instance) {
        getSharedPreferences(context).edit().putString(key, value).apply()
    }

}
