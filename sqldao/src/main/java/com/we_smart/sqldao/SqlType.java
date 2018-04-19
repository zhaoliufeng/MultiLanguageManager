package com.we_smart.sqldao;

/**
 * Created by zhaol on 2018/4/19.
 */

public class SqlType {

    public String getStringType(Class<?> clazz) {
        String clazzType = clazz.getSimpleName();
        switch (clazzType) {
            case "int":
                return "INTEGER";
            case "String":
                return "VARCHAR";
            default:
                return clazzType;
        }
    }
}
