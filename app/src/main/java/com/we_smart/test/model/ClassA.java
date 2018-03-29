package com.we_smart.test.model;

/**
 * Created by zhaol on 2018/3/29.
 */

public class ClassA {

    public int val = 0;

    public String getString(){
        return "A";
    }

    public void setVal(int v){
        System.out.println("设置了Val " + v);
        this.val = v;
    }

    public int getVal(){
        return val;
    }
}
