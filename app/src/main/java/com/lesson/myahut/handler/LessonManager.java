package com.lesson.myahut.handler;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lesson.myahut.entity.Lesson;
import com.lesson.myahut.entity.LessonInfo;
import com.lesson.myahut.util.DataBaseHelper;

/**
 * Created by qidunwei on 2016/1/13.
 */
public class LessonManager {

    private static LessonManager lessonManager;

    private Context context;
    private DataBaseHelper DBHelper;
    private Lesson lessons[][];

    public LessonManager(Context context0) {
        context = context0;
        DBHelper = new DataBaseHelper(context, "myahutlesson");
        getAllLessons();
    }

    public static LessonManager getInstance(Context context) {
        if (lessonManager == null) {
            lessonManager = new LessonManager(context);
        }
        return lessonManager;
    }

    public Lesson[][] getLessons() {
        if (lessons == null)
            getAllLessons();
        return lessons;
    }

    public void getAllLessons() {
        lessons = new Lesson[7][5];
        SQLiteDatabase db = DBHelper.getWritableDatabase();

        String name, place, teacher;
        int week, time, startweek, endweek;

        String[] cols = {"lessonname", "lessonplace", "teachername",
                "startweek", "endweek", "week", "time"};
        Cursor lessoninfo = db.query("myLesson", cols, null, null, null, null, null);
        if (lessoninfo.getCount() == 0) {
            lessoninfo.close();
            db.close();
            return;
        }
        lessoninfo.moveToFirst();
        do {
            name = lessoninfo.getString(0);
            place = lessoninfo.getString(1);
            teacher = lessoninfo.getString(2);
            startweek = lessoninfo.getInt(3);
            endweek = lessoninfo.getInt(4);
            week = lessoninfo.getInt(5);
            time = lessoninfo.getInt(6);
            lessons[week][time] = new Lesson(name, place, teacher,
                    startweek, endweek, week, time);
        } while (lessoninfo.moveToNext());
        lessoninfo.close();
        db.close();
        return;
    }

    public Lesson getLessonAt(int week0, int time0) {
        if (!TimeTable.isValidWeek(week0) || !TimeTable.isValidTime(time0))
            return null;
        return lessons[week0][time0];
    }

    public void deleteDB() {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        db.delete("myLesson", null, null);
        db.close();
        lessons = new Lesson[7][5];
    }

    public void lessonlistToDB(ArrayList<LessonInfo> lessonlist) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        db.delete("myLesson", null, null);
        Iterator<LessonInfo> i = lessonlist.iterator();
        while (i.hasNext()) {
            LessonInfo lesson = i.next();
            ContentValues cv = new ContentValues();
            cv.put("lessonname", lesson.name);
            cv.put("teachername", lesson.teacher);
            cv.put("lessonplace", lesson.place);
            cv.put("startweek", lesson.startweek);
            cv.put("endweek", lesson.endweek);
            cv.put("week", lesson.week);
            cv.put("time", lesson.time);
            db.insert("myLesson", null, cv);
        }
        db.close();
        lessonManager = new LessonManager(context);
    }

}
