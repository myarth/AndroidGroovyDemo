package com.myth.myarth.gradleapplication.fragment

import android.app.Activity
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.http.HttpClient
import com.myth.myarth.gradleapplication.http.HttpResponseHandler
import com.myth.myarth.gradleapplication.ui.quickadapter.BaseAdapterHelper
import com.myth.myarth.gradleapplication.ui.quickadapter.QuickAdapter
import com.myth.myarth.gradleapplication.utils.UiUtil
import com.myth.myarth.pulltorefresh.library.PullToRefreshBase
import com.myth.myarth.pulltorefresh.library.PullToRefreshListView
import com.squareup.okhttp.Request
import com.squareup.picasso.Picasso
import groovy.transform.CompileStatic

@CompileStatic
class SwissKnifeFragment extends Fragment {

    @InjectView
    PullToRefreshListView listView

    int pno = 1
    boolean isLoadAll = false
    Activity fragmentActivity
    QuickAdapter adapter

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        def view = inflater.inflate(R.layout.fragment_swiss_knife, container, false)
        SwissKnife.inject(this, view)
        view
    }

    @Override
    void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState)
        fragmentActivity = activity
        initData()
        initView()
    }

    void initData() {
        pno = 1
        isLoadAll = false
    }

    void initView() {
        adapter = new QuickAdapter(fragmentActivity, R.layout.fragment_swiss_knife_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, Object item) {
                def map = item as Map
                helper.setText(R.id.cai_name, map['name'] as String)
                        .setText(R.id.address, map['addr'] as String)
                        .setImageUrl(R.id.logo, map['logo'] as String)
            }
        }
        listView.withLoadMoreView(context)
        listView.adapter = adapter

        // 下拉刷新
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initData()
                loadData()
            }
        })

        // 加载更多
        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                loadData()
            }
        })

        // 点击事件
        listView.onClick {
            UiUtil.showLogin(fragmentActivity)
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_FLING) {
                    Picasso.with(fragmentActivity).pauseTag(fragmentActivity)
                } else {
                    Picasso.with(fragmentActivity).resumeTag(fragmentActivity)
                }
            }

            @Override
            void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        })
    }

    void loadData() {
        if (isLoadAll) {
            return
        }
        listView.setLoadMoreViewTextLoading()

        // TODO http request and refresh list
    }

    @Override
    void onResume() {
        super.onResume()
        Picasso.with(fragmentActivity).resumeTag(fragmentActivity)
    }

    @Override
    void onPause() {
        super.onPause()
        Picasso.with(fragmentActivity).pauseTag(fragmentActivity)
    }

    @Override
    void onDestroy() {
        super.onDestroy()
        Picasso.with(fragmentActivity).cancelTag(fragmentActivity)
    }

}
