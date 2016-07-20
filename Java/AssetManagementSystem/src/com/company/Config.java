package com.company;

/**
 *
 * Created by li_rz on 2016/7/13.
 * 数据库配置类
 */


public class Config {

    private String username;
    private String name;
    private String driver;
    private String url;
    private String port;
    private String database;
    private String password;


    public String getUrl () {
        return driver + ":" + name + "://" + url + ":" + port + ";DataBaseName=" + database;
    }

    public String getUsername () {
        return username;
    }

    public String getPassword () {
        return password;
    }

    public Config () {
        username = "root";
        name = "sqlserver";
        driver = "jdbc";
        url = "localhost";
        port = "1433";
        database = "Asset";
        password = "erfvgt";
    }

    public Config (String driver, String username, String password, String url, String port, String name, String database) {
        this.driver = driver;
        this.database = database;
        this.password = password;
        this.name = name;
        this.url = url;
        this.port = port;
        this.username = username;
    }
}
