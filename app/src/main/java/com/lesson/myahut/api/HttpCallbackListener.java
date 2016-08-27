package com.lesson.myahut.api;

/**
 * Created by qidunwei on 2016/1/11.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
