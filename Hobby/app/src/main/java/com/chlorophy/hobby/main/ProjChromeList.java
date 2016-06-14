package com.chlorophy.hobby.main;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chlorophy.hobby.xmpp.UserInfoEnhanced;

import java.util.ArrayList;

public class ProjChromeList extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.8f;
        DisplayMetrics out = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(out);
        params.height = out.heightPixels / 2;
        getWindow().setAttributes(params);

        ArrayList<String> labelSet = new ArrayList<>();
        for(String key : UserInfoEnhanced.chromList.keySet())
            labelSet.add(key);

        ListView view = new ListView(this);
        view.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, labelSet));
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = ((TextView)view).getText().toString().trim();
                Log.e("=======", key);
                UserInfoEnhanced.projInfoList = UserInfoEnhanced.chromList.get(key);
                finish();
            }
        });
        setContentView(view);
    }
}
