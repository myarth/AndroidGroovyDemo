package com.myth.myarth.gradleapplication.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.KeyEvent
import android.widget.RadioGroup
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.fragment.HomePageFragment
import com.myth.myarth.gradleapplication.fragment.MemberFragment
import com.myth.myarth.gradleapplication.fragment.SwissKnifeFragment
import groovy.transform.CompileStatic

@CompileStatic
class MainActivity extends BaseFragmentActivity {

    Fragment homePagerFragment = new HomePageFragment()
    Fragment swissKnifeFragment1 = new SwissKnifeFragment()
    Fragment swissKnifeFragment2 = new SwissKnifeFragment()
    Fragment memberFragment = new MemberFragment()
    List<Fragment> fragmentList = [homePagerFragment, swissKnifeFragment1, swissKnifeFragment2, memberFragment]

    @Lazy
    FragmentManager fManager = supportFragmentManager
    int currSel = 0 // listener 创建了 MainActivity$1 的类，此处不能使用 static 修饰符（groovy closure 可以使用）

    @InjectView
    RadioGroup group

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SwissKnife.inject(this)
        initFootBar()
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data)
        fragmentList[currSel]?.onActivityResult(requestCode, resultCode, data)
    }

    void initFootBar() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.foot_bar_home) {
                    currSel = 0
                } else if (checkedId == R.id.foot_bar_im) {
                    currSel = 1
                } else if (checkedId == R.id.foot_bar_interest) {
                    currSel = 2
                } else if (checkedId == R.id.main_footbar_user) {
                    currSel = 3
                }
                addFragmentToStack(currSel)
            }
        })
        addFragmentToStack(0)
    }

    void addFragmentToStack(int sel) {
        def fragmentTransaction = fManager.beginTransaction()
        def fragment = fragmentList[sel]
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, fragment)
        }
        fragmentList.eachWithIndex { Fragment entry, i ->
            if (i == sel && entry.isAdded()) {
                fragmentTransaction.show(entry)
            } else if (entry?.isAdded() && entry?.isVisible()) {
                fragmentTransaction.hide(entry)
            }
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    @Override
    boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
