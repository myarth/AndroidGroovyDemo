package com.myth.myarth.gradleapplication.common

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import groovy.transform.CompileStatic

@CompileStatic
class AppContext extends Application {

    private static AppContext app

    AppContext() {
        app = this
    }

    static synchronized AppContext getInstance() {
        if (app == null) {
            app = new AppContext()
        }
        app
    }

    @Override
    void onCreate() {
        super.onCreate()
        LeakCanary.install(this)
//        Thread.setDefaultUncaughtExceptionHandler(AppException.appExceptionHandler)
    }

}
