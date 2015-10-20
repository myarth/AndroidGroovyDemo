package com.myth.myarth.gradleapplication.utils

import com.myth.myarth.gradleapplication.common.AppContext
import dalvik.system.DexFile
import groovy.transform.CompileStatic

@CompileStatic
class ScannerUtil {

    static def scan(String packageNamePrefix) {
        def classNames = []
        try {
            def dexFile = new DexFile(AppContext.instance.packageResourcePath)
            dexFile.entries().each { String entry ->
                if (entry.startsWith(packageNamePrefix) && !entry.contains('&')) {
                    classNames << entry
                }
            }
        } catch (e) {
            e.printStackTrace()
        }
        classNames
    }

}
