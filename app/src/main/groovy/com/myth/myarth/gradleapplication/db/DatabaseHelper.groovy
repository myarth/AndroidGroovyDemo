package com.myth.myarth.gradleapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.myth.myarth.gradleapplication.utils.ScannerUtil
import groovy.transform.CompileStatic

import java.sql.SQLException

@CompileStatic
class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "gradle2.db"
    private static final int DATABASE_VERSION = 2

    private static DatabaseHelper instance
    private Map<String, Dao> daos = [:]
    private boolean isFetched = false
    private List<Class> entityClassList = []

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION)
    }

    static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper) {
                if (instance == null) {
                    instance = new DatabaseHelper(context.applicationContext)
                }
            }
        }
        instance
    }

    synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null

        def name = clazz.simpleName
        if (daos.containsKey(name)) {
            dao = daos[name]
        }
        if (!dao) {
            dao = super.getDao(clazz)
            daos << [(name): dao]
        }

        dao
    }

    @Override
    void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            getEntityClassList()?.each { Class clazz ->
                TableUtils.createTable(connectionSource, clazz)
            }
        } catch (SQLException e) {
            e.printStackTrace()
        }
    }

    @Override
    void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            getEntityClassList()?.each { Class clazz ->
                TableUtils.dropTable(connectionSource, clazz, true)
            }
            onCreate(database, connectionSource)
        } catch (SQLException e) {
            e.printStackTrace()
        }
    }

    @Override
    void close() {
        super.close()
        daos?.each { k, v ->
            v = null
        }?.clear()
    }

    // groovy 默认先调用 property 的 getter 方法，因此在该 getter 方法内部对字段赋值时，使用 this.@filedName.
    synchronized List<Class> getEntityClassList() {
        if (!isFetched && !this.@entityClassList) {
            def entityClassNames = ScannerUtil.scan("${DatabaseHelper.package.name}.entity" as String)
            entityClassNames?.each { String className ->
                this.@entityClassList << DatabaseHelper.classLoader.loadClass(className)
            }
            isFetched = true
        }
        this.@entityClassList
    }

}
