package com.lesson.myahut.handler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.lesson.myahut.R;
import com.lesson.myahut.api.HttpEntity;
import com.lesson.myahut.api.HttpUtils;
import com.lesson.myahut.entity.UpdateInfo;
import com.lesson.myahut.util.GlobalContext;
import com.lesson.myahut.util.Util;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Statement;

/**
 * Created by qidunwei on 2016/3/21.
 */
public class UpdateHandler {

    final static int LAST_VERSION = -1;
    final static int UPDATE_CLIENT = 0;
    final static int GET_UPDATE_INFO_ERROR = 1;
    final static int DOWN_ERROR = 2;

    static Message msg = new Message();

    public void Check() {
        new CheckTask().execute();
    }


    private class CheckTask extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected void onPreExecute() {

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
                /* 获取基本更新的信息 */
                Log.e("Tag", "001");
                HttpEntity.getInstance(GlobalContext.mainActivity).getUpdateInfo();
                Log.e("getVersion", UpdateInfo.getInstance().getVersion());

                if (Util.getAppVersion(GlobalContext.mainActivity)
                        .equals(UpdateInfo.getInstance().getVersion())) {
                    Log.e("Tag", "版本号相同，无需升级");
                    Message msg = new Message();
                    msg.what = LAST_VERSION;
                    handler.sendMessage(msg);
                    return false;
                } else {
                    Log.e("Tag", "版本号不同，提示用户升级");
                    Message msg = new Message();
                    msg.what = UPDATE_CLIENT;
                    handler.sendMessage(msg);
                    return true;
                }
            } catch (Exception e) {
                msg.what = GET_UPDATE_INFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
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
            if (res) {
                showUpdataDialog();
            } else {
            }
        }
    }

    public File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "ahutcourse.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case LAST_VERSION:
                    Util.makeToast(GlobalContext.mainActivity, "当前已是最新版本");
                    break;
                case UPDATE_CLIENT:
                    Util.makeToast(GlobalContext.mainActivity, "发现最新版本，请更新");
                    break;
                case GET_UPDATE_INFO_ERROR:
                    Util.makeToast(GlobalContext.mainActivity, "获取服务器更新信息失败");
                    break;
                case DOWN_ERROR:
                    Util.makeToast(GlobalContext.mainActivity, "下载新版本失败");
                    break;
            }
        }
    };

    /*
     *
     * 弹出对话框通知用户更新程序
     *
     * 弹出对话框的步骤：
     * 	1.创建alertDialog的builder.
     *	2.要给builder设置属性, 对话框的内容,样式,按钮
     *	3.通过builder 创建一个对话框
     *	4.对话框show()出来
     */
    protected void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(GlobalContext.mainActivity);
        builer.setTitle("版本升级");
        builer.setMessage("请下载更新最新版本");
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.e("Tag", "下载apk,更新");

                if (!Util.isOnline(GlobalContext.mainActivity)) {
                    Util.makeToast(GlobalContext.mainActivity, "请检查网络链接");
                    return;
                }
                downLoadApk();
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(GlobalContext.mainActivity);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(UpdateInfo.getInstance().getUrl(), pd);
                    sleep(1000);
                    /* 安装下载的新版本spk */
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        GlobalContext.mainActivity.startActivity(intent);
    }

    /*
     * 进入程序的主界面
     */
    private void LoadMain() {
        GlobalContext.mainActivity.refreshActivity();
    }

}
