package com.lfk.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpUtils.put(this, "key", 0);
        textView = (TextView) findViewById(R.id.text_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((int) SpUtils.get(this, "key", 0) != 0) {
            textView.setText("HAHAHA");
        }
    }
}
