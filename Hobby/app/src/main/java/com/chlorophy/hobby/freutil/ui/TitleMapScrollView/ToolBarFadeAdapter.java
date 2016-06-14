package com.chlorophy.hobby.freutil.ui.TitleMapScrollView;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chlorophy.hobby.freutil.ui.SlidingMenu.MenuFadeAdapter;

public class ToolBarFadeAdapter {
    private int finalAlpha = 255;
    private int startAlpha = 0;
    private int bgStartAlpha = 255;
    private int bgFinalAlpha = 0;
    private android.support.v7.widget.Toolbar toolbar = null;
    private ImageView topImg = null;
    private String color = "008888";
    private String backgroundColor = "000000";
    private TitleMapScrollView scrollView = null;
    private MenuFadeAdapter menuAdapter = null;

    private ToolBarFadeAdapter(){}

    public ToolBarFadeAdapter(android.support.v7.widget.Toolbar toolbar) {
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

    protected void onBkgrndAlphaChanged(int alpha) {
        if(scrollView != null && topImg != null) {
            String alphaStr = Integer.toHexString(alpha);
            alphaStr = alphaStr.length() == 1 ?  "0" + alphaStr : alphaStr;
            scrollView.setBackgroundColor(Color.parseColor("#" + alphaStr + backgroundColor));
            topImg.setImageAlpha(alpha);
        }
    }

    public void saveAndNotifySettingsChanged() {
        onAlphaChanged(startAlpha);
    }

    public void setAlpha(int finalAlpha, int startAlpha) {
        if(finalAlpha >= 0 && finalAlpha <= 255)
            this.finalAlpha = finalAlpha;
        if(startAlpha >= 0 && startAlpha <= 255)
            this.startAlpha = startAlpha;
    }

    public int getFinalAlpha() {
        return finalAlpha;
    }

    public int getStartAlpha() {
        return startAlpha;
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

    public void setMenuAdapter(MenuFadeAdapter menuAdapter) {
        this.menuAdapter = menuAdapter;
        menuAdapter.setMaxAlpha(startAlpha);
    }

    public void setColor(String RRGGBB) {
        this.color = RRGGBB;
    }

    public void onScroll(int scrollY, int picHeight) {
        int alpha = 255 * scrollY / (picHeight - toolbar.getHeight());
        if(alpha <= finalAlpha && alpha >= startAlpha) {
            onAlphaChanged(alpha);
            if(menuAdapter != null)
                menuAdapter.setMaxAlpha(alpha);
        }

        alpha = 255 - alpha;
        if((bgStartAlpha > bgFinalAlpha && alpha <= bgStartAlpha && alpha >= bgFinalAlpha) ||
                (bgStartAlpha < bgFinalAlpha && alpha >= bgStartAlpha && alpha <= bgFinalAlpha)) {
            onBkgrndAlphaChanged(alpha);
        }
    }

    public void enableBackgroundFade(TitleMapScrollView scrollView, String color,
                                     int bgStartAlpha, int bgFinalAlpha) {
        this.scrollView = scrollView;
        this.backgroundColor = color;
        if(bgStartAlpha >= 0 && bgStartAlpha <= 255)
            this.bgStartAlpha = bgStartAlpha;
        if(bgFinalAlpha >= 0 && bgFinalAlpha <= 255)
            this.bgFinalAlpha = bgFinalAlpha;

        ViewGroup wrapper = (ViewGroup) scrollView.getChildAt(0);
        topImg = (ImageView) wrapper.getChildAt(0);
    }

    public void disableBackgroundFade() {
        scrollView = null;
    }

}
