package com.we_smart.sqldao;

import org.junit.Test;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        String sql = SqlBuilder.getInstance().createTable(TestBean.class);
        TestBean bean = new TestBean();
        System.out.println(sql);
        bean.id = "1";
        bean.name = "aa";
        bean.boo = true;
        System.out.println(SqlBuilder.getInstance().insertObject(bean));
        System.out.println(SqlBuilder.getInstance().deleteObject(bean));
        System.out.println(SqlBuilder.getInstance().updateObject(bean));
    }
}