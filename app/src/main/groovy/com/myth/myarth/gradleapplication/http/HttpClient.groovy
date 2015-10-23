package com.myth.myarth.gradleapplication.http

import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.common.Constants
import com.myth.myarth.gradleapplication.ui.UiUtil
import com.myth.myarth.gradleapplication.utils.Utils
import com.squareup.okhttp.*
import groovy.transform.CompileStatic
import org.apache.http.client.utils.URLEncodedUtilsHC4
import org.apache.http.message.BasicNameValuePair

import java.util.concurrent.TimeUnit

@CompileStatic
public class HttpClient {

    static final int PAGE_SIZE = 30

    static final String TAG = "HttpClient"
    static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse('text/plain;')
    static final OkHttpClient client = new OkHttpClient()
    static {
        client.setConnectTimeout(30, TimeUnit.SECONDS)
    }

    static def get(String url, HttpResponseHandler responseHandler) {
        get(url, null, responseHandler)
    }

    static def get(String url, Map<String, String> params, HttpResponseHandler responseHandler) {
        doRequest(url, convert(params), responseHandler, 'get')
    }

    static def post(String url, Map<String, String> params, HttpResponseHandler responseHandler) {
        doRequest(url, convert(params), responseHandler, 'post')
    }

    private static List<BasicNameValuePair> convert(Map<String, String> params) {
        params?.inject([]) { List pairs, Map.Entry<String, String> entry ->
            pairs << new BasicNameValuePair(entry.key, entry.value)
        } as List
    }

    static
    def doRequest(String url, List<BasicNameValuePair> pairs, HttpResponseHandler responseHandler, String method = 'get') {
        if (!Utils.isNetworkConnected()) {
            UiUtil.toast(R.string.no_network_connection_toast)
            return
        }

        def params = URLEncodedUtilsHC4.format(pairs ?: [], Constants.UTF_8)
        def requestBuilder = new Request.Builder()
        if (params) {
            if (method?.toLowerCase() == 'post') {
                requestBuilder.post(RequestBody.create(MEDIA_TYPE_PLAIN, params))
            } else {
                url += ("?${params}" as String)
            }
        }
        requestBuilder.url(url)

        // TODO 处理网络请求框架的异常（url == ''）
        try {
            client.newCall(requestBuilder.build()).enqueue(new Callback() {
                @Override
                void onFailure(Request request1, IOException e) {
                    responseHandler.sendFailureMessage(request1, e)
                }

                @Override
                void onResponse(Response response) throws IOException {
                    responseHandler.sendSuccessMessage(response)
                }
            })
        } catch (e) {
            e.printStackTrace()
        }

    }

}
