package com.myth.myarth.gradleapplication.utils

import groovy.transform.CompileStatic

import java.security.MessageDigest


@CompileStatic
class MiscUtil {

    static String getMD5(byte[] bytes) {
        def md5 = ''
        try {
            def algorithm = MessageDigest.getInstance('MD5')
            algorithm.reset()
            algorithm.update(bytes)
            md5 = toHexString(algorithm.digest())
        } catch (e) {
        }
        md5
    }

    static String toHexString(byte[] bytes) {
        bytes.inject(new StringBuilder()) { StringBuilder s, byte b ->
            def hex = Integer.toHexString(0xFF & b)
            if (hex.length() == 1) {
                s.append('0')
            }
            s.append(hex)
        }.toString()
    }

}
