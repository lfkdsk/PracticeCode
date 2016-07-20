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
 * 资产信息浏览和查询
 * Created by lfk_dsk on 2016/7/18.
 */
public class ManageAssetSearch extends Controller {
    public ManageAssetSearch(Scanner scanner, SQL sql) {
        super(scanner, sql);
    }

    @Override
    public void manage(CompanyInterface companyInterface) {
        companyInterface.showSearchChoice();
        int type = getType(companyInterface);

        switch (type) {
            case CompanyInputType.SearchInformation:
                _manageSearchInformation(companyInterface);
                break;

            case CompanyInputType.SearchType:
                _manageSearchType(companyInterface);
                break;

            case CompanyInputType.SystemExit:
                break;

            default:
                companyInterface.illegalInput();
                break;
        }
    }

    /**
     * 查询资产信息
     * @param companyInterface CompanyInterface
     */
    private void _manageSearchInformation (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showSearchInformationChoice();
        int type = getType(companyInterface);

        switch (type) {
            case CompanyInputType.SearchInformationId:
                __manageSearchInformationId(companyInterface);
                break;

            case CompanyInputType.SearchInformationType:
                __manageSearchInformationType(companyInterface);
                break;

            case CompanyInputType.SearchInformationConsumer:
                __manageSearchInformationConsumer(companyInterface);
                break;

            case CompanyInputType.SystemExit:
                break;

            default:
                companyInterface.illegalInput();
        }
    }

    /**
     * 按编号查找
     * @param companyInterface CompanyInterface
     */
    private void __manageSearchInformationId (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showSearchInformationById();
        HashMap<String, String>queryMap = new HashMap<>();
        ArrayList<String>emptyList = new ArrayList<>();
        ArrayList<HashMap<String, String>>result = null;

        queryMap.put("id", scanner.nextLine());

        try {
            result = sql.query(queryMap, Table.Information, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (result == null || result.isEmpty()) {
            companyInterface.searchFailure();
            return;
        }
        companyInterface.showSearchResult(result);
    }

    /**
     * 按类型查找
     * @param companyInterface CompanyInterface
     */
    private void __manageSearchInformationType (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showSearchInformationByType();
        HashMap<String, String>queryMap = new HashMap<>();
        ArrayList<String>emptyList = new ArrayList<>();
        ArrayList<HashMap<String, String>>result = null;

        queryMap.put("type_name", scanner.nextLine());

        try {
            result = sql.query(queryMap, Table.Secondary_Type, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if (result == null || result.isEmpty()) {
            companyInterface.searchFailure();
            return;
        }
        String id = result.get(0).get("id");
        result.clear();
        queryMap.clear();

        queryMap.put("type", id);

        try {
            result = sql.query(queryMap, Table.Information, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (result == null || result.isEmpty()) {
            companyInterface.searchFailure();
            return;
        }

        companyInterface.showSearchResult(result);

    }

    /**
     * 按使用者查找
     * @param companyInterface CompanyInterface
     */
    private void __manageSearchInformationConsumer (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showSearchInformationByConsumer();
        HashMap<String, String>queryMap = new HashMap<>();
        ArrayList<String>emptyList = new ArrayList<>();
        ArrayList<HashMap<String, String>>result = null;

        queryMap.put("consumer", scanner.nextLine());

        try {
            result = sql.query(queryMap, Table.Information, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if (result == null || result.isEmpty()) {
            companyInterface.searchFailure();
            return;
        }

        companyInterface.showSearchResult(result);
    }

    /**
     * 按资产类别浏览
     * @param companyInterface CompanyInterface
     */
    private void _manageSearchType (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showSearchTypeChoice();
        int type = getType(companyInterface);

        switch (type) {
            case CompanyInputType.SearchMainType:
                _manageSearchMainType(companyInterface);
                break;

            case CompanyInputType.SearchSecondaryType:
                _manageSearchSecondaryType(companyInterface);
                break;

            case CompanyInputType.SystemExit:
                break;

            default:
                companyInterface.illegalInput();
                break;
        }
    }

    /**
     * 大类查找
     * @param companyInterface CompanyInterface
     */
    private void _manageSearchMainType (CompanyInterface companyInterface) {
        scanner.nextLine();
        companyInterface.showInsertMainType();
        HashMap<String, String> searchMap = new HashMap<>();
        ArrayList<String> emptyList = new ArrayList<>();

        ArrayList<HashMap<String, String>>result = null;

        searchMap.put("type_name", scanner.nextLine());

        try {
            result = sql.query(searchMap, Table.Main_Type, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            companyInterface.iDontKonwWhatFuckError();
            return;
        }

        if (result == null || result.isEmpty()) {
            companyInterface.showNotThisType();
            return;
        }

        String main_id = result.get(0).get("id");
        String main_name = result.get(0).get("type_name");
        searchMap.clear();
        searchMap.put("main_type", main_id);
        result.clear();

        try {
            result = sql.query(searchMap, Table.Secondary_Type, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            companyInterface.iDontKonwWhatFuckError();
            return;
        }

        if (result == null || result.isEmpty()) {
            companyInterface.showNotThisType();
            return;
        }

        companyInterface.showTypeName(main_name);
        for (HashMap<String, String >hashMap : result) {
            companyInterface.showTypeName(hashMap.get("type_name"));
            String secondary_id = hashMap.get("id");
            HashMap<String, String>informationMap = new HashMap<>();
            informationMap.put("type", secondary_id);
            ArrayList<HashMap<String, String>>informationResult = null;

            try {
                informationResult = sql.query(informationMap, Table.Information, emptyList);
            } catch (SQLException e) {
                e.printStackTrace();
                companyInterface.iDontKonwWhatFuckError();
            }

            companyInterface.showSearchResult(informationResult);
        }

    }

    /**
     * 小类查找
     * @param companyInterface CompanyInterface
     */
    private void _manageSearchSecondaryType (CompanyInterface companyInterface) {
        scanner.nextLine();

        companyInterface.showInsertSecondaryType();
        HashMap<String, String>searchMap = new HashMap<>();
        ArrayList<String>emptyList = new ArrayList<>();

        ArrayList<HashMap<String, String>>result = null;

        searchMap.put("type_name", scanner.nextLine());

        try {
            result = sql.query(searchMap, Table.Secondary_Type, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (result == null || result.isEmpty()) {
            companyInterface.showNotThisType();
            return;
        }
        String secondary_id = result.get(0).get("id");
        String secondary_name = result.get(0).get("type_name");


        searchMap.clear();
        result.clear();

        searchMap.put("type", secondary_id);

        try {
            result = sql.query(searchMap, Table.Information, emptyList);
        } catch (SQLException e) {
            e.printStackTrace();
            companyInterface.iDontKonwWhatFuckError();
            return;
        }

        if (result == null || result.isEmpty()) {
            companyInterface.showNotThisType();
            return;
        }

        companyInterface.showTypeName(secondary_name);
        companyInterface.showSearchResult(result);

    }
}
