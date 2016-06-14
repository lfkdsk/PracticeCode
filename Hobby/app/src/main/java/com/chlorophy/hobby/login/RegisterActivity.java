package com.chlorophy.hobby.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chlorophy.hobby.R;
import com.chlorophy.hobby.xmpp.SMSValid;
import com.chlorophy.hobby.xmpp.UserInfoBasic;
import com.chlorophy.hobby.xmpp.XSCHelper;

public class RegisterActivity extends ActionBarActivity {

    private float oldX, oldY;
    private String account;
    private String password;
    private String nickname;

    public static final int PHONEACCOUNT = 920;
    public static final int VALIDNUM = 921;
    public static final int PASSWORD = 922;
    public static final int NICKNAME = 923;
    public static final int WRONG = -921;
    public static final int LOGINSUCCESS = 929;
    public static final int INITFAILURE = 924;
    public static final int ROOMSUCCESS = 925;
    private int nowState = PHONEACCOUNT;

    private boolean disable = false;
    private Context context;

    private Animation FadeInAnimation;
    private Animation FlyOutAnimation;
    private Animation FadeOutAnimation;
    private Animation FlyBackAnimation;

    private RelativeLayout layout;
    private EditText phoneEdit;
    private EditText validEdit;
    private EditText nameEdit;
    private EditText pswEdit;
    private ProgressBar waitBar;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            disable = false;    // 优先解锁屏幕冻结
            switch (msg.what) {
                case WRONG:
                    if(nowState == PHONEACCOUNT) {
                        Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
                        backViewAnimation(phoneEdit);
                    }
                    if(nowState == VALIDNUM) {
                        backViewAnimation(validEdit);
                        Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PHONEACCOUNT:
                    nowState++;
                    waitBar.setAlpha(0);
                    waitBar.setVisibility(View.GONE);
                    validEdit.setVisibility(View.VISIBLE);
                    break;
                case VALIDNUM:
                    nowState++;
                    Toast.makeText(context, "验证成功", Toast.LENGTH_SHORT).show();
                    waitBar.setAlpha(0);
                    waitBar.setVisibility(View.GONE);
                    pswEdit.setVisibility(View.VISIBLE);
                    break;

                ////////////////////--注册结果--////////////////////////

                case XSCHelper.SUCCESS:
                    UserInfoBasic.account = account;
                    UserInfoBasic.password = password;
                    UserInfoBasic.nickName = nickname;
                    XSCHelper.getInstance().logIn(account, password, new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.what == XSCHelper.SUCCESS) {
                                handler.sendEmptyMessage(LOGINSUCCESS);
                            }
                            else {
                                handler.sendEmptyMessage(INITFAILURE);
                            }
                        }
                    });
                    break;
                case XSCHelper.ERROR:
                    Toast.makeText(context, "连接超时请重试", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case XSCHelper.EXIST:
                    Toast.makeText(context, "此手机号已注册", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case XSCHelper.FAILURE:
                    Toast.makeText(context, "注册失败", Toast.LENGTH_LONG).show();
                    finish();
                    break;

                ////////////////////--登陆结果--////////////////////////

                case LOGINSUCCESS:
                    XSCHelper.getInstance().createRoom(account, 0,  "", new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == XSCHelper.SUCCESS)
                                handler.sendEmptyMessage(ROOMSUCCESS);
                            else
                                handler.sendEmptyMessage(INITFAILURE);
                        }
                    });
                    break;

                ////////////////////--房间结果--////////////////////////

                case ROOMSUCCESS:
                    XSCHelper.getInstance().setUserBasicData(null, nickname);
                    Toast.makeText(context, "用户创建完成", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case INITFAILURE:
                    Toast.makeText(context, "无法完成用户初始化", Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;

        //设置界面
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.8f;
        DisplayMetrics out = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(out);
        params.height = out.heightPixels / 4;
        params.width = out.widthPixels * 4 / 5;
        getWindow().setAttributes(params);

        //获取控件
        phoneEdit = (EditText) findViewById(R.id.registerEdit);
        validEdit = (EditText) findViewById(R.id.validEdit);
        pswEdit = (EditText) findViewById(R.id.passwordEdit);
        nameEdit = (EditText) findViewById(R.id.nicknameEdit);
        waitBar = (ProgressBar) findViewById(R.id.registerProgress);
        layout = (RelativeLayout)findViewById(R.id.register);

        initAnimation();

        validEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layout.removeView(phoneEdit);
                return false;
            }
        });

        pswEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layout.removeView(validEdit);
                return false;
            }
        });

//        nameEdit.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                activity_explorer.removeView(pswEdit);
//                return false;
//            }
//        });

        //初始化验证码获取机制
        SMSValid.init(this, new SMSValid.SMSValidListener() {

            @Override
            public void onMsgSent() {
                handler.sendEmptyMessage(PHONEACCOUNT);
            }

            @Override
            public void onVerifySucceeded() {
                handler.sendEmptyMessage(VALIDNUM);
            }

            @Override
            public void onVerifyFailed() {
                handler.sendEmptyMessage(WRONG);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(disable)
            return true;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            oldX = event.getRawX();
            oldY = event.getRawY();
        }

        if(event.getAction() == MotionEvent.ACTION_UP
                && Math.abs(event.getRawX() - oldX) < 200
                    && oldY - event.getRawY() > 350) {
            switch (nowState) {


                case PHONEACCOUNT:
                    String phone = phoneEdit.getText().toString();
                    if(phone.length() < 11) {
                        Toast.makeText(context, "请输入大陆手机号", Toast.LENGTH_LONG).show();
                        break;
                    }
                    account = phone;
                    if(SMSValid.sendRequest(phone)) {
                        enterWaitAnimation(phoneEdit);
                        disable = true;
                    }
                    break;


                case VALIDNUM:
                    String valid = validEdit.getText().toString();
                    if(valid.length() < 4) {
                        Toast.makeText(context, "请输正确验证码", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if(SMSValid.verifyRequest(account, valid)) {
                        enterWaitAnimation(validEdit);
                        disable = true;
                    }
                    break;


                case PASSWORD:
                    String psw = pswEdit.getText().toString();
                    if(psw.length() < 6) {
                        Toast.makeText(context, "至少六位", Toast.LENGTH_LONG).show();
                        break;
                    }
                    password = psw;
                    enterWaitAnimation(pswEdit);
                    nowState++;
                    waitBar.setAlpha(0);
                    waitBar.setVisibility(View.GONE);
                    nameEdit.setVisibility(View.VISIBLE);
                    nameEdit.setEnabled(true);
                    break;
                case NICKNAME:
                    String nick = nameEdit.getText().toString();
                    if(nick.equals("")) {
                        nick = "Tempest";
                    }
                    nickname = nick;
                    XSCHelper.getInstance().regist(account, password, handler);
                    disable = true;
                    break;
            }
        }
        return true;
    }

    private void enterWaitAnimation(View oldView){
        oldView.startAnimation(FlyOutAnimation);
        waitBar.setVisibility(View.VISIBLE);
        waitBar.startAnimation(FadeInAnimation);
        oldView.setEnabled(false);
    }

    private void backViewAnimation(View oldView) {
        oldView.startAnimation(FlyBackAnimation);
        waitBar.startAnimation(FadeOutAnimation);
        waitBar.setAlpha(0);
        waitBar.setVisibility(View.GONE);
        oldView.setEnabled(true);
    }

    private void initAnimation() {
        FlyOutAnimation = new TranslateAnimation(0, 0, 0, -1000);
        FlyOutAnimation.setDuration(500);
        FlyOutAnimation.setFillAfter(true);

        FadeInAnimation = new AlphaAnimation(0, 1);
        FadeInAnimation.setDuration(500);
        FadeInAnimation.setFillAfter(true);

        FlyBackAnimation = new TranslateAnimation(0, 0, -1000, 0);
        FlyBackAnimation.setDuration(500);
        FlyBackAnimation.setFillAfter(true);

        FadeOutAnimation = new AlphaAnimation(1, 0);
        FadeOutAnimation.setDuration(500);
        FadeOutAnimation.setFillAfter(true);
    }

    @Override
    protected void onStop() {
        SMSValid.release();
        XSCHelper.getInstance().releaseConnection();
        super.onStop();
    }
}
