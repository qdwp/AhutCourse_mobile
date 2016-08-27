package com.lesson.myahut.entity;

import com.google.gson.Gson;

/**
 * Created by qidunwei on 2016/3/20.
 */
public class PreTaskInfo {
    private static PreTaskInfo preTaskInfo;
    private String uid;
    private String pwd;
    private String xnd;
    private String xqd;

    private PreTaskInfo(PreTaskSerializable preTaskSerializable) {
        this.uid = preTaskSerializable.getUid();
        this.pwd = preTaskSerializable.getPwd();
        this.xnd = preTaskSerializable.getXnd();
        this.xqd = preTaskSerializable.getXqd();
    }

    public static PreTaskInfo getInstance(PreTaskSerializable preTaskSerializable) {
        if (preTaskInfo == null) {
            preTaskInfo = new PreTaskInfo(preTaskSerializable);
        } else {
            preTaskInfo.uid = preTaskSerializable.getUid();
            preTaskInfo.pwd = preTaskSerializable.getPwd();
            preTaskInfo.xnd = preTaskSerializable.getXnd();
            preTaskInfo.xqd = preTaskSerializable.getXqd();
        }
        return preTaskInfo;
    }
    public String getJson() {
        Gson gson = new Gson();
        String json = gson.toJson(preTaskInfo);
        return json;
    }
}
