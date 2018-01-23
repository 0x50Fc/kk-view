package cn.kkmofang.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by hailong11 on 2018/1/18.
 */

public class KKView extends ViewGroup {

    public KKView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = getChildCount();

        for(int i=0;i<count;i++) {

            View v = getChildAt(i);

            if(v.getVisibility() == View.VISIBLE) {

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
