package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理系统提示信息显示
 * Created by li_rz on 2016/7/16.
 */

/**
 *
 * Modified by lfk_dsk on 2016/7/17.
 */
public class CompanyInterface {
    CompanyInterface () {}

    /**
     * 主选择选项界面
     */

    public void showMainChoice () {
        System.out.println("**********************************************");
        System.out.println("选择操作：");
        System.out.println("1、固定资产类别管理");
        System.out.println("2、固定资产信息管理");
        System.out.println("3、人员信息管理");
        System.out.println("4、资产领用登记");
        System.out.println("5、资产归还登记");
        System.out.println("6、资产信息浏览和查询");
        System.out.println("7、管理员账号管理");
        System.out.println("0、退出系统");
        System.out.println("**********************************************");
    }

    /**
     * 非法输入
     */
    public void illegalInput () {
        System.out.println("非法输入");
    }

    /**
     * 固定资产类别管理选择
     */
    public void showTypeManageChoice() {
        System.out.println("**********************************************");
        System.out.println("1、管理大类");
        System.out.println("2、管理小类");
        System.out.println("0、返回主界面");
        System.out.println("**********************************************");
    }

    /**
     * 类别增删
     */
    public void showTypeInsertAndDelete () {
        System.out.println("**********************************************");
        System.out.println("1、添加类别");
        System.out.println("2、删除类别");
        System.out.println("0、返回主界面");
        System.out.println("**********************************************");
    }

    /**
     * 输入要插入的名字
     */
    public void showInsertNameClue () {
        System.out.println("**********************************************");
        System.out.println("输入要插入的名字：");
        System.out.println("**********************************************");
    }

    /**
     * 输入要删除的名字
     */
    public void showDeleteNameClue () {
        System.out.println("**********************************************");
        System.out.println("输入要删除的名字：");
        System.out.println("**********************************************");
    }

    /**
     * Delete Clue
     */
    public void showtypeDeleteWarning () {
        System.out.println("**********************************************");
        System.out.println("删除该类别会将该类别下所有子项目删除，是否删除（Y/N）");
        System.out.println("**********************************************");
    }

    /**
     * 输入从属的大类
     */
    public void showInsertMainType () {
        System.out.println("**********************************************");
        System.out.println("输入从属的大类：");
        System.out.println("**********************************************");
    }

    public void showInsertSecondaryType () {
        System.out.println("**********************************************");
        System.out.println("输入从属的小类：");
        System.out.println("**********************************************");
    }
    /**
     * 没有这种类型
     */
    public void showNotThisType () {
        System.out.println("没有这种类型");
    }

    public void showAccountManage () {
        System.out.println("**********************************************");
        System.out.println("1、修改密码");
        System.out.println("0、返回主界面");
        System.out.println("**********************************************");
    }

    /**
     * 输入当前密码
     */
    public void showCurrentPassword () {
        System.out.println("请输入当前密码：");
    }

    /**
     * 输入新密码
     */
    public void showNewPassword () {
        System.out.println("请输入新密码：");
    }

    /**
     * 密码错误
     */
    public void showErrorPassword () {
        System.out.println("密码错误!");
    }

    /**
     * 修改成功
     */
    public void changeSuccess () {
        System.out.println("修改成功");
    }

    /**
     * 修改失败
     */
    public void changeFailure () {
        System.out.println("修改失败");
    }

    /**
     * 增删改信息
     */
    public void showInformationOperator () {
        System.out.println("**********************************************");
        System.out.println("1、信息增加");
        System.out.println("2、信息删除");
        System.out.println("3、信息修改");
        System.out.println("0、返回主菜单");
        System.out.println("**********************************************");
    }


    public void showInformationKeyMessage (String key) {
        if (key.equals("purchase_date")) {
            System.out.println(key + " 格式为:(YYYY-MM-DD)");
        } else if (key.equals("remark") || key.equals("consumer")) {
            System.out.println(key + "(按回车不输入)");
        } else if (key.equals("status")) {
            System.out.println(key + "(正常、维修、报废)");
        } else {
            System.out.println(key);
        }
    }

    public void showIllegalDate () {
        System.out.println("非法日期格式");
    }

    public void mustInsert () {
        System.out.println("此为必须输入列");
    }

    public void insertSuccess () {
        System.out.println("插入成功");
    }

    public void deleteSuccess () {
        System.out.println("删除成功");
    }

    public void updateSuccess () {
        System.out.println("更新成功");
    }

    public void searchSuccess () {
        System.out.println("查找成功");
    }

    public void searchFailure () {
        System.out.println("查找失败");
    }

    public void showInformationDelete () {
        System.out.println("输入要删除的信息编号");
    }

    public void showInformationUpdate () {
        System.out.println("输入要更新的信息编号");
    }

    public void showInformationUpdateType () {
        System.out.println("**********************************************");
        System.out.println("选择要修改的项目：");
        System.out.println("1、编号");
        System.out.println("2、名称");
        System.out.println("3、类别");
        System.out.println("4、型号");
        System.out.println("5、价值");
        System.out.println("6、状态");
        System.out.println("7、购买日期");
        System.out.println("8、使用者");
        System.out.println("9、备注");
        System.out.println("0、返回主菜单");
        System.out.println("**********************************************");
    }

    public void notThisInformation () {
        System.out.println("没有这条信息");
    }

    public void showInformationUpdateClue () {
        System.out.println("输入要修改的信息");
    }

    public void iDontKonwWhatFuckError () {
        System.out.println("未知错误");
    }

    public void showAllOperator () {
        System.out.println("**********************************************");
        System.out.println("1、增加");
        System.out.println("2、删除");
        System.out.println("3、修改");
        System.out.println("4、查找");
        System.out.println("0、返回主菜单");
        System.out.println("**********************************************");
    }

    public void cannotFindMember () {
        System.out.println("没有该使用者");
    }

    public void showMemberDeleteClue () {
        System.out.println("输入要删除的工号：");
    }

    public void showMemberUpdateClue () {
        System.out.println("输入要更新的工号：");
    }

    public void memberUpdateChoice () {
        System.out.println("**********************************************");
        System.out.println("1、工号");
        System.out.println("2、名字");
        System.out.println("3、职务");
        System.out.println("4、备注");
        System.out.println("0、返回主菜单");
        System.out.println("**********************************************");
    }

    public void showMemberSearchClue () {
        System.out.println("输入要查找的工号：");
    }

    public void showMemberSearchChoice () {
        System.out.println("**********************************************");
        System.out.println("1、全部");
        System.out.println("2、个别");
        System.out.println("0、返回主菜单");
        System.out.println("**********************************************");
    }

    public void showSearchResult (ArrayList<HashMap<String, String>>arrayList) {
        System.out.println("**********************************************");
        for (HashMap<String, String>hashMap : arrayList) {
            System.out.println("**********************************************");
            for (Map.Entry entry: hashMap.entrySet()) {
                System.out.print(entry.getKey() + ": " + entry.getValue());
                System.out.println();
            }
            System.out.println("**********************************************");
        }
        System.out.println("**********************************************");
    }

    public void showLendChoice () {
        System.out.println("**********************************************");
        System.out.println("1、添加借用记录");
        System.out.println("0、返回主菜单");
        System.out.println("**********************************************");
    }

    public void showLendNumber () {
        System.out.println("输入要借的项目编号");
    }

    public void showLendKeyMessage (String key) {
        if (key.equals("admin") || key.equals("device_number") || key.equals("id")) {
            return;
        }
        if (key.equals("remark")) {
            System.out.println(key + "(按回车不输入)");
        } else if (key.equals("date")) {
            System.out.println(key + "(格式为: YYYY-MM-DD)");
        } else {
            System.out.println(key);
        }
    }

    public void inputConsumer () {
        System.out.println("输入借用者");
    }

    public void cannotLend () {
        System.out.println("该项目不能借");
    }

    public void showReturnChoice () {
        System.out.println("**********************************************");
        System.out.println("1、添加归还记录");
        System.out.println("0、返回主菜单");
        System.out.println("**********************************************");
    }

    public void showReturnNumber () {
        System.out.println("输入要归还的项目编号");
    }

    public void cannotReturn () {
        System.out.println("该项目不需要归还");
    }

    public void showSearchChoice () {
        System.out.println("**********************************************");
        System.out.println("1、按资产类别浏览");
        System.out.println("2、查询资产信息");
        System.out.println("0、返回主菜单");
        System.out.println("**********************************************");
    }

    public void showSearchTypeChoice () {
        System.out.println("**********************************************");
        System.out.println("1、按大类查找");
        System.out.println("2、按小类查找");
        System.out.println("0、返回主菜单");
        System.out.println("**********************************************");
    }

    public void showTypeName (String type) {
        System.out.println(type + ": ");
    }

    public void showSearchInformationChoice () {
        System.out.println("**********************************************");
        System.out.println("1、按编号查找");
        System.out.println("2、按类别查找");
        System.out.println("3、按使用者查找");
        System.out.println("0、返回主菜单");
        System.out.println("**********************************************");
    }

    public void showSearchInformationById () {
        System.out.println("输入要查找的编号");
    }

    public void showSearchInformationByType () {
        System.out.println("输入要查找的类别（小类）");
    }

    public void showSearchInformationByConsumer () {
        System.out.println("输入要查找的使用者");
    }
}
