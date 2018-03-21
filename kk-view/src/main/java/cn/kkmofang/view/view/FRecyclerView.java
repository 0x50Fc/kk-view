package cn.kkmofang.view.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.kkmofang.view.ScrollElement;

/**
 * Created by wangchao15 on 2018/2/9.
 */

public class FRecyclerView extends RecyclerView implements NestedScrollingParent{
    private Context mContext;
    private boolean scrollStatechanged;
    public FRecyclerView(Context context) {
        this(context, null);
    }

    public FRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setNestedScrollingEnabled(false);
        setFocusable(false);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
    }

    private int mLastX;
    private int mLastY;
    private boolean isScroll = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int touchSlop = ViewConfiguration.get(mContext.getApplicationContext()).getScaledTouchSlop();
        ScrollElement.LayoutManager manager = (ScrollElement.LayoutManager) getLayoutManager();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                isScroll = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (getLayoutManager() != null && getLayoutManager() instanceof ScrollElement.LayoutManager){

                    int vertical = manager.getOffset() + manager.getVerticalSpace() - deltaY;
                    int horizontal = manager.getOffset() + manager.getHorizontalSpace() - deltaX;
                    if (manager.canScrollVertically()){//当前view垂直滑动时
                        boolean shouldIntercept = manager.shouldIntercept();
                        if (!shouldIntercept){
                            if (Math.abs(deltaY) < Math.abs(deltaX) && !isScroll){//用于当外部容器水平滑动时
                                getParent().requestDisallowInterceptTouchEvent(false);
                            }else if (Math.abs(deltaY) > touchSlop){
                                isScroll = true;
                            }
                        }

                        if (shouldIntercept){
                            if ((vertical >= manager.getTotalHeight() && deltaY < 0) ||
                                    (manager.getOffset() == 0&& deltaY > 0)){
                                FRecyclerView scrollView = manager.getParentScrollView();
                                if (scrollView != null){
                                    scrollView.scrollStatechanged = true;
                                }
                                getParent().requestDisallowInterceptTouchEvent(false);
                            }
                        }

                    }else if (manager.canScrollHorizontally()){
                        boolean shouldIntercept = manager.shouldIntercept();
                        if (!shouldIntercept){
                            if (Math.abs(deltaX) < Math.abs(deltaY) && !isScroll){//用于当外部容器垂直滑动时
                                getParent().requestDisallowInterceptTouchEvent(false);
                            }else if (Math.abs(deltaX) > touchSlop){
                                isScroll = true;
                            }
                        }


                        if (manager.shouldIntercept()){
                            //同向滑动时
                            if ((horizontal >= manager.getTotalWidth() && deltaX < 0) ||
                                    (manager.getOffset() == 0 && deltaX > 0)){
                                FRecyclerView scrollView = manager.getParentScrollView();
                                if (scrollView != null){
                                    scrollView.scrollStatechanged = true;
                                }
                                getParent().requestDisallowInterceptTouchEvent(false);
                            }
                        }
                    }


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
        return super.dispatchTouchEvent(ev);
    }

    private void fireRecyclerViewScrollState(int value, String name, Object obj){
            try {
                Field f = RecyclerView.class.getDeclaredField(name);
                f.setAccessible(true);
                f.set(obj, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (scrollStatechanged){//此处用于同向滑动时产生的滑动跳跃
            int valueY  = (int) (e.getY() + 0.5f);
            int valueX = (int) (e.getX() + 0.5f);
            fireRecyclerViewScrollState(valueY, "mLastTouchY", this);
            fireRecyclerViewScrollState(valueX, "mLastTouchX", this);
            scrollStatechanged = false;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        super.onInterceptTouchEvent(e);
        int action = e.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            return false;
        }
        return true;
    }


}
