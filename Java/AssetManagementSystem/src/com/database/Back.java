package com.database;

/**
 * 数据库模型，Back(资产归还)表，用作增删改查的Where模型
 * Created by li_rz on 2016/7/14.
 */


/**
 *
 * Modified by li_rz on 2016/7/14.
 */

import java.sql.Types;

/**
 * device_number => INT
 * admin => INT
 * date => DATE
 */
public class Back extends DataBase{

    /**
     * 初始化
     *
     * @param keys   - 键值
     * @param values - 值
     * @throws Exception - 键值与值不能一一对应或者传入不存在的行
     */
    public Back(String[] keys, String[] values) throws IllegalArgumentException {
        super(keys, values);
    }

    public Back() {
        super();
    }

    @Override
    protected void initKeyList() {
        keyList.clear();
        keyList.put("id", true);
        keyList.put("device_number", true);
        keyList.put("admin", true);
        keyList.put("date", true);
        need_key_num = 3;
        if_auto_increase_id = true;
    }

    @Override
    protected void initTypeList() {
        typeList.clear();
        typeList.put("id", Types.INTEGER);
        typeList.put("device_number", Types.INTEGER);
        typeList.put("admin", Types.INTEGER);
        typeList.put("date", Types.DATE);
    }

    @Override
    public String getDataBaseName() {
        return "Back";
    }
}
