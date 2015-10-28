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
    /*
       com.arasthel.swissknife.utils.AnnotationUtils.processArray
       对数组类型的字段进行了赋值
       com.arasthel.swissknife.utils.AnnotationUtils.processCommonVariable
       对基本数据类型、Parcelable、Serializable、ArrayList 进行了赋值
       notice：此处不能使用 List 来注入参数值，因为该方法检查的是 ArrayList 类型
     */
    @Extra
    ArrayList<String> images

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
