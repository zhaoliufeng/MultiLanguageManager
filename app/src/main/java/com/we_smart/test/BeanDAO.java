package com.we_smart.test;

import com.we_smart.sqldao.BaseDAO;
import com.we_smart.test.model.SqlTestBean;

import java.util.List;

/**
 * Created by zhaol on 2018/4/20.
 */

public class BeanDAO extends BaseDAO<SqlTestBean> {

    public BeanDAO() {
        super(SqlTestBean.class);
    }

    public List<SqlTestBean> getListById(int id){
        return query("groupId", String.valueOf(id));
    }
}
