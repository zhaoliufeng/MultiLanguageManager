package com.we_smart.sqldao;

import android.database.Cursor;
import android.util.SparseArray;

import java.util.List;

/**
 * 基础数据库操作类
 * Created by Zhao Liufeng on 2018/4/18.
 */

public class BaseDAO<T> {

    public String mTableName;
    private Class<?> mClazz;

    public BaseDAO(Class<?> clazz) {
        exeSql(SqlBuilder.getInstance().createTable(clazz));
        mClazz = clazz;
    }

    //增
    public void insert(T obj) {
        exeSql(SqlBuilder.getInstance().insertObject(obj));
    }

    //删
    public void delete(T obj) {
        exeSql(SqlBuilder.getInstance().deleteObject(obj));
    }

    //查
    public List<T> query() {
        return null;
    }

    public List<T> query(String whereKey, String whereValue) {
        Cursor cursor = getQueryCursor(whereKey, whereValue);
        return null;
    }

    //改
    public void update(T obj) {
        exeSql(SqlBuilder.getInstance().updateObject(obj));
    }

    private void exeSql(String sql) {
        DBHelper.getInstance().openDatabase().execSQL(sql);
    }

    private Cursor getQueryCursor(String whereKey, String whereValue) {
        return DBHelper.getInstance().openDatabase().query(mClazz.getSimpleName(), null,
                String.format("%s=?", whereKey), new String[]{whereValue},
                null, null, null);
    }

}
