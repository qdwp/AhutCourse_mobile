package com.lesson.myahut.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lesson.myahut.R;
import com.lesson.myahut.handler.ViewAvgPointHandler;
import com.lesson.myahut.util.GlobalContext;
import com.lesson.myahut.util.Util;


public class PreAvgPointActivity extends AppCompatActivity {
    private EditText textAvgUid;
    private Button avgBtn;
    private String uid;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_avgpoint);

        /* 添加新版本的 actionbar 功能，并显示返回按钮 */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        textAvgUid = (EditText) findViewById(R.id.avg_uid);
        avgBtn = (Button) findViewById(R.id.avgBtn);
        avgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uid = textAvgUid.getText().toString();
                Log.e("uid", uid);

                if (uid.length() == 0) {
                    Util.makeToast(PreAvgPointActivity.this, "请输入学号");
                    return;
                } else if (Util.isOnline(GlobalContext.mainActivity)) {
                    new AvgPointTask().execute();
                } else {
                    Util.makeToast(PreAvgPointActivity.this, "请检查网络链接");
                }
            }
        });


    }

    android.app.ProgressDialog progressDialog;

    private class AvgPointTask extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PreAvgPointActivity.this, null, "正在获取信息...", true);
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
                /* 后台刷新第一专业平均学分绩信息，并赋值 */
                info = ViewAvgPointHandler.getInstance(PreAvgPointActivity.this, uid).getAvgPointInfo();
                /**/
                ViewAvgPointHandler.getInstance(PreAvgPointActivity.this, uid).makeEmpty();
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
                if (info == null) {
                    Util.makeToast(PreAvgPointActivity.this, "服务器端错误，无法读取学分绩");
                }else if(info == "error"){
                    Util.makeToast(PreAvgPointActivity.this, "请输入正确的学号");
                } else {
                    new AlertDialog.Builder(PreAvgPointActivity.this)
                            .setTitle("平均学分绩")
                            .setMessage(info)
                            .setPositiveButton("确定", null)
                            .create().show();
                }
//                openActivity(MainActivity.class);
//                PreAvgPointActivity.this.finish();
            } else {
                /* 登陆失败，用户名或密码错误 */
                if (GlobalContext.msg != null) {
                    Util.makeToast(PreAvgPointActivity.this, GlobalContext.msg);
                    GlobalContext.msg = null;
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
        uid = null;
        info = null;
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
