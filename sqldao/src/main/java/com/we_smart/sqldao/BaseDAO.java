package com.we_smart.sqldao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    private T mObj;

    protected BaseDAO(Class<T> clazz) {
        this.mClazz = clazz;
    }

    /**
     * 新建表 表名是当前实体类的类名
     */
    public void createTable(SQLiteDatabase database) {
        database.execSQL(SqlBuilder.getInstance().createTable(mClazz));
    }

    //增
    protected boolean insert(T obj) {
        return SqlBuilder.getInstance().insertObject(obj,
                DBHelper.getInstance().openDatabase());
    }

    //删
    protected boolean delete(T obj, String[] whereKey, String[] whereValue) {
        return SqlBuilder.getInstance().deleteObject(obj,
                DBHelper.getInstance().openDatabase(),
                whereKey, whereValue);
    }

    //删
    protected BaseDAO delete(T obj) {
        mObj = obj;
        return this;
    }

    protected boolean update(T obj, String[] whereKey, String[] whereValue) {
        return SqlBuilder.getInstance().updateObject(obj,
                DBHelper.getInstance().openDatabase(),
                whereKey, whereValue);
    }

    //查
    protected List<T> query(String[] whereKey, String[] whereValue) {
        List<T> list = new ArrayList<>();
        try {
            Cursor cursor = getQueryCursor(whereKey, whereValue);
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
            for (int i = 0; i < dbFields.length; i++) {
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
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //判断某个值是否存在
    public boolean isExists(String key, String value) {
        return query(new String[]{key}, new String[]{value}).size() != 0;
    }

    private Cursor getQueryCursor(String[] whereKey, String[] whereValue) {
        return DBHelper.getInstance().openDatabase().query(
                mClazz.getSimpleName(),
                null,
                whereKey == null ? null : getSelection(whereKey),
                whereValue,
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
        } else if (clazz == Boolean.class ||
                clazz == boolean.class) {
            field.set(obj, cursor.getInt(index) != 0);
        } else if (clazz == Long.class ||
                clazz == long.class) {
            field.set(obj, cursor.getLong(index));
        }
    }

    //获取查询时的AND字符串
    private String getSelection(String[] whereKey) {
        StringBuilder selection = new StringBuilder();
        if (whereKey != null) {
            for (int i = 0; i < whereKey.length; i++) {
                if (i < whereKey.length - 1) {
                    selection.append(String.format("%s=?", whereKey[i])).append(" AND ");
                } else {
                    selection.append(String.format("%s=?", whereKey[i]));
                }

            }
        }
        return selection.toString();
    }
}
