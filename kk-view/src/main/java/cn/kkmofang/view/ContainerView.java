package cn.kkmofang.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;

import cn.kkmofang.view.value.Position;

/**
 * Created by zhanghailong on 2018/4/19.
 */

public class ContainerView extends ElementView {


    public final ContentView contentView;
    public final ScrollView scrollView;

    public ContainerView(Context context) {
        super(context);
        scrollView = new ScrollView(context,this);
        addView(scrollView);
        contentView = new ContentView(context);
        scrollView.addView(contentView);
    }

    public void setContentSize(int width,int height) {
        contentView.setContentSize(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = getChildCount();

        for(int i=0;i<count;i++) {

            View v = getChildAt(i);

            if(v != null && v.getVisibility() == View.VISIBLE) {

                WeakReference<ViewElement> e = (WeakReference<ViewElement>) v.getTag(cn.kkmofang.unity.R.id.kk_view_element);

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

                    if(_positions != null) {

                        boolean isPosotionView = false;

                        for(int position : _positions.keySet()) {
                            PositionView positionView = _positions.get(position);
                            if(positionView == v) {

                                isPosotionView = true;

                                ViewElement ee = positionView.element();

                                if(ee != null) {

                                    float mleft = ee.margin.left.floatValue(0, 0);
                                    float mright = ee.margin.right.floatValue(0, 0);
                                    float mtop = ee.margin.top.floatValue(0, 0);
                                    float mbottom = ee.margin.bottom.floatValue(0, 0);
                                    int width = (int) Math.ceil( ee.width() + mleft + mright);
                                    int height = (int) Math.ceil( ee.height() + mtop + mbottom);

                                    if(position == Position.Top.intValue()) {
                                        v.layout(0,0,width,height);
                                    } else if(position == Position.Bottom.intValue()) {
                                        v.layout(0,b - t - height,width,height);
                                        break;
                                    } else if(position == Position.Pull.intValue()) {
                                        v.layout(0,-height,width,height);
                                        break;
                                    }
                                }

                                break;
                            }
                        }


                        if(isPosotionView) {
                            continue;
                        }
                    }

                    v.layout(0,0,r-l,b-t);
                }
            }

        }
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        if(_OnScrollListener != null) {
            _OnScrollListener.onScroll(l,t);
        }
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


    private Map<Integer,PositionView> _positions;

    public boolean isPosition(ViewElement element) {
        return _positions != null && _positions.containsKey(element.position.intValue()) && _positions.get(element.position.intValue()).element() != element;
    }

    public void setPosition(ViewElement element,Position position) {

        if(element == null) {

            if(_positions != null && _positions.containsKey(position.intValue())) {
                PositionView v = _positions.get(position.intValue());
                v.setVisibility(View.GONE);

                ViewElement e = v.element();

                if(e != null) {
                    e.obtainView(contentView);
                }

                v.setElement(null);
            }

        } else {

            if(_positions == null) {
                _positions = new TreeMap<>();
            }

            PositionView v;

            if(_positions.containsKey(position.intValue())) {
                v = _positions.get(position.intValue());
            } else {
                v = new PositionView(getContext());
                _positions.put(position.intValue(),v);
                addView(v);
            }

            v.setVisibility(View.VISIBLE);

            ViewElement e = v.element();

            if(e == element) {
                return;
            }

            if(e != null) {
                e.obtainView(contentView);
            }

            v.setElement(element);

            element.obtainView(v);

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

    public static class ScrollView extends android.widget.ScrollView {

        private WeakReference<ContainerView> _containerView;

        public ScrollView(Context context,ContainerView containerView) {
            super(context);
            _containerView = new WeakReference<>(containerView);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l,t,oldl,oldt);
            ContainerView v = _containerView.get();
            if(v != null ){
                v.onScrollChanged(l,t,oldl,oldt);
            }
        }

    }

    public static class PositionView  extends FrameLayout {

        public PositionView(Context context) {
            super(context);
        }

        private ViewElement _element;

        public ViewElement element() {
            return _element;
        }

        public void setElement(ViewElement element) {
            _element = element;
        }


        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

            if(_element != null) {
                View v = _element.view();
                if(v != null) {
                    v.layout(l,t,r,b);
                }
            }

        }
    }
}
