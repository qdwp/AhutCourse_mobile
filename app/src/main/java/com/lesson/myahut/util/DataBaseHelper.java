package com.lesson.myahut.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Created by qidunwei on 2016/1/13.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name, CursorFactory cursorFactory,
                          int version) {
        super(context, name, cursorFactory, version);
    }

    public static final int dbVersion = 1;
    public static final String lessonTableName = "myLesson";
    public static final String userInfoTableName = "userInfo";
    public static final String settingTableName = "setting";
    public static final String createUserInfoTableCmd = "CREATE Table "+ userInfoTableName +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, userXH TEXT, userMM TEXT, userXM TEXT, userBJ TEXT,userXY TEXT, userZY TEXT);";
    public static final String createLessonTableCmd = "CREATE TABLE " + lessonTableName + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, lessonname TEXT, teachername TEXT, lessonplace TEXT, startweek INTEGER, endweek INTEGER, week INTEGER ,time INTEGER);";
    public static final String createSettingTableCmd = "CREATE TABLE " +settingTableName +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, winter INTEGER, year INTEGER, month INTEGER, day INTEGER);";

    public DataBaseHelper(Context baseContext, String name) {
        super(baseContext, name, null, dbVersion);
    }

    /**
     * 创建数据库表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUserInfoTableCmd);
        db.execSQL(createLessonTableCmd);
        db.execSQL(createSettingTableCmd);
    }

    /**
     * 数据库版本更新
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion == 1 && newVersion == 2) {
//            dropColumn(db, lessonTableName, createLessonTableCmd, new String[]{"homework", "lessonalias"});
//        }
    }

    /**
     * 删除一个表的多列
     * 参考自：http://stackoverflow.com/questions/8442147/how-to-delete-or-add-column-in-sqlite
     *
     * @param db
     * @param tableName
     * @param createTableCmd
     * @param colsToRemove
     */
    private void dropColumn(SQLiteDatabase db,
                            String tableName,
                            String createTableCmd,
                            String[] colsToRemove) {

        List<String> updatedTableColumns = getTableColumns(db, tableName);
        // Remove the columns we don't want anymore from the table's list of columns
        updatedTableColumns.removeAll(Arrays.asList(colsToRemove));

        String columnsSeperated = TextUtils.join(",", updatedTableColumns);

        db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;");

        // Creating the table on its new format (no redundant columns)
        db.execSQL(createTableCmd);

        // Populating the table with the data
        db.execSQL("INSERT INTO " + tableName + "(" + columnsSeperated + ") SELECT "
                + columnsSeperated + " FROM " + tableName + "_old;");
        db.execSQL("DROP TABLE " + tableName + "_old;");
    }

    public List<String> getTableColumns(SQLiteDatabase db, String tableName) {
        ArrayList<String> columns = new ArrayList<String>();
        String cmd = "pragma table_info(" + tableName + ");";
        Cursor cur = db.rawQuery(cmd, null);

        while (cur.moveToNext()) {
            columns.add(cur.getString(cur.getColumnIndex("name")));
        }
        cur.close();

        return columns;
    }
}
