package com.lesson.myahut.api;

/**
 * Created by qidunwei on 2016/1/14.
 */
public class HttpURLs {
    private static final String PATH = "http://ahutcourse.sinaapp.com/";
//    private static final String PATH = "http://myahut.applinzi.com/";

    private static final String LOGIN_URL = "login_layout";
    private static final String COURSE_URL = "getCourse";
    private static final String TASK_URL = "getTaskInfo";
    private static final String MAKEUP_URL = "getMakeUpInfo";
    private static final String SCORE_URL = "getScore";
    private static final String AVGPOINT_URL = "getAvgPoint";
    private static final String UPDATE_URL = "getUpdateInfo";

    public static String getLoginUrl() {
        return PATH + LOGIN_URL; }

    public static String getCourseUrl() {
        return PATH + COURSE_URL;
    }

    public static String getTaskUrl() {
        return PATH + TASK_URL;
    }

    public static String getMakeUpUrl() {
        return PATH + MAKEUP_URL;
    }

    public static String getScoreUrl() {
        return PATH + SCORE_URL;
    }

    public static String getAvgpointUrl() {
        return PATH + AVGPOINT_URL;
    }

    public static String getUpdateInfo() {
        return PATH + UPDATE_URL;
    }
}
