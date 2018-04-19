package cn.kkmofang.view.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

import cn.kkmofang.view.v1.PagerElement;


/**
 * Created by wangchao15 on 2018/2/12.
 */

public class FViewPager extends ViewPager {
    private Context mContext;
    private PagerElement pagerElement;
    private boolean scrollable = true;

    public FViewPager(Context context) {
        this(context, null);
    }

    public FViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return scrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollable && super.onInterceptTouchEvent(ev);
    }

    private int mLastX;
    private int mLastY;
    private boolean isScroll = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int touchSlop = ViewConfiguration.get(mContext.getApplicationContext()).getScaledTouchSlop();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                isScroll = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaY) > Math.abs(deltaX) && !isScroll){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else if (Math.abs(deltaX) > touchSlop){
                    isScroll = true;
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                break;
            }
            default:{
                break;
            }

        }
        mLastX = x;
        mLastY = y;


        //处理viewpager自动滑动触摸
        if (pagerElement != null && pagerElement.isAtuoPlay()){
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_CANCEL ||
                    action == MotionEvent.ACTION_OUTSIDE){
                pagerElement.startAutoPlay();
            }
            else if (action == MotionEvent.ACTION_DOWN){
                pagerElement.stopAtuoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void fireRecyclerViewScrollState(Object value, String name, Object obj){
        try {
            Field f = ViewPager.LayoutParams.class.getDeclaredField(name);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public void setPagerElement(PagerElement pagerElement) {
        this.pagerElement = pagerElement;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));

        super.onLayout(changed, l, t, r, b);


    }
}
