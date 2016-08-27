package com.lesson.myahut.entity;

import android.util.Log;

import com.google.gson.Gson;
import com.lesson.myahut.util.GlobalContext;
import com.lesson.myahut.util.Util;

/**
 * Created by qidunwei on 2016/1/14.
 */
public class LoginUser {

    private static LoginUser loginUser;
    private String uid;
    private String pwd;
    private String ver;
    private String mod;
    private String apk;

    private LoginUser(String uid, String pwd) {
        this.uid = uid;
        this.pwd = pwd;
        this.ver = android.os.Build.VERSION.RELEASE;
        this.mod = android.os.Build.MODEL;
        this.apk = Util.getAppVersionName(GlobalContext.mainActivity);
    }

    public static LoginUser getInstance(String uid, String pwd){
        if(loginUser == null){
            loginUser = new LoginUser(uid,pwd);
        }else{
            loginUser.uid = uid;
            loginUser.pwd = pwd;
            loginUser.ver = android.os.Build.VERSION.RELEASE;
            loginUser.mod = android.os.Build.MODEL;
            loginUser.apk = Util.getAppVersionName(GlobalContext.mainActivity);
        }
        return loginUser;
    }

    public String getJson(){
        Gson gson = new Gson();
        String json = gson.toJson(loginUser);
        return json;
    }
}
