package cn.kkmofang.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;

import cn.kkmofang.view.event.Event;
import cn.kkmofang.view.value.Edge;
import cn.kkmofang.view.value.V;

/**
 * Created by zhanghailong on 2018/4/19.
 */

public class ScrollElement extends ViewElement {

    public ScrollElement() {
        super();
    }

    @Override
    public Class<?> viewClass() {
        return ScrollView.class;
    }

    private boolean _tracking = false;


    public void setView(View view) {
        ScrollView v = scrollView();
        if(v != null) {
            v.setOnTouchListener(null);
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

            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    ScrollElement v = e.get();
                    if(v != null) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                v.setTracking(true);
                                break;
                            case MotionEvent.ACTION_MOVE:
                                break;
                            default:
                                v.setTracking(false);
                                break;
                        }
                    }

                    return false;
                }
            });

        }
    }

    public boolean isTracking() {
        return _tracking;
    }

    private void setTracking(boolean v) {
        _tracking = v;
        if(!v){
            _anchor = null;
        }
    }

    private boolean _anchorScrolling;
    private boolean _animatting;

    protected void onScrollAnimatedEnd() {
        _animatting = false;
        _anchorScrolling = false;
    }

    @Override
    public void emit(String name, cn.kkmofang.view.event.Event event) {
        super.emit(name,event);

        if(event instanceof Element.Event) {

            if("scrolltop".equals(name)) {
                ((Element.Event) event).setCancelBubble(true);
                ScrollView v = scrollView();
                if(v != null) {
                    final WeakReference<ScrollElement> e = new WeakReference<>(this);
                    @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofFloat(v, "scrollY", (int) contentOffsetY(), 0);
                    animator.setDuration(300);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ScrollElement v = e.get();
                            if(v != null) {
                                v.onScrollAnimatedEnd();
                            }

                        }
                    });
                    _animatting = true;
                    animator.start();

                }
            } else if("anchor".equals(name)) {

                View view = scrollView();

                ((Element.Event) event).setCancelBubble(true);

                if(view != null) {
                    Object data = ((Element.Event) event).data();

                    Edge margin = new Edge();

                    margin.set(V.stringValue(V.get(data, "margin"), null));

                    String anchor = V.stringValue(V.get(data, "anchor"), null);

                    ViewElement element = null;

                    if (anchor != null) {
                        Element p = firstChild();

                        while (p != null) {

                            if (p instanceof ViewElement) {
                                String v = p.get("anchor");
                                if (anchor.equals(v)) {
                                    element = (ViewElement) p;
                                    break;
                                }
                            }

                            p = p.nextSibling();
                        }
                    }

                    if (element != null) {
                        float x = element.x() - margin.left.floatValue(0, 0);
                        float y = element.y() - margin.top.floatValue(0, 0);
                        final WeakReference<ScrollElement> e = new WeakReference<>(this);
                        if(x != contentOffsetX()) {
                            @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofInt(view, "scrollX", (int) contentOffsetX(), (int) x);
                            animator.setDuration(300);
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    ScrollElement v = e.get();
                                    if(v != null) {
                                        v.onScrollAnimatedEnd();
                                    }

                                }
                            });
                            _anchorScrolling = true;
                            _animatting = true;
                            animator.start();
                        }
                        if(y != contentOffsetY()) {
                            @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofInt(view, "scrollY", (int) contentOffsetY(), (int) y);
                            animator.setDuration(300);
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    ScrollElement v = e.get();
                                    if(v != null) {
                                        v.onScrollAnimatedEnd();
                                    }

                                }
                            });
                            _animatting = true;
                            _anchorScrolling = true;
                            animator.start();
                        }
                    }
                }

            }
        }
    }

    public ScrollView scrollView() {
        View v = view();
        if(v != null && v instanceof ScrollView) {
            return (ScrollView)v;
        }
        return null;
    }

    private String _anchor;

    public void setContentOffset(float x, float y) {
        super.setContentOffset(x,y);


        if(hasEvent("scroll")) {

            Map<String,Object> data =new TreeMap<>();

            data.put("tracking",_tracking);
            data.put("x",x);
            data.put("y",y);
            data.put("w",contentWidth());
            data.put("h",contentHeight());
            data.put("width",width());
            data.put("height",height());

            Element.Event e = new Event(this);

            e.setData(data);

            emit("scroll",e);

        }

        if(!_anchorScrolling && hasEvent("anchor")) {

            Element p = firstChild();

            String anchor = null;
            ViewElement element = null;
            ViewElement vElement = null;

            float width = contentWidth();
            float height = contentHeight();

            while(p != null) {

                if(p instanceof ViewElement) {
                    ViewElement e = (ViewElement) p;
                    anchor = p.get("anchor");
                    if(anchor != null) {
                        float tx = e.x() + e.translateX();
                        float ty = e.y() + e.translateY();
                        float twidth = e.width();
                        float theight = e.height();
                        float l  = Math.max(x,tx);
                        float r = Math.min(x + width,tx + twidth);
                        float t = Math.max(y,ty);
                        float b = Math.min(y + height,ty + theight);
                        if(vElement == null) {
                            if (r - l > 0.0f && b - t > 0.0f) {
                                element = vElement = e;
                            }
                            if (r - l >= twidth && b - t >= theight) {
                                break;
                            }
                        } else {
                            if (r - l >= twidth && b - t >= theight) {
                                element = e;
                            }
                            break;
                        }
                    }
                }

                p = p.nextSibling();
            }

            if(element != null) {
                anchor = element.get("anchor");

                if(anchor != null && !anchor.equals(_anchor)) {
                    _anchor = anchor;

                    Map<String,Object> data =new TreeMap<>();

                    data.put("tracking",_tracking);
                    data.put("x",x);
                    data.put("y",y);
                    data.put("w",contentWidth());
                    data.put("h",contentHeight());
                    data.put("width",width());
                    data.put("height",height());

                    Map<String,Object> view = new TreeMap<>();

                    view.put("width",element.width());
                    view.put("height",element.height());
                    view.put("x",element.x());
                    view.put("y",element.y());
                    view.put("anchor",anchor);


                    data.put("view",view);

                    Element.Event e = new Event(this);

                    e.setData(data);

                    emit("anchor",e);

                }
            }
        }


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

    public boolean isChildrenVisible(ViewElement element) {

        switch (element.position) {
            case Top:
            {
                float offsetY = contentOffsetY();
                float mtop = element.margin.top.floatValue(0,0);
                float y = element.y();
                if(y - offsetY - mtop < 0) {
                    element.translateTo(0,offsetY - y + mtop);
                } else {
                    element.translateTo(0,0);
                }

            }
                break;
            case Bottom:
                float y = element.y();
                float h = element.height();
                float mbottom = element.margin.bottom.floatValue(0,0);
                float pbottom = padding.bottom.floatValue(0,0);
                float dy = contentOffsetY() + height() - h -mbottom - y;
                if(dy > 0) {
                    element.translateTo(0,Math.min(dy,pbottom));
                } else {
                    element.translateTo(0,0);
                }

        }

        return super.isChildrenVisible(element);
    }

    @Override
    protected void onLayout(View view){
        super.onLayout(view);
        ScrollView v = scrollView();
        if(v != null) {

            float width = contentWidth();
            float height = contentHeight();

            if("scroll".equals(get("overflow-y"))) {
                height = Math.max(height, height() + 1);
            }

            if("scroll".equals(get("overflow-x"))) {
                width = Math.max(width, width() +1);
            }

            v.setContentSize((int) Math.ceil(width),(int) Math.ceil(height));
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
            p.requestLayout();
        }
    }


}
