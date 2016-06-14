package com.chlorophy.hobby.proj;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chlorophy.hobby.R;
import com.chlorophy.hobby.freutil.ui.CodeView.CodeView;

import java.io.File;

public class CodeActivity extends ActionBarActivity {

    private CodeView codeView = null;
    long exitTime = 0;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        codeView = (CodeView) findViewById(R.id.mcodeview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);

        File dir = null;
        Uri fileUri = getIntent().getData();
        if (fileUri != null) {
            dir = new File(fileUri.getPath());
        }

        if (dir != null) {
            codeView.setDirSource(dir);
            toolbar.setSubtitle(dir.getName());
        }
        else
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_code, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis() - exitTime < 1000) {
                finish();
            }
            exitTime = System.currentTimeMillis();
            Toast.makeText(this, "再次点击退出小猿搜题", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_code) {
            if (!codeView.isEditable()) {
                item.setTitle("完成");
                codeView.setContentEditable(true);
            } else {
                item.setTitle("编辑");
                codeView.setContentEditable(false);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}


