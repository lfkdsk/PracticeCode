package com.Controller;

import com.company.CompanyInputType;
import com.company.CompanyInterface;
import com.company.SQL;
import com.company.Table;
import com.database.Back;
import com.database.DataBase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * 资产归还登记
 * Created by lfk_dsk on 2016/7/18.
 */
public class ManageReturn extends Controller {
    private static String id;

    public ManageReturn(Scanner scanner, SQL sql, String id) {
        super(scanner, sql);
        ManageReturn.id = id;
    }

    @Override
    public void manage(CompanyInterface companyInterface) {
        companyInterface.showReturnChoice();
        int type = getType(companyInterface);
        switch (type) {
            case CompanyInputType.InsertReturnItem:
                _manageAssetReturnInsert(companyInterface);
                break;

            case CompanyInputType.SystemExit:
                break;

            default:
                companyInterface.illegalInput();
                break;
        }
    }

    /**
     * 添加归还记录
     * @param companyInterface CompanyInterface
     */
    private void _manageAssetReturnInsert (CompanyInterface companyInterface) {
        scanner.nextLine();
        HashMap<String, String> returnMap = new HashMap<>();
        companyInterface.showReturnNumber();
        returnMap.put("id", scanner.nextLine());

        ArrayList<String> emptyList = new ArrayList<>();
        ArrayList<HashMap<String, String>>result = null;

        try {
            result = sql.query(returnMap, Table.Information, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // 查不到
        if (result == null || result.isEmpty()) {
            companyInterface.illegalInput();
            return;
        }

        if (result.get(0).get("consumer") == null) {
            companyInterface.cannotReturn();
            return;
        }

        DataBase tableReturn = new Back();
        HashMap<String, Boolean>keyList = new HashMap<>(tableReturn.getKeyList());
        HashMap<String, String>insertMap = new HashMap<>();

        for (Map.Entry entry: keyList.entrySet()) {
            if (entry.getKey().equals("id") ||
                    entry.getKey().equals("device_number") ||
                    entry.getKey().equals("admin")) {
                continue;
            }
            companyInterface.showLendKeyMessage((String) entry.getKey());
            String input = scanner.nextLine();
            if (entry.getKey().equals("date")) {
                Matcher matcher = pattern.matcher(input);
                if (matcher.matches()) {
                    insertMap.put("date", input);
                } else {
                    companyInterface.illegalInput();
                    return;
                }
            }
        }
        HashMap<String, String>selectMap = new HashMap<>();
        selectMap.put("id", result.get(0).get("id"));

        HashMap<String, String>renewMap = new HashMap<>();
        renewMap.put("consumer", "");

        insertMap.put("admin", id);
        insertMap.put("device_number", selectMap.get("id"));

        try {
            sql.create(insertMap, Table.Back);
            sql.update(selectMap, renewMap, Table.Information);
            companyInterface.insertSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
            companyInterface.iDontKonwWhatFuckError();
        }
    }
}
