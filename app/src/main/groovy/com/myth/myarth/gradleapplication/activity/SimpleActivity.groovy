package com.myth.myarth.gradleapplication.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.Extra
import com.arasthel.swissknife.annotations.InjectView
import com.myth.myarth.gradleapplication.R
import com.myth.myarth.gradleapplication.db.dao.ImUserDao
import com.myth.myarth.gradleapplication.db.entity.ImUser
import com.myth.myarth.gradleapplication.http.HttpResponseHandler
import com.myth.myarth.gradleapplication.http.HttpClient
import com.myth.myarth.gradleapplication.utils.SharedPreferencesUtil
import com.myth.myarth.gradleapplication.ui.UiUtil
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
            def text = editText.text.toString()
            imUserDao.createOrUpdate(new ImUser(
                    username: 'gradle',
                    nick: text
            ))
            showNick(true)
        }

        button2.onClick {
            def isFirstTimeUse = SharedPreferencesUtil.instance.getBoolean(SharedPreferencesUtil.FIRST_TIME_USE, true)
            UiUtil.longToast(isFirstTimeUse as String)

            HttpClient.get('http://www.ikaili.com/', [:], new HttpResponseHandler() {
                @Override
                public void onSuccess(String body) {
                    println body
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    println e.message
                }
            })
        }

        imUserDao = new ImUserDao(this)
        showNick()
    }


    void showNick(boolean isUpdate = false) {
        def imUsers = imUserDao.queryForAll()
        if (imUsers) {
            def nick = imUsers[0].nick + ', extra=' + extraHi
            isUpdate ? UiUtil.longToast(nick) : editText.setText(nick)
        }
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
