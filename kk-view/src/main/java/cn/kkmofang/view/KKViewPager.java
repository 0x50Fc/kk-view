package cn.kkmofang.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

public class KKViewPager extends ViewPager {

    public KKViewPager(Context context) {
        this(context, null);
    }

    public KKViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContainerView getParentView(){
        ViewParent parent = getParent();
        while (parent != null){
            if (parent instanceof ContainerView){
                return (ContainerView) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ContainerView parent = getParentView();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (parent != null){
                    parent.setCancelPullScrolling(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (parent != null){
                    parent.setCancelPullScrolling(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public void setListener(final OnPageLoopChangeListener listener) {
        this.listener = listener;
        ContainerView parent = getParentView();
        if (parent != null){
            parent.setScrollListener(new ContainerView.OnScrollListener() {
                @Override
                public void onScroll(int x, int y) {

                }

                @Override
                public void onStartTracking() {
                    if (listener != null){
                        listener.onStopLoop();
                    }
                }

                @Override
                public void onStopTracking() {
                    if (listener != null){
                        listener.onStartLoop();
                    }
                }
            });
        }
    }

    private OnPageLoopChangeListener listener;
    public interface OnPageLoopChangeListener{
        void onStopLoop();

        void onStartLoop();
    }

}
