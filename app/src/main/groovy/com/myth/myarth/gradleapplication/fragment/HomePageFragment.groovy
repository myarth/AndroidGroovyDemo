package com.myth.myarth.gradleapplication.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.resources.StringArrayRes
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.ui.tabstrip.PagerSlidingTabStrip
import groovy.transform.CompileStatic


@CompileStatic
class HomePageFragment extends Fragment {

    @InjectView
    ViewPager pager
    @InjectView
    PagerSlidingTabStrip tabs
    @StringArrayRes
    String[] news_titles

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        def view = inflater.inflate(R.layout.fragment_home_page, container, false)
        SwissKnife.inject(this, view)
        view
    }

    @Override
    void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState)
        pager.adapter = new NewsAdapter(childFragmentManager)
        pager.pageMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, resources.displayMetrics) as int
        tabs.viewPager = pager
    }

    class NewsAdapter extends FragmentPagerAdapter {

        NewsAdapter(FragmentManager fm) {
            super(fm)
        }

        @Override
        Fragment getItem(int position) { // 控件 getItem 是初始化每个 fragment 时调用，和之后切换的方法一样，会预加载一个邻近的 fragment
            Fragment fragment
            if (position == 0) {
                fragment = new PageZeroFragment()
            } else if (position == 3) {
                fragment = new PageThreeFragment()
            } else {
                fragment = new SwissKnifeFragment()
            }
            fragment
        }

        @Override
        CharSequence getPageTitle(int position) {
            news_titles[position]
        }

        @Override
        int getCount() {
            news_titles.length
        }
    }

}
