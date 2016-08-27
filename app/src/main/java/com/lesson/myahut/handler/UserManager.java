package com.lesson.myahut.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lesson.myahut.api.HttpEntity;
import com.lesson.myahut.entity.UserInfo;
import com.lesson.myahut.entity.UserLessonInfo;
import com.lesson.myahut.util.DataBaseHelper;

/**
 * Created by qidunwei on 2016/1/14.
 */
public class UserManager {
    private static UserManager userManager;
    private DataBaseHelper DBHelper;

    private Context context;
    private UserInfo user = null;
    private String datebaseName = "myahutlesson";
    private String myLessonTable = "myLesson";
    private String userInfoTable = "userInfo";

    private LessonManager lessonManager;

    public UserManager(Context context0) {
        context = context0;
        DBHelper = new DataBaseHelper(context, datebaseName);
        loadUser();
    }

    public static UserManager getInstance(Context context) {
        if (userManager == null) {
            userManager = new UserManager(context);
        }
        return userManager;
    }

    public boolean logoutUser() {
        try {
            SQLiteDatabase db = DBHelper.getWritableDatabase();
            db.delete(myLessonTable, null, null);
            db.delete(userInfoTable, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasLocalUser() {
        return getUserXH() != null;
    }

    public String getUserXH() {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        String userXH = null;
        String[] colls = {"userXH"};
        Cursor cursor = null;
        try {
            cursor = db.query(userInfoTable, colls, null, null, null, null, null);
            if (cursor.getCount() == 0) {
                userXH = null;
            } else {
                cursor.moveToFirst();
                userXH = cursor.getString(cursor.getColumnIndex("userXH"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
        return userXH;
    }

    public void loadUser() {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        String[] colls = {"userXH", "userXM", "userBJ", "userXY"};
        Cursor cursor = null;
        try {
            cursor = db.query(userInfoTable, colls, null, null, null, null, null);
            cursor.moveToFirst();
            user.setUserXH(cursor.getString(cursor.getColumnIndex("userXH")));
            user.setUserXH(cursor.getString(cursor.getColumnIndex("userXM")));
            user.setUserXH(cursor.getString(cursor.getColumnIndex("userBJ")));
            user.setUserXH(cursor.getString(cursor.getColumnIndex("userXY")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
    }

    public boolean validLogin(String uid, String pwd) throws Exception {
        return HttpEntity.getInstance(context).toLogin(uid, pwd);
    }

    public void setUserToDB(UserInfo userInfo) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        db.delete("userInfo", null, null);
        ContentValues cv = new ContentValues();
        cv.put("userXH", userInfo.getUserXH());
        cv.put("userXM", userInfo.getUserXM());
        cv.put("userMM", userInfo.getUserMM());
        cv.put("userBJ", userInfo.getUserBJ());
        cv.put("userXY", userInfo.getUserXY());
        cv.put("userZY", userInfo.getUserZY());
        db.insert("userInfo", null, cv);

        db.close();
    }

    public void loginAndUpdateLessonDB(String uid, String pwd) throws Exception {
        UserLessonInfo userLessonInfo = HttpEntity.getInstance(context).getUserLessonInfo(uid, pwd);
        LessonManager lessonManager = LessonManager.getInstance(context);
        lessonManager.lessonlistToDB(userLessonInfo.courses);
        userManager.setUserToDB(userLessonInfo.student);
        SettingManager.getInstance().initSettingTable();/* 初始化设置表的数据 */
    }
}
