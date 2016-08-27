package com.lesson.myahut.entity;

import com.google.gson.Gson;

/**
 * Created by qidunwei on 2016/3/20.
 */
public class PreMakeUpInfo {
    private static PreMakeUpInfo preMakeUpInfo;
    private String uid;
    private String pwd;
    private String xnd;
    private String xqd;

    private PreMakeUpInfo(PreMakeUpSerializable preMakeUpSerializable) {
        this.uid = preMakeUpSerializable.getUid();
        this.pwd = preMakeUpSerializable.getPwd();
        this.xnd = preMakeUpSerializable.getXnd();
        this.xqd = preMakeUpSerializable.getXqd();
    }

    public static PreMakeUpInfo getInstance(PreMakeUpSerializable preMakeUpSerializable) {
        if (preMakeUpInfo == null) {
            preMakeUpInfo = new PreMakeUpInfo(preMakeUpSerializable);
        } else {
            preMakeUpInfo.uid = preMakeUpSerializable.getUid();
            preMakeUpInfo.pwd = preMakeUpSerializable.getPwd();
            preMakeUpInfo.xnd = preMakeUpSerializable.getXnd();
            preMakeUpInfo.xqd = preMakeUpSerializable.getXqd();
        }
        return preMakeUpInfo;
    }
    public String getJson() {
        Gson gson = new Gson();
        String json = gson.toJson(preMakeUpInfo);
        return json;
    }
}
