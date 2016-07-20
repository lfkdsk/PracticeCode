package com.Controller;

import com.company.CompanyInterface;
import com.company.SQL;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * Created by lfk_dsk on 2016/7/18.
 */
public abstract class Controller {

    // protected static CompanyInterface companyInterface;

    protected static Scanner scanner;
    protected static final Pattern pattern= Pattern.compile("^\\d{4}-(0|1)[0-9]-[0-3][0-9]$"); // 日期合法格式
    protected static SQL sql;

    protected int getType (CompanyInterface companyInterface) {
        int type;
        while (true) {
            try {
                type = scanner.nextInt();
                // scanner.next();
                break;
            } catch (NumberFormatException | InputMismatchException nfe) {
                companyInterface.illegalInput();
                scanner.nextLine();
            }
        }
        return type;
    }

    Controller (Scanner scanner, SQL sql) {
        Controller.scanner = scanner;
        Controller.sql = sql;
    }

    protected abstract void manage (CompanyInterface companyInterface);
}
