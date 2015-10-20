package com.myth.myarth.gradleapplication.utils

import android.graphics.Bitmap
import com.myth.myarth.gradleapplication.common.AppContext
import groovy.transform.CompileStatic


@CompileStatic
class ImageUtil {

    private static final String PICASSO_CACHE = "picasso-cache"

    static File saveBitmapToFile(Bitmap bitmap, String filename) {
        if (!bitmap) {
            return null
        }
        def file = new File(filename)
        if (!file.exists() && !file.isFile() && !file.canWrite()) {
            return null
        }
        if (!file.parentFile.exists() && !file.parentFile.mkdirs()) {
            return null
        }
        file.withOutputStream { OutputStream out ->
            out.write(bitmap2byte(bitmap))
        }
        file
    }

    static byte[] bitmap2byte(Bitmap bitmap) {
        def arrayOutputStream = new ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, arrayOutputStream)
        arrayOutputStream.toByteArray()
    }

    static String getImageCacheDir() {
        def cache = new File(AppContext.instance.cacheDir, PICASSO_CACHE)
        if (!cache.exists()) {
            cache.mkdirs()
        }
        cache.absolutePath
    }

}
