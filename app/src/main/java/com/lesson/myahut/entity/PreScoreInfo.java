package com.lesson.myahut.entity;

import com.google.gson.Gson;
import com.lesson.myahut.activity.PreScoreActivity;

/**
 * Created by qidunwei on 2016/3/19.
 */
public class PreScoreInfo {
    private static PreScoreInfo preScoreInfo;
    private String uid;
    private String tid;
    private String drop_xn;
    private String drop_xq;

    private PreScoreInfo(PreScoreSerializable preScoreSerializable) {
        this.uid = preScoreSerializable.getUid();
        this.tid = preScoreSerializable.getTid();
        this.drop_xn = preScoreSerializable.getDrop_xn();
        this.drop_xq = preScoreSerializable.getDrop_xq();
    }

    public static PreScoreInfo getInstance(PreScoreSerializable preScoreSerializable) {
        if (preScoreInfo == null) {
            preScoreInfo = new PreScoreInfo(preScoreSerializable);
        } else {
            preScoreInfo.uid = preScoreSerializable.getUid();
            preScoreInfo.tid = preScoreSerializable.getTid();
            preScoreInfo.drop_xn = preScoreSerializable.getDrop_xn();
            preScoreInfo.drop_xq = preScoreSerializable.getDrop_xq();
        }
        return preScoreInfo;
    }
    public String getJson() {
        Gson gson = new Gson();
        String json = gson.toJson(preScoreInfo);
        return json;
    }
}
