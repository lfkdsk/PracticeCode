package com.database;

/**
 *
 * Created by li_rz on 2016/7/14.
 */

/**
 *
 * Modified by li_rz on 2016/7/18.
 */

import java.sql.Types;


public class Lend extends DataBase {
    /**
     * 初始化
     *
     * @param keys   - 键值
     * @param values - 值
     * @throws Exception - 键值与值不能一一对应或者传入不存在的行
     */
    public Lend(String[] keys, String[] values) throws IllegalArgumentException {
        super(keys, values);
    }

    public Lend() {
        super();
    }

    @Override
    protected void initKeyList() {
        keyList.clear();
        keyList.put("id", true);
        keyList.put("device_number", true);
        keyList.put("admin", true);
        keyList.put("purpose", true);
        keyList.put("remark", false);
        keyList.put("date", true);
        need_key_num = 4;
        if_auto_increase_id = true;
    }

    @Override
    protected void initTypeList() {
        typeList.clear();
        typeList.put("id", Types.INTEGER);
        typeList.put("device_number", Types.INTEGER);
        typeList.put("admin", Types.INTEGER);
        typeList.put("purpose", Types.NVARCHAR);
        typeList.put("remark", Types.NVARCHAR);
        typeList.put("date", Types.DATE);
    }

    @Override
    public String getDataBaseName() {
        return "Lend";
    }
}
