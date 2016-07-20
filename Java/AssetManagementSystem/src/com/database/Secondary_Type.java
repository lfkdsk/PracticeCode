package com.database;

/**
 * 数据库模型，Secondary_Type(固定资产小类)表，用作增删改查的Where模型
 * Created by li_rz on 2016/7/14.
 */


import java.sql.Types;

/**
 * main_type => INT
 * type_name => NVARCHAR
 */
public class Secondary_Type extends DataBase{

    /**
     * 初始化
     *
     * @param keys   - 键值
     * @param values - 值
     * @throws Exception - 键值与值不能一一对应或者传入不存在的行
     */
    public Secondary_Type(String[] keys, String[] values) throws IllegalArgumentException {
        super(keys, values);
    }

    public Secondary_Type () {
        super();
    }

    @Override
    protected void initKeyList() {
        keyList.clear();
        keyList.put("id", true);
        keyList.put("main_type", true); // 该小类属于的大类
        keyList.put("type_name", true); // 小类名
        need_key_num = 2;
        if_auto_increase_id = true;
    }

    @Override
    protected void initTypeList() {
        typeList.clear();
        typeList.put("id", Types.INTEGER);
        typeList.put("main_type", Types.INTEGER);
        typeList.put("type_name", Types.NVARCHAR);
    }

    @Override
    public String getDataBaseName() {
        return "Secondary_Type";
    }


}
