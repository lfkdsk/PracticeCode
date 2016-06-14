package com.chlorophy.hobby.xmpp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class UserInfoEnhanced {
    public static double credit;
    public static int contributed;
    public static int shared;
    public static int projs;
    public static ArrayList<ProjInfo> projInfoList = new ArrayList<>();
    public static Map<String, ArrayList<ProjInfo>> chromList = new Hashtable<>();

    //禁止实例化
    private UserInfoEnhanced(){}
}
