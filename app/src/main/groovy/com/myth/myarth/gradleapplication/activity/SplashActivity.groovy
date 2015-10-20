package com.myth.myarth.gradleapplication.activity

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnClick
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.ui.viewpagerindicator.CirclePageIndicator
import com.myth.myarth.gradleapplication.utils.SharedPreferencesUtil
import com.myth.myarth.gradleapplication.utils.UiUtil
import groovy.transform.CompileStatic

@CompileStatic
class SplashActivity extends FragmentActivity {

    @InjectView
    ImageView guideImage
    @InjectView
    ViewPager pager
    @InjectView
    Button btnHome
    @InjectView
    CirclePageIndicator indicator

    Animation fadeIn
    List images = [R.drawable.newer01, R.drawable.newer02, R.drawable.newer03, R.drawable.newer04]

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        SwissKnife.inject(this)

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)
        def isFirstTimeUse = SharedPreferencesUtil.instance.getBoolean(SharedPreferencesUtil.FIRST_TIME_USE, true)
        if (isFirstTimeUse) {
            initGuideGallery()
        } else {
            initLaunchLogo()
        }
    }

    private void initLaunchLogo() {
        guideImage.show()
        new Handler().postDelayed(new Runnable() {
            @Override
            void run() {
                btnHome.show()
                btnHome.startAnimation(fadeIn)
                UiUtil.showHome(SplashActivity.this)
            }
        }, 500)
    }

    @OnClick(R.id.btnHome)
    public void onClick() {
        SharedPreferencesUtil.instance.putBoolean(SharedPreferencesUtil.FIRST_TIME_USE, false)
        this.startActivity(MainActivity) {
            putExtra('extraHi', 'I got it.')
        }
    }

    private void initGuideGallery() {
        pager.show()
        pager.adapter = new GalleryPagerAdapter()

        indicator.show()
        indicator.viewPager = pager

        // indicator 不是继承自 ViewPager, 因此不能使用注解的方式
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            void onPageSelected(int position) {
                if (position == images.size() - 1) {
                    btnHome.show()
                    btnHome.startAnimation(fadeIn)
                } else {
                    btnHome.hide()
                }
            }

            @Override
            void onPageScrollStateChanged(int state) {

            }
        })

        // SwissKnife Event Methods
//        btnHome.onClick {
//            SharedPreferencesUtil.instance.putBoolean(SharedPreferencesUtil.FIRST_TIME_USE, false)
//            UiUtil.showHome(this)
//        }
    }

    // 该方法在使用了 indicator 后无效
//    @OnPageChanged(value = R.id.pager, method = OnPageChanged.Method.PAGE_SELECTED)
//    public void onPageSelected(int position) {
//        if (position == images.size() - 1) {
//            btnHome.show()
//            btnHome.startAnimation(fadeIn)
//        } else {
//            btnHome.hide()
//        }
//    }

    class GalleryPagerAdapter extends PagerAdapter {

        @Override
        int getCount() {
            return images.size()
        }

        @Override
        boolean isViewFromObject(View view, Object object) {
            return view == object
        }

        Object instantiateItem(ViewGroup container, int position) {
            def imageView = new ImageView(SplashActivity.this)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.imageResource = images[position] as int
            container.addView(imageView)
            imageView
        }

        void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(object as View)
        }

    }

}