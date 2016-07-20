package com.database;

import java.util.HashMap;

/**
 * 数据库模型接口，基类
 * Created by li_rz on 2016/7/14.
 */

/**
 * 0: INT
 * 1: VARCHAR
 * 2: NVARCHAR
 * 3: TEXT
 * 4: DATE
 */
public abstract class DataBase {
    private static HashMap<String, String> module = new HashMap<>();
    static HashMap<String, Boolean> keyList = new HashMap<>();
    static HashMap<String, Integer> typeList = new HashMap<>();
    static int need_key_num;
    static boolean if_auto_increase_id;

    /**
     * 初始化
     * @param keys - 键值
     * @param values - 值
     * @throws IllegalArgumentException - 键值与值不能一一对应或者传入不存在的行
     */
    public DataBase(String[] keys, String[] values) throws IllegalArgumentException {
        initKeyList();
        initTypeList();
        if (keys.length != values.length) {
            throw new IllegalArgumentException("键值与值必须一一对应！！！");
        }
        for (int i = 0; i < values.length; ++i) {
            if (keyList.containsKey(keys[i])) {
                module.put(keys[i], values[i]);
            } else {
                throw new IllegalArgumentException("数据库里没有" + keys[i] + "行");
            }
        }
    }

    /**
     * 获取数据库键值对和类型
     */
    public DataBase() {
        initKeyList();
        initTypeList();
    }

    /**
     * 初始化KeyList，存储数据库内有的列名，并且确认其是否是必要项目
     */
    protected abstract void initKeyList();

    /**
     * 初始化typeList，存储数据库内列的类型
     */
    protected abstract void initTypeList();

    public abstract String getDataBaseName();

    /**
     * 获取查询模型
     * @return module
     */
    public HashMap<String, String>getModule () {
        return module;
    }

    /**
     * 获取键值对表
     * @return keyList
     */
    public HashMap<String, Boolean>getKeyList () {
        return keyList;
    }

    /**
     * 获取类型表
     * @return typeList
     */
    public HashMap<String, Integer>getTypeList () {
        return typeList;
    }

    public int getNeed_key_num () {
        return need_key_num;
    }

    public boolean getIfAutoIncreaseId () {
        return if_auto_increase_id;
    }
}
