package com.lesson.myahut.entity;

import com.google.gson.Gson;

/**
 * Created by qidunwei on 2016/3/15.
 */
public class AvgPoint {

    private static AvgPoint avgPoint;
    private String uid;

    private AvgPoint(String uid) {
        this.uid = uid;
    }

    public static AvgPoint getInstance(String uid) {
        if (avgPoint == null) {
            avgPoint = new AvgPoint(uid);
        } else {
            avgPoint.uid = uid;
        }
        return avgPoint;
    }

    public String getJson() {
        Gson gson = new Gson();
        String json = gson.toJson(avgPoint);
        return json;
    }
}
