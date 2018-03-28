package com.we_smart.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zhaol on 2018/3/21.
 */
public class AddFuncationTest {

    AddFuncation funcation;

    @Before
    public void setUp() {
        funcation = new AddFuncation();
        String s = "ab_cd";
        System.out.println(s.substring(0, 2));
        System.out.print(s.substring(3, 5));
    }

    @Test
    public void sum() throws Exception {
        assertEquals(funcation.sum(2,2), 4);
    }

    @Test
    public void sum2() throws Exception{
        assertEquals(funcation.sum(1,2), 3);
    }
}