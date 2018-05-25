package cn.kkmofang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import java.lang.ref.WeakReference;
import cn.kkmofang.unity.R;


/**
 * Created by zhanghailong on 2018/1/18.
 */

public class ElementView extends FrameLayout implements IElementView{


    public ElementView(Context context) {
        super(context);
        setClipChildren(false);
    }

    public ElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = getDefaultSize(Integer.MAX_VALUE,widthMeasureSpec);
        int height = getDefaultSize(Integer.MAX_VALUE,heightMeasureSpec);

        WeakReference<ViewElement> e = (WeakReference<ViewElement>) getTag(R.id.kk_view_element);

        if(e != null) {
            ViewElement element = e.get();
            if (element != null) {
                width = (int) Math.ceil(element.width());
                height =(int) Math.ceil(element.height());
            }
        }

        setMeasuredDimension(width, height);

        measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        for(int i=0;i<count;i++) {

            View v = getChildAt(i);

            if(v != null && v.getVisibility() == View.VISIBLE) {

                WeakReference<ViewElement> e = (WeakReference<ViewElement>) v.getTag(R.id.kk_view_element);

                if(e != null) {
                    ViewElement element = e.get();
                    if(element != null) {
                        v.layout(
                                (int) (element.left()),
                                (int) (element.top()),
                                (int) Math.ceil(element.right()),
                                (int) Math.ceil(element.bottom()));
                    }
                } else {
                    v.layout(0,0,r-l,b-t);
                }
            }

        }
    }

    @Override
    public void setProperty(View view, ViewElement element, String key, String value) {

    }

    private boolean _layouting = false;

    protected void setNeedsLayout() {

        if(_layouting) {
            return;
        }

        _layouting = true;

        final WeakReference<ElementView> v = new WeakReference<>(this);

        getHandler().post(new Runnable() {
            @Override
            public void run() {
                ElementView vv = v.get();
                if(vv != null) {
                    vv._layouting = false;
                    vv.requestLayout();
                }
            }
        });

    }

    @Override
    public void layout(View view, ViewElement element) {
        setNeedsLayout();
    }

    @Override
    public void obtainView(View view, ViewElement element) {

    }

    @Override
    public void recycleView(View view, ViewElement element) {

    }
}
