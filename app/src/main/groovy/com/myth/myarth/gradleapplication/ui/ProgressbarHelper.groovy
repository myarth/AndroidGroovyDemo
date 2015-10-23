package com.myth.myarth.gradleapplication.ui

import android.app.Activity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.utils.Utils
import groovy.transform.CompileStatic

@CompileStatic
class ProgressBarHelper {

    boolean isLoading = false
    Activity context
    ProgressBar progressBar
    ProgressBarClickListener progressBarClickListener
    View loading
    TextView text

    ProgressBarHelper(Activity context, View inView) {
        this.context = context
        loading = inView ?: context?.view(R.id.ll_data_loading)
        text = loading?.findViewById(R.id.loading_tip_txt) as TextView
        progressBar = loading?.findViewById(R.id.loading_progress_bar) as ProgressBar
    }

    void showNetError() {
        context.runOnUiThread(refresh(R.string.progress_load_error))
    }

    void showNoData() {
        context.runOnUiThread(refresh(R.string.progress_load_no_data))
    }

    Runnable refresh(int textResId) {
        new Runnable() {
            @Override
            void run() {
                isLoading = false
                text?.setText(textResId)
                progressBar?.hide()
                loading?.show()
                loading?.onClick {
                    if (progressBarClickListener
                            && !progressBar?.shown
                            && progressBar?.visibility == GONE
                            && Utils.isNetworkAvailable()) {
                        progressBar.show()
                        progressBarClickListener.clickRefresh()
                    }
                }
            }
        }
    }

    void showLoading() {
        context.runOnUiThread(new Runnable() {
            @Override
            void run() {
                isLoading = true
                text?.setText(R.string.progress_loading)
                loading?.show()
                if (progressBar && !progressBar.shown) {
                    progressBar.show()
                }
            }
        })
    }

    void goneLoading() {
        context.runOnUiThread(new Runnable() {
            @Override
            void run() {
                isLoading = false
                loading?.hide()
            }
        })
    }

    boolean isAllGone() {
        loading?.visibility == View.GONE
    }

    interface ProgressBarClickListener {
        void clickRefresh()
    }

    public void setProgressBarClickListener(ProgressBarClickListener progressBarClickListener) {
        this.progressBarClickListener = progressBarClickListener;
    }

}
