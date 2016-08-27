package com.lesson.myahut.api;

import android.content.Context;
import android.util.Log;

import com.lesson.myahut.entity.AvgPoint;
import com.lesson.myahut.entity.LoginUser;
import com.lesson.myahut.entity.PreMakeUpInfo;
import com.lesson.myahut.entity.PreScoreInfo;
import com.lesson.myahut.entity.PreTaskInfo;
import com.lesson.myahut.entity.UpdateInfo;
import com.lesson.myahut.entity.ViewMakeUpInfo;
import com.lesson.myahut.entity.ViewScoreInfo;
import com.lesson.myahut.entity.UserLessonInfo;
import com.lesson.myahut.entity.ViewTaskInfo;
import com.lesson.myahut.handler.ViewMakeUpHandler;
import com.lesson.myahut.handler.ViewScoreHandler;
import com.lesson.myahut.handler.ViewTaskHandler;
import com.lesson.myahut.util.GlobalContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qidunwei on 2016/1/14.
 */
public class HttpEntity {

    private static HttpEntity httpEntity;
    private Context context;

    private HttpEntity(Context content) {
        this.context = content;
    }

    public static HttpEntity getInstance(Context content) {
        if (httpEntity == null) {
            httpEntity = new HttpEntity(content);
        }
        return httpEntity;
    }

    /**
     * 执行登录功能
     *
     * @param uid
     * @param pwd
     * @return
     */
    public boolean toLogin(String uid, String pwd) {
        boolean success = false;
        try {
            String result = HttpUtils.doPostMethod(HttpURLs.getLoginUrl(), LoginUser.getInstance(uid, pwd).getJson());
            JSONObject json = new JSONObject(result);
            success = json.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 获取用户信息及课程信息
     *
     * @param uid
     * @param pwd
     * @return
     */
    public UserLessonInfo getUserLessonInfo(String uid, String pwd) {
        boolean success = false;
        UserLessonInfo userLessonInfo = null;
        try {
            Log.e("登陆", LoginUser.getInstance(uid, pwd).getJson());
            String result = HttpUtils.doPostMethod(HttpURLs.getCourseUrl(), LoginUser.getInstance(uid, pwd).getJson());
            JSONObject json = new JSONObject(result);
            success = json.getBoolean("success");
            Log.e("result", result);
            if (success == true) {
                userLessonInfo = UserLessonInfo.getInstance().getUserLessonInfo(result);
            } else {
                GlobalContext.msg = json.getString("msg");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userLessonInfo;
    }

    /**
     * 获取第一专业平均学分绩
     *
     * @param uid
     * @return
     */
    public String getAvgPointInfo(String uid) {
        boolean success = false;
        String data = null;
        Log.e("getAvgpointUrl", HttpURLs.getAvgpointUrl());
        Log.e("getJson", AvgPoint.getInstance(uid).getJson());
        String result = HttpUtils.doPostMethod(HttpURLs.getAvgpointUrl(), AvgPoint.getInstance(uid).getJson());
        try {
            JSONObject json = new JSONObject(result);
            success = json.getBoolean("success");
            Log.e("success", String.valueOf(success));
            if (success == true) {
                data = json.getString("data");
                GlobalContext.msg = null;
            } else {
                GlobalContext.msg = json.getString("msg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取成绩信息
     *
     * @param preScoreInfo
     */
    public void getScoreInfos(PreScoreInfo preScoreInfo) {
        ViewScoreInfo[] viewScoreInfos = null;
        boolean success = false;
        Log.e("getJson", preScoreInfo.getJson());
        String result = HttpUtils.doPostMethod(HttpURLs.getScoreUrl(), preScoreInfo.getJson());

        JSONObject json = null;
        try {
            json = new JSONObject(result);
            success = json.getBoolean("success");
            Log.e("success", String.valueOf(success));
            if (success == true) {
                JSONArray jsonArray = json.getJSONArray("score");
                JSONObject score;
                viewScoreInfos = new ViewScoreInfo[jsonArray.length()];
                ViewScoreHandler.getInstance(context).viewScoreInfos = new ViewScoreInfo[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    score = jsonArray.getJSONObject(i);
                    Log.e("kcm", score.getString("kcm"));
                    viewScoreInfos[i] = new ViewScoreInfo(
                            score.getString("kcm"),
                            score.getString("kcsx"),
                            score.getString("jsxm"),
                            score.getString("xf"),
                            score.getString("xn"),
                            score.getString("xq"),
                            score.getString("zpcj"),
                            score.getString("qmcj"),
                            score.getString("pscj"),
                            score.getString("bkcj"));
                }
                ViewScoreHandler.getInstance(context).viewScoreInfos = viewScoreInfos;
                GlobalContext.msg = null;
            } else {
                GlobalContext.msg = json.getString("msg");
                ViewScoreHandler.getInstance(context).viewScoreInfos = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取考试信息
     *
     * @param preTaskInfo
     */
    public void getTaskInfos(PreTaskInfo preTaskInfo) {
        ViewTaskInfo[] viewTaskInfos = null;
        boolean success = false;
        Log.e("getJson", preTaskInfo.getJson());
        String result = HttpUtils.doPostMethod(HttpURLs.getTaskUrl(), preTaskInfo.getJson());

        JSONObject json = null;
        try {
            json = new JSONObject(result);
            success = json.getBoolean("success");
            Log.e("success", String.valueOf(success));
            Log.e("result", result);
            if (success == true) {
                JSONArray jsonArray = json.getJSONArray("task");
                JSONObject task;
                viewTaskInfos = new ViewTaskInfo[jsonArray.length()];
                ViewTaskHandler.getInstance(context).viewTaskInfos = new ViewTaskInfo[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    task = jsonArray.getJSONObject(i);
                    viewTaskInfos[i] = new ViewTaskInfo(
                            task.getString("kcmc"),
                            task.getString("ksdd"),
                            task.getString("kssj"),
                            task.getString("ksxs"),
                            task.getString("xkkh"),
                            task.getString("xq"),
                            task.getString("zwh"));
                }
                ViewTaskHandler.getInstance(context).viewTaskInfos = viewTaskInfos;
                GlobalContext.msg = null;
            } else {
                GlobalContext.msg = json.getString("msg");
                ViewTaskHandler.getInstance(context).viewTaskInfos = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取补考信息
     *
     * @param preMakeUpInfo
     */
    public void getMakeUpInfos(PreMakeUpInfo preMakeUpInfo) {
        ViewMakeUpInfo[] viewMakeUpInfos = null;
        boolean success = false;
        Log.e("getJson", preMakeUpInfo.getJson());
        String result = HttpUtils.doPostMethod(HttpURLs.getMakeUpUrl(), preMakeUpInfo.getJson());

        JSONObject json = null;
        try {
            json = new JSONObject(result);
            success = json.getBoolean("success");
            Log.e("success", String.valueOf(success));
            Log.e("result", result);
            if (success == true) {
                JSONArray jsonArray = json.getJSONArray("makeup");
                JSONObject make;
                viewMakeUpInfos = new ViewMakeUpInfo[jsonArray.length()];
                ViewMakeUpHandler.getInstance(context).viewMakeUpInfos = new ViewMakeUpInfo[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    make = jsonArray.getJSONObject(i);
                    viewMakeUpInfos[i] = new ViewMakeUpInfo(
                            make.getString("kcmc"),
                            make.getString("ksdd"),
                            make.getString("kssj"),
                            make.getString("ksxs"),
                            make.getString("xkkh"),
                            make.getString("xm"),
                            make.getString("zwh"));
                }
                ViewMakeUpHandler.getInstance(context).viewMakeUpInfos = viewMakeUpInfos;
                GlobalContext.msg = null;
            } else {
                GlobalContext.msg = json.getString("msg");
                ViewMakeUpHandler.getInstance(context).viewMakeUpInfos = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取更新信息
     */
    public void getUpdateInfo() {
        boolean success = false;
        Log.e("Tag", "003");
        String result = HttpUtils.doGetMethod(HttpURLs.getUpdateInfo(), null);
        Log.e("Tag", result);
        JSONObject json = null;
        try {
            json = new JSONObject(result);
            success = json.getBoolean("success");
            if (success == true) {
                JSONObject ver = new JSONObject(json.getString("update"));
                String version  = ver.getString("version");
                String url  = ver.getString("url");
                UpdateInfo.getInstance().setVersion(version);
                UpdateInfo.getInstance().setUrl(url);
                GlobalContext.msg = null;
            } else {
                GlobalContext.msg = json.getString("msg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
