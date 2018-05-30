package com.we_smart.sqldao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.we_smart.sqldao.Annotation.DBFiled;

import java.lang.reflect.Field;

/**
 * 生成可执行的sql语句
 * Created by zhaol on 2018/4/19.
 */

class SqlBuilder {

    private static SqlBuilder mInstance;
    private static SqlType mSqlType;

    private SqlBuilder() {
    }

    static SqlBuilder getInstance() {
        if (mInstance == null) {
            mInstance = new SqlBuilder();
            mSqlType = new SqlType();
        }
        return mInstance;
    }

    //生成创建sql数据表sql语句 表名为当前类的类名
    String createTable(Class<?> clazz) {
        Field fields[] = clazz.getFields();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("CREATE TABLE IF NOT EXISTS %s(", clazz.getSimpleName()));
        //添加数据表字段 name dataType key
        for (Field field : fields) {
            if (field.isAnnotationPresent(DBFiled.class)) {
                sb.append(field.getName()).append(" ").append(mSqlType.getStringType(field));
                if (field.getAnnotation(DBFiled.class).isPrimary()) {
                    sb.append(" ").append("PRIMARY KEY");
                }
                if (field.getAnnotation(DBFiled.class).isAutoIncrement()) {
                    sb.append(" ").append("AUTOINCREMENT");
                }
                sb.append(",");
            }
        }
        //删除最后一个多余的，
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    boolean insertObject(Object obj, SQLiteDatabase database) {
        long row;
        try {
            String tableName = obj.getClass().getSimpleName();
            Field fields[] = obj.getClass().getFields();
            ContentValues contentValues = new ContentValues();
            for (Field field : fields) {
                if (field.isAnnotationPresent(DBFiled.class)) {
                    if (field.getAnnotation(DBFiled.class).isAutoIncrement()) {
                        continue;
                    }
                    putValues(contentValues, obj, field);
                }
            }
            row = database.insert(tableName, null, contentValues);
            return row != -1;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean updateObject(Object obj, SQLiteDatabase database,String[] whereKey, String[] whereValue) {
        long row;
        try {
            String tableName = obj.getClass().getSimpleName();
            Field fields[] = obj.getClass().getFields();
            ContentValues contentValues = new ContentValues();
            for (Field field : fields) {
                if (field.isAnnotationPresent(DBFiled.class)) {
                    if (field.getAnnotation(DBFiled.class).isAutoIncrement() ||
                            field.getAnnotation(DBFiled.class).isPrimary()) {
                        continue;
                    }
                    putValues(contentValues, obj, field);
                }
            }
            row = database.update(tableName, contentValues, getSelection(whereKey), whereValue);
            return row != -1;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    //生成删除数据sql语句 删除的关键字是primary key
    boolean deleteObject(Object obj,  SQLiteDatabase database, String[] whereKey, String[] whereValue) {
        String tableName = obj.getClass().getSimpleName();
        return  database.delete(tableName, getSelection(whereKey), whereValue) != -1;
    }

    //生成更新数据sql语句 更新的关键字是primary key
    String updateObject(Object obj) {
        try {
            Field fields[] = obj.getClass().getFields();
            String primaryKey = "";
            String primaryValue = "";
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("UPDATE %s SET ", obj.getClass().getSimpleName()));
            for (Field field : fields) {
                if (field.isAnnotationPresent(DBFiled.class)) {
                    if (!field.getAnnotation(DBFiled.class).isPrimary()) {
                        sb.append(getKeyValueAppend(field, obj)).append(",");
                    } else {
                        primaryKey = field.getName();
                        Object keyObj = field.get(obj);
                        primaryValue = String.valueOf(field.get(obj));
                        if (keyObj.getClass() == String.class) {
                            primaryValue = String.format("\"%s\"", primaryValue);
                        }
                    }
                }
            }
            //删除最后一个多余的，
            sb.deleteCharAt(sb.length() - 1);
            sb.append(String.format(" WHERE %s = %s", primaryKey, primaryValue));
            return sb.toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    //获取键值对文字
    private String getKeyValueAppend(Field field, Object obj) throws IllegalAccessException {
        Class clazz = field.getType();
        StringBuilder sb = new StringBuilder();
        sb.append(field.getName())
                .append(" = ");
        //如果是字符串 sql语句中则需要加上“”
        if (clazz == String.class) {
            sb.append("\"").append(field.get(obj)).append("\"");
        } else {
            sb.append(field.get(obj));
        }
        return sb.toString();
    }

    //获取插入值文字
    private String getValueAppend(Field field, Object obj) throws IllegalAccessException {
        Class clazz = field.getType();
        StringBuilder sb = new StringBuilder();
        //如果是字符串 sql语句中则需要加上“”
        if (clazz == String.class) {
            sb.append("\"").append(field.get(obj)).append("\"");
        } else {
            sb.append(field.get(obj));
        }
        return sb.toString();
    }

    private void putValues(ContentValues contentValues, Object obj, Field field) throws IllegalAccessException {
        if (isString(field.getType())) {
            contentValues.put(field.getName(), String.valueOf(field.get(obj)));
        } else if (isInteger(field.getType())) {
            contentValues.put(field.getName(), (Integer) field.get(obj));
        } else if (isLong(field.getType())) {
            contentValues.put(field.getName(), (Long) field.get(obj));
        } else if (isBoolean(field.getType())) {
            contentValues.put(field.getName(), (Boolean) field.get(obj));
        } else if (isFloat(field.getType())) {
            contentValues.put(field.getName(), (Float) field.get(obj));
        }
    }

    private boolean isInteger(Class<?> clazz) {
        return clazz == int.class ||
                clazz == Integer.class ||
                clazz == short.class ||
                clazz == Short.class ||
                clazz == Byte.class ||
                clazz == byte.class;
    }

    private boolean isLong(Class<?> clazz) {
        return clazz == long.class ||
                clazz == Long.class;
    }

    private boolean isFloat(Class<?> clazz) {
        return clazz == Float.class ||
                clazz == float.class ||
                clazz == Double.class ||
                clazz == double.class;
    }

    private boolean isString(Class<?> clazz) {
        return clazz == String.class;
    }

    private boolean isBoolean(Class<?> clazz) {
        return clazz == boolean.class ||
                clazz == Boolean.class;
    }

    private String getSelection(String[] whereKey){
        StringBuilder selection = new StringBuilder();
        if (whereKey != null){
            for (int i = 0; i<whereKey.length;i++) {
                if (i < whereKey.length - 1){
                    selection.append(String.format("%s=?", whereKey[i])).append(" AND ");
                }else{
                    selection.append(String.format("%s=?", whereKey[i]));
                }

            }
        }
        return selection.toString();
    }
}
