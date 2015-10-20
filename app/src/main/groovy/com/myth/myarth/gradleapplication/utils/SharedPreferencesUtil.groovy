package com.myth.myarth.gradleapplication.utils

import android.content.Context
import android.content.SharedPreferences
import com.myth.myarth.gradleapplication.common.AppContext
import groovy.transform.CompileStatic


@CompileStatic
@Singleton
class SharedPreferencesUtil {

    private static final String SP_NAME = 'gradleapplication'

    static final String FIRST_TIME_USE = 'first-time-use'

    SharedPreferences getSharedPreferences() {
        AppContext.instance.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    boolean getBoolean(String key, boolean defValue) {
        def value = sharedPreferences?.getBoolean(key, defValue)
        value != null ? value : defValue
    }

    void putBoolean(String key, boolean value) {
        sharedPreferences?.edit()?.putBoolean(key, value)?.apply()
    }

}
