package com.Controller;

import com.company.CompanyInputType;
import com.company.CompanyInterface;
import com.company.SQL;
import com.company.Table;
import com.database.DataBase;
import com.database.Member;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 人员信息管理
 * Created by lfk_dsk on 2016/7/18.
 */
public class ManageUser extends Controller {
    public ManageUser(Scanner scanner, SQL sql) {
        super(scanner, sql);
    }

    @Override
    public void manage(CompanyInterface companyInterface) {
        companyInterface.showAllOperator();
        int type = getType(companyInterface);

        switch (type) {
            case CompanyInputType.OperatorInsert:
                _manageUserInsert(companyInterface);
                break;

            case CompanyInputType.OperatorDelete:
                _manageUserDelete(companyInterface);
                break;

            case CompanyInputType.OperatorUpdate:
                _manageUserUpdate(companyInterface);
                break;

            case CompanyInputType.OperatorSearch:
                _manageUserSearch(companyInterface);
                break;
        }
    }

    /**
     * 添加人员信息
     * @param companyInterface CompanyInterface
     */
    private void _manageUserInsert (CompanyInterface companyInterface) {
        scanner.nextLine();
        DataBase table = new Member();
        HashMap<String, Boolean> keyList = new HashMap<>(table.getKeyList());
        HashMap<String, String>insertMap = new HashMap<>();
        String input;
        for (Map.Entry entry : keyList.entrySet()) {
            companyInterface.showInformationKeyMessage(String.valueOf(entry.getKey()));
            input = scanner.nextLine();
            if (entry.getValue().equals(false)) {
                if (input.length() != 0) {
                    insertMap.put((String) entry.getKey(), input);
                }
            } else {
                insertMap.put((String) entry.getKey(), input);
            }
        }

        try {
            sql.create(insertMap, Table.Member);
            companyInterface.insertSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除人员信息
     * @param companyInterface CompanyInterface
     */
    private void _manageUserDelete (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showMemberDeleteClue();
        HashMap<String, String>deleteMap = new HashMap<>();
        deleteMap.put("id", scanner.nextLine());
        try {
            sql.delete(deleteMap, Table.Member);
            companyInterface.deleteSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新人员信息
     * @param companyInterface CompanyInterface
     */
    private void _manageUserUpdate (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showMemberUpdateClue();
        HashMap<String, String>updateMap = new HashMap<>();
        updateMap.put("id", scanner.nextLine());
        companyInterface.memberUpdateChoice();
        int type = getType(companyInterface);
        HashMap<String, String>renewMap = new HashMap<>();
        companyInterface.showInformationUpdateClue();
        String key = "";
        String input = scanner.nextLine();
        switch (type) {
            case CompanyInputType.MemberId:
                key = "id";
                break;

            case CompanyInputType.MemberName:
                key = "name";
                break;

            case CompanyInputType.MemberResign:
                key = "resign";
                break;

            case CompanyInputType.MemberRemark:
                key = "remark";
                break;

            case CompanyInputType.SystemExit:
                break;

            default:
                companyInterface.illegalInput();
                break;
        }

        renewMap.put(key, input);
        sql.update(updateMap, renewMap, Table.Member);
        companyInterface.updateSuccess();
    }

    /**
     * 查找人员信息
     * @param companyInterface CompanyInterface
     */
    private void _manageUserSearch (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showMemberSearchChoice();
        int type = getType(companyInterface);
        scanner.nextLine();
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        HashMap<String, String>searchMap = new HashMap<>();
        ArrayList<String>emptyList = new ArrayList<>();

        switch (type) {
            case CompanyInputType.AllSearchMember:
                try {
                    result = sql.query(searchMap, Table.Member, emptyList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case CompanyInputType.OneSearchMember:
                companyInterface.showMemberSearchClue();
                searchMap.put("id", scanner.nextLine());
                try {
                    result = sql.query(searchMap, Table.Member, emptyList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case CompanyInputType.SystemExit:
                break;

            default:
                companyInterface.illegalInput();
                break;
        }

        if (result.isEmpty()) {
            companyInterface.searchFailure();
            return;
        }
        companyInterface.searchSuccess();
        companyInterface.showSearchResult(result);
    }
}
