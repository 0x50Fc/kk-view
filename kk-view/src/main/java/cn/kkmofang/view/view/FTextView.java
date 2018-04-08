package cn.kkmofang.view.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import cn.kkmofang.view.IElementView;
import cn.kkmofang.view.ViewElement;

/**
 * Created by wangchao15 on 2018/3/1.
 */

public class FTextView extends TextView{

    public FTextView(Context context) {
        super(context);
    }

    public FTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        measure(MeasureSpec.makeMeasureSpec(right - left, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(bottom - top, MeasureSpec.EXACTLY));
        super.onLayout(changed, left, top, right, bottom);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.onTouchEvent(event);
    }
}
