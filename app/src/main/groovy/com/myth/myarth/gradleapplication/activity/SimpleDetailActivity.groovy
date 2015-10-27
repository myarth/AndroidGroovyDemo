package com.myth.myarth.gradleapplication.activity

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.ui.loopviewpager.AutoLoopViewPager
import com.myth.myarth.gradleapplication.ui.swipebacklayout.SwipeBackActivity
import com.myth.myarth.gradleapplication.ui.viewpagerindicator.CirclePageIndicator
import groovy.transform.CompileStatic


@CompileStatic
class SimpleDetailActivity extends SwipeBackActivity {

    @InjectView
    Button btnBack
    @InjectView
    Button btnShare
    @InjectView
    TextView textHeadTitle
    @InjectView
    AutoLoopViewPager pager
    @InjectView
    CirclePageIndicator indicator

    List<Integer> imageViewIds
    List<String> imageList = [
            'http://pic.nipic.com/2008-07-11/20087119630716_2.jpg',
            'http://pic.nipic.com/2008-07-11/20087119630716_2.jpg',
            'http://pic.nipic.com/2008-07-11/20087119630716_2.jpg',
    ]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_detail)
        SwissKnife.inject(this)
        initView()
    }

    void initView() {
        textHeadTitle.text = '世纪嘉园'
        btnBack.show()
        btnBack.onClick { finish() }
        btnShare.onClick {}

        imageViewIds = [R.drawable.house_background, R.drawable.house_background_1, R.drawable.house_background_2]
        pager.adapter = new GalleryPagerAdapter()
        indicator.viewPager = pager
        indicator.setPadding(5, 5, 10, 5)
    }

    @Override
    protected void onResume() {
        super.onResume()
        pager.startAutoScroll()
    }

    @Override
    protected void onPause() {
        super.onPause()
        pager.stopAutoScroll()
    }

    class GalleryPagerAdapter extends PagerAdapter {

        @Override
        int getCount() {
            return imageViewIds.size()
        }

        @Override
        boolean isViewFromObject(View view, Object object) {
            return view == object
        }

        @Override
        Object instantiateItem(ViewGroup container, int position) {
            def imageView = new ImageView(SimpleDetailActivity.this)
            imageView.with {
                imageResource = imageViewIds[position]
                layoutParams = new ViewGroup.LayoutParams(-1, -1)
                scaleType = ImageView.ScaleType.FIT_XY
                imageView.onClick {
                    SimpleDetailActivity.this.startActivity(ImageGalleryActivity) {
                        putStringArrayListExtra('images', imageList as ArrayList)
                        putExtra('position', position)
                    }
                }
            }
            container.addView(imageView)
            imageView
        }

        @Override
        void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(object as View)
        }
    }
}
