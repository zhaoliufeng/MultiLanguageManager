package com.we_smart.sqldao;

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
                sb.append(",");
            }
        }
        //删除最后一个多余的，
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    //生成插入数据sql语句
    String insertObject(Object obj) {
        try {
            Field fields[] = obj.getClass().getFields();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("INSERT INTO %s VALUES(", obj.getClass().getSimpleName()));
            for (Field field : fields) {
                if (field.isAnnotationPresent(DBFiled.class)) {
                    sb.append(getValueAppend(field, obj)).append(",");
                }
            }
            //删除最后一个多余的，
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            return sb.toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    //生成删除数据sql语句 删除的关键字是primary key
    String deleteObject(Object obj) {
        try {
            Field fields[] = obj.getClass().getFields();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("DELETE FROM %s WHERE ", obj.getClass().getSimpleName()));
            for (Field field : fields) {
                if (field.isAnnotationPresent(DBFiled.class)) {
                    if (field.getAnnotation(DBFiled.class).isPrimary()) {
                        sb.append(getKeyValueAppend(field, obj));
                    }
                }
            }
            return sb.toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
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
                        if (keyObj.getClass() == String.class){
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
        Class clazz = field.get(obj).getClass();
        StringBuilder sb = new StringBuilder();
        sb.append(field.getName())
                .append(" = ");
        //如果是字符串 sql语句中则需要加上“”
        if (clazz == String.class) {
            sb.append("\"").append(field.get(obj)).append("\"");
        }else {
            sb.append(field.get(obj));
        }
        return sb.toString();
    }

    //获取插入值文字
    private String getValueAppend(Field field, Object obj) throws IllegalAccessException {
        Class clazz = field.get(obj).getClass();
        StringBuilder sb = new StringBuilder();
        //如果是字符串 sql语句中则需要加上“”
        if (clazz == String.class) {
            sb.append("\"").append(field.get(obj)).append("\"");
        }else {
            sb.append(field.get(obj));
        }
        return sb.toString();
    }
}
