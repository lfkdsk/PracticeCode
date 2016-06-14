package com.chlorophy.hobby.freutil.ui.ProjInfoView;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chlorophy.hobby.R;
import com.chlorophy.hobby.xmpp.ProjInfo;

public class ProjInfoView extends FrameLayout {

    private TextView projName = null;
    private TextView projProcess = null;
    private TextView projDate = null;
    private ViewGroup shadowLayout = null;
    private ProjInfo info = null;

    public ProjInfoView(Context context, ProjInfo info) {
        super(context);
        this.info = info;
        initView(context);
    }

    public ProjInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ProjInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.layout_info_card, this);
        projName = (TextView) findViewById(R.id.projName);
        projDate = (TextView) findViewById(R.id.projDate);
        projProcess = (TextView) findViewById(R.id.projProcess);
        shadowLayout = (ViewGroup) findViewById(R.id.shadowLayout);
        setInfo(info);
    }

    public Uri getUri() {return info.getProjDir();}

    public ProjInfo getInfo() {
        return info;
    }

    public void setInfo(ProjInfo info) {
        this.info = info;
        setProjName(info.getName());
        setProjDate(info.getFinalDate());
        setProjProcess(info.getProgress() + "%");
    }

    public void setProjName(String name) {
        projName.setText(name);
    }

    public void setProjDate(String date) {
        projDate.setText(date);
    }

    public void setProjProcess(String process) {
        projProcess.setText(process);
    }

}
