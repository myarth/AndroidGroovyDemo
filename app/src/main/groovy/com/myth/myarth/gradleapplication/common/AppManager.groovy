package com.myth.myarth.gradleapplication.common

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import groovy.transform.CompileStatic


/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
@CompileStatic
@Singleton
class AppManager {

    private static LinkedList<Activity> activityStack = []

    /**
     * 添加Activity到堆栈
     */
    def addActivity(Activity activity) {
        activityStack << activity
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    Activity currentActivity() {
        activityStack?.last
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    void finishActivity() {
        finishActivity(activityStack?.last)
    }

    /**
     * 结束指定的Activity
     */
    void finishActivity(Activity activity) {
        if (activity) {
            activityStack?.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    void finishActivity(Class<?> clazz) {
        activityStack?.each {
            if (it.class == clazz) {
                finishActivity(it)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    void finishAllActivity() {
        activityStack?.each {
            it?.finish()
            it = null
        }?.clear()
    }

    /**
     * 退出应用程序
     */
    void AppExit(Context context) {
        try {
            finishAllActivity()
            def activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.restartPackage(context.packageName)
            System.exit(0)
        } catch (Exception e) {
        }
    }

    int ActivityStackSize() {
        activityStack?.size() ?: 0
    }

}
