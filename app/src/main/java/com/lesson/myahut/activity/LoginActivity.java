package com.lesson.myahut.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lesson.myahut.R;
import com.lesson.myahut.handler.UserManager;
import com.lesson.myahut.util.GlobalContext;
import com.lesson.myahut.util.Util;

import java.lang.reflect.Field;

/**
 * Created by qidunwei on 2016/1/14.
 */
public class LoginActivity {
    private static LoginActivity loginActivity;

    private String uid, pwd;

    private ProgressBar mProgressView;
    private AutoCompleteTextView mIdView;
    private EditText mPasswordView;
    private AlertDialog alertDialog;

    private LoginActivity() {
    }

    public static LoginActivity getInstance() {
        if (loginActivity == null) {
            loginActivity = new LoginActivity();
        }
        return loginActivity;
    }

    public void showLoginDialog() {
        final LinearLayout loginLayout = (LinearLayout) GlobalContext.mainActivity
                .getLayoutInflater().inflate(R.layout.activity_login, null);
        alertDialog = new AlertDialog.Builder(GlobalContext.mainActivity)
                .setTitle("哎课-登录")
                .setView(loginLayout)
                .setPositiveButton("登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Field field = dialog.getClass()
                                    .getSuperclass().getDeclaredField(
                                            "mShowing");
                            field.setAccessible(true);
                            // 将mShowing变量设为false，表示对话框已关闭
                            field.set(dialog, false);
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mProgressView = (ProgressBar) loginLayout.findViewById(R.id.login_progress);
                        mIdView = (AutoCompleteTextView) loginLayout.findViewById(R.id.id);
                        mPasswordView = (EditText) loginLayout.findViewById(R.id.password);
                        uid = mIdView.getText().toString();
                        pwd = mPasswordView.getText().toString();
                        if (uid.length() == 0 || pwd.length() == 0) {
                            Util.makeToast(GlobalContext.mainActivity, "用户名和密码不能为空!");
                        } else if (Util.isOnline(GlobalContext.mainActivity)) {
                            new LoginTask().execute();
                        } else {
                            Util.makeToast(GlobalContext.mainActivity, "请检查网络链接");
                        }
                        return;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GlobalContext.mainActivity.finish();
                    }
                }).show();
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login_layout);
//
//        Button btnLogin = (Button) findViewById(R.id.btnLogin);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText etLoginXH = (EditText) findViewById(R.id.etLoginXH);
//                EditText etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
//                uid = etLoginXH.getText().toString();
//                pwd = etLoginPassword.getText().toString();
//                if (uid.length() == 0 || pwd.length() == 0) {
//                    Util.makeToast(LoginActivity.this, "用户名和密码不能为空!");
//                } else if (Util.isOnline(GlobalContext.mainActivity)) {
//                    new LoginTask().execute();
//                } else {
//                    Util.makeToast(LoginActivity.this, "请检查网络链接");
//                }
//            }
//        });
//
//    }

    android.app.ProgressDialog progressDialog;

    private class LoginTask extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(GlobalContext.mainActivity, null, "正在获取帐号信息...", true);
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
                /* 执行登录并刷新课表数据及个人信息 */
                UserManager.getInstance(GlobalContext.mainActivity).loginAndUpdateLessonDB(uid, pwd);
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
//                openActivity(MainActivity.class);
//                LoginActivity.this.finish();
                alertDialog.hide();
                GlobalContext.mainActivity.loadView();
                GlobalContext.mainActivity.loadLocalInfo();
            } else {
                /* 登陆失败，用户名或密码错误 */
                showLoginDialog();
                if (GlobalContext.msg != null) {
                    Util.makeToast(GlobalContext.mainActivity, GlobalContext.msg);
                    GlobalContext.msg = null;
                }
            }
        }
    }
}