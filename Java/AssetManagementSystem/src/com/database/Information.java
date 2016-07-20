package com.database;

/**
 * 数据库模型，Information(固定资产信息)表，用作增删改查的Where模型
 * Created by li_rz on 2016/7/14.
 */


import java.sql.Types;


public class Information extends DataBase {
    @Override
    protected void initKeyList() {
        keyList.clear();
        keyList.put("id", true); // 固定资产编号
        keyList.put("name", true); // 固定资产名称
        keyList.put("type", true); // 固定资产类别(小类)
        keyList.put("model", true); // 产品型号
        keyList.put("value", true); // 固定资产价值
        keyList.put("purchase_date", true); // 购买日期
        keyList.put("status", true); // 状态 0: 正常 1: 维修 2: 报废
        keyList.put("consumer", false); // 使用者, null为无人使用
        keyList.put("remark", false); // 备注
        need_key_num = 6;
        if_auto_increase_id = false;
    }

    @Override
    protected void initTypeList() {
        typeList.clear();
        typeList.put("id", Types.INTEGER); // 固定资产编号
        typeList.put("name", Types.NVARCHAR); // 固定资产名称
        typeList.put("type", Types.INTEGER); // 固定资产类别(小类)
        typeList.put("model", Types.NVARCHAR); // 产品型号
        typeList.put("value", Types.INTEGER); // 固定资产价值
        typeList.put("purchase_date", Types.DATE); // 购买日期
        typeList.put("status", Types.INTEGER); // 状态 0: 正常 1: 维修 2: 报废
        typeList.put("consumer", Types.NVARCHAR); // 使用者, null为无人使用
        typeList.put("remark", Types.NVARCHAR); // 备注
    }

    @Override
    public String getDataBaseName() {
        return "Information";
    }

    /**
     * 初始化Information模型
     * @param keys - 键值集合
     * @param values - 值集合
     * @throws Exception - 键值与值不能一一对应或者传入不存在的行
     */
    public Information(String[] keys, String[] values) throws IllegalArgumentException{
        super(keys, values);
    }

    public Information () {
        super();
    }
}
