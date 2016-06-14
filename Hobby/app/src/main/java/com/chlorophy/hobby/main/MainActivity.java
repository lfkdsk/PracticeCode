package com.chlorophy.hobby.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AnalogClock;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chlorophy.hobby.R;
import com.chlorophy.hobby.freutil.system.SysUtil;
import com.chlorophy.hobby.freutil.ui.CircleImageView.CircleImageView;
import com.chlorophy.hobby.freutil.ui.ProjInfoView.ProjInfoView;
import com.chlorophy.hobby.freutil.ui.SlidingMenu.MenuFadeAdapter;
import com.chlorophy.hobby.freutil.ui.SlidingMenu.SlidingMenu;
import com.chlorophy.hobby.freutil.ui.TitleMapScrollView.ToolBarFadeAdapter;
import com.chlorophy.hobby.main.settings.ProjSettingsActivity;
import com.chlorophy.hobby.proj.ExplorerActivity;
import com.chlorophy.hobby.xmpp.ProjInfo;
import com.chlorophy.hobby.xmpp.UserInfoBasic;
import com.chlorophy.hobby.xmpp.UserInfoEnhanced;
import com.chlorophy.hobby.xmpp.XSCHelper;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    //本类全局变量
    private SharedPreferences pref = null;
    private File pIconDir = null;
    private File pIconFile = null;
    private ViewGroup projListWrapper = null;
    private SlidingMenu menu = null;
    private CircleImageView pIconView = null;
    private TextView infoName = null;
    private TextView infoCredit = null;
    private TextView infoProjs = null;
    private Toolbar toolbar = null;
    private ToolBarFadeAdapter toolBarFadeAdapter = null;
    private MenuFadeAdapter menuFadeAdapter = null;
    private MultiUserChat room = null;
    private ScrollView projList = null;
    private long exitTimeRecord = 0;
    public static final String ToolbarColor = "008888";
    public static final int RESULT_OK = -1;
    public static final int CROPIMAGE = 1;
    public static final int GETIMAGE = 0;
    public static final String ICONDIR =
            Environment.getExternalStorageDirectory().getPath() + "/Dollars/cache/Icon/";

    ////////////////////////////////////////////////////////////////////////
    ////////////////             initialize          ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "喵佩罗娜!", Toast.LENGTH_SHORT).show();
        XSCHelper.getInstance().loadProjListFromLocal(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.LTGRAY);
        toolbar.setSubtitle(UserInfoBasic.nickName);
        toolbar.setSubtitleTextColor(Color.LTGRAY);
        setSupportActionBar(toolbar);

        menuFadeAdapter = new MenuFadeAdapter(toolbar);
        menuFadeAdapter.setColor(ToolbarColor);
        menuFadeAdapter.setMaxAlpha(255);
        menuFadeAdapter.saveAndNotifySettingsChanged();

        toolBarFadeAdapter = new ToolBarFadeAdapter(toolbar);
        toolBarFadeAdapter.setColor(ToolbarColor);
        toolBarFadeAdapter.setAlpha(255, 0);
        toolBarFadeAdapter.setMenuAdapter(menuFadeAdapter);
        toolBarFadeAdapter.saveAndNotifySettingsChanged();

        initWidgets();
        initSettings();
    }

    /**
     * 初始化UI组件
     */
    private void initWidgets() {
        menu = (SlidingMenu) findViewById(R.id.menu);
        menu.setMenuFadeAdapter(menuFadeAdapter);

        projList = (ScrollView)findViewById(R.id.projList);
//        projList.setToolBarFadeAdapter(toolBarFadeAdapter);
//        toolBarFadeAdapter.enableBackgroundFade(projList, "000000", 255, 0);

        pIconView = (CircleImageView) findViewById(R.id.infoIcon);
        infoName = (TextView) findViewById(R.id.infoName);
        infoCredit = (TextView) findViewById(R.id.infoCredit);
        infoProjs = (TextView) findViewById(R.id.infoProjs);

        projListWrapper = (ViewGroup) projList.getChildAt(0);

        pIconView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivityForResult(
                        SysUtil.getInstance().getChooseImageFromAlbumIntent(), GETIMAGE);
                return false;
            }
        });

        //退出按钮
        final ImageButton logOffButton = (ImageButton) findViewById(R.id.infoButton);
        final Animation offButtonAnimation =
                new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f);
        offButtonAnimation.setDuration(100);
        offButtonAnimation.setFillAfter(true);

        final Animation offButtonAnimationReverse =
                new ScaleAnimation(1.3f, 1.0f, 1.3f, 1.0f);
        offButtonAnimation.setDuration(100);
        offButtonAnimation.setFillAfter(true);

        logOffButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        logOffButton.startAnimation(offButtonAnimation);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        logOffButton.startAnimation(offButtonAnimationReverse);
                        break;
                    case MotionEvent.ACTION_UP:
                        System.exit(0);
                        break;
                }
                return false;
            }
        });
        initChromeRoom();
        //时光机部分
        final AnalogClock chrome = (AnalogClock) findViewById(R.id.chrome);
        chrome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initChromeRoom();
                Intent intent = new Intent(MainActivity.this, ProjChromeList.class);
                startActivity(intent);
            }
        });

        chrome.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                initChromeRoom();
                if(room != null && room.isJoined() && UserInfoEnhanced.projInfoList.size() != 0) {
                    XSCHelper.getInstance().sendProjChromMsg(room, UserInfoEnhanced.projInfoList);
                    Toast.makeText(getBaseContext(), "信息云同步完成", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }

    private void initChromeRoom() {
        if(room == null)
            XSCHelper.getInstance().joinProjChromRoom(new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == XSCHelper.SUCCESS)
                        room = (MultiUserChat) msg.obj;
                    if(msg.what == XSCHelper.MESSAGE) {
                        ArrayList<ProjInfo> infoList = (ArrayList) msg.obj;
                        if(infoList != null)
                            UserInfoEnhanced.chromList.put(
                                    msg.getData().getString(XSCHelper.MSGCLOUD_KEY),
                                    infoList);
                    }
                }
            });
    }

    /**
     * 初始化本地设置
     */
    private void initSettings() {
        pref = getPreferences(Context.MODE_PRIVATE);
        ///////////////
        /// pIconFile
        if (pIconDir == null)
            pIconDir = new File(ICONDIR);
        if (!pIconDir.exists())
            pIconDir.mkdirs();
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////        Refresh Information      ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    //在Handler里处理接受到事件更新UI
    private Handler updateInfo = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            toolbar.setSubtitle(UserInfoBasic.nickName);
            infoName.setText(UserInfoBasic.nickName);
            infoCredit.setText("Credit:             " + UserInfoEnhanced.credit);
            infoProjs.setText("Sync Projs:     " + UserInfoEnhanced.projs);
        }
    };

    private Handler updateIcon = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(pIconFile != null && pIconFile.exists()) {
                pIconView.setImageDrawable(Drawable.createFromPath(pIconFile.getPath()));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        menu.shut();

        if (pIconFile == null && UserInfoBasic.account != null) {
            pIconFile = new File(pIconDir, UserInfoBasic.account + "_pIcon.am");
        }

        if(pIconFile == null || !pIconFile.exists())
            pref.edit().putString(XSCHelper.AVATAG, "").apply();

        new Thread(){
            @Override
            public void run() {
                //必须在新线程更新网络信息
                while(UserInfoBasic.nickName == null || UserInfoBasic.nickName.equals("")) {
                    XSCHelper.getInstance().refreshUserBasicData();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                XSCHelper.getInstance().refreshUserEnhancedData();
                updateInfo.sendEmptyMessage(0);

                XSCHelper.getInstance().getAvatar(pIconFile, pref);
                updateIcon.sendEmptyMessage(0);
            }
        }.start();

        while (projListWrapper.getChildCount() > 0)
            projListWrapper.removeViewAt(projListWrapper.getChildCount() - 1);

        for (ProjInfo info : UserInfoEnhanced.projInfoList)
            addProjInfoView(info);

        XSCHelper.getInstance().saveProjListToLocal(this);
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////         OnActivityResult        ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GETIMAGE:
                    if (SysUtil.getInstance().parseImageIntentToFile(
                            getContentResolver(), data, pIconFile))
                        SysUtil.getInstance().cropImage(
                                pIconFile, 1, 1, 500, 500, CROPIMAGE, this);
                    break;
                case CROPIMAGE:
                    new Thread(){
                        @Override
                        public void run() {
                            //上传头像，更新本地信息
                            XSCHelper.getInstance().setAvatar(
                                    SysUtil.getInstance().parseImageIntentToBytes(
                                            getContentResolver(), data));
                            XSCHelper.getInstance().getAvatar(pIconFile, pref);
                            updateIcon.sendEmptyMessage(0);
                        }
                    }.start();

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        XSCHelper.getInstance().saveProjListToLocal(this);
        super.onStop();
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              按键重写            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTimeRecord <= 2000) {
                //false意义：如果此Activity是本程序栈内最后一个活动，则执行
                //moveTaskToBack：后台执行当前Activity
                moveTaskToBack(false);
            } else {
                exitTimeRecord = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
            }
            return true;       //此行为已经被处理了，不需要继续传递
        }
        return super.onKeyDown(keyCode, event);
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              菜单重写            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, ProjSettingsActivity.class);
        startActivity(intent);
        return false;
    }

    public void addProjInfoView(final ProjInfo info) {
        final ProjInfoView view = new ProjInfoView(this, info);
        projListWrapper.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExplorerActivity.class);
                intent.setData(view.getUri());
                startActivity(intent);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((ViewGroup)(view.getParent())).removeView(view);
                UserInfoEnhanced.projInfoList.remove(view.getInfo());
                XSCHelper.getInstance().saveProjListToLocal(MainActivity.this);
                //TODO: ANIMATION
                return true;
            }
        });
    }
}