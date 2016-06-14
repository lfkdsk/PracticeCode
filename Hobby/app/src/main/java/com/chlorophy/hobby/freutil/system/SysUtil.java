package com.chlorophy.hobby.freutil.system;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SysUtil {

    //本类全局变量
    private static SysUtil util = new SysUtil();

    ////////////////////////////////////////////////////////////////////////
    ////////////////              单例模式            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    //单例模式
    private SysUtil() {
    }

    synchronized public static SysUtil getInstance() {
        return util;
    }


    ////////////////////////////////////////////////////////////////////////
    ////////////////              输入输出            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    /**
     * 在某个环境（context）中收起小桌板（输入法面板）
     *
     * @param context 所处的环境Context，可以是getApplicationContext()
     *                获取应用上下文，也可以是当前Activity的上下文，也就是
     *                (Activity.)this。（前面的活动名在非内部类时可以省略）
     * @param view    触发者（必须与输入法调用者控件在同一窗口）
     */
    public void hideInputKeyboard(Context context, View view){
        //收起小桌板 - 3 -
        InputMethodManager manager =
                (InputMethodManager) context.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              日期时间            ///////////////////////
    ////////////////////////////////////////////////////////////////////////
    public String getDateFormated(){
        String date = Calendar.getInstance().get(Calendar.YEAR) + "-"
                + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return date;
    }

    public String getDateAndTimeFormated(){
        String date = Calendar.getInstance().get(Calendar.YEAR) + "-"
                + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "  "
                + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":"
                + Calendar.getInstance().get(Calendar.MINUTE) + ":"
                + Calendar.getInstance().get(Calendar.SECOND);
        return date;
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              获取图片            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    /**
     * 获取照片并启动Intent
     * @param fragment     启动环境（Fragment）
     * @param requestCode  标志码，onActivityResult的识别标志
     */
    public void chooseImageFromAlbum(Fragment fragment, int requestCode){
        fragment.startActivityForResult(getChooseImageFromAlbumIntent(), requestCode);
    }

    /**
     * 获取照片并启动Intent
     * @param activity     启动环境（Activity）
     * @param requestCode  标志码，onActivityResult的识别标志
     */
    public void chooseImageFromAlbum(Activity activity, int requestCode){
        activity.startActivityForResult(getChooseImageFromAlbumIntent(), requestCode);
    }

    /**
     * 获取照片Intent
     *
     * @return Intent      返回包装好的Intent
     */
    public Intent getChooseImageFromAlbumIntent(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              剪裁图片            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    /**
     * 获取剪裁Intent
     *
     * @param uri          图片内容Uri
     * @param aspectX      剪裁框X比例
     * @param aspectY      剪裁框Y比例
     * @param outputX      输出尺寸X大小
     * @param outputY      输出尺寸Y大小
     * @return Intent      返回包装好的Intent
     */
    public Intent getCropImageIntent(Uri uri, int aspectX, int aspectY, int outputX,
                                       int outputY){
        //初始化Intent
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //设置剪裁框比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        //设置图片输出大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //设置图片格式以及返回类型
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", "true");
        intent.putExtra("return-data", "true");
        return intent;
    }

    /**
     * 获取剪裁Intent
     *
     * @param file         图片内容File
     * @param aspectX      剪裁框X比例
     * @param aspectY      剪裁框Y比例
     * @param outputX      输出尺寸X大小
     * @param outputY      输出尺寸Y大小
     * @return Intent      返回包装好的Intent
     */
    public Intent getCropImageIntent(File file, int aspectX, int aspectY,
                                     int outputX, int outputY){
        return getCropImageIntent(
                Uri.fromFile(file), aspectX, aspectY, outputX, outputY);
    }

    /**
     * 启动图片剪裁
     *
     * @param file         图片内容File
     * @param aspectX      剪裁框X比例
     * @param aspectY      剪裁框Y比例
     * @param outputX      输出尺寸X大小
     * @param outputY      输出尺寸Y大小
     * @param fragment     启动环境
     * @param requestCode  启动标识码（onActivityResult）
     */
    public void cropImage(File file, int aspectX, int aspectY, int outputX,
                          int outputY, int requestCode, Fragment fragment){
        fragment.startActivityForResult(
                getCropImageIntent(file, aspectX, aspectY, outputX, outputY), requestCode);
    }

    /**
     * 启动图片剪裁
     *
     * @param uri          图片内容Uri
     * @param aspectX      剪裁框X比例
     * @param aspectY      剪裁框Y比例
     * @param outputX      输出尺寸X大小
     * @param outputY      输出尺寸Y大小
     * @param fragment     启动环境
     * @param requestCode  启动标识码（onActivityResult）
     */
    public void cropImage(Uri uri, int aspectX, int aspectY, int outputX,
                          int outputY, int requestCode, Fragment fragment){
        fragment.startActivityForResult(
                getCropImageIntent(uri, aspectX, aspectY, outputX, outputY), requestCode);
    }

    /**
     * 启动图片剪裁
     *
     * @param file         图片内容File
     * @param aspectX      剪裁框X比例
     * @param aspectY      剪裁框Y比例
     * @param outputX      输出尺寸X大小
     * @param outputY      输出尺寸Y大小
     * @param activity     启动环境
     * @param requestCode  启动标识码（onActivityResult）
     */
    public void cropImage(File file, int aspectX, int aspectY, int outputX,
                          int outputY, int requestCode, Activity activity){
        activity.startActivityForResult(
                getCropImageIntent(file, aspectX, aspectY, outputX, outputY), requestCode);
    }

    /**
     * 启动图片剪裁
     *
     * @param uri          图片内容Uri
     * @param aspectX      剪裁框X比例
     * @param aspectY      剪裁框Y比例
     * @param outputX      输出尺寸X大小
     * @param outputY      输出尺寸Y大小
     * @param activity     启动环境
     * @param requestCode  启动标识码（onActivityResult）
     */
    public void cropImage(Uri uri, int aspectX, int aspectY, int outputX,
                          int outputY, int requestCode, Activity activity){
        activity.startActivityForResult(
                getCropImageIntent(uri, aspectX, aspectY, outputX, outputY), requestCode);
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              解析图片            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    /**
     * 从带图像的Intent中解析图像到Bitmap
     *
     * @param resolver     解析器，需要当前context.getContentResolver()
     * @param data         带数据的Intent
     * @return Bitmap      解析成功返回Bitmap，否则返回null
     */
    public Bitmap parseImageIntentToBitmap(ContentResolver resolver, Intent data) {
        Uri photoUri = data.getData();
        Bitmap photo = null;

        if(photoUri != null){
            try {
                photo = MediaStore.Images.Media.getBitmap(resolver, photoUri);
            } catch (IOException e) {
                photo = null;
            }
        } else {
            Bundle extra = data.getExtras();
            if(extra != null){
                photo = (Bitmap)extra.get("data");
            }
        }
        return photo;
    }

    /**
     * 从带图像的Intent中解析图像到byte[]
     *
     * @param resolver     解析器，需要当前context.getContentResolver()
     * @param data         带数据的Intent
     * @return byte[]      解析成功返回byte[]，否则返回null
     */
    public byte[] parseImageIntentToBytes(ContentResolver resolver, Intent data){
        ByteArrayOutputStream buffer = parseImageIntentToStream(resolver, data);
        return buffer != null ? buffer.toByteArray() : null;
    }

    /**
     * 从带图像的Intent中解析图像到ByteArrayOutputStream
     *
     * @param resolver     解析器，需要当前context.getContentResolver()
     * @param data         带数据的Intent
     * @return ByteArrayOutputStream      解析成功返回ByteArrayOutputStream，否则返回null
     */
    public ByteArrayOutputStream parseImageIntentToStream(
            ContentResolver resolver, Intent data) {
        //使用转换流，并压缩图片
        ByteArrayOutputStream buffer = null;
        Bitmap image = parseImageIntentToBitmap(resolver, data);
        try {
            if (image != null) {
                buffer = new ByteArrayOutputStream();
                if (image.compress(Bitmap.CompressFormat.JPEG, 100, buffer)) {
                    buffer.flush();
                } else {
                    buffer.close();
                    buffer = null;
                }
            }
        } catch (IOException e) {
            buffer = null;
        }
        return buffer;
    }

    /**
     * 从带图像的Intent中解析图像到File
     *
     * @param resolver     解析器，需要当前context.getContentResolver()
     * @param data         带数据的Intent
     * @param dir          输出文件路径
     * @return boolean     解析成功返回true，否则返回false
     */
    public boolean parseImageIntentToFile(ContentResolver resolver, Intent data, File dir) {
        try {
            FileOutputStream out = new FileOutputStream(dir);
            ByteArrayOutputStream buffer = parseImageIntentToStream(resolver, data);
            if(buffer == null)
                return false;
            buffer.writeTo(out);
            buffer.flush();
            buffer.close();
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
