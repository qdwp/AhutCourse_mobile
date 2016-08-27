package com.lesson.myahut.handler;

import android.content.Context;

import com.lesson.myahut.api.HttpEntity;
import com.lesson.myahut.entity.PreTaskInfo;
import com.lesson.myahut.entity.PreTaskSerializable;
import com.lesson.myahut.entity.ViewTaskInfo;

import java.util.Calendar;

/**
 * Created by qidunwei on 2016/3/20.
 */
public class ViewTaskHandler {
    private static ViewTaskHandler viewTaskHandler;
    private Context context;
    public ViewTaskInfo[] viewTaskInfos;

    public String[] yearList = new String[5];
    public String[] monthList = new String[2];

    private ViewTaskHandler(Context context) {
        this.context = context;

    }

    public static ViewTaskHandler getInstance(Context context) {
        if (viewTaskHandler == null) {
            viewTaskHandler = new ViewTaskHandler(context);
        }
        return viewTaskHandler;
    }

    /**
     * 初始化时间下拉框的信息
     */
    public void initTimeList() {
        int year = 0;
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 8) {
            year = currentYear - 1;
            monthList[0] = "2";
            monthList[1] = "1";
        } else {
            year = currentYear;
            monthList[0] = "1";
            monthList[1] = "2";
        }
        for (int i = 0; i < 5; year--, i++) {
            String temp = year + "-" + (year + 1);
            yearList[i] = temp;
        }
    }

    public ViewTaskInfo[] getTaskInfos(PreTaskSerializable preTaskSerializable) {
        try {
            PreTaskInfo preTaskInfo = PreTaskInfo.getInstance(preTaskSerializable);
            HttpEntity.getInstance(context).getTaskInfos(preTaskInfo);
            return viewTaskInfos;
        } catch (Exception e) {
            return null;
        }
    }

}
