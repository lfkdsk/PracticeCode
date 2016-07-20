package com.database;

/**
 * 数据库模型，Member(人员信息)表，用作增删改查的Where模型
 * Created by li_rz on 2016/7/14.
 */

/**
 * 数据库模型，Member(人员信息)表，用作增删改查的Where模型
 * Modified by li_rz on 2016/7/17.
 */

import java.sql.Types;

/**
 * id => INT
 * name => NVARCHAR
 * resign => NVARCHAR
 * remark => TEXT
 */
public class Member extends DataBase {
    @Override
    protected void initKeyList() {
        keyList.clear();
        keyList.put("id", true); // 工号
        keyList.put("name", true); // 姓名
        keyList.put("resign", true); // 职务
        keyList.put("remark", false); // 备注
        need_key_num = 3;
        if_auto_increase_id = false;
    }

    @Override
    protected void initTypeList() {
        typeList.clear();
        typeList.put("id", Types.INTEGER);
        typeList.put("name", Types.NVARCHAR);
        typeList.put("resign", Types.NVARCHAR);
        typeList.put("remark", Types.NVARCHAR);
    }

    @Override
    public String getDataBaseName() {
        return "Member";
    }

    public Member(String[] keys, String[] values) throws IllegalArgumentException {
        super(keys, values);
    }

    public Member() {
        super();
    }
}
