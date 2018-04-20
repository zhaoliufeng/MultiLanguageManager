package com.we_smart.sqldao;

import android.database.Cursor;
import android.database.SQLException;
import android.util.SparseArray;

import com.we_smart.sqldao.Annotation.DBFiled;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础数据库操作类
 * Created by Zhao Liufeng on 2018/4/18.
 */

public class BaseDAO<T> {

    private Class<T> mClazz;

    public BaseDAO(Class<T> clazz) {
        this.mClazz = clazz;
        exeSql(SqlBuilder.getInstance().createTable(mClazz));
    }

    //增
    public boolean insert(T obj) {
        return exeSql(SqlBuilder.getInstance().insertObject(obj));
    }

    //删
    public boolean delete(T obj) {
        return exeSql(SqlBuilder.getInstance().deleteObject(obj));
    }

    //改
    public boolean update(T obj) {
        return exeSql(SqlBuilder.getInstance().updateObject(obj));
    }

    //查
    public List<T> query() {
        return query(null, null);
    }

    public List<T> query(String whereKey, String whereValue) {
        try {
            Cursor cursor = getQueryCursor(whereKey, whereValue);
            List<T> list = new ArrayList<>();
            Field fields[] = mClazz.getFields();
            Field dbFields[] = new Field[cursor.getColumnCount()];
            //过滤不是数据库字段的属性
            for (int i = 0, dbCount = 0; i < fields.length; i++) {
                if (fields[i].isAnnotationPresent(DBFiled.class)) {
                    dbFields[dbCount] = fields[i];
                    dbCount++;
                }
            }
            int index[] = new int[cursor.getColumnCount()];
            for (int i = 0; i < dbFields.length; i++){
                index[i] = cursor.getColumnIndex(dbFields[i].getName());
            }

            //获取下标
            while (cursor.moveToNext()) {
                Object obj = mClazz.newInstance();
                for (int i = 0; i < dbFields.length; i++) {
                    setFieldValue(cursor, index[i], obj, dbFields[i]);
                }
                list.add((T) obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行sql指令
     *
     * @param sql sql指令
     * @return 是否操作成功
     */
    private boolean exeSql(String sql) {
        try {
            DBHelper.getInstance().openDatabase().execSQL(sql);
        } catch (SQLException se) {
            return false;
        }
        return true;
    }

    private Cursor getQueryCursor(String whereKey, String whereValue) {
        return DBHelper.getInstance().openDatabase().query(
                mClazz.getSimpleName(),
                null,
                whereKey == null ? null : String.format("%s=?", whereKey),
                whereValue == null ? null : new String[]{whereValue},
                null,
                null,
                null);
    }

    private void setFieldValue(Cursor cursor, int index, Object obj, Field field) throws IllegalAccessException {
        Class clazz = field.getType();
        if (clazz == String.class) {
            field.set(obj, cursor.getString(index));
        } else if (clazz == Integer.class ||
                clazz == int.class) {
            field.set(obj, cursor.getInt(index));
        }else if (clazz == Boolean.class ||
                clazz == boolean.class){
            field.set(obj, cursor.getInt(index) != 0);
        }
    }
}
