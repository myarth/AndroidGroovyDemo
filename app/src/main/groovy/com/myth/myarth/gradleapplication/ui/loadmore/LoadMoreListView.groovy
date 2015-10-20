package com.myth.myarth.gradleapplication.ui.loadmore

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.*
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.http.HttpClient
import groovy.transform.CompileStatic

@CompileStatic
class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    ProgressBar mProgressBar
    TextView mTextView

    boolean mLastItemVisible
    boolean mIsLoadingMore = false
    AbsListView.OnScrollListener mOnScrollListener
    OnLoadMoreListener mOnLoadMoreListener

    LoadMoreListView(Context context) {
        super(context)
        init()
    }

    LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs)
        init()
    }

    LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr)
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes)
        init()
    }

    def init() {
        def mFooterView = inflate(context, R.layout.layout_load_more, null) as RelativeLayout
        mTextView = mFooterView.findViewById(R.id.text) as TextView
        mProgressBar = mFooterView.findViewById(R.id.progress) as ProgressBar

        removeFooterView(mFooterView)
        setFooterDividersEnabled(false)
        addFooterView(mFooterView)
        super.setOnScrollListener(this)
    }

    @Override
    void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        /**
         * Set whether the Last Item is Visible. lastVisibleItemIndex is a
         * zero-based index, so we minus one totalItemCount to check
         */
        if (mOnLoadMoreListener) {
            mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1)
        }

        // Finally call OnScrollListener if we have one
        if (mOnScrollListener) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount)
        }
    }

    @Override
    void onScrollStateChanged(AbsListView view, int scrollState) {
        /**
         * Check that the scrolling has stopped, and that the last item is
         * visible.
         */
        if (!mIsLoadingMore && scrollState == SCROLL_STATE_IDLE && mLastItemVisible) {
            mIsLoadingMore = true
            onLoadMore()
        }

        if (mOnScrollListener) {
            mOnScrollListener.onScrollStateChanged(view, scrollState)
        }
    }

    void onLoadMore() {
        if (mOnLoadMoreListener) {
            mOnLoadMoreListener.onLoadMore()
        }
    }

    void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener
    }

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener
    }

    interface OnLoadMoreListener {
        void onLoadMore()
    }

    void updateLoadMoreViewText(List data) {
        if (adapter.count == 0 && !data) {
            setLoadMoreViewTextNoData()
        } else if (data?.size() < HttpClient.PAGE_SIZE) {
            setLoadMoreViewTextNoMoreData()
        }
        mIsLoadingMore = false
    }

    void setLoadMoreViewTextNoData() {
        mTextView.setText(R.string.pull_to_refresh_no_data_label)
        mProgressBar.visibility = GONE
        mIsLoadingMore = false
    }

    void setLoadMoreViewTextNoMoreData() {
        mTextView.setText(R.string.pull_to_refresh_no_more_data_label)
        mProgressBar.visibility = GONE
        mIsLoadingMore = false
    }

    void setLoadMoreViewTextError() {
        mTextView.setText(R.string.pull_to_refresh_net_error_label)
        mProgressBar.visibility = GONE
        mIsLoadingMore = false
    }

}
