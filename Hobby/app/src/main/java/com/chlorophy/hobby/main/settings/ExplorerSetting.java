package com.chlorophy.hobby.main.settings;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chlorophy.hobby.R;
import com.chlorophy.hobby.freutil.ui.FileExplorer.FileExplorer;
import com.chlorophy.hobby.freutil.ui.FileExplorer.OnFolderChosenListener;
import com.chlorophy.hobby.freutil.ui.FileExplorer.OnPathChangedListener;

public class ExplorerSetting extends ActionBarActivity {

    private FileExplorer explorer;
    private long exitTime = 0;
    public static final int RESULT = 1;
    public static final int CANCELED = 0;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer_setting);
        toolbar = (Toolbar)findViewById(R.id.extoolbar);
        toolbar.setTitle("选择项目所在文件夹");
        setSupportActionBar(toolbar);
        explorer = (FileExplorer) findViewById(R.id.explorerproj);
        getSupportActionBar().setSubtitle(explorer.getCurrentPath());
        explorer.setOnItemChosenListener(new OnFolderChosenListener() {
            @Override
            public void onItemChosen(String path) {
                setResult(RESULT, getIntent().setData(Uri.parse(path)));
                finish();
            }
        });

        explorer.setOnPathChangedListener(new OnPathChangedListener() {
            @Override
            public void onPathChanged(String path) {
                getSupportActionBar().setSubtitle(explorer.getCurrentPath());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(!explorer.toParentDir()){
                if(System.currentTimeMillis() - exitTime < 1000) {
                    setResult(CANCELED);
                    finish();
                }
                exitTime = System.currentTimeMillis();
                Toast.makeText(this, "祝您身体健康!!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_explorer_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            explorer.setCurrentDir(Environment.getExternalStorageDirectory().getPath());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
