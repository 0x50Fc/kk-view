package cn.kkmofang.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import cn.kkmofang.view.value.Edge;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.Position;
import cn.kkmofang.view.value.V;

/**
 * Created by zhanghailong on 2018/4/19.
 */

public class ScrollElement extends ViewElement {


    protected static final int TAP_SCROLL_TYPE_NONE = 0;
    protected static final int TAP_SCROLL_TYPE_TOP = 1;
    protected static final int TAP_SCROLL_TYPE_BOTTOM = 2;


    public ScrollElement() {
        super();
    }

    @Override
    protected View createView() {
        if(layout() == HorizontalLayout) {
            return new ContainerView(viewContext.getContext(),true);
        }
        return new ContainerView(viewContext.getContext(),false);
    }

    private boolean _tracking = false;

    private final Pixel _tapbottom = new Pixel();
    private final Pixel _taptop = new Pixel();

    private int _tapScrollType = TAP_SCROLL_TYPE_NONE;

    public void setView(View view) {
        ContainerView v = scrollView();
        if(v != null) {
            v.setOnTouchListener(null);
            v.removeScrollListeners();
        }
        super.setView(view);
        v = scrollView();
        if(v != null) {

            final WeakReference<ScrollElement> e = new WeakReference<ScrollElement>(this);

            v.setScrollListener(new ContainerView.OnScrollListener() {
                @Override
                public void onScroll(int x, int y) {
                    ScrollElement v = e.get();
                    if(v != null) {
                        v.onScroll(x,y);
                    }
                }

                @Override
                public void onStartTracking() {
                    ScrollElement v = e.get();
                    if(v != null) {
                        v.onStartTracking();
                    }
                }

                @Override
                public void onStopTracking() {
                    ScrollElement v = e.get();
                    if(v != null) {
                        v.onStopTracking();
                    }
                }
            });

        }
    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);

        if("taptop".equals(key)) {
            _taptop.set(get(key));
        } else if("tapbottom".equals(key)) {
            _tapbottom.set(get(key));
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
                ContainerView v = scrollView();
                if(v != null) {
                    final WeakReference<ScrollElement> e = new WeakReference<>(this);
                    @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofFloat(v.scrollView, "scrollY", (int) contentOffsetY(), 0);
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

                ContainerView view = scrollView();

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
                            @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofInt(view.scrollView, "scrollX", (int) contentOffsetX(), (int) x);
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
                            @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofInt(view.scrollView, "scrollY", (int) contentOffsetY(), (int) y);
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



    public ContainerView scrollView() {
        View v = view();
        if(v != null && v instanceof ContainerView) {
            return (ContainerView)v;
        }
        return null;
    }

    private String _anchor;

    public void setContentOffset(float x, float y) {
        super.setContentOffset(x,y);

        int tapScrollType = TAP_SCROLL_TYPE_NONE;

        {
            float v = _tapbottom.floatValue(0,0);

            if(v > 0.0f
                    && contentHeight() > height()
                    && y > 0
                    && contentHeight() - y - height() <= v) {
                tapScrollType = TAP_SCROLL_TYPE_BOTTOM;
            }
        }

        if(tapScrollType == TAP_SCROLL_TYPE_NONE) {

            float v = _taptop.floatValue(0,0);

            if(v > 0.0f
                    && y < 0
                    && -y >= v ) {
                tapScrollType = TAP_SCROLL_TYPE_TOP;
            }
        }

        if(tapScrollType != _tapScrollType) {
            _tapScrollType = tapScrollType;
            if(_tapScrollType == TAP_SCROLL_TYPE_BOTTOM) {

                final Element.Event e = new Element.Event(this);

                e.setData(this.data());

                if(hasEvent("tapbottoming")) {
                    emit("tapbottoming",e);
                }

                if(hasEvent("tapbottom")) {
                    View v = view();
                    if (v != null) {

                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                emit("tapbottom", e);
                            }
                        });

                    }
                }
            }
        }

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

            Element p = lastChild();

            String anchor = null;
            ViewElement element = null;

            Edge margin = new Edge();

            while(p != null) {

                if(p instanceof ViewElement) {
                    ViewElement e = (ViewElement) p;
                    anchor = p.get("anchor");
                    if(anchor != null) {
                        margin.set(p.get("anchor-margin"));
                        int ty = (int) e.y() - (int) margin.top.floatValue(0,0);
                        if(y >= ty) {
                            element = e;
                            break;
                        }

                    }
                }

                p = p.prevSibling();
            }

            if(element != null) {

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

    protected void onStartTracking() {
        _tracking = true;
    }

    protected void onStopTracking() {
        _tracking = false;

        if(_tapScrollType == TAP_SCROLL_TYPE_TOP) {

            final Element.Event e = new Element.Event(this);

            e.setData(this.data());

            if(hasEvent("taptoping")) {
                emit("taptoping",e);
            }

            if(hasEvent("taptop")) {
                View v = view();
                if (v != null) {
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            emit("taptop", e);
                        }
                    });
                }
            }

        }

        _tapScrollType = TAP_SCROLL_TYPE_NONE;
    }

    protected void onScroll(int x,int y) {
        setContentOffset(x,y);
        setNeedObtainChildrening();
    }

    private boolean _obtainChildrening = false;

    public void setNeedObtainChildrening() {

        ContainerView v = scrollView();

        if(v == null) {
            return;
        }

        if(_obtainChildrening) {
            return;
        }

        _obtainChildrening = true;

        Handler handler = v.getHandler();
        if (handler != null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    obtainChildrenView();
                    _obtainChildrening = false;
                }
            });
        }
    }

    @Override
    protected void obtainChildrenView(View view) {

        if (view != null) {

            ContainerView v = (ContainerView) view();

            Set<Position> positions = new HashSet<>();

            positions.add(Position.Top);
            positions.add(Position.Bottom);
            positions.add(Position.Pull);

            Element p = firstChild();

            while (p != null) {
                if (p instanceof ViewElement) {
                    ViewElement e = (ViewElement) p;

                    if(e.position == Position.Top) {
                        float offsetY = contentOffsetY();
                        float mtop = e.margin.top.floatValue(0,0);
                        float y = e.y();
                        if(y - offsetY - mtop < 0) {
                            v.setPosition(e,Position.Top);
                            positions.remove(Position.Top);
                            p = p.nextSibling();
                            continue;
                        }
                    } else if(e.position == Position.Bottom) {
                        float y = e.y();
                        float h = e.height();
                        float mbottom = e.margin.bottom.floatValue(0,0);
                        float dy = contentOffsetY() + height() - h -mbottom - y;
                        if(dy > 0) {
                            v.setPosition(e,Position.Bottom);
                            positions.remove(Position.Bottom);
                            p = p.nextSibling();
                            continue;
                        }
                    } else if(e.position == Position.Pull) {
                        if(get("taptop") != null) {
                            v.setPosition(e, Position.Pull);
                            positions.remove(Position.Pull);
                        }
                        p = p.nextSibling();
                        continue;
                    }

                    if (isChildrenVisible(e)) {
                        e.obtainView(view);
                    } else {
                        e.recycleView();
                    }
                }
                p = p.nextSibling();
            }

            for(Position position : positions) {
                v.setPosition(null,position);
            }

        }
    }

    @Override
    public void obtainChildrenView() {
        ContainerView v = scrollView();
        if(v != null) {
            obtainChildrenView(v.contentView);
        }
    }

    @Override
    public boolean isChildrenVisible(ViewElement element) {
        Element pre = element.prevSibling();
        Element next = element.nextSibling();
        while (pre != null){
            if (pre instanceof ViewElement){
                break;
            }
            pre = pre.prevSibling();
        }

        while (next != null){
            if (next instanceof ViewElement){
                break;
            }
            next = next.nextSibling();
        }

        return  (super.isChildrenVisible(element)) ||
                (pre != null && super.isChildrenVisible((ViewElement) pre)) ||
                (next != null && super.isChildrenVisible((ViewElement) next));


    }

    @Override
    protected void onLayout(View view){
        super.onLayout(view);

        if(view instanceof ContainerView) {

            ContainerView v = (ContainerView) view;

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
        }
    }


}
