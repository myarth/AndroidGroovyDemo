package com.myth.myarth.gradleapplication.activity

import android.annotation.TargetApi
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.WindowManager
import com.myth.myarth.gradleapplication.common.AppManager
import com.umeng.analytics.MobclickAgent
import groovy.transform.CompileStatic

@CompileStatic
class BaseFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        AppManager.instance.addActivity(this)
    }

    @Override
    protected void onResume() {
        super.onResume()
        // 友盟session统计，获取新增用户、活跃用户、启动次数、使用时长等基本数据
//        MobclickAgent.onResume(this)
    }

    @Override
    protected void onPause() {
        super.onPause()
//        MobclickAgent.onPause(this)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy()
        // 结束Activity从堆栈中移除
        AppManager.instance.finishActivity(this)
    }

    @TargetApi(19)
    protected void setTranslucentStatus() {
        def window = getWindow()
        // Translucent status bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // Translucent navigation bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }

}
