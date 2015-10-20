package com.myth.myarth.gradleapplication.db.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import groovy.transform.CompileStatic

@CompileStatic
@DatabaseTable(tableName = "im_user")
class ImUser implements Serializable {

    @DatabaseField(id = true)
    String username // 用户名
    @DatabaseField
    String nick // 昵称
    @DatabaseField
    String avatar // 头像

}
