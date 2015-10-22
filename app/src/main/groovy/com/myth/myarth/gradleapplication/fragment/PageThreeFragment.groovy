package com.myth.myarth.gradleapplication.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.Nullable
import android.widget.ArrayAdapter
import android.widget.ListView
import com.arasthel.swissknife.dsl.components.GAsyncTask
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.ui.UiUtil
import com.myth.myarth.pulltorefresh.library.PullToRefreshBase
import com.myth.myarth.pulltorefresh.library.PullToRefreshListFragment
import com.myth.myarth.pulltorefresh.library.PullToRefreshListView
import groovy.transform.CompileStatic

@CompileStatic
class PageThreeFragment extends PullToRefreshListFragment {

    PullToRefreshListView mPullRefreshListView
    List<String> mListItems
    ArrayAdapter<String> mAdapter

    List<String> mStrings = ['Abbaye de Belloc', 'Abbaye du Mont des Cats', 'Abertam', 'Abondance', 'Ackawi',
                             'Acorn', 'Adelost', 'Affidelice au Chablis', 'Afuega\'l Pitu', 'Airag', 'Airedale', 'Aisy Cendre',
                             'Allgauer Emmentaler', 'Abbaye de Belloc', 'Abbaye du Mont des Cats', 'Abertam', 'Abondance', 'Ackawi',
                             'Acorn', 'Adelost', 'Affidelice au Chablis', 'Afuega\'l Pitu', 'Airag', 'Airedale', 'Aisy Cendre',
                             'Allgauer Emmentaler']

    @Override
    void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState)

        // Get PullToRefreshListView from Fragment
        mPullRefreshListView = pullToRefreshListView
        // Set a listener to be invoked when the list should be refreshed
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                new GetDataTask().execute()
                getGAsyncTask().execute()
            }
        })

        mListItems = []
        mListItems += mStrings
        mAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, mListItems)

        // You can also just use mPullRefreshListFragment.getListView()
        ListView actualListView = mPullRefreshListView.refreshableView
        actualListView.adapter = mAdapter
        setListShown(true)
    }

    GAsyncTask getGAsyncTask() {
        def gTask = new GAsyncTask({ param, gTask ->
            mStrings
        })
        gTask.after {
            mListItems.putAt(0, 'Added after refresh...' + new Random().nextInt(100))
            mAdapter.notifyDataSetChanged()
            mPullRefreshListView.onRefreshComplete()
        }
        gTask.error {
            mPullRefreshListView.onRefreshComplete()
            UiUtil.longToast(R.string.progress_load_error)
        }
        gTask
    }

    class GetDataTask extends AsyncTask<Object, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Object... params) {
            mStrings
        }

        @Override
        void onPostExecute(List<String> result) {
            mListItems.putAt(0, 'Added after refresh...' + new Random().nextInt(100))
            mAdapter.notifyDataSetChanged()
            mPullRefreshListView.onRefreshComplete()
            super.onPostExecute(result)
        }

    }

}
