package com.lesson.myahut.handler;

import android.content.Context;
import android.util.Log;

import com.lesson.myahut.api.HttpEntity;

/**
 * Created by qidunwei on 2016/3/15.
 */
public class ViewAvgPointHandler {
    private static ViewAvgPointHandler viewAvgPointHandler = null;
    private Context context;
    private String data = null;
    private String uid = null;

    private ViewAvgPointHandler(Context context, String uid) {
        this.context = context;
        this.uid = uid;
    }

    public static ViewAvgPointHandler getInstance(Context context, String uid) {
        if (viewAvgPointHandler == null) {
            viewAvgPointHandler = new ViewAvgPointHandler(context, uid);
        } else if (viewAvgPointHandler.uid == null) {
            viewAvgPointHandler.uid = uid;
        }
        return viewAvgPointHandler;
    }

    public void makeEmpty() {
        this.data = null;
        this.uid = null;
    }

    public String getAvgPointInfo() {
        String info = null;
        try {
            Log.e("Tag", "000");
            data = HttpEntity.getInstance(context)
                    .getAvgPointInfo(uid);
            if (data.equals("非数字")) {
                info = "error";
            } else {
                info = "学号 " + viewAvgPointHandler.uid + " 第一专业学分绩为：" + data + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }
}
