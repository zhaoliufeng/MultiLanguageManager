package com.we_smart.sqldao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zhao Liufeng on 2018/4/20.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 3;

    public DBOpenHelper(Context context, String name) {
        super(context, name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                String sql_upgrade = "alter table SqlTestBean add groupId integer";
                db.execSQL(sql_upgrade);
                break;
            case 2:
                sql_upgrade = "alter table SqlTestBean add boo Boolean";
                db.execSQL(sql_upgrade);
                break;
        }
    }
}
