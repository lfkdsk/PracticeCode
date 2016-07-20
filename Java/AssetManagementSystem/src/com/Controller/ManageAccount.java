package com.Controller;

import com.company.CompanyInputType;
import com.company.CompanyInterface;
import com.company.SQL;
import com.company.Table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 账号管理
 * Created by lfk_dsk on 2016/7/18.
 */
public class ManageAccount extends Controller {
    private static String username;

    public ManageAccount(Scanner scanner, SQL sql, String username) {
        super(scanner, sql);
        ManageAccount.username = username;
    }

    @Override
    public void manage(CompanyInterface companyInterface) {
        companyInterface.showAccountManage();
        int type = getType(companyInterface);
        switch (type) {
            case CompanyInputType.ChangePassword:
                _manageAccount(companyInterface);
                break;

            case CompanyInputType.SystemExit:
                break;

            default:
                companyInterface.illegalInput();
                break;
        }
    }

    private void _manageAccount (CompanyInterface companyInterface) {
        companyInterface.showCurrentPassword();
        ArrayList<HashMap<String, String>>result = null;
        ArrayList<String> emptyColumn = new ArrayList<>();
        HashMap<String, String>hashMap = new HashMap<>();
        scanner.nextLine();
        hashMap.put("pwd", scanner.nextLine());
        hashMap.put("name", username);
        try {
            result = sql.query(hashMap, Table.Admin, emptyColumn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (result == null || result.isEmpty()) {
            companyInterface.showErrorPassword();
            return;
        }
        companyInterface.showNewPassword();
        HashMap<String, String>renewMap = new HashMap<>();
        renewMap.put("pwd", scanner.nextLine());
        if (sql.update(hashMap, renewMap, Table.Admin)) {
            companyInterface.changeSuccess();
        } else {
            companyInterface.changeFailure();
        }
    }
}
