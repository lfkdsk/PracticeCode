package com.company;

/**
 * 输入方法类型
 * Created by li_rz on 2016/7/16.
 */

/**
 *
 * Modified by lfk_dsk on 2016/7/17.
 */
public class CompanyInputType{

    // 固定资产类别管理
    public static final int TypeManage = 1;

    // 固定资产信息管理
    public static final int InformationManage = 2;

    // 人员信息管理
    public static final int UserManage = 3;

    // 资产领用登记
    public static final int AssetLendManage = 4;

    // 资产归还登记
    public static final int AssetReturnManage = 5;

    // 资产信息浏览和查询
    public static final int AssetSearch = 6;

    // 管理员账号管理
    public static final int AccountManage = 7;

    /**
     * --------------------------------------------------------------
     */
    // 退出系统
    public static final int SystemExit = 0;

    // 大类
    public static final int MainType = 1;

    // 小类
    public static final int SecondaryType = 2;

    /**
     * ----------------------------------------------------------------
     */

    // 增
    public static final int OperatorInsert = 1;

    // 删
    public static final int OperatorDelete = 2;

    // 改
    public static final int OperatorUpdate = 3;

    // 查
    public static final int OperatorSearch = 4;

    /**
     * ----------------------------------------------------------------
     */

    public static final int ChangePassword = 1;

    /**
     * ----------------------------------------------------------------
     */

    public static final int InformationId = 1;

    public static final int InformationName = 2;

    public static final int InformationType = 3;

    public static final int InformationModel = 4;

    public static final int InformationValue = 5;

    public static final int InformationPurchaseDate = 7;

    public static final int InformationStatus = 6;

    public static final int InformationConsumer = 8;

    public static final int InformationRemark = 9;


    /**
     * ----------------------------------------------------------------
     */

    public static final int MemberId = 1;

    public static final int MemberName = 2;

    public static final int MemberResign = 3;

    public static final int MemberRemark = 4;

    /**
     * ----------------------------------------------------------------
     */

    public static final int AllSearchMember = 1;

    public static final int OneSearchMember = 2;

    /**
     * ----------------------------------------------------------------
     */

    public static final int InsertLendItem = 1;

    /**
     * ----------------------------------------------------------------
     */

    public static final int InsertReturnItem = 1;

    /**
     * ----------------------------------------------------------------
     */

    public static final int SearchType = 1;

    public static final int SearchInformation = 2;

    /**
     * ----------------------------------------------------------------
     */

    public static final int SearchMainType = 1;

    public static final int SearchSecondaryType = 2;

    /**
     * ----------------------------------------------------------------
     */

    public static final int SearchInformationId = 1;

    public static final int SearchInformationType = 2;

    public static final int SearchInformationConsumer = 3;

}
