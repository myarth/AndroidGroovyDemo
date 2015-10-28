package com.myth.myarth.gradleapplication.oos

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.alibaba.sdk.android.oss.model.OSSException
import groovy.transform.CompileStatic


@CompileStatic
class MessageHandler {

    static final int SUCCESS_MESSAGE = 0
    static final int PROGRESS_MESSAGE = 1
    static final int FAILURE_MESSAGE = 2

    Handler handler

    MessageHandler() {
        if (Looper.myLooper()) {
            handler = new Handler() {
                @Override
                void handleMessage(Message msg) {
                    MessageHandler.this.handleMessage(msg)
                }
            }
        }
    }

    void handleMessage(Message msg) {
        switch (msg.what) {
            case SUCCESS_MESSAGE:
                def response = msg.obj as List
                handleSuccessMessage(response[0] as String)
                break
            case PROGRESS_MESSAGE:
                def response = msg.obj as List
                handleProgressMessage(response[0] as String, response[1] as int, response[2] as int)
                break
            case FAILURE_MESSAGE:
                def response = msg.obj as List
                handleFailureMessage(response[0] as String, response[1] as OSSException)
                break
        }
    }

    void handleSuccessMessage(String responseBody) {
        onSuccess(responseBody)
    }

    void handleProgressMessage(String objectKey, int byteCount, int totalSize) {
        onProgress(objectKey, byteCount, totalSize)
    }

    void handleFailureMessage(String objectKey, OSSException e) {
        onFailure(objectKey, e)
    }

    void onSuccess(String objectKey) {

    }

    void onProgress(String objectKey, int byteCount, int totalSize) {

    }

    void onFailure(String objectKey, OSSException e) {

    }

    void sendSuccessMessage(String objectKey) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, [objectKey]))
    }

    void sendProgressMessage(String objectKey, int byteCount, int totalSize) {
        sendMessage(obtainMessage(PROGRESS_MESSAGE, [objectKey, byteCount, totalSize]))
    }

    void sendFailureMessage(String objectKey, OSSException e) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, [objectKey, e]))
    }

    void sendMessage(Message msg) {
        handler ? handler.sendMessage(msg) : handleMessage(msg)
    }

    Message obtainMessage(int responseMessage, Object response) {
        handler ?
                handler.obtainMessage(responseMessage, response) :
                Message.obtain().with {
                    what = responseMessage
                    obj = response
                    it
                }
    }

}
