package com.myth.myarth.gradleapplication.db.dao

import android.content.Context
import com.j256.ormlite.dao.Dao
import com.myth.myarth.gradleapplication.db.DatabaseHelper
import com.myth.myarth.gradleapplication.db.entity.ImUser
import groovy.transform.CompileStatic

@CompileStatic
class ImUserDao {

    private Dao<ImUser, String> imUserDao

    ImUserDao(Context context) {
        try {
            imUserDao = DatabaseHelper.getHelper(context).getDao(ImUser)
        } catch (e) {
            e.printStackTrace()
        }
    }

    List<ImUser> queryForAll() {
        imUserDao?.queryForAll()
    }

    def create(ImUser imUser) {
        imUserDao?.create(imUser)
    }

    def createIfNotExists(ImUser imUser) {
        imUserDao?.createIfNotExists(imUser)
    }

    def createOrUpdate(ImUser imUser) {
        imUserDao?.createOrUpdate(imUser)
    }

    def update(ImUser imUser) {
        imUserDao?.update(imUser)
    }

    def deleteById(String id) {
        imUserDao?.deleteById(id)
    }

}
