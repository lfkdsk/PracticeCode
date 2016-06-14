package com.chlorophy.hobby.freutil.ui.SlidingMenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.nineoldandroids.view.ViewHelper;

public class SlidingMenu extends HorizontalScrollView{
    private int screenWidth;
    private int menuRightPadding;
    private int menuWidth;
    private int halfMenuWidth;  //滑动落脚点确定界限

    private Context context;
    private ViewGroup menu;
    private ViewGroup content;
    private MenuFadeAdapter menuFadeAdapter = null;

    private boolean isPoped = false;
    private boolean onDraw = false;

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setMenuRightPadding(100);

        WindowManager manager =
                (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics disp = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(disp);
        screenWidth = disp.widthPixels;
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context) {
        this(context, null, 0);
    }

    /**
     * 确定大小后初始化数据
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!onDraw) {
            menuWidth = screenWidth - menuRightPadding;
            halfMenuWidth = menuWidth / 2;

            //对两个Layout（菜单/主界面）进行布局（设置宽）
            LinearLayout wrapper = (LinearLayout)getChildAt(0);
            menu = (ViewGroup) wrapper.getChildAt(0);
            menu.getLayoutParams().width = menuWidth;

            content = (ViewGroup) wrapper.getChildAt(1);
            content.getLayoutParams().width = screenWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 布局初始化（在初始化时收起菜单）
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed) {
            scrollTo(menuWidth, 0);
            onDraw = true;
        }
    }

    /**
     * 当触摸为“抬起”时，判断应吸附那个方向作为落脚点
     */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            // getScrollX() 屏幕左上角距离复合View左上角的距离
            if(getScrollX() < halfMenuWidth )
                pop();
            else
                pack();
            return true;    //已处理，不再继续传递
        }
        return super.onTouchEvent(event);
    }

    /**
     * 移动时放大缩小动画
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / menuWidth;     //var [0 - 1]

        ViewHelper.setTranslationX(menu, 0.7f * menuWidth * scale);

        ViewHelper.setPivotX(content, 0);
        ViewHelper.setPivotY(content, content.getHeight() / 2);

        if(menuFadeAdapter != null)
            menuFadeAdapter.sendEmptyMessage((int)(scale*255));
    }

    /**
     * 收起菜单
     */
    public void pack() {
        smoothScrollTo(menuWidth, 0);
        isPoped = false;
        if (menuFadeAdapter != null) {
            menuFadeAdapter.sendEmptyMessage(255);
        }
    }

    public void shut() {
        scrollTo(menuWidth, 0);
        isPoped = false;
        if (menuFadeAdapter != null) {
            menuFadeAdapter.sendEmptyMessage(255);
        }
    }

    /**
     * 弹出菜单
     */
    public void pop() {
        smoothScrollTo(0, 0);
        isPoped = true;
        if (menuFadeAdapter != null) {
            menuFadeAdapter.sendEmptyMessage(0);
        }
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if(isPoped)
            pack();
        else
            pop();
    }

    /**
     * 设置菜单右边正常页面留多少
     *
     * @param padding 显示菜单时右边正常页面的保留部分，单位dp，整数型
     */
    public void setMenuRightPadding(int padding) {
        menuRightPadding =
                (int)(context.getResources().getDisplayMetrics().density
                        * padding + 0.5f);
    }

    /**
     * 设置移动菜单时的Listener
     *
     * @param adapter 侦听者，MenuFadeAdapter类型
     */
    public void setMenuFadeAdapter(MenuFadeAdapter adapter) {
        menuFadeAdapter = adapter;
    }

}