package com.we_smart.test;

import com.we_smart.test.model.ClassA;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Created by zhaol on 2018/3/21.
 */
public class AddFuncationTest {

    AddFuncation funcation;

    @Before
    public void setUp() {
        funcation = new AddFuncation();
        ClassA a = (ClassA) printClassInfo(ClassA.class);
        System.out.println(a.getVal());
    }

    @Test
    public void sum() throws Exception {
        assertEquals(funcation.sum(2, 2), 4);
    }

    @Test
    public void sum2() throws Exception {
        assertEquals(funcation.sum(1, 2), 3);
    }

    public Object printClassInfo(Class clazz) {
        Object obj = null;
        try {
            obj = clazz.newInstance();
            Method[] methods = clazz.getMethods();
            for (Method me : methods) {
                System.out.println(me.toString());
                if (me.getName().equals("setVal")) {
                    me.invoke(obj, 20);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}