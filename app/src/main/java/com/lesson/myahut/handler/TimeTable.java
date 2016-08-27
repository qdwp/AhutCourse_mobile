package com.lesson.myahut.handler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lesson.myahut.R;
import com.lesson.myahut.entity.Lesson;
import com.lesson.myahut.util.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.Days;

/**
 * Created by qidunwei on 2016/1/13.
 */
public class TimeTable {
    private static TimeTable timetable;

    private Context context;
    private Calendar cal;

    private static SettingManager settingManager;
    private DataBaseHelper DBHelper;
    private String datebaseName = "myahutlesson";
    private String settingTableName = "setting";

    public String[] begintime = new String[5];
    public String[] endtime = new String[5];
    public int beginDate_year, beginDate_month, beginDate_day;
    public int year, month, dayOfMonth, dayOfYear, weekDay, beginOfweek;
    public int begintimemin[] = new int[5];
    public int endtimemin[] = new int[5];
    public int numOfWeek = -1;
    public boolean seasonWinter = false;
    public String[] weekName = new String[7], lessontimeName = new String[5];

    public TimeTable(Context context0) {
        context = context0;

        DBHelper = new DataBaseHelper(context, datebaseName);
        settingManager = SettingManager.getInstance();
        weekName = context.getResources().getStringArray(R.array.week_name);
        lessontimeName = context.getResources().getStringArray(
                R.array.lessontime_name);
        loadData();
        initTime();
    }

    public static TimeTable getInstance(Context context) {
        if (timetable == null) {
            timetable = new TimeTable(context);
        }
        return timetable;
    }

    /**
     * 载入/刷新数据
     */
    public void loadData() {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        String[] colls = {"winter", "year", "month", "day"};
        Cursor cursor = null;
        try {
            cursor = db.query(settingTableName, colls, null, null, null, null, null);
            cursor.moveToFirst();
            seasonWinter = (cursor.getInt(cursor.getColumnIndex("winter")) == 1) ? true : false;
            beginDate_year = cursor.getInt(cursor.getColumnIndex("year"));
            beginDate_month = cursor.getInt(cursor.getColumnIndex("month"));
            beginDate_day = cursor.getInt(cursor.getColumnIndex("day"));

        } catch (Exception e) {
            seasonWinter = false;
            beginDate_year = 2100;
            beginDate_month = 1;
            beginDate_day = 1;

        } finally {
            cursor.close();
            db.close();
        }

        loadBeginEndTime(seasonWinter);
    }

    public TimeTable refreshBeginEndTime(boolean seasonWinter) {
        loadBeginEndTime(seasonWinter);
        if (timetable == null) {
            timetable = new TimeTable(context);
        }
        return timetable;
    }

    /**
     * 加载上课/下课的时间设置
     *
     * @param seasonWinter
     */
    private void loadBeginEndTime(boolean seasonWinter) {
        if (seasonWinter == false) {
            begintime[0] = "08:00";
            begintime[1] = "10:00";
            begintime[2] = "14:30";
            begintime[3] = "16:30";
            begintime[4] = "19:00";

            endtime[0] = "09:35";
            endtime[1] = "11:35";
            endtime[2] = "16:05";
            endtime[3] = "18:05";
            endtime[4] = "21:30";

            begintimemin[0] = 480;
            begintimemin[1] = 600;
            begintimemin[2] = 870;
            begintimemin[3] = 990;
            begintimemin[4] = 1140;

            endtimemin[0] = 575;
            endtimemin[1] = 695;
            endtimemin[2] = 965;
            endtimemin[3] = 1085;
            endtimemin[4] = 1290;
        } else {
            begintime[0] = "08:00";
            begintime[1] = "10:00";
            begintime[2] = "14:00";
            begintime[3] = "16:00";
            begintime[4] = "18:30";

            endtime[0] = "09:35";
            endtime[1] = "11:35";
            endtime[2] = "15:35";
            endtime[3] = "17:35";
            endtime[4] = "21:00";

            begintimemin[0] = 480;
            begintimemin[1] = 600;
            begintimemin[2] = 840;
            begintimemin[3] = 960;
            begintimemin[4] = 1110;

            endtimemin[0] = 575;
            endtimemin[1] = 695;
            endtimemin[2] = 935;
            endtimemin[3] = 1055;
            endtimemin[4] = 1260;
        }

		/*
         * for(int i = 0;i < 5; i++){ begintimemin[i] =
		 * time2minute(begintime[i]); }
		 *
		 * for(int i = 0;i < 5; i++){ endtimemin[i] = time2minute(endtime[i]); }
		 */
    }

    /**
     * 转换季节
     */

    public void toggleSeason() {
        seasonWinter = !seasonWinter;
        loadBeginEndTime(seasonWinter);
    }

    /**
     * 设置当前为夏令时/冬令时
     *
     * @param winter false for summer, true for winter
     */
    public void setSeasonWinter(boolean winter) {
        settingManager.setWinter(winter);
    }

    /**
     * 初始化时间
     */
    public void initTime() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        weekDay = getCurrentWeekDay();
        numOfWeek = getNumOfWeekSincePeriod();
    }

    /**
     * 获取当前为周几
     *
     * @return
     */
    public static int getCurrentWeekDay() {
        Calendar calendar = Calendar.getInstance();
        return systemWeek2NormalWeek(calendar.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * 刷新当前第几周
     */
    public void refreshNumOfWeek() {
        loadData();
        numOfWeek = getNumOfWeekSincePeriod();
    }

    /**
     * 刷新当前第几周（执行）
     *
     * @return
     */
    public int getNumOfWeekSincePeriod() {
        // 计算开学第几周
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(beginDate_year, beginDate_month, beginDate_day-1);
        Date beginDate = beginCal.getTime();
        Date nowDate = Calendar.getInstance().getTime();

        if (nowDate.compareTo(beginDate) < 0)
            return 0;

        int days = Days.daysBetween(new org.joda.time.DateTime(beginDate),
                new org.joda.time.DateTime(nowDate))
                .getDays();
        /* 引用 org.joda.time 无效，原因未知
         * 后可以引用，原因未知
         */
        /* 计算当前是第几周 */
        Log.e("days", days + "");
        beginOfweek = beginCal.get(Calendar.DAY_OF_WEEK);
        Log.e("beginOfweek", beginOfweek + "123");
        if (days >= 0 && days <= 180) {
            if ((7 - beginOfweek) < (days % 7)) {
                return days / 7 + 2;
            }
            return days / 7 + 1;
        } else
            return 0;

    }

    /**
     * 判断当前年份的天数
     *
     * @param year
     * @return
     */
    public int dayOfYear(int year) {
        // 一年中的天数
        if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0)
            return 365;
        else
            return 366;
    }

    /**
     * 计算上半学期/下半学期
     *
     * @return true summer, false spring
     */
    public boolean formerPeriodOfYear() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        return ((month == 0) || (month >= 8)) ? true : false;
//        return ((month >= 0 && month <= 1) || (month >= 8)) ? true : false;
    }

    /**
     * 计算当前学期的开学年份
     *
     * @return
     */
    public int getYearOfCurrentPeriod() {
        // 计算当前学期的开学年份
        if (formerPeriodOfYear()) {
            return year - 1;
        } else {
            return year;
        }
    }


    public boolean canAppend(Lesson lesson) {
        // 后两节有课
        if (lesson.time == 0 || lesson.time == 2) {
            Lesson appendLesson = LessonManager.getInstance(context)
                    .getLessonAt(lesson.week, lesson.time + 1);
            if (appendLesson != null) {
                if (appendLesson.name == lesson.name) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAppended(Lesson lesson) {
        // 前两节有课
        if (lesson.time == 1 || lesson.time == 3) {
            Lesson appendLesson = LessonManager.getInstance(context)
                    .getLessonAt(lesson.week, lesson.time - 1);
            if (appendLesson != null) {
                if (appendLesson.name == lesson.name) {
                    return true;
                }
            }
        }
        return false;
    }

    public int appendMode(Lesson lesson) {
        if (canAppend(lesson))
            return 1;
        if (isAppended(lesson))
            return -1;
        return 0;
    }

    public int isNowHavingLesson(int week, int time) {
        // 本周本课状态
        // -1还没上，0正在上，1上过了
        weekDay = getCurrentWeekDay();
        Calendar curCal = Calendar.getInstance();
        if (weekDay < week) {
            return -1;
        } else if (weekDay > week) {
            return 1;
        } else {
            int curMinute = curCal.get(Calendar.HOUR_OF_DAY) * 60
                    + curCal.get(Calendar.MINUTE);
            if (curMinute < begintimemin[time]) {
                return -1;
            } else if (curMinute >= begintimemin[time]
                    && curMinute <= endtimemin[time]) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public int isNowHavingLesson(Lesson lesson) {
        return isNowHavingLesson(lesson.week, lesson.time);
    }

    public static int systemWeek2NormalWeek(int sys) {
        int week = sys - 2;
        if (week == -1)
            return 6;
        return week;
    }

    public static int normalWeek2SystemWeek(int week) {
        if (week == 6)
            return 1;
        return week + 2;
    }

    public static String miliTime2String(long miliTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM月d日 HH:mm");
        Date date = new Date();
        date.setTime(miliTime);
        return sDateFormat.format(date);
    }

    public static int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) * 60
                + calendar.get(Calendar.MINUTE);
    }

    public static boolean isValidWeek(int week) {
        return (week >= 0 && week <= 6) ? true : false;
    }

    public static boolean isValidTime(int time) {
        return (time >= 0 && time <= 4) ? true : false;
    }

    public static boolean isValidWeekTime(int week, int time) {
        return isValidWeek(week) && isValidTime(time);
    }

    public boolean nowIsAtLessonBreak(int week, int i) {
        // 0 早上四节课的课间， 2 下午四节课的课间
        if (week != weekDay)
            return false;
        int min = getCurrentMinute();
        switch (i) {
            case 0:
                return (min >= endtimemin[0] && min <= begintimemin[1]) ? true
                        : false;
            case 2:
                return (min >= endtimemin[2] && min <= begintimemin[3]) ? true
                        : false;
        }
        return false;
    }

    public void setTimeTableSetting(TimeTableSetting timetableSetting) {
        if (timetableSetting.month >= 1 && timetableSetting.month <= 12
                && timetableSetting.day >= 1 && timetableSetting.day <= 31) {
            settingManager.setBeginTime(timetableSetting.year, timetableSetting.month, timetableSetting.day);
            beginDate_year = timetableSetting.year;
            beginDate_month = timetableSetting.month - 1;
            beginDate_day = timetableSetting.day;
        }
        refreshNumOfWeek();
        setSeasonWinter(timetableSetting.seasonWinter);
    }

    public TimeTableSetting getTimeTableSetting() {
        TimeTableSetting timetableSetting = new TimeTableSetting();
        timetableSetting.year = beginDate_year;
        timetableSetting.month = beginDate_month + 1;
        timetableSetting.day = beginDate_day;
        timetableSetting.seasonWinter = seasonWinter;
        return timetableSetting;
    }

}
