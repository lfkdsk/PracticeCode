package com.database;

/**
 * 数据库模型，Main_Type（固定资产大类）表，用作增删改查的Where模型
 * Created by li_rz on 2016/7/13.
 */


import java.sql.Types;

/**
 * type_name => NVARCHAR
 */
public class Main_Type extends DataBase {
    // private HashMap<String, String> module = new HashMap<>();
    // private HashSet<String> keyList = new HashSet<>();

    @Override
    protected void initKeyList () {
        keyList.clear();
        keyList.put("id", true);
        keyList.put("type_name", true);
        need_key_num = 1;
        if_auto_increase_id = true;
    }

    @Override
    protected void initTypeList() {
        typeList.clear();
        typeList.put("id", Types.INTEGER);
        typeList.put("type_name", Types.NVARCHAR);
    }

    @Override
    public String getDataBaseName() {
        return "Main_Type";
    }

    /**
     * 初始化Main_Type模型
     * @param keys - 键值集合
     * @param values - 值集合
     * @throws Exception - 键值与值不能一一对应或者传入不存在的行
     */
    public Main_Type(String[] keys, String[] values) throws IllegalArgumentException{
        super(keys, values);
    }

    public Main_Type () {
        super();
    }
}
