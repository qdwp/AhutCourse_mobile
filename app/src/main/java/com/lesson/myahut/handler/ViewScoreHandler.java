package com.lesson.myahut.handler;

import android.content.Context;

import com.lesson.myahut.api.HttpEntity;
import com.lesson.myahut.entity.PreScoreInfo;
import com.lesson.myahut.entity.PreScoreSerializable;
import com.lesson.myahut.entity.ViewScoreInfo;

import java.util.Calendar;

/**
 * Created by qidunwei on 2016/3/16.
 */
public class ViewScoreHandler {
    private static ViewScoreHandler viewScoreHandler;
    private Context context;
    public ViewScoreInfo[] viewScoreInfos;

    public String[] yearList = new String[5];
    public String[] monthList = new String[2];

    private ViewScoreHandler(Context context) {
        this.context = context;

    }

    public static ViewScoreHandler getInstance(Context context) {
        if (viewScoreHandler == null) {
            viewScoreHandler = new ViewScoreHandler(context);
        }
        return viewScoreHandler;
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

    public ViewScoreInfo[] getScoreInfos(PreScoreSerializable preScoreSerializable) {
        try {
            PreScoreInfo preScoreInfo = PreScoreInfo.getInstance(preScoreSerializable);
            HttpEntity.getInstance(context).getScoreInfos(preScoreInfo);
            return viewScoreInfos;
        } catch (Exception e) {
            return null;
        }
    }

}
