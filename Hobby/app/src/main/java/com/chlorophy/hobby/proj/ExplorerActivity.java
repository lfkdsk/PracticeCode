package com.chlorophy.hobby.proj;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chlorophy.hobby.R;
import com.chlorophy.hobby.freutil.ui.FileExplorer.FileExplorer;
import com.chlorophy.hobby.freutil.ui.FileExplorer.OnFileChosenListener;
import com.chlorophy.hobby.freutil.ui.FileExplorer.OnPathChangedListener;
import com.chlorophy.hobby.freutil.ui.Proportionview.ProportionView;

public class ExplorerActivity extends ActionBarActivity {

    private FileExplorer explorer;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_explorer);
        final ProportionView view = (ProportionView) findViewById(R.id.pv);
        explorer = (FileExplorer) findViewById(R.id.ex);

        //覆盖屏蔽原有长按事件
        explorer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        //新路径下分析文件比例，搞比利，比例比例摔跤的比利
        explorer.setOnPathChangedListener(new OnPathChangedListener() {
            @Override
            public void onPathChanged(String path) {
                    try {
                        view.setData(explorer.getPropotionText(path));
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "此路径下不可访问或文件夹下无文件", Toast.LENGTH_LONG).show();
                    }
                }
        });
        //点击文件打开小猿搜题，开启手脚提前S精模式
        explorer.setOnFileChosenListener(new OnFileChosenListener() {
            @Override
            public void onFileChosen(Uri fileUri) {
                Intent intent = new Intent(ExplorerActivity.this, CodeActivity.class);
                intent.setData(fileUri);
                startActivity(intent);
            }
        });

        explorer.setCurrentDir(getIntent().getData().getPath());
        explorer.setRootDir(getIntent().getData().getPath());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(!explorer.toParentDir()){
                if(System.currentTimeMillis() - exitTime < 1000)
                    finish();
                exitTime = System.currentTimeMillis();
                Toast.makeText(this, "再次点击手脚提前射精", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
