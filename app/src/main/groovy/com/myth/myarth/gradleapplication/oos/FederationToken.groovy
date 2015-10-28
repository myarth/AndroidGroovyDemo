package com.myth.myarth.gradleapplication.oos

import groovy.transform.CompileStatic


@CompileStatic
class FederationToken {

    String accessKeyId
    String accessKeySecret
    String securityToken
    long expiration

    FederationToken(String akId, String aks, String st, long et) {
        accessKeyId = akId
        accessKeySecret = aks
        securityToken = st
        expiration = et
    }

}
