package com.lesson.myahut.handler;

/**
 * Created by qidunwei on 2016/1/13.
 */
public class TimeTableSetting {

    public int year, month, day;
    public boolean seasonWinter = false;
    public void setSeason(int seasonIsWinter) {
        if(seasonIsWinter == 1)
            seasonWinter = true;
    }

    public String getSeason() {
        return seasonWinter ? "1" : "0";
    }

    public String getBeginDate() {
        return year + "-" + month + "-" + day;
    }
}
