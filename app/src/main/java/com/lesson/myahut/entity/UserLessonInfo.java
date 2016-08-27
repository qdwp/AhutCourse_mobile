package com.lesson.myahut.entity;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qidunwei on 2016/1/27.
 */
public class UserLessonInfo {
    private static UserLessonInfo userLessonInfo;

    public boolean success;
    public UserInfo student;
    public ArrayList<LessonInfo> courses;

    private UserLessonInfo() {
    }

    public static UserLessonInfo getInstance() {
        if (userLessonInfo == null) {
            userLessonInfo = new UserLessonInfo();
        }
        return userLessonInfo;
    }

    public UserLessonInfo getUserLessonInfo(String json) {

        Gson gson = new Gson();
        userLessonInfo = gson.fromJson(json, new TypeToken<UserLessonInfo>() {
        }.getType());

        return userLessonInfo;
    }

}
