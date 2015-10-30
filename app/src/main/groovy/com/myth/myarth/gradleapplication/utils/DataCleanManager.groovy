package com.myth.myarth.gradleapplication.utils

import android.content.Context
import groovy.io.FileType
import groovy.transform.CompileStatic

@CompileStatic
class DataCleanManager {

    static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.cacheDir)
    }

    static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.filesDir)
    }

    static void cleanSharedPreferences(Context context) {
        deleteFilesByDirectory("/data/data/${context.packageName}/shared_prefs" as String)
    }

    static void cleanDatabases(Context context) {
        deleteFilesByDirectory("/data/data/${context.packageName}/databases" as String)
    }

    static void cleanDatabase(Context context, String name) {
        context.deleteDatabase(name)
    }

    static void cleanExternalCache(Context context) {
        if (Utils.exitsSdcard) {
            deleteFilesByDirectory(context.externalCacheDir)
        }
    }

    static void cleanApplicationData(Context context) {
        cleanInternalCache(context)
        cleanSharedPreferences(context)
        cleanDatabases(context)
        cleanFiles(context)
        cleanExternalCache(context)
    }

    private static void deleteFilesByDirectory(String dirPath, boolean delSelf = false) {
        deleteFilesByDirectory(new File(dirPath), delSelf)
    }

    private static void deleteFilesByDirectory(File dir, boolean delSelf = false) {
        if (dir?.exists()) {
            if (dir.isDirectory()) {
                def descendantDirs = []
                dir.eachFileRecurse {
                    if (it.isDirectory()) {
                        descendantDirs << it
                    } else {
                        it.delete()
                    }
                }
                descendantDirs.each { (it as File).delete() }
            }
            if (delSelf) {
                dir.delete()
            }
        }
    }

    static String getCacheSize(String filepath) {
        getCacheSize(new File(filepath))
    }

    static String getCacheSize(File file) {
        getFormatSize(getFileSize(file))
    }

    static long getFileSize(String filepath) {
        getFileSize(new File(filepath))
    }

    static long getFileSize(File file) {
        long size = 0
        if (file?.exists()) {
            if (file.isDirectory()) {
                file.eachFileRecurse(FileType.FILES) {
                    size += it.length()
                }
            } else {
                size = file.length()
            }
        }
        size
    }

    static String getFormatSize(double size) {
        double kilobyte = size / 1024
        double megabyte = kilobyte / 1024
        double gigabyte = megabyte / 1024

        if (gigabyte < 1) {
            return formatSize(megabyte, 'MB')
        }

        double terabyte = gigabyte / 1024
        if (terabyte < 1) {
            return formatSize(gigabyte, 'GB')
        }

        formatSize(terabyte, 'TB')
    }

    private static String formatSize(double bytes, String desc) {
        def decimal = new BigDecimal(Double.toString(bytes))
        "${decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()}${desc}" as String
    }

}
