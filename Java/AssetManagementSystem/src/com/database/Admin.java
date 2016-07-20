package com.database;

/**
 * 数据库模型，Admin（管理员）表，用作增删改查的Where模型
 * Created by li_rz on 2016/7/14.
 */


import java.sql.Types;


public class Admin extends DataBase{

    @Override
    protected void initKeyList() {
        keyList.clear();
        keyList.put("id", true);
        keyList.put("name", true); // 管理员用户名
        keyList.put("pwd", true); // 管理员密码
        keyList.put("power", true); // 管理员权力
        need_key_num = 3;
        if_auto_increase_id = true;
    }

    @Override
    protected void initTypeList() {
        typeList.clear();
        typeList.put("name", Types.VARCHAR);
        typeList.put("pwd", Types.VARCHAR);
        typeList.put("power", Types.INTEGER);
        typeList.put("id", Types.INTEGER);
    }

    @Override
    public String getDataBaseName() {
        return "Admin";
    }

    /**
     * 初始化Admin模型
     * @param keys - 键值集合
     * @param values - 值集合
     * @throws Exception - 键值与值不能一一对应或者传入不存在的行
     */
    public Admin(String[] keys, String[] values) throws IllegalArgumentException{
        super(keys, values);
    }

    public Admin() {
        super();
    }
}
