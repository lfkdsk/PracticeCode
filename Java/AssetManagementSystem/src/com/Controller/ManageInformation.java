package com.Controller;

import com.company.CompanyInputType;
import com.company.CompanyInterface;
import com.company.SQL;
import com.company.Table;
import com.database.DataBase;
import com.database.Information;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 固定资产信息管理
 * Created by lfk_dsk on 2016/7/18.
 */
public class ManageInformation extends Controller {

    public ManageInformation(Scanner scanner, SQL sql) {
        super(scanner, sql);
    }

    @Override
    public void manage(CompanyInterface companyInterface) {
        companyInterface.showInformationOperator();
        int type = getType(companyInterface);
        switch (type) {
            case CompanyInputType.OperatorInsert:
                _manageInsertInformation(companyInterface);
                break;
            case CompanyInputType.OperatorDelete:
                _manageDeleteInformation(companyInterface);
                break;
            case CompanyInputType.OperatorUpdate:
                _manageUpdateInformation(companyInterface);
                break;
            case CompanyInputType.SystemExit:
                break;
            default:
                companyInterface.illegalInput();
                break;
        }
    }


    /**
     * 信息插入
     * @param companyInterface CompanyInterface
     */
    private void _manageInsertInformation (CompanyInterface companyInterface) {
        scanner.nextLine();
        HashMap<String, String> hashMap = new HashMap<>();

        ArrayList<String> emptyList = new ArrayList<>();

        DataBase dataBase = new Information();
        HashMap<String, Boolean>keyList = new HashMap<>(dataBase.getKeyList());

        for (Map.Entry entry: keyList.entrySet()) {
            companyInterface.showInformationKeyMessage(String.valueOf(entry.getKey()));
            String input = scanner.nextLine();

            if (entry.getValue().equals(true) && input.length() == 0) {
                companyInterface.mustInsert();
                return;
            }
            // 日期合法性
            if (entry.getKey().equals("purchase_date")) {
                Matcher matcher = pattern.matcher(input);
                if (!matcher.matches()) {
                    companyInterface.showIllegalDate();
                    return;
                }
                hashMap.put((String) entry.getKey(), input);
            }
            // 查找种类id
            else if (entry.getKey().equals("type")) {
                // 先查找小类id再进行插入
                ArrayList<HashMap<String, String>>result = null;
                HashMap<String, String>queryMap = new HashMap<>();
                queryMap.put("type_name", input);
                try {
                    result = sql.query(queryMap, Table.Secondary_Type, emptyList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (result == null || result.isEmpty()) {
                    companyInterface.showNotThisType();
                    return;
                }
                String id = result.get(0).get("id");

                hashMap.put((String) entry.getKey(), id);
            }
            else if (entry.getKey().equals("status")) {
                if (input.equals("报废")) {
                    hashMap.put((String) entry.getKey(), "2");
                } else if (input.equals("维修")) {
                    hashMap.put((String) entry.getKey(), "1");
                } else if (input.equals("正常")) {
                    hashMap.put((String) entry.getKey(), "0");
                } else {
                    companyInterface.illegalInput();
                    return;
                }
            }
            // 非必要项目
            else if (entry.getValue().equals(false)) {
                if (input.length() != 0) {
                    hashMap.put((String) entry.getKey(), input);
                }

            } else {
                hashMap.put((String) entry.getKey(), input);
            }
        }

        try {
            sql.create(hashMap, Table.Information);
            companyInterface.insertSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 信息删除
     * @param companyInterface CompanyInterface
     */
    private void _manageDeleteInformation (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showInformationDelete();
        HashMap<String, String>hashMap = new HashMap<>();
        hashMap.put("id", scanner.nextLine());
        try {
            sql.delete(hashMap, Table.Information);
            companyInterface.deleteSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 信息更新
     * @param companyInterface CompanyInterface
     */
    private void _manageUpdateInformation (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showInformationUpdate();
        HashMap<String, String>hashMap = new HashMap<>();
        ArrayList<String>emptyList = new ArrayList<>();
        ArrayList<HashMap<String, String >>result = null;
        hashMap.put("id", scanner.nextLine());
        try {
            result = sql.query(hashMap, Table.Information, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if (result == null || result.isEmpty()) {
            companyInterface.notThisInformation();
            return;
        }

        HashMap<String, String>selectMap = new HashMap<>();
        selectMap.put("id", result.get(0).get("id"));

        companyInterface.showInformationUpdateType();
        int type = getType(companyInterface);
        scanner.nextLine();

        // 更新
        companyInterface.showInformationUpdateClue();
        String input = scanner.nextLine();
        String key = "";
        HashMap<String, String>renewMap = new HashMap<>();
        switch (type) {
            case CompanyInputType.InformationId:
                key = "id";
                break;

            case CompanyInputType.InformationName:
                key = "name";
                break;

            case CompanyInputType.InformationType:
                key = "type";
                ArrayList<HashMap<String, String>>typeResult = null;
                HashMap<String, String>updateMap = new HashMap<>();
                updateMap.put("type_name", input);
                try {
                    typeResult = sql.query(updateMap, Table.Secondary_Type, emptyList);
                } catch (SQLException e) {
                    companyInterface.iDontKonwWhatFuckError();
                    e.printStackTrace();
                    return;
                }
                if (typeResult == null || typeResult.isEmpty()) {
                    companyInterface.iDontKonwWhatFuckError();
                    return;
                }
                input = typeResult.get(0).get("id");
                break;

            case CompanyInputType.InformationModel:
                key = "model";
                break;

            case CompanyInputType.InformationValue:
                key = "value";
                break;

            case CompanyInputType.InformationPurchaseDate:
                key = "purchase_date";
                Matcher matcher = pattern.matcher(input);
                if (!matcher.matches()) {
                    companyInterface.showIllegalDate();
                    return;
                }
                break;

            case CompanyInputType.InformationStatus:
                key = "status";
                if (Objects.equals(input, "正常")) {
                    input = "0";
                } else if (Objects.equals(input, "报废")) {
                    input = "2";
                } else if (Objects.equals(input, "维修")) {
                    input = "1";
                }
                break;

            case CompanyInputType.InformationConsumer:
                key = "consumer";
                break;

            case CompanyInputType.InformationRemark:
                key = "remark";
                break;
        }
        System.out.println(selectMap);
        renewMap.put(key, input);
        sql.update(selectMap, renewMap, Table.Information);
        companyInterface.updateSuccess();
    }
}
