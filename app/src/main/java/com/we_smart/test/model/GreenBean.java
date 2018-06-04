package com.we_smart.test.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by zhaol on 2018/5/30.
 */

@Entity()
public class GreenBean {
    @Id
    private Long id;
    private String name;

}
