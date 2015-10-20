/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License")
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.myth.myarth.gradleapplication.http

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.squareup.okhttp.Headers
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import groovy.transform.CompileStatic

/**
 * AsyncHttpClient client = new AsyncHttpClient()
 * client.get("http://www.google.com", new AsyncHttpResponseHandler() {*     &#064Override
 *     void onStart() {*         // Initiated the request
 *}*
 *     &#064Override
 *     void onSuccess(String response) {*         // Successfully got a response
 *}*
 *     &#064Override
 *     void onFailure(Throwable e, String response) {*         // Response failed :(
 *}*
 *     &#064Override
 *     void onFinish() {*         // Completed the request (either success or failure)
 *}*})
 * </pre>
 */
@CompileStatic
class HttpResponseHandler {
    protected static final int SUCCESS_MESSAGE = 0
    protected static final int FAILURE_MESSAGE = 1

    private Handler handler

    /**
     * Creates a new AsyncHttpResponseHandler
     */
    HttpResponseHandler() {
        // Set up a handler to post events back to the correct thread if possible
        if (Looper.myLooper() != null) {
            handler = new Handler() {
                @Override
                void handleMessage(Message msg) {
                    HttpResponseHandler.this.handleMessage(msg)
                }
            }
        }
    }

    //
    // Callbacks to be overridden, typically anonymously
    //
    /**
     * Fired when a request returns successfully, override to handle in your own code
     *
     * @param content the body of the HTTP response from the server
     */
    void onSuccess(String content) {
        // JsonResult result = JSON.parseObject(content, JsonResult.class)
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code
     *
     * @param statusCode the status code of the response
     * @param headers the headers of the HTTP response
     * @param content the body of the HTTP response from the server
     */
    void onSuccess(int statusCode, Headers headers, String content) {
        onSuccess(statusCode, content)
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code
     *
     * @param statusCode the status code of the response
     * @param content the body of the HTTP response from the server
     */
    void onSuccess(int statusCode, String content) {
        onSuccess(content)
    }

    /**
     * Called when the request could not be executed due to cancellation, a
     * connectivity problem or timeout. Because networks can fail during an
     * exchange, it is possible that the remote server accepted the request
     * before the failure.
     */
    void onFailure(Request request, IOException e) {
        e.printStackTrace()
    }

    //
    // 后台线程调用方法，通过Handler sendMessage把结果转到UI主线程
    //
    protected void sendSuccessMessage(Response response) {
        try {
            sendMessage(obtainMessage(SUCCESS_MESSAGE, [response.code(), response.headers(), response.body().string()]))
        } catch (IOException e) {
            e.printStackTrace()
        }
    }

    protected void sendFailureMessage(Request request, IOException e) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, [request, e]))
    }

    //
    // Pre-processing of messages (in original calling thread, typically the UI thread)
    //

    protected void handleSuccessMessage(int statusCode, Headers headers, String responseBody) {
        onSuccess(statusCode, headers, responseBody)
    }

    protected void handleFailureMessage(Request request, IOException e) {
        onFailure(request, e)
    }

    // Methods which emulate android's Handler and Message methods
    protected void handleMessage(Message msg) {
        def response

        switch (msg.what) {
            case SUCCESS_MESSAGE:
                response = msg.obj as List
                handleSuccessMessage(((Integer) response[0]).intValue(), (Headers) response[1], (String) response[2])
                break
            case FAILURE_MESSAGE:
                response = msg.obj as List
                handleFailureMessage((Request) response[0], (IOException) response[1])
                break
        }
    }

    protected void sendMessage(Message msg) {
        if (handler != null) {
            handler.sendMessage(msg)
        } else {
            handleMessage(msg)
        }
    }

    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg
        if (handler != null) {
            msg = this.handler.obtainMessage(responseMessage, response)
        } else {
            msg = Message.obtain()
            msg.what = responseMessage
            msg.obj = response
        }
        return msg
    }

}
