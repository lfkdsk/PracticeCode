package com.chlorophy.hobby.freutil.ui.TitleMapScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class TitleMapScrollView extends ScrollView{

    private OnScrollListener listener = null;
    private ToolBarFadeAdapter adapter = null;
    private int picHeight;

    public TitleMapScrollView(Context context) {
        super(context);
    }

    public TitleMapScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleMapScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup wrapper = (ViewGroup)getChildAt(0);
        picHeight = wrapper.getChildAt(0).getHeight();

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            int scroll = adapter != null
                    ? picHeight - adapter.getToolbar().getHeight()
                    : picHeight;
            if(getScrollY() < picHeight / 3) {
                smoothScrollTo(0, 0);
            } else if (getScrollY() < scroll) {
                smoothScrollTo(0, scroll);
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(listener != null) {
            listener.onScrolled(this, l, t, oldl, oldt);
        }

        if(adapter != null && t <= picHeight) {
            adapter.onScroll(t, picHeight);
        }
    }

    public void setToolBarFadeAdapter(ToolBarFadeAdapter adapter) {
        this.adapter = adapter;
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }
}
