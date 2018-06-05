package cn.kkmofang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author yujiangtao
 * @version ${VERSION}
 * @since 2018/6/5
 */
public class KKViewPager extends android.support.v4.view.ViewPager{
    private ViewGroup parentView;

    public KKViewPager(Context context) {
        super(context);
    }

    public void setParentView(ViewGroup parentView){
        this.parentView = parentView;
    }

    public ViewGroup getParentView() {
        return parentView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (parentView != null) {
                    //禁止上一层的View不处理该事件,屏蔽父组件的事件
                    if(parentView instanceof ContainerView) {
                        ((ContainerView) parentView).disallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:

                if (parentView != null) {
                    //拦截
                    if(parentView instanceof ContainerView) {
                        ((ContainerView) parentView).disallowInterceptTouchEvent(false);
                    }
                }
                break;


            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}