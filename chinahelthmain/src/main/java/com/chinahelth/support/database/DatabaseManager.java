package com.chinahelth.support.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chinahelth.HealthApplication;
import com.chinahelth.support.database.table.ArticleItemTable;

/**
 * Created by caihanyuan on 15-8-31.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private final static String DB_NAME = "chinahealth.db";

    private final static int DB_VERSION = 1;
    private static DatabaseManager mInstance = null;
    private final String CREATE_ARTICLEITEM_TABLE_SQL = "create table " + ArticleItemTable.TABLE_NAME
            + "("
            + ArticleItemTable.UID + " integer primary key autoincrement,"
            + ArticleItemTable.GROUP_TYPE + " integer,"
            + ArticleItemTable.TYPE + " integer,"
            + ArticleItemTable.TITLE + " text,"
            + ArticleItemTable.SOURCE + " text,"
            + ArticleItemTable.COMMENT_NUMS + " integer,"
            + ArticleItemTable.PUBLISH_TIME + " integer,"
            + ArticleItemTable.THUMBNAIL_URIS + " text,"
            + ArticleItemTable.IS_READED + " integer"
            + ");";

    private DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static DatabaseManager getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseManager(HealthApplication.getInstance(), DB_NAME, null, DB_VERSION);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLEITEM_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ArticleItemTable.TABLE_NAME);

        onCreate(db);
    }
}
