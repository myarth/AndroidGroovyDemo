package com.myth.myarth.gradleapplication.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.Button
import android.widget.TextView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.Extra
import com.arasthel.swissknife.annotations.InjectView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.ui.photoview.PhotoViewAdapter
import com.myth.myarth.gradleapplication.ui.swipebacklayout.SwipeBackActivity
import groovy.transform.CompileStatic


@CompileStatic
class ImageGalleryActivity extends SwipeBackActivity {

    @InjectView
    TextView textHeadTitle
    @InjectView
    Button btnBack
    @InjectView
    ViewPager viewer
    @Extra
    int position
    @Extra
    List<String> images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch_gallery)
        SwissKnife.inject(this)
        SwissKnife.loadExtras(this)

        if (images == null) {
            images = []
        }

        initView()
        initGalleryViewPager()
    }

    void initView() {
        textHeadTitle.text = "1/${images.size()}" as String
        btnBack.show()
        btnBack.onClick { finish() }
    }

    void initGalleryViewPager() {
        def photoViewAdapter = new PhotoViewAdapter(this, images)
        photoViewAdapter.onItemChangeListener = new PhotoViewAdapter.OnItemChangeListener() {
            @Override
            void onItemChange(int currentPosition) {
                textHeadTitle.text = "${(currentPosition + 1)}/${images.size()}" as String
            }
        }
        viewer.with {
            offscreenPageLimit = 3
            adapter = photoViewAdapter
            currentItem = position
        }
    }
}
