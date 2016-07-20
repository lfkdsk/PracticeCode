package com.Controller;

import com.company.CompanyInputType;
import com.company.CompanyInterface;
import com.company.SQL;
import com.company.Table;
import com.database.DataBase;
import com.database.Lend;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 资产领用登记
 * Created by lfk_dsk on 2016/7/18.
 */
public class ManageLend extends Controller {
    private static String id;

    public ManageLend(Scanner scanner, SQL sql, String id) {
        super(scanner, sql);

        ManageLend.id = id;
    }

    @Override
    public void manage(CompanyInterface companyInterface) {
        companyInterface.showLendChoice();
        int type = getType(companyInterface);
        switch (type) {
            case CompanyInputType.InsertLendItem:
                _manageAssetLendInsert(companyInterface);
                break;

            case CompanyInputType.SystemExit:
                break;

            default:
                companyInterface.illegalInput();
                break;
        }
    }

    /**
     * 添加领用记录
     * @param companyInterface CompanyInterface
     */
    private void _manageAssetLendInsert (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showLendNumber();
        HashMap<String, String> lendMap = new HashMap<>();
        lendMap.put("id", scanner.nextLine());
        ArrayList<String> emptyList = new ArrayList<>();
        ArrayList<HashMap<String, String>>result = null;

        try {
            result = sql.query(lendMap, Table.Information, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // 查不到
        if (result == null || result.isEmpty()) {
            companyInterface.illegalInput();
            return;
        }

        // 不能借
        if (!Objects.equals(result.get(0).get("status"), "0") ||
                result.get(0).get("consumer") != null) {
            companyInterface.cannotLend();
            return;
        }

        DataBase table1 = new Lend();
        HashMap<String, Boolean>keyList = new HashMap<>(table1.getKeyList());
        HashMap<String, String>insertMap = new HashMap<>();

        // 迭代keyList
        for (Map.Entry entry : keyList.entrySet()) {
            companyInterface.showLendKeyMessage((String) entry.getKey());

            // 自动填入数据，无需添加
            if ((entry.getKey().equals("id") ||
                    entry.getKey().equals("device_number") ||
                    entry.getKey().equals("admin"))) {
                continue;
            }

            String input = scanner.nextLine();
            if (input.length() == 0 && entry.getValue().equals(true)) {
                companyInterface.illegalInput();
                return;
            } else if (entry.getValue().equals(false) && input.length() != 0) {
                insertMap.put((String) entry.getKey(), input);
            } else if (entry.getKey().equals("date")) {
                Matcher matcher = pattern.matcher(input);
                if (!matcher.matches()) {
                    companyInterface.illegalInput();
                    return;
                } else {
                    insertMap.put((String) entry.getKey(), input);
                }
            } else {
                insertMap.put((String) entry.getKey(), input);
            }
        }

        HashMap<String, String>selectMap = new HashMap<>();
        selectMap.put("id", result.get(0).get("id"));

        companyInterface.inputConsumer();
        HashMap<String, String>renewMap = new HashMap<>();
        renewMap.put("consumer", scanner.nextLine());

        // 查找是否有该用户
        HashMap<String, String>confirmMap = new HashMap<>();
        confirmMap.put("name", renewMap.get("consumer"));
        result.clear();
        try {
            result = sql.query(confirmMap, Table.Member, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            companyInterface.iDontKonwWhatFuckError();
            return;
        }

        if (result.isEmpty()) {
            companyInterface.cannotFindMember();
            return;
        }

        // 自动填入数据
        insertMap.put("admin", id);
        insertMap.put("device_number", selectMap.get("id"));
        try {
            sql.create(insertMap, Table.Lend);
            sql.update(selectMap, renewMap, Table.Information);
            companyInterface.insertSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
            companyInterface.iDontKonwWhatFuckError();
        }
    }
}
