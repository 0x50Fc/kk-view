package cn.kkmofang.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import cn.kkmofang.image.ImageGravity;
import cn.kkmofang.unity.R;
import cn.kkmofang.image.ImageStyle;
import cn.kkmofang.view.layout.FlexLayout;
import cn.kkmofang.view.layout.HorizontalLayout;
import cn.kkmofang.view.layout.RelativeLayout;
import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Edge;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.Position;
import cn.kkmofang.view.value.Shadow;
import cn.kkmofang.view.value.V;
import cn.kkmofang.view.value.VerticalAlign;


/**
 * Created by zhanghailong on 2018/1/17.
 */

public class ViewElement extends Element implements Cloneable{

    private float _x;
    private float _y;
    private float _width;
    private float _height;
    private float _contentWidth;
    private float _contentHeight;
    private float _contentOffsetX;
    private float _contentOffsetY;
    private Layout _layout = RelativeLayout;

    public IViewContext viewContext;

    public final Edge padding = new Edge();
    public final Edge margin = new Edge();

    public final Pixel width = new Pixel();
    public final Pixel minWidth = new Pixel();
    public final Pixel maxWidth = new Pixel();

    public final Pixel height = new Pixel();
    public final Pixel minHeight = new Pixel();
    public final Pixel maxHeight = new Pixel();

    public final Pixel left = new Pixel();
    public final Pixel top = new Pixel();
    public final Pixel right = new Pixel();
    public final Pixel bottom = new Pixel();

    public final Pixel borderWidth = new Pixel();
    public final Pixel borderRadius = new Pixel();
    public final Shadow shadow = new Shadow();

    public VerticalAlign verticalAlign = VerticalAlign.Top;
    public Position position = Position.None;

    //view背景
    private GradientDrawable gradientDrawable;

    private View _view;

    public ViewElement() {
        viewContext = ViewContext.current();
    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        String v = get(key);

        if("padding".equals(key)) {
            padding.set(v);
        } else if ("margin".equals(key)) {
            margin.set(v);
        } else if ("width".equals(key)) {
            width.set(v);
        } else if ("min-width".equals(key)) {
            minWidth.set(v);
        } else if ("max-width".equals(key)) {
            maxWidth.set(v);
        } else if ("height".equals(key)) {
            height.set(v);
        } else if ("min-height".equals(key)) {
            minHeight.set(v);
        } else if ("max-height".equals(key)) {
            maxHeight.set(v);
        } else if ("left".equals(key)) {
            left.set(v);
        } else if ("top".equals(key)) {
            top.set(v);
        } else if ("right".equals(key)) {
            right.set(v);
        } else if ("bottom".equals(key)) {
            bottom.set(v);
        } else if ("layout".equals(key)) {
            setLayout(v);
        } else if ("vertical-align".equals(key)) {
            verticalAlign = VerticalAlign.valueOfString(v);
        } else if ("position".equals(key)) {
            position = Position.valueOfString(v);
        } else if("border-width".equals(key)) {
            borderWidth.set(v);
        } else if("border-radius".equals(key)) {
            borderRadius.set(v);
        } else if("box-shadow".equals(key)) {
            shadow.set(v);
        }

        if(_view != null) {
            onSetProperty(_view,key,v);
        }
    }

    public float x() {
        return _x;
    }

    public float y() {
        return _y;
    }

    public float left() {
        return _x;
    }

    public float top() {
        return _y;
    }

    public float right() {
        return _x + _width;
    }

    public float bottom() {
        return _y + _height;
    }

    public float width() {
        return _width;
    }

    public float height() {
        return _height;
    }

    public void setWidth(float width) {
        _width = width;
    }

    public void setHeight(float height) {
        _height = height;
    }

    public void setX(float x) {
        _x = x;
    }

    public void setY(float y) {
        _y = y;
    }

    public float contentWidth() {
        return _contentWidth;
    }

    public float contentHeight() {
        return _contentHeight;
    }

    public void setContentSize(float width, float height) {
        _contentWidth = width;
        _contentHeight = height;
    }


    public float contentOffsetX() {
        return _contentOffsetX;
    }

    public float contentOffsetY() {
        return _contentOffsetY;
    }

    public void setContentOffset(float x, float y) {
        _contentOffsetX = x;
        _contentOffsetY = y;
    }

    public Layout layout() {
        return _layout;
    }

    public void setLayout(Layout v) {
        _layout = v;
    }

    public void setLayout(String v) {
        if ("relative".equals(v)) {
            _layout = RelativeLayout;
        } else if ("flex".equals(v)) {
            _layout = FlexLayout;
        } else if ("horizontal".equals(v)) {
            _layout = HorizontalLayout;
        } else {
            _layout = null;
        }
    }

    public View view() {
        return _view;
    }

    public void setView(View v) {
        _view = v;
    }

    public String reuse() {
        String v = get("reuse");
        if (v == null) {
            return "#" + levelId();
        }
        return v;
    }

    public Class<?> viewClass() {
        String v = get("#view");
        if (v == null) {
            return ElementView.class;
        }
        try {
            return Class.forName(v);
        } catch (Throwable e) {
            Log.d(Tag.Tag, Log.getStackTraceString(e));
        }
        return ElementView.class;
    }

    protected View createView() {

        View vv = null;

        Class<?> viewClass = viewClass();
        if (viewClass != null) {
            try {
                vv = (View) viewClass.getConstructor(Context.class).newInstance(viewContext.getContext());
            } catch (Throwable e) {
                Log.d(Tag.Tag, Log.getStackTraceString(e));
            }
        }

        return vv;
    }

    public void obtainView(View view) {

        if (_view != null && _view.getParent() == view) {

            obtainChildrenView();

            return;
        }

        recycleView();

        Class<?> viewClass = this.viewClass();

        View vv = null;

        String reuse = reuse();

        if (reuse != null && !"".equals(reuse)) {

            Map<String, Queue<View>> dequeue = (Map<String, Queue<View>>) view.getTag(R.id.kk_view_dequeue);

            if (dequeue != null && dequeue.containsKey(reuse)) {
                Queue<View> queue = dequeue.get(reuse);
                while (!queue.isEmpty()) {
                    vv = queue.poll();
                    if(viewClass.isAssignableFrom(vv.getClass())) {
                        break;
                    }
                }
            }

        }

        if (vv == null) {
            vv = createView();
        }

        if (vv == null) {
            vv = new ElementView(viewContext.getContext());
        }

        Element p = parent();

        if (p != null && p instanceof ViewElement) {
            ((ViewElement) p).addSubview(vv, this, view);
        } else if (view instanceof ViewGroup) {
            ((ViewGroup) view).addView(vv);
        }

        setView(vv);
        onObtainView(vv);
        onLayout(vv);

        for (String key : keys()) {
            String v = get(key);
            onSetProperty(vv, key, v);
        }

        obtainChildrenView();
    }

    public void recycleView() {

        if (_view != null) {

            ViewGroup p = (ViewGroup) _view.getParent();

            if (p != null) {

                String reuse = reuse();

                if (reuse != null && !"".equals(reuse) && !(_view instanceof SurfaceView)) {

                    Map<String, Queue<View>> dequeue = (Map<String, Queue<View>>) p.getTag(R.id.kk_view_dequeue);

                    if (dequeue == null) {
                        dequeue = new TreeMap<>();
                        p.setTag(R.id.kk_view_dequeue, dequeue);
                    }

                    Queue<View> queue;

                    if (dequeue.containsKey(reuse)) {
                        queue = dequeue.get(reuse);
                    } else {
                        queue = new LinkedList<>();
                        dequeue.put(reuse, queue);
                    }

                    queue.add(_view);

                }

                p.removeView(_view);
            }

            onRecycleView(_view);

            setView(null);

            Element e = firstChild();

            while (e != null) {
                if (e instanceof ViewElement) {
                    ((ViewElement) e).recycleView();
                }
                e = e.nextSibling();
            }

        }
    }

    public void obtainChildrenView() {
        obtainChildrenView(_view);
    }

    protected void obtainChildrenView(View view) {

        if (view != null) {

            Element p = firstChild();

            while (p != null) {
                if (p instanceof ViewElement) {
                    ViewElement e = (ViewElement) p;
                    if (isChildrenVisible(e)) {
                        e.obtainView(view);
                    } else {
                        e.recycleView();
                    }
                }
                p = p.nextSibling();
            }

        }
    }

    public void addSubview(View view, ViewElement element, View toView) {
        if (toView instanceof ViewGroup) {
            ViewGroup p = (ViewGroup) toView;
            String v = element.get("floor");
            if ("back".equals(v)) {
                p.addView(view, 0);
            } else {
                p.addView(view);
            }
            p.requestLayout();
        }
    }

    @Override
    protected void onWillRemoveChildren(Element element) {
        super.onWillRemoveChildren(element);

        if (element instanceof ViewElement) {
            ((ViewElement) element).recycleView();
        }
    }

    @Override
    protected void onDidAddChildren(Element element) {
        super.onDidAddChildren(element);

        if(element instanceof ViewElement) {

            ViewElement e = (ViewElement) element;

            if(e.viewLayer() != View.LAYER_TYPE_NONE) {
                setViewLayer(e.viewLayer());
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }


    @Override
    public void remove() {
        recycleView();
        super.remove();
    }

    public boolean isChildrenVisible(ViewElement element) {

        if(V.booleanValue(element.get("keepalive"),false)) {
            return true;
        }

        int l = (int) Math.max(element.left(), contentOffsetX());
        int t = (int) Math.max(element.top(), contentOffsetY());
        int r = (int) Math.ceil(Math.min(element.right(), contentOffsetX() + width()));
        int b = (int) Math.ceil(Math.min(element.bottom(), contentOffsetY() + height()));

        return r > l && b > t;
    }

    public boolean isHidden() {
        return V.booleanValue(get("hidden"), false);
    }

    public void layoutChildren() {
        if (_layout != null) {
            _layout.layout(this);
        }
    }

    public void willLayout() {

    }

    public void onLayout() {
        if (_view != null) {
            onLayout(_view);
        }
    }

    public void layout(int width, int height) {
        _width = width;
        _height = height;
        if (_layout != null) {
            _layout.layout(this);
        }
        onLayout();
    }

    protected void onSetProperty(View view, String key, String value) {

        if("opacity".equals(key)) {
            setAlpha(value, view);
            view.setAlpha(V.floatValue(value,1.0f));
        } else if("hidden".equals(key)) {
            setVisible(!V.booleanValue(value, true), view);
        } else if("overflow".equals(key)) {
            if ("hidden".equals(value)) {
                if (_viewLayer != View.LAYER_TYPE_HARDWARE) {
                    if (view instanceof ViewGroup) {
                        ((ViewGroup) view).setClipChildren(true);
                        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    }
                }
            }
        } else if("animation".equals(key)) {

            view.clearAnimation();

            if (value != null) {

                Element p = firstChild();

                while (p != null) {
                    if (p instanceof AnimationElement) {
                        if (value.equals(p.get("name"))) {
                            Animation anim = ((AnimationElement) p).getAnimation();
                            if (anim != null) {
                                view.startAnimation(anim);
                            }
                            break;
                        }
                    }
                    p = p.nextSibling();
                }
            }
        } else if("transform".equals(key)) {
            AnimationElement.Transform.valueOf(view.getMatrix(),value);
        } else if(key.startsWith("border") || key.startsWith("background")) {
            setBackground(key, value, view);
        }

        if (view instanceof IElementView) {
            ((IElementView) view).setProperty(view, this, key, value);
        }
    }

    private int _viewLayer = View.LAYER_TYPE_NONE;

    public int viewLayer() {
        return _viewLayer;
    }

    public void setViewLayer(int viewLayer) {

        _viewLayer = viewLayer;

        if(_viewLayer != View.LAYER_TYPE_NONE) {

            View v = view();

            if(v != null) {
                v.setLayerType(_viewLayer,null);
            }

            Element p = parent();

            while(p != null && p instanceof ViewElement) {

                ((ViewElement) p).setViewLayer(viewLayer);

                p = p.parent();
            }
        }
    }

    /**
     * 设置view是否隐藏
     * @param visible
     */
    private void setVisible(boolean visible, View view){
        view.setVisibility(visible?View.VISIBLE:View.GONE);
    }


    /**
     * 设置alpha
     * @param value
     * @param view
     */
    private void setAlpha(String value, View view){
        float alpha = V.floatValue(value, 1.0f);
        if (alpha > 1.0f)alpha = 1.0f;
        if (alpha <= 0)alpha = 0;
        view.setAlpha(alpha);
    }

    /**
     * 设置view背景
     * @param key 属性
     * @param value 属性值
     * @param view
     */
    protected void setBackground(String key, String value, View view){
        Drawable background = view.getBackground();

        if (background == null || gradientDrawable == null){
            gradientDrawable = new GradientDrawable();

            ImageStyle style = new ImageStyle(viewContext.getContext());
            style.gravity = ImageGravity.RESIZE;
            Drawable imgDrawable = viewContext.getImage(get("background-image"), style);
            if (imgDrawable == null){
                background = new LayerDrawable(new Drawable[]{gradientDrawable});
            }else {
                background = new LayerDrawable(new Drawable[]{imgDrawable});
            }
        }

        switch (key){
            case "border-color":
            case "border-width":
                gradientDrawable.setStroke(
                        (int) borderWidth.floatValue(0, 0),
                        Color.valueOf(get("border-color"), 0));
                break;
            case "border-radius":
                borderRadius.set(value);
                gradientDrawable.setCornerRadius(borderRadius.floatValue(0, 0));
                break;
            case "background-color":
                gradientDrawable.setColor(Color.valueOf(value, 0));
                break;
        }

        view.setBackground(background);
    }

    protected void onLayout(View view) {
        if (view instanceof IElementView) {
            ((IElementView) view).layout(view, this);
        }
    }

    protected void onObtainView(View view) {
        view.setTag(R.id.kk_view_element, new WeakReference<ViewElement>(this));
        if (view instanceof IElementView) {
            ((IElementView) view).obtainView(view, this);
        }
    }

    protected void onRecycleView(View view) {

        if (view instanceof IElementView) {
            ((IElementView) view).recycleView(view, this);
        }

        view.setTag(R.id.kk_view_element, null);
    }

    public static interface Layout {
        public void layout(ViewElement element);
    }

    /**
     * 相对布局 "relative"
     */
    public static Layout RelativeLayout = new RelativeLayout();

    /**
     * 流式布局 "flex" 左到右 上到下
     */
    public static Layout FlexLayout = new FlexLayout();

    /**
     * 水平布局 "horizontal" 左到右
     */
    public static Layout HorizontalLayout = new HorizontalLayout();
}
