package com.we_smart.sqldao;

import com.we_smart.sqldao.Annotation.DBFiled;

import java.lang.reflect.Field;

/**
 * Created by zhaol on 2018/4/19.
 */

class SqlType {

    String getStringType(Field field) {
        String clazzType = field.getType().getSimpleName();
        switch (clazzType) {
            case "int":
                return "INTEGER";
            case "String":
                //判断是否需要长文本
                if (field.getAnnotation(DBFiled.class).isText()){
                    return "TEXT";
                }
                return "VARCHAR";
            default:
                return clazzType;
        }
    }
}
