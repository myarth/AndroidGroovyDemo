package com.myth.myarth.gradleapplication.activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.Extra
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnUIThread
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.common.AppContext
import com.myth.myarth.gradleapplication.db.dao.ImUserDao
import com.myth.myarth.gradleapplication.db.entity.ImUser
import com.myth.myarth.gradleapplication.http.HttpClient
import com.myth.myarth.gradleapplication.http.HttpResponseHandler
import com.myth.myarth.gradleapplication.ui.UiUtil
import com.myth.myarth.gradleapplication.utils.SharedPreferencesUtil
import com.squareup.okhttp.Request
import groovy.transform.CompileStatic

@CompileStatic
class SimpleActivity extends BaseFragmentActivity {

    @InjectView
    EditText editText
    @InjectView
    Button button
    @InjectView
    Button button2
    @Extra
    String extraHi

    ImUserDao imUserDao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        SwissKnife.inject(this)
        SwissKnife.loadExtras(this)

        button.onClick {
//            def text = editText.text.toString()
//            imUserDao.createOrUpdate(new ImUser(
//                    username: 'gradle',
//                    nick: text
//            ))
//            showNick(true)
            UiUtil.toast(AppContext.instance.packageName)

        }

        button2.onClick {
//            def isFirstTimeUse = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.FIRST_TIME_USE, true)
//            UiUtil.toast(isFirstTimeUse as String, false)

            doOnUIThread()

//            HttpClient.get('http://www.ikaili.com/', [:], new HttpResponseHandler() {
//                @Override
//                public void onSuccess(String body) {
//                    println body
//                }
//
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    println e.message
//                }
//            })
        }

        imUserDao = new ImUserDao(this)
        showNick()
    }


    void showNick(boolean isUpdate = false) {
        def imUsers = imUserDao.queryForAll()
        if (imUsers) {
            def nick = imUsers[0].nick + ', extra=' + extraHi
            isUpdate ? UiUtil.toast(nick, false) : editText.setText(nick)
        }
    }

    @OnUIThread
    public void doOnUIThread() {
//        UiUtil.toast('uiHandler invoke.')
        UiUtil.toast(AppContext.instance.filesDir.path)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId()

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
