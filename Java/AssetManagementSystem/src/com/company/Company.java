package com.company;

import com.Controller.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 视图和控制层
 * Created by li_rz on 2016/7/16.
 */

/**
 *
 * Modified by lfk_dsk on 2016/7/18.
 */
public class Company {
    private static SQL sql;
    private static final Pattern pattern= Pattern.compile("^\\d{4}-(0|1)[0-9]-[0-3][0-9]$"); // 日期合法格式
    private static Scanner scanner;
    private static String username = null;
    private static String id = null;
    public Company() {
        scanner = new Scanner(System.in);
    }

    /**
     * 登录界面管理
     */
    private void loginSystem () {
        HashMap<String, String>hashMap = new HashMap<>();
        ArrayList<String>column = new ArrayList<>();
        System.out.println("欢迎使用固定资产管理系统，请先登录。");
        System.out.println("请输入用户名：");
        hashMap.put("name", scanner.nextLine());
        System.out.println("请输入密码：");
        hashMap.put("pwd", scanner.nextLine());
        try {
            sql = new SQL();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        sql.connectionDataBase();
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        try {
            data = sql.query(hashMap, Table.Admin, column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (data.isEmpty()) {
            System.out.println("未知用户!!");
            System.exit(-1);
        }
        username = data.get(0).get("name");
        id = data.get(0).get("id");
        System.out.println("用户" + username + "欢迎进入固定资产管理系统。");
        data.clear();
    }



    /**
     * 启动程序
     */
    public void runProgramme () {
        // Scanner scanner = new Scanner(System.in);
        loginSystem();
        CompanyInterface companyInterface = new CompanyInterface();
        while (true) {
            companyInterface.showMainChoice();
            int type;
            try {
                type = scanner.nextInt();
                // scanner.nextLine();
            } catch (NumberFormatException nfe) {
                companyInterface.illegalInput();
                scanner.nextLine();
                continue;
            } catch (InputMismatchException ime) {
                companyInterface.illegalInput();
                scanner.nextLine();
                continue;
            }
            switch (type) {
                case CompanyInputType.TypeManage:
                    ManageType manageType = new ManageType(scanner, sql);
                    manageType.manage(companyInterface);
                    break;

                case CompanyInputType.InformationManage:
                    ManageInformation manageInformation = new ManageInformation(scanner, sql);
                    manageInformation.manage(companyInterface);
                    break;

                case CompanyInputType.UserManage:
                    ManageUser manageUser = new ManageUser(scanner, sql);
                    manageUser.manage(companyInterface);
                    break;

                case CompanyInputType.AssetLendManage:
                    ManageLend manageLend = new ManageLend(scanner, sql, id);
                    manageLend.manage(companyInterface);
                    break;

                case CompanyInputType.AssetReturnManage:
                    ManageReturn manageReturn = new ManageReturn(scanner, sql, id);
                    manageReturn.manage(companyInterface);
                    break;

                case CompanyInputType.AssetSearch:
                    ManageAssetSearch manageAssetSearch = new ManageAssetSearch(scanner, sql);
                    manageAssetSearch.manage(companyInterface);
                    break;

                case CompanyInputType.AccountManage:
                    ManageAccount manageAccount = new ManageAccount(scanner, sql, username);
                    manageAccount.manage(companyInterface);
                    break;

                case CompanyInputType.SystemExit:
                    sql.close();
                    System.exit(0);
                    break;

                default:
                    companyInterface.illegalInput();
                    break;
            }
        }
    }
}
