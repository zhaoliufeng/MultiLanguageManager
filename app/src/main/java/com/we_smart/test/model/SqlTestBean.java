package com.we_smart.test.model;

import com.we_smart.sqldao.Annotation.DBFiled;

/**
 * Created by Zhao Liufeng on 2018/4/20.
 */

public class SqlTestBean {
    @DBFiled(isPrimary = true)
    public int id;
    @DBFiled()
    public String name;
    @DBFiled()
    public int groupId;
    @DBFiled()
    public boolean boo = false;
    public String useless;
}
