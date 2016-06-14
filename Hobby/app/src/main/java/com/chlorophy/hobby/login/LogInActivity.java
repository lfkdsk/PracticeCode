package com.chlorophy.hobby.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.chlorophy.hobby.R;
import com.chlorophy.hobby.freutil.system.SysUtil;
import com.chlorophy.hobby.main.MainActivity;
import com.chlorophy.hobby.xmpp.UserInfoBasic;
import com.chlorophy.hobby.xmpp.XSCHelper;

public class LogInActivity extends Activity {

    //本类全局变量
    private EditText nameEdit;
    private EditText pswEdit;
    private ImageButton logIn;
    private ImageView icon;

    private Animation FadeOutAnimation = null;
    private Animation FadeInAnimation = null;
    private AnimationSet FlashFadeInnOutSet = null;
    private Animation logInAnimationReverse = null;

    ////////////////////////////////////////////////////////////////////////
    ////////////////             Handler             ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XSCHelper.FAILURE:     //登录失败
                    Toast.makeText(getApplicationContext(),
                            "登录失败", Toast.LENGTH_LONG).show();
                    LogInErrorAnimationSet();
                    break;
                case XSCHelper.ERROR:       //服务器连接失败
                    //动态更新IP
                    XSCHelper.getInstance().refreshIPbyHost();
                    Toast.makeText(getApplicationContext(),
                            "服务器连接失败", Toast.LENGTH_LONG).show();
                    LogInErrorAnimationSet();
                    break;
                case XSCHelper.SUCCESS:     //登陆成功
                    new Thread(){
                        @Override
                        public void run() {
                            while(UserInfoBasic.nickName == null || UserInfoBasic.nickName.equals("")) {
                                XSCHelper.getInstance().refreshUserBasicData();
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };

    ////////////////////////////////////////////////////////////////////////
    ////////////////              创生部分            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //动态从域名获取IP
        XSCHelper.getInstance().refreshIPbyHost();

        //初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //获取组件实例
        icon = (ImageView) findViewById(R.id.iconView);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        pswEdit = (EditText) findViewById(R.id.pswEdit);
        logIn = (ImageButton) findViewById(R.id.loginButton);
        initAnimation();

        //获取本地化数据，自动填充已有用户名，密码。设置焦点
        final SharedPreferences preferences = this.getPreferences(MODE_PRIVATE);
        nameEdit.setText(preferences.getString("savedAccount", ""));
        if (!nameEdit.getText().toString().equals("")) {
            pswEdit.requestFocus();
        }
        pswEdit.setNextFocusDownId(R.id.loginButton);

        //登陆按钮监听（设置变色，登陆）
        logIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    view.setBackgroundResource(R.drawable.enter_button_hover);
                else {
                    view.setBackgroundResource(R.drawable.enter_button);

                    //获取文本域内容
                    String name = nameEdit.getText().toString();
                    String psw = pswEdit.getText().toString();

                    //本地化存储。优化用户体验
                    preferences.edit().putString("savedAccount", name).apply();

                    //收起小桌板 - 3 -
                    SysUtil.getInstance().hideInputKeyboard(LogInActivity.this, view);

                    //如果输入内容不为空，载入动画，登陆
                    if (!name.equals("") && !psw.equals("")) {
                        XSCHelper.getInstance().logIn(name, psw, handle);
                        LogInAnimationSet();
                    }
                }
                return false;
            }
        });

        //注册按钮监听
        icon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                icon.setVisibility(View.INVISIBLE);
                nameEdit.setVisibility(View.INVISIBLE);
                pswEdit.setVisibility(View.INVISIBLE);
                logIn.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        icon.setVisibility(View.VISIBLE);
        nameEdit.setVisibility(View.VISIBLE);
        pswEdit.setVisibility(View.VISIBLE);
        logIn.setVisibility(View.VISIBLE);
        super.onResume();
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              动画定义            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    //初始化动画
    private void initAnimation() {
        Animation logInAnimation = new TranslateAnimation(0, 0, 0, 400);
        logInAnimation.setFillAfter(true);
        logInAnimation.setDuration(1300);
        logInAnimation.setInterpolator(this,
                android.R.anim.accelerate_decelerate_interpolator);

        FadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        FadeOutAnimation.setDuration(600);
        FadeOutAnimation.setFillAfter(true);

        FadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        FadeInAnimation.setDuration(600);
        FadeInAnimation.setFillAfter(true);

        Animation flashFadeInnOut = new AlphaAnimation(0.0f, 1.0f);
        flashFadeInnOut.setDuration(1000);
        flashFadeInnOut.setFillAfter(true);
        flashFadeInnOut.setInterpolator(this,
                android.R.anim.accelerate_decelerate_interpolator);
        flashFadeInnOut.setRepeatMode(Animation.REVERSE);
        flashFadeInnOut.setRepeatCount(Animation.INFINITE);

        FlashFadeInnOutSet = new AnimationSet(true);
        FlashFadeInnOutSet.addAnimation(flashFadeInnOut);
        FlashFadeInnOutSet.addAnimation(logInAnimation);

        logInAnimationReverse = new TranslateAnimation(0, 0, icon.getY(), 0);
        logInAnimationReverse.setDuration(2000);
        logInAnimationReverse.setInterpolator(this, android.R.anim.decelerate_interpolator);
        logInAnimationReverse.setFillAfter(true);
    }

    //运行登陆动画
    private void LogInAnimationSet() {
        nameEdit.startAnimation(FadeOutAnimation);
        pswEdit.startAnimation(FadeOutAnimation);
        logIn.startAnimation(FadeOutAnimation);

        nameEdit.setEnabled(false);
        pswEdit.setEnabled(false);
        logIn.setEnabled(false);
        icon.setEnabled(false);

        icon.startAnimation(FlashFadeInnOutSet);
    }

    //运行“返回正常”动画
    private void LogInErrorAnimationSet() {
        icon.startAnimation(logInAnimationReverse);

        nameEdit.startAnimation(FadeInAnimation);
        pswEdit.startAnimation(FadeInAnimation);
        logIn.startAnimation(FadeInAnimation);

        nameEdit.setEnabled(true);
        pswEdit.setEnabled(true);
        logIn.setEnabled(true);
        icon.setEnabled(true);
    }

}
