package cn.kkmofang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import com.kk.view.R;

/**
 * Created by hailong11 on 2018/1/18.
 */

public class ElementView extends FrameLayout {


    public ElementView(Context context) {
        super(context);
    }

    public ElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                                element.left() + element.translateX(),
                                element.top()+ element.translateY(),
                                element.right() + element.translateX(),
                                element.bottom() + element.translateY());
                    }
                }
            }

        }
    }


}
