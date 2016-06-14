package com.chlorophy.hobby.freutil.ui.SlidingMenu;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

/**
 * 配置移动菜单时渐变的适配器（Adapter）
 */
public class MenuFadeAdapter extends Handler {

    int maxAlpha = 0;
    android.support.v7.widget.Toolbar toolbar = null;
    String color = "008888";

    private MenuFadeAdapter(){}

    public MenuFadeAdapter(android.support.v7.widget.Toolbar toolbar) {
        super();
        setToolbar(toolbar);
    }

    protected void onAlphaChanged(int alpha) {
        if(toolbar != null) {
            String alphaStr = Integer.toHexString(alpha);
            alphaStr = alphaStr.length() == 1 ?  "0" + alphaStr : alphaStr;
            toolbar.setBackgroundColor(Color.parseColor("#" + alphaStr + color));
        }
    }

    public void saveAndNotifySettingsChanged() {
        onAlphaChanged(maxAlpha);
    }

    public void setMaxAlpha(int maxAlpha) {
        if(maxAlpha >= 0 && maxAlpha <= 255)
            this.maxAlpha = maxAlpha;
    }

    public int getMaxAlpha() {
        return maxAlpha;
    }

    public void setToolbar(android.support.v7.widget.Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public android.support.v7.widget.Toolbar getToolbar() {
        return toolbar;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String RRGGBB) {
        this.color = RRGGBB;
    }

    @Override
    public void handleMessage(Message msg) {
        if(msg.what <= maxAlpha) {
            onAlphaChanged(msg.what);
        }
    }
}

