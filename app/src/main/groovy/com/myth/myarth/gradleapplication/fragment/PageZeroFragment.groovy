package com.myth.myarth.gradleapplication.fragment

import android.app.Activity
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.ui.loadmore.LoadMoreListView
import com.myth.myarth.gradleapplication.ui.quickadapter.BaseAdapterHelper
import com.myth.myarth.gradleapplication.ui.quickadapter.QuickAdapter
import com.myth.myarth.gradleapplication.utils.DeviceUtil
import com.myth.myarth.gradleapplication.ui.UiUtil
import com.squareup.picasso.Picasso
import groovy.transform.CompileStatic
import in.srain.cube.views.ptr.PtrClassicFrameLayout
import in.srain.cube.views.ptr.PtrDefaultHandler
import in.srain.cube.views.ptr.PtrFrameLayout
import in.srain.cube.views.ptr.PtrHandler
import in.srain.cube.views.ptr.header.StoreHouseHeader

@CompileStatic
class PageZeroFragment extends Fragment {

    @InjectView(R.id.rotate_header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame
    @InjectView
    LoadMoreListView listView

    int pno = 1
    boolean isLoadAll = false
    Activity fragmentActivity
    QuickAdapter adapter

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        def view = inflater.inflate(R.layout.fragment_page_zero, container, false)
        SwissKnife.inject(this, view)
        view
    }

    @Override
    void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState)
        fragmentActivity = activity
        initData()
        initView()
        loadData()
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
        listView.adapter = adapter

        def storeHouseHeader = new StoreHouseHeader(fragmentActivity)
        storeHouseHeader.setPadding(0, DeviceUtil.dp2px(context, 15), 0, 0)
        storeHouseHeader.initWithString('Fine')
        mPtrFrame.headerView = storeHouseHeader
        mPtrFrame.addPtrUIHandler(storeHouseHeader)

        // 下拉刷新
        mPtrFrame.lastUpdateTimeRelateObject = this
        mPtrFrame.ptrHandler = new PtrHandler() {
            @Override
            void onRefreshBegin(PtrFrameLayout frame) {
                initData()
                loadData()
            }

            @Override
            boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header)
            }
        }

        // 加载更多
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            void onLoadMore() {
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
