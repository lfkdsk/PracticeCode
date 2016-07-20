package com.company;

import com.database.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * Created by li-rz on 2016/7/12.
 */

/**
 *
 * Modified by li-rz on 2016/7/18.
 */

/**
 *
 * Modified by lfk_dsk on 2016/7/18.
 */
public class SQL {
    // https://msdn.microsoft.com/zh-cn/library/ms378914.aspx

    private Connection connection; // https://msdn.microsoft.com/zh-cn/library/ff427223.aspx
    // private static Statement statement; // https://msdn.microsoft.com/zh-cn/library/ms378582.aspx
    private ResultSet resultSet; // https://msdn.microsoft.com/zh-cn/library/ms378188.aspx
    private PreparedStatement preparedStatement; // https://msdn.microsoft.com/zh-cn/library/ms378190.aspx
    //step1 加载驱动

    // 默认加载SQL Server
    SQL () throws ClassNotFoundException {
        connection = null;
        resultSet = null;
        preparedStatement = null;
        try
        {
            // 加载驱动
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
        catch(ClassNotFoundException exception)
        {
            Logger.getLogger(String.valueOf(exception));
            throw exception;
        }


    }

    /**
     * 建立连接
     */
    public void connectionDataBase () {
        Config config = new Config();
        try{
            connection = DriverManager
                    .getConnection(config.getUrl(),
                            config.getUsername(),
                            config.getPassword());
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void close () {
        try {
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException nex) {
            Logger.getLogger(String.valueOf(nex));
        }
    }
    /**
     *
     * @param hashMap - 集合
     * @return key and value
     */
    private ArrayList<ArrayList<String>>getKeyAndValue (HashMap<String, String> hashMap) {
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();
        ArrayList<String>keyList = new ArrayList<>(); // 存key
        ArrayList<String> valueList = new ArrayList<>(); // 存value
        for (Map.Entry entry : hashMap.entrySet()) {
            keyList.add((String) entry.getKey());
            valueList.add((String) entry.getValue());
        }
        returnArray.add(keyList);
        returnArray.add(valueList);
        return returnArray;
    }


    /**
     * 获取表模型
     * @param table_type - 表模型编号
     * @return table objects
     */
    private DataBase getDataBaseObject (int table_type) throws Exception {
        switch (table_type) {
                case Table.Admin:
                    return new Admin();

                case Table.Information:
                    return new Information();

                case Table.Lend:
                    return new Lend();

                case Table.Main_Type:
                   return new Main_Type();

                case Table.Back:
                    return new Back();

                case Table.Secondary_Type:
                   return new Secondary_Type();

                case Table.Member:
                    return new Member();

                default:
                    throw new Exception("没有这种类型");
        }
    }

    /**
     * checkDataBaseParameter内嵌执行函数
     * @param dataBase - database 对象
     * @param keyList - key列表
     * @param if_create - 是否是create操作
     * @return true 全部检查通过
     *          false 有出错
     */
    private boolean checkDataBaseParameter (DataBase dataBase,
                                             ArrayList<String>keyList,
                                             boolean if_create) {

        int flag = 0;
        HashMap<String, Boolean>dataBaseKeyList = dataBase.getKeyList();

        for (String key: keyList) {
            if (!dataBaseKeyList.containsKey(key)) {
                return false;
            }

            if (dataBaseKeyList.get(key)) {
                ++flag;
            }
        }
        return !(if_create && flag < dataBase.getNeed_key_num());
    }

    /**
     * 设置SQL语句中的参数
     * @param column_type - 列类型
     * @param value - 获取的值
     * @param parameter_index - 参数编号
     */
    private void setSQLParameter (int column_type,
                                  String value,
                                  int parameter_index) throws SQLException {

        // 未输入数据设为Null
        if (value.length() == 0) {
            preparedStatement.setNull(parameter_index, column_type);
            return;
        }
        // 只用了这几种，所以只写了这几种
        switch (column_type) {
            case Types.VARCHAR:
                preparedStatement.setString(parameter_index, value);
                break;

            case Types.INTEGER:
                preparedStatement.setInt(parameter_index, Integer.parseInt(value));
                break;

            case Types.NVARCHAR:
                preparedStatement.setNString(parameter_index, value);
                break;

            case Types.DATE:

                preparedStatement.setDate(parameter_index, Date.valueOf(value));
                break;

            default:
                break; // throw new SQLException("没有这种类型！！");
        }
    }

    /**
     * 构建WHERE字段
     * @param keyList key list
     * @param sql_sentence sql
     * @param end 终结符
     * @return sql
     */
    private StringBuilder addParameter (ArrayList<String>keyList,
                                 StringBuilder sql_sentence,
                                 String end) {
        Iterator<String> iterator = keyList.iterator();
        while (iterator.hasNext()) {
            sql_sentence.append(" ")
                    .append(iterator.next())
                    .append("=?")
                    .append(iterator.hasNext() ? ", " : end);
        }
        return sql_sentence;
    }

    /**
     *
     * @param keyList key list
     * @param valueList value list
     * @param table DataBase Module Objects
     * @param start_index start index
     * @return index or -1
     */
    private int addValue (
            ArrayList<String> keyList,
            ArrayList<String>valueList,
            DataBase table,
            int start_index) {
        HashMap<String, Integer>tableType = table.getTypeList();
        int index;

        for (index = 0; index < keyList.size(); ++index) {
            int column_type = tableType.get( keyList.get(index) );
            try {
                setSQLParameter(column_type,
                        valueList.get(index),
                        index + 1 + start_index);
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return index;
    }

    /**
     * 更新数据库内表
     * @param selectData 选定数据
     * @param renewData 查询字段，使用HashMap存储
     * @param table_type 数据库表名
     * @return true - 更新成功
     *          false - 更新失败
     */
    public boolean update (HashMap<String, String>selectData,
                           HashMap<String, String>renewData,
                           int table_type) {

        if (selectData.size() == 0 || renewData.size() == 0) {
            return true;
        }
        // 选择集合
        ArrayList<ArrayList<String>>allSelectData = getKeyAndValue(selectData);
        ArrayList<String> selectKeyList = allSelectData.get(0);
        ArrayList<String> selectValueList = allSelectData.get(1);

        // 更新集合
        ArrayList<ArrayList<String >> allRenewData = getKeyAndValue(renewData);
        ArrayList<String>renewKeyList = allRenewData.get(0); // 存key
        ArrayList<String>renewValueList = allRenewData.get(1); // 存value

        DataBase table;
        try {
            table = getDataBaseObject(table_type);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // check out parameter
        if (checkDataBaseParameter(table, renewKeyList, false)
                && checkDataBaseParameter(table, selectKeyList, false)) {

            // 构建SQL语句
            StringBuilder sql_sentence = new StringBuilder(
                            "UPDATE " +
                            table.getDataBaseName() +
                            " SET");
            sql_sentence = addParameter(renewKeyList, sql_sentence, " WHERE");
            Iterator<String>iterator = selectKeyList.iterator();
            while (iterator.hasNext()) {
                sql_sentence.append(" ")
                        .append(iterator.next())
                        .append("=?")
                        .append(iterator.hasNext() ? " AND " : ";");
            }
//            sql_sentence = addParameter(selectKeyList, sql_sentence, ";");

            Logger.getLogger(String.valueOf(sql_sentence));
            try {
                preparedStatement = connection.prepareStatement(sql_sentence.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    preparedStatement.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                return false;
            }


            // 传参
            HashMap<String, Integer>tableType = table.getTypeList();

            int renew_iterator = addValue(renewKeyList, renewValueList, table, 0);

            if (renew_iterator == -1) {
                return false;
            }

            if (addValue(selectKeyList,
                    selectValueList,
                    table,
                    renew_iterator) == -1) {
                return false;
            }


            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        return false;
    }

    /**
     * 查询数据库
     * @param hashMap 查询的Where字段，使用HashMap存储
     * @param table_type 数据库表名
     * @param columnList 查询的列
     * @return 查询结果 or Null {查不到或者错误}
     */
    public ArrayList<HashMap<String, String>> query(
            HashMap<String, String> hashMap,
            int table_type,
            ArrayList<String> columnList) throws SQLException {

        ArrayList<HashMap<String, String>> returnData = new ArrayList<>();

        ArrayList<ArrayList<String >> allData = getKeyAndValue(hashMap);
        ArrayList<String>keyList = allData.get(0); // 存key
        ArrayList<String> valueList = allData.get(1); // 存value


        DataBase table;
        try {
            table = getDataBaseObject(table_type);
        } catch (Exception e) {
            e.printStackTrace();
            return returnData;
        }
        // check out parameter
        if (checkDataBaseParameter(table, keyList, false)) {

            // 构造SQL语句
            StringBuilder sql_sentence = new StringBuilder("SELECT ");
            if (columnList.size() == 0) {
                sql_sentence.append("* ");
            } else if (checkDataBaseParameter(table, columnList, false)) {
                sql_sentence = addParameter(keyList, sql_sentence, "");
            } else {
                return returnData;
            }

            sql_sentence.append("FROM ")
                    .append(table.getDataBaseName());
            if (hashMap.size() != 0) {
                sql_sentence.append(" WHERE");
                Iterator<String> iterator = keyList.iterator();
                while (iterator.hasNext()) {
                    sql_sentence.append(" ")
                            .append(iterator.next())
                            .append("=?")
                            .append(iterator.hasNext() ? " AND " : ";");
                }
            } else {
                sql_sentence.append(";");
            }
            // System.out.println(sql_sentence.toString());
            try {
                preparedStatement = connection.prepareStatement(sql_sentence.toString());
            } catch (SQLException e) {
                try {
                    preparedStatement.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }

            addValue(keyList, valueList, table, 0);
            try {
                resultSet = preparedStatement.executeQuery();
                // System.out.println(resultSet.next());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            HashMap<String, Integer>typeList = table.getTypeList();
            // System.out.println(table.getKeyList());
            while (resultSet.next()) {
                HashMap<String, String>insertData = new HashMap<>();
                for (String key :typeList.keySet()) {
                    insertData.put(key, resultSet.getString(key));
                }
                returnData.add(insertData);
            }
            preparedStatement.close();
            resultSet.close();
        }
        return returnData;
    }

    /**
     * 增加行数，往指定表中
     * @param hashMap 查询字段，使用HashMap存储
     * @param table_type 数据库表名
     * @return true - 创建成功
     *          false - 创建失败
     */
    public boolean create (
            HashMap<String, String> hashMap,
            int table_type) throws SQLException {

        ArrayList<ArrayList<String >> allData = getKeyAndValue(hashMap);
        ArrayList<String>keyList = allData.get(0); // 存key
        ArrayList<String> valueList = allData.get(1); // 存value

        DataBase table;
        try {
            table = getDataBaseObject(table_type);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // check out parameter
        if (checkDataBaseParameter(table, keyList, true)) {
            StringBuilder sql_sentence = new StringBuilder(
                            "INSERT INTO " +
                            table.getDataBaseName() +
                                    " (");
            Iterator<String>iterator = keyList.iterator();
            while (iterator.hasNext()) {
                sql_sentence
                        .append(iterator.next())
                        .append(iterator.hasNext() ? "," : ")");
            }
            sql_sentence.append(" VALUES (");
            for (int i = 0; i < keyList.size(); ++i) {
                sql_sentence
                        .append("?")
                        .append(i < keyList.size() - 1 ? "," : ");");
            }
            Logger.getLogger(String.valueOf(sql_sentence));
            try {
                preparedStatement = connection.prepareStatement(sql_sentence.toString());
            } catch (SQLException e) {
                try {
                    preparedStatement.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                return false;
            }
            addValue(keyList, valueList, table, 0);

            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                preparedStatement.close();
            }
            return true;
        }
        return false;
    }

    /**
     * 删除行
     * @param hashMap 查询字段，使用HashMap存储
     * @param table_type 数据库表名
     * @return true - 删除成功
     *          false - 删除失败
     */
    public boolean delete (HashMap<String, String> hashMap,
                           int table_type) throws SQLException {
        ArrayList<ArrayList<String >> allData = getKeyAndValue(hashMap);
        ArrayList<String>keyList = allData.get(0); // 存key
        ArrayList<String> valueList = allData.get(1); // 存value

        DataBase table;
        try {
            table = getDataBaseObject(table_type);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // check out parameter
        if (checkDataBaseParameter(table, keyList, false)) {
            StringBuilder sql_sentence = new StringBuilder("DELETE FROM " +
                    table.getDataBaseName());

            if (hashMap.size() != 0) {
                sql_sentence.append(" WHERE");
                Iterator<String>iterator = keyList.iterator();
                while (iterator.hasNext()) {
                    sql_sentence
                            .append(" ")
                            .append(iterator.next())
                            .append("=?")
                            .append(iterator.hasNext() ? " AND" : ";");
                }
            } else {
                sql_sentence.append(";");
            }

            Logger.getLogger(String.valueOf(sql_sentence));

            try {
                preparedStatement = connection.prepareStatement(sql_sentence.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                preparedStatement.close();
                return false;
            }
            addValue(keyList, valueList, table, 0);
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            } finally {
                preparedStatement.close();
            }
            return true;
        }
        return false;
    }

    // 测试用
    public static void main (String... args) throws ClassNotFoundException {
    }

}
