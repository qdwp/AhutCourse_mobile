package com.lesson.myahut.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.lesson.myahut.R;
import com.lesson.myahut.entity.PreMakeUpSerializable;
import com.lesson.myahut.entity.ViewMakeUpInfo;
import com.lesson.myahut.handler.ListView2MakeUpAdapter;
import com.lesson.myahut.handler.ViewMakeUpHandler;
import com.lesson.myahut.util.GlobalContext;
import com.lesson.myahut.util.Util;

public class ViewMakeUpActivity extends AppCompatActivity {
    private ViewMakeUpInfo[] viewMakeUpInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_makeup);
        Log.e("tag", "test");
        /* 添加新版本的 actionbar 功能，并显示返回按钮 */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Util.isOnline(GlobalContext.mainActivity)) {
            new MakeUpAsyncTask().execute();
        } else {
            Util.makeToast(ViewMakeUpActivity.this, "请检查网络链接");
        }
    }

    ProgressDialog progressDialog;

    private class MakeUpAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ViewMakeUpActivity.this, null, "正在获取信息...", true);
        }

        /**
         * 返回的结果作为 onPostExcute() 的参数
         *
         * @param params
         * @return
         */
        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                // 获取启动该ResultActivity的Intent
                Intent intent = getIntent();
                // 获取该Intent所携带的数据
                Bundle bundle = intent.getExtras();
                // 从bundle数据包中取出数据
                PreMakeUpSerializable preMakeUpSerializable = (PreMakeUpSerializable) bundle.getSerializable("preMakeUpSerializable");

                Log.e("preMakeUpSerializable", preMakeUpSerializable.getUid());

                viewMakeUpInfos = null;
                viewMakeUpInfos = ViewMakeUpHandler.getInstance(ViewMakeUpActivity.this).getMakeUpInfos(preMakeUpSerializable);
                Log.e("viewMakeUpInfos", viewMakeUpInfos.length + "");

                return true;
            } catch (Exception e) {
                return false;
            }
        }

        /**
         * 根据获得的参数判断执行是否成功
         *
         * @param res
         */
        @Override
        protected void onPostExecute(Boolean res) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (Exception e) {
            }
            if (res) {
                if (viewMakeUpInfos.length == 0 || viewMakeUpInfos == null) {
                    Util.makeToast(ViewMakeUpActivity.this, "未获取到结果，请检查账号信息");
                } else {
                    ListView listView = (ListView) findViewById(R.id.makeupListView);
                    listView.setAdapter(new ListView2MakeUpAdapter(viewMakeUpInfos));
                }

            } else {
                /* 登陆失败，用户名或密码错误 */
                if (GlobalContext.msg != null) {
                    Util.makeToast(ViewMakeUpActivity.this, GlobalContext.msg);
                    GlobalContext.msg = null;
                } else {
                    Util.makeToast(ViewMakeUpActivity.this, "网络连接超时");
                }
            }
        }
    }

    @Override
    protected void onPause() {
        try {
            progressDialog.dismiss();
            progressDialog = null;
        } catch (Exception e) {
        }
//        uid = null;
//        info = null;
        super.onPause();
    }

    /**
     * 捕获 actionbar 返回按钮点击事件，并实现回退功能
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
