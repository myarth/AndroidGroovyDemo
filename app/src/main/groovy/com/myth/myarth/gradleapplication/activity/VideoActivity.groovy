package com.myth.myarth.gradleapplication.activity

import android.app.Activity
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.Extra
import com.arasthel.swissknife.annotations.InjectView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.ui.UiUtil
import groovy.transform.CompileStatic

@CompileStatic
class VideoActivity extends Activity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, View.OnTouchListener {

    static final String TAG = 'VideoActivity'

    @InjectView
    VideoView videoView
    @InjectView
    ProgressBar progressBar
    @InjectView
    Button btnBack
    @Extra
    String source_url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        SwissKnife.inject(this)

        if (!source_url) {
            UiUtil.toast('视频链接为空', this)
            finish()
        }

        btnBack.onClick { finish() }
        videoView.with {
            // 设置VideoView的控制条
            mediaController = new MediaController(this)
            // 设置播放完成以后监听
            setOnCompletionListener(this)
            // 设置发生错误监听，如果不设置VideoView会向用户提示发生错误
            setOnErrorListener(this)
            // 设置在视频文件在加载完毕以后的回调函数
            setOnPreparedListener(this)
            // 设置VideoView的点击监听
            setOnTouchListener(this)

            videoURI = Uri.parse(source_url)

            // 设置为全屏模式播放
            def layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT)
            [
                    RelativeLayout.ALIGN_PARENT_BOTTOM,
                    RelativeLayout.ALIGN_PARENT_TOP,
                    RelativeLayout.ALIGN_PARENT_LEFT,
                    RelativeLayout.ALIGN_PARENT_LEFT
            ].each {
                layoutParams.addRule(it as int)
            }
            setLayoutParams(layoutParams)

            if (savedInstanceState && savedInstanceState.getInt('pos') != 0) {
                videoView.seekTo(savedInstanceState.getInt('pos'))
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState)
        if (videoView.playing) {
            outState.putInt('pos', videoView.currentPosition)
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState && savedInstanceState.getInt('pos') != 0) {
            videoView.seekTo(savedInstanceState.getInt('pos'))
        }
    }

    @Override
    protected void onStart() {
        super.onStart()
        videoView.with {
            start() //启动视频播放
            focusable = true //设置获取焦点
        }
    }

    @Override
    void onCompletion(MediaPlayer mp) {
        finish()
    }

    @Override
    boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.e(TAG, '发生未知错误')
                break
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.e(TAG, '媒体服务器死机')
                break
            default:
                Log.e(TAG, 'onError+' + what)
                break
        }
        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                //io读写错误
                Log.e(TAG, '文件或网络相关的IO操作错误')
                break
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                //文件格式不支持
                Log.e(TAG, '比特流编码标准或文件不符合相关规范')
                break
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                //一些操作需要太长时间来完成,通常超过3 - 5秒。
                Log.e(TAG, '操作超时')
                break
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                //比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
                Log.e(TAG, '比特流编码标准或文件符合相关规范,但媒体框架不支持该功能')
                break
            default:
                Log.e(TAG, 'onError+' + extra)
                break
        }
        false
    }

    @Override
    void onPrepared(MediaPlayer mp) {
        progressBar.hide()
    }

    @Override
    boolean onTouch(View v, MotionEvent event) {
        return false
    }
}
