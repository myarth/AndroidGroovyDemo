package com.myth.myarth.gradleapplication.fragment

import android.app.Activity
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.activity.SimpleActivity
import com.myth.myarth.gradleapplication.ui.UiUtil
import com.myth.myarth.gradleapplication.ui.pulltozoomview.PullToZoomScrollViewEx
import groovy.transform.CompileStatic

@CompileStatic
class MemberFragment extends Fragment {

    @InjectView
    PullToZoomScrollViewEx scrollView

    Activity fragmentActivity

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        def view = inflater.inflate(R.layout.fragment_member, container, false)
        SwissKnife.inject(this, view)
        view
    }

    @Override
    void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState)
        fragmentActivity = activity
        initView()
    }

    void initView() {
        def headView = LayoutInflater.from(fragmentActivity).inflate(R.layout.member_head_view, null, false)
        def zoomView = LayoutInflater.from(fragmentActivity).inflate(R.layout.member_zoom_view, null, false)
        def contentView = LayoutInflater.from(fragmentActivity).inflate(R.layout.member_content_view, null, false)
        scrollView.headerView = headView
        scrollView.zoomView = zoomView
        scrollView.scrollContentView = contentView

        headView.findViewById(R.id.tv_login)?.onClick {
            UiUtil.showLogin(activity)
        }
        headView.findViewById(R.id.tv_register)?.onClick {
            activity.startActivity(SimpleActivity)
        }

        def pullRootView = scrollView.pullRootView
        pullRootView.findViewById(R.id.tv_test1)?.onClick {}
        pullRootView.findViewById(R.id.tv_test2)?.onClick {}
        pullRootView.findViewById(R.id.tv_test3)?.onClick {}
    }
}
