package com.chlorophy.hobby.xmpp;

import android.net.Uri;

import java.io.Serializable;

public class ProjInfo implements Serializable{
    private String name;
    private String initialDate;
    private String finalDate;
    private int progress;
    private String projDir;
    private long ID;

    public ProjInfo(){
        name = "ThronBird";
        initialDate = "2015/6/1";
        finalDate = "2017/6/1";
        progress = 0;
    }

    public ProjInfo(String name, String initialDate, String finalDate) {
        this(name, initialDate, finalDate, 0);
    }

    public ProjInfo(String name, String initialDate, String finalDate, int progress) {
        this.name = name;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.progress = progress;
    }

    public Uri getProjDir() {
        return Uri.parse(projDir);
    }

    public void setProjDir(Uri projDir) {
        this.projDir = projDir.getPath();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
