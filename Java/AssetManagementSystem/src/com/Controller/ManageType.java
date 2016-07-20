package com.Controller;

import com.company.CompanyInputType;
import com.company.CompanyInterface;
import com.company.SQL;
import com.company.Table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

/**
 * 固定资产类别管理
 * Created by lfk_dsk on 2016/7/18.
 */
public class ManageType extends Controller {
    public ManageType(Scanner scanner, SQL sql) {
        super(scanner, sql);
    }

    /**
     * 处理类型管理事件
     */
    @Override
    public void manage (CompanyInterface companyInterface) {
        companyInterface.showTypeManageChoice ();
        int type = getType(companyInterface);

        switch (type) {
            case CompanyInputType.MainType:
            case CompanyInputType.SecondaryType:
                manageTypeOperator(companyInterface, type);
                break;

            case CompanyInputType.SystemExit:
                break;

            default:
                companyInterface.illegalInput();
                break;
        }
    }

    /**
     * 类别增删操作
     * @param companyInterface CompanyInterface
     * @param typeNum MainType or SecondaryType
     */
    private void manageTypeOperator (CompanyInterface companyInterface, int typeNum) {
        companyInterface.showTypeInsertAndDelete();
        int type = getType(companyInterface);


        if (type != CompanyInputType.OperatorDelete &&
                type != CompanyInputType.OperatorInsert &&
                type != CompanyInputType.SystemExit) {
            companyInterface.illegalInput();
        } else if (type == CompanyInputType.OperatorInsert) {
            _manageTypeOperatorInsert(companyInterface, typeNum);
        } else if (type == CompanyInputType.OperatorDelete) {
            _manageTypeOperatorDelete(companyInterface, typeNum);
        }
    }

    /**
     * 内嵌插入
     * @param companyInterface CompanyInterface
     * @param typeNum MainType or SecondaryType
     */
    private void _manageTypeOperatorInsert (CompanyInterface companyInterface, int typeNum) {
        HashMap<String, String> hashMap = new HashMap<>();
        companyInterface.showInsertNameClue();

        scanner.nextLine();
        hashMap.put("type_name", scanner.nextLine());
        // scanner.nextLine();

        // 如果是小类
        if (typeNum == CompanyInputType.SecondaryType) {
            companyInterface.showInsertMainType();
            HashMap<String, String>queryMap = new HashMap<>();
            ArrayList<String> columnList = new ArrayList<>();
            queryMap.put("type_name", scanner.nextLine());
            // scanner.nextLine();

            ArrayList<HashMap<String, String>> queryResult = new ArrayList<>();
            try {
                queryResult = sql.query(queryMap, Table.Main_Type, columnList);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (queryResult.isEmpty()) {
                companyInterface.showNotThisType();
                return;
            }

            String main_type = queryResult.get(0).get("id");
            hashMap.put("main_type", main_type);
            queryResult.clear();
        }
        try {
            sql.create(hashMap,
                    (typeNum == CompanyInputType.SecondaryType ?
                            Table.Secondary_Type : Table.Main_Type));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 内嵌删除
     * @param companyInterface CompanyInterface
     * @param typeNum MainType or SecondaryType
     */
    private void _manageTypeOperatorDelete (CompanyInterface companyInterface, int typeNum) {
        companyInterface.showtypeDeleteWarning();
        scanner.nextLine();
        String result = scanner.nextLine();
        if (!Objects.equals(result, "Y") &&
                !Objects.equals(result, "y")) {
            return;
        }

        HashMap<String, String>hashMap = new HashMap<>();
        ArrayList<String>emptyArrayList = new ArrayList<>();
        ArrayList<HashMap<String, String>>returnDataList = null;
        companyInterface.showDeleteNameClue();
        hashMap.put("type_name", scanner.nextLine());

        // 如果是大类
        if (typeNum == CompanyInputType.MainType) {

            try {
                returnDataList = sql.query(hashMap, Table.Main_Type, emptyArrayList);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            HashMap<String, String>mainTypeIdMap = new HashMap<>();
            mainTypeIdMap.put("main_type", returnDataList.get(0).get("id"));
            returnDataList.clear();
            try {
                returnDataList = sql.query(mainTypeIdMap, Table.Secondary_Type, emptyArrayList);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            // 删除所有信息
            __deleteInformation(returnDataList);

            // 删除小类
            try {
                sql.delete(mainTypeIdMap, Table.Secondary_Type);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // 删除大类
            try {
                sql.delete(hashMap, Table.Main_Type);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else if (typeNum == CompanyInputType.SecondaryType) {
            try {
                returnDataList = sql.query(hashMap, Table.Secondary_Type, emptyArrayList);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // 删除所有信息
            __deleteInformation(returnDataList);

            try {
                sql.delete(hashMap, Table.Secondary_Type);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void __deleteInformation (ArrayList<HashMap<String, String>>returnDataList) {
        for (HashMap<String, String> values: returnDataList) {
            HashMap<String, String>deleteMap = new HashMap<>();
            deleteMap.put("type", values.get("id"));
            try {
                sql.delete(deleteMap, Table.Information);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
