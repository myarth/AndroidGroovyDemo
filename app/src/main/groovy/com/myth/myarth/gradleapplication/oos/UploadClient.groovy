package com.myth.myarth.gradleapplication.oos

import com.alibaba.sdk.android.oss.OSSService
import com.alibaba.sdk.android.oss.OSSServiceProvider
import com.alibaba.sdk.android.oss.callback.SaveCallback
import com.alibaba.sdk.android.oss.model.*
import com.alibaba.sdk.android.oss.storage.OSSBucket
import com.alibaba.sdk.android.oss.util.OSSLog
import com.alibaba.sdk.android.oss.util.OSSToolKit
import groovy.transform.CompileStatic

import java.util.concurrent.TimeUnit

@CompileStatic
class UploadClient {

    static final String ACCESS_KEY = ''
    static final String ACCESS_KEY_SECRET = ''
    static final String ENDPOINT = 'oss-cn-beijing.aliyuncs.com'
    static final String BUCKET_NAME = 'bucketName'
    static final String OSS_PATH = 'http://bucketName.oss-cn-beijing.aliyuncs.com'
    static final String DEFAULT_CONTENT_TYPE = 'image/jpeg'

    static OSSService ossService
    static OSSBucket bucket
    static {
        initOSS()
    }

    static void initOSS() {
        OSSLog.enableLog()
        ossService = OSSServiceProvider.service
        ossService.with {
            globalDefaultHostId = ENDPOINT
            globalDefaultACL = AccessControlList.PRIVATE
            authenticationType = AuthenticationType.ORIGIN_AKSK
            customStandardTimeWithEpochSec = System.currentTimeMillis() / 1000
            clientConfiguration = new ClientConfiguration(
                    connectTimeout: TimeUnit.SECONDS.toMillis(15) as int, // 默认30s
                    socketTimeout: TimeUnit.SECONDS.toMillis(15) as int, // 默认30s
                    maxConnections: 50, // 默认50
            )
            setGlobalDefaultTokenGenerator(new TokenGenerator() {
                @Override
                String generateToken(String httpMethod, String md5, String type,
                                     String date, String ossHeaders, String resource) {
                    def content = "${[httpMethod, md5, type, date, ossHeaders].join('\n')}${resource}" as String
                    OSSToolKit.generateToken(ACCESS_KEY, ACCESS_KEY_SECRET, content as String)
                }
            })
        }
        bucket = ossService.getOssBucket(BUCKET_NAME)
    }

    static void asyncUpload(File file, MessageHandler handler, String contentType = DEFAULT_CONTENT_TYPE) throws FileNotFoundException {
        def inputStream = new FileInputStream(file)
        asyncUpload(file.name, inputStream, file.length() as int, handler, contentType)
    }

    static void asyncUpload(String filename, InputStream inputStream, int inputLength,
                            MessageHandler handler, String contentType = DEFAULT_CONTENT_TYPE) {
        def data = ossService.getOssData(bucket, filename)
        data.setInputstream(inputStream, inputLength, contentType)
        data.uploadInBackground(new SaveCallback() {
            @Override
            void onSuccess(String objectKey) {
                handler.sendSuccessMessage(objectKey)
            }

            @Override
            void onProgress(String objectKey, int byteCount, int totalSize) {
                handler.sendProgressMessage(objectKey, byteCount, totalSize)
            }

            @Override
            void onFailure(String objectKey, OSSException e) {
                handler.sendFailureMessage(objectKey, e)
            }
        })
    }

}
