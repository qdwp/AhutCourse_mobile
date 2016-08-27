package com.lesson.myahut.handler;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.DatePicker;

import com.lesson.myahut.util.DataBaseHelper;
import com.lesson.myahut.util.GlobalContext;
import com.lesson.myahut.util.Util;

import java.util.Calendar;

/**
 * Created by qidunwei on 2016/3/3.
 */
public class SettingManager {
    private static SettingManager settingManager;
    private DataBaseHelper DBHelper;
    private String datebaseName = "myahutlesson";
    private String settingTableName = "setting";

    public SettingManager() {
        DBHelper = new DataBaseHelper(GlobalContext.mainActivity, datebaseName);
    }

    public static SettingManager getInstance() {
        if (settingManager == null) {
            settingManager = new SettingManager();
        }
        return settingManager;
    }

    public void showSetBeginTimeDialog() {

        Calendar cal = Calendar.getInstance();
        final DatePickerDialog dpl = new DatePickerDialog(GlobalContext.mainActivity, null,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpl.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int witch) {
                DatePicker datePicker = dpl.getDatePicker();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();


                setBeginTime(year, month, day);
                GlobalContext.mainActivity.refreshDateInfo();
            }
        });
        dpl.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int witch) {
                    /* 消极取消按钮的事件处理 */
            }
        });
        dpl.show();
    }

    public void setBeginTime(int year, int month, int day) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("year", year);
        cv.put("month", month);
        cv.put("day", day);
        db.update(settingTableName, cv, null, null);
        db.close();
        Log.e("Tag", "OK");

    }

    public void setWinter(Boolean winter) {
        int win = 0;
        if (winter)
            win = 1;
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("winter", win);
        db.update(settingTableName, cv, null, null);
        db.close();
    }

    public boolean getWinter() {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        String[] colls = {"winter"};
        Cursor cursor = null;
        cursor = db.query(settingTableName, colls, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            return false;
        } else {
            cursor.moveToFirst();
            if (cursor.getInt(cursor.getColumnIndex("winter")) == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void initSettingTable() {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        db.delete(settingTableName, null, null);
        ContentValues cv = new ContentValues();
        cv.put("winter", 0);
        cv.put("year", 2100);
        cv.put("month", 1);
        cv.put("day", 1);
        db.insert(settingTableName, null, cv);
        db.close();
    }

}
