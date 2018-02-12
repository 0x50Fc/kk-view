package cn.kkmofang.view.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.view.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.kkmofang.view.PagerElement;
import cn.kkmofang.view.ViewElement;


/**
 * Created by wangchao15 on 2018/2/12.
 */

public class FViewPager extends ViewPager {
    private Context mContext;
    private PagerElement pagerElement;

    public FViewPager(Context context) {
        this(context, null);
    }

    public FViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = pagerElement == null?0:pagerElement._elements.size();
        for (int i = 0; i < count; i++) {
            ViewElement p = pagerElement._elements.get(i);
            if (p.view() == null){
                p.obtainView(this);
            }
            ViewPager.LayoutParams params = new ViewPager.LayoutParams();
            fireRecyclerViewScrollState(true, "needsMeasure", params);
            p.view().setLayoutParams(params);
        }

        measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));

        super.onLayout(changed, l, t, r, b);


    }
}
