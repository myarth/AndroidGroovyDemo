package com.myth.myarth.gradleapplication.activity

import android.os.Bundle
import com.myth.myarth.gradleapplication.R
import groovy.transform.CompileStatic

@CompileStatic
class LoginActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // 目前仓库中 com.arasthel.swissknife.dsl.AndroidDSL.build(java.lang.Object, int, groovy.lang.Closure) 没有对闭包参数判空，需要加上一个空闭包
        this.button(R.id.btnClose) {}?.onClick {
            finish()
        }
    }

}
