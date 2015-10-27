package com.myth.myarth.gradleapplication.activity

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.ui.swipebacklayout.SwipeBackActivity
import groovy.transform.CompileStatic

@CompileStatic
class LoginActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // 目前仓库中 com.arasthel.swissknife.dsl.AndroidDSL.build(java.lang.Object, int, groovy.lang.Closure) 没有对闭包参数判空，需要加上一个空闭包
        button(R.id.btnClose, {})?.onClick { finish() }
    }

}
