package cn.kkmofang.view;

import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by zhanghailong on 2018/4/19.
 */

public class ScrollElement extends ViewElement {

    public ScrollElement() {
        super();
        set("#view",ScrollView.class.getName());
    }

    public void setView(View view) {
        ScrollView v = scrollView();
        if(v != null) {
            v.setOnScrollListener(null);
        }
        super.setView(view);
        v = scrollView();
        if(v != null) {

            final WeakReference<ScrollElement> e = new WeakReference<ScrollElement>(this);

            v.setOnScrollListener(new ScrollView.OnScrollListener() {
                @Override
                public void onScroll(int x, int y) {
                    ScrollElement v = e.get();
                    if(v != null) {
                        v.onScroll(x,y);
                    }
                }
            });
        }
    }

    public ScrollView scrollView() {
        View v = view();
        if(v != null && v instanceof ScrollView) {
            return (ScrollView)v;
        }
        return null;
    }

    protected void onScroll(int x,int y) {
        setContentOffset(x,y);
        setNeedObtainChildrening();
    }

    private boolean _obtainChildrening = false;

    public void setNeedObtainChildrening() {

        ScrollView v = scrollView();

        if(v == null) {
            return;
        }

        if(_obtainChildrening) {
            return;
        }

        _obtainChildrening = true;

        v.getHandler().post(new Runnable() {
            @Override
            public void run() {
                obtainChildrenView();
                _obtainChildrening = false;
            }
        });
    }

    @Override
    public void obtainChildrenView() {
        ScrollView v = scrollView();
        if(v != null) {
            obtainChildrenView(v.contentView);
        }
    }

    @Override
    protected void onLayout(View view){
        super.onLayout(view);
        ScrollView v = scrollView();
        if(v != null) {
            int width = contentWidth();
            int height = contentHeight();

            if("scroll".equals(get("overflow-y"))) {
                height = Math.max(height, height());
            }

            if("scroll".equals(get("overflow-x"))) {
                width = Math.max(width, width());
            }

            v.setContentSize(width,height);
        }
    }

    @Override
    public void addSubview(View view, ViewElement element, View toView) {
        if (toView instanceof ViewGroup) {
            ViewGroup p = (ViewGroup) toView;
            String v = element.get("floor");
            if ("front".equals(v)) {
                p.addView(view);
            } else {
                p.addView(view,0);
            }
        }
    }

}
