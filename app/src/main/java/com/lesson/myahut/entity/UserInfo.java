package com.lesson.myahut.entity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lesson.myahut.util.DataBaseHelper;

/**
 * Created by qidunwei on 2016/1/10.
 */

public class UserInfo {

    private String userXM;
    private String userXH;
    private String userMM;
    private String userBJ;
    private String userXY;
    private String userZY;

    public UserInfo() {
    }

    public UserInfo(String userXM, String userID, String userMM, String userBJ, String userXY, String userZY) {
        this.userXM = userXM;
        this.userXH = userID;
        this.userMM = userMM;
        this.userBJ = userBJ;
        this.userXY = userXY;
        this.userZY = userZY;
    }

    public void loadLocalUser(Context context) {
        UserInfo userInfo = new UserInfo();
        SQLiteDatabase db = new DataBaseHelper(context, "myahutlesson").getWritableDatabase();
        String[] cols = {"userXM", "userXH", "userBJ", "userXY", "userZY", "userMM"};
        Cursor cursor = db.query("userInfo", cols, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return;
        }
        cursor.moveToFirst();
        this.userXM = cursor.getString(0);
        this.userXH = cursor.getString(1);
        this.userBJ = cursor.getString(2);
        this.userXY = cursor.getString(3);
        this.userZY = cursor.getString(4);
        this.userMM = cursor.getString(5);
        cursor.close();
        db.close();

        return;
    }

    public String getUserXM() {
        return userXM;
    }

    public String getUserXH() {
        return userXH;
    }

    public String getUserBJ() {
        return userBJ;
    }

    public String getUserXY() {
        return userXY;
    }

    public String getUserZY() {
        return userZY;
    }

    public String getUserMM() {
        return userMM;
    }

    public void setUserXM(String userXM) {
        this.userXM = userXM;
    }

    public void setUserXH(String userXH) {
        this.userXH = userXH;
    }

    public void setUserBJ(String userBJ) {
        this.userBJ = userBJ;
    }

    public void setUserXY(String userXY) {
        this.userXY = userXY;
    }

    public void setUserZY(String userZY) {
        this.userZY = userZY;
    }

    public void setUserMM(String userMM) {
        this.userMM = userMM;
    }
}
