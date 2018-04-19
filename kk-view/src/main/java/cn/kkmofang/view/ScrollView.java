package cn.kkmofang.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by zhanghailong on 2018/4/19.
 */

public class ScrollView extends android.widget.ScrollView {

    public final ContentView contentView;

    public ScrollView(Context context) {
        super(context);
        contentView = new ContentView(context);
        addView(contentView);
    }

    public void setContentSize(int width,int height) {
        contentView.setContentSize(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l,t,oldl,oldt);
        if(_OnScrollListener != null) {
            _OnScrollListener.onScroll(l,t);
        }
    }

    private OnScrollListener _OnScrollListener;

    public void setOnScrollListener(OnScrollListener v) {
        _OnScrollListener = v;
    }

    public OnScrollListener getOnScrollListener() {
        return _OnScrollListener;
    }

    public static interface OnScrollListener {
        void onScroll(int x,int y);
    }

    public static class ContentView extends ElementView {

        private int _contentWidth;
        private int _contentHeight;

        public ContentView(Context context) {
            super(context);
        }

        public void setContentSize(int width,int height) {
            _contentWidth = width;
            _contentHeight = height;
            requestLayout();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(_contentWidth,_contentHeight);
            measureChildren(widthMeasureSpec,heightMeasureSpec);
        }

    }
}
