package com.lesson.myahut.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.lesson.myahut.R;
import com.lesson.myahut.entity.PreScoreSerializable;
import com.lesson.myahut.entity.ViewScoreInfo;
import com.lesson.myahut.handler.ListView2ScoreAdapter;
import com.lesson.myahut.handler.ViewScoreHandler;
import com.lesson.myahut.util.GlobalContext;
import com.lesson.myahut.util.Util;

public class ViewScoreActivity extends AppCompatActivity {
    private ViewScoreInfo[] viewScoreInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_score);

        /* 添加新版本的 actionbar 功能，并显示返回按钮 */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Util.isOnline(GlobalContext.mainActivity)) {
            new ScoreAsyncTask().execute();
        } else {
            Util.makeToast(ViewScoreActivity.this, "请检查网络链接");
        }


    }


    android.app.ProgressDialog progressDialog;

    private class ScoreAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ViewScoreActivity.this, null, "正在获取信息...", true);
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
                PreScoreSerializable preScoreSerializable = (PreScoreSerializable) bundle.getSerializable("preScoreSerializable");
                viewScoreInfos = null;
                viewScoreInfos = ViewScoreHandler.getInstance(ViewScoreActivity.this).getScoreInfos(preScoreSerializable);
                Log.e("viewScoreInfos", viewScoreInfos.length + "");

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

                if (viewScoreInfos.length == 0 || viewScoreInfos == null) {
                    Util.makeToast(ViewScoreActivity.this, "未获取到成绩，请检查账号信息");
                } else {
                    Log.e("课程名", viewScoreInfos[6].kcm);
                    ListView listView = (ListView) findViewById(R.id.scoreListView);
                    listView.setAdapter(new ListView2ScoreAdapter(viewScoreInfos));
                }

            } else {
                /* 登陆失败，用户名或密码错误 */
                if (GlobalContext.msg != null) {
                    Util.makeToast(ViewScoreActivity.this, GlobalContext.msg);
                    GlobalContext.msg = null;
                } else {
                    Util.makeToast(ViewScoreActivity.this, "网络连接超时");
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
