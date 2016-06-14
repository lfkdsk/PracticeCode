package com.chlorophy.hobby.xmpp;

import android.content.Context;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SMSValid {
    private static String APPKEY = "842d19003518";
    private static String APPSEC = "520085d7bdcc7308387a8e599b3b9945";
    private static String CHINA = "86";

    private static Context context;
    private static EventHandler handler;
    private static SMSValidListener listener;
    private static long registTime = 0;
    private static boolean isReady = false;

    private SMSValid(){}

    /**
     * 初始化资源，准备回调，创建监听器
     */
    public static void init(Context con, SMSValidListener smsValidListener) {
        if(!isReady) {
            context = con;
            listener = smsValidListener;
            SMSSDK.initSDK(context, APPKEY, APPSEC);
            handler = new EventHandler(){
                @Override
                public void afterEvent(int event, int result, Object data) {
                    if(listener == null)
                        return;

                    if(result == SMSSDK.RESULT_COMPLETE) {
                        if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                            listener.onVerifySucceeded();
                        if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE)
                            listener.onMsgSent();
                    } else {
                        listener.onVerifyFailed();
                        ((Throwable)data).printStackTrace();
                    }
                }
            };
            SMSSDK.registerEventHandler(handler);
            isReady = true;
        }
    }

    /**
     * 发送验证码请求
     */
    public static boolean sendRequest(String phone) {
        if (!isReady)
            return false;

        if (!XSCHelper.getInstance().connect()) {
            Toast.makeText(context, "当前喵网络链接", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (System.currentTimeMillis() - registTime >= 60000) {
            registTime = System.currentTimeMillis();
            SMSSDK.getVerificationCode(CHINA, phone);
            return true;
        } else {
            Toast.makeText(context, "请求验证码间隔少于60喵，请原地喵", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 核实验证码，结果发送到Listener去
     */
    public static boolean verifyRequest(String phone, String code) {
        if (!isReady)
            return false;

        if (!XSCHelper.getInstance().connect()) {
            Toast.makeText(context, "当前喵网络链接", Toast.LENGTH_SHORT).show();
            return false;
        }

        SMSSDK.submitVerificationCode(CHINA, phone, code);
        return true;
    }



    /**
     * 注册成功以后要释放资源，释放以后如需使用还要再重新初始化
     */
    public static void release() {
        isReady = false;
        SMSSDK.unregisterAllEventHandler();
    }

    public static void setListener(SMSValidListener listener) {
        SMSValid.listener = listener;
    }

    /**
     * 短信验证Listener
     */
    public interface SMSValidListener {
        void onVerifySucceeded();
        void onVerifyFailed();
        void onMsgSent();
    }

}
