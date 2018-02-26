package cn.kkmofang.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import cn.kkmofang.view.layout.FlexLayout;
import cn.kkmofang.view.layout.HorizontalLayout;
import cn.kkmofang.view.layout.RelativeLayout;
import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Edge;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.Position;
import cn.kkmofang.view.value.V;
import cn.kkmofang.view.value.VerticalAlign;
import com.kk.view.R;
import static android.R.attr.radius;


/**
 * Created by hailong11 on 2018/1/17.
 */

public class ViewElement extends Element implements Cloneable{

    private int _x;
    private int _y;
    private int _width;
    private int _height;
    private int _contentWidth;
    private int _contentHeight;
    private int _contentOffsetX;
    private int _contentOffsetY;
    private int _translateX;
    private int _translateY;
    private Layout _layout = RelativeLayout;

    public final ViewContext viewContext;

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

    public VerticalAlign verticalAlign = VerticalAlign.Top;
    public Position position = Position.None;


    private View _view;

    public ViewElement() {
        viewContext = ViewContext.current();
    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);

        String v = get(key);

        if ("padding".equals(key)) {
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
            verticalAlign = VerticalAlign.valueOf(v);
        } else if ("position".equals(key)) {
            position = Position.valueOf(v);
        }

        if (_view != null) {
            onSetProperty(_view, key, v);
        }
    }

    public int x() {
        return _x;
    }

    public int y() {
        return _y;
    }

    public int left() {
        return _x;
    }

    public int top() {
        return _y;
    }

    public int right() {
        return _x + _width;
    }

    public int bottom() {
        return _y + _height;
    }

    public int width() {
        return _width;
    }

    public int height() {
        return _height;
    }

    public void setWidth(int width) {
        _width = width;
    }

    public void setWidth(float width) {
        _width = (int) Math.ceil(width);
    }

    public void setHeight(int height) {
        _height = height;
    }

    public void setHeight(float height) {
        _height = (int) Math.ceil(height);
    }

    public void setX(int x) {
        _x = x;
    }

    public void setX(float x) {
        _x = (int) Math.ceil(x);
    }

    public void setY(int y) {
        _y = y;
    }

    public void setY(float y) {
        _y = (int) Math.ceil(y);
    }

    public int contentWidth() {
        return _contentWidth;
    }

    public int contentHeight() {
        return _contentHeight;
    }

    public void setContentSize(int width, int height) {
        _contentWidth = width;
        _contentHeight = height;
    }

    public void setContentSize(float width, float height) {
        _contentWidth = (int) Math.ceil(width);
        _contentHeight = (int) Math.ceil(height);
    }

    public int contentOffsetX() {
        return _contentOffsetX;
    }

    public int contentOffsetY() {
        return _contentOffsetY;
    }

    public void setContentOffset(int x, int y) {
        _contentOffsetX = x;
        _contentOffsetY = y;
    }

    public int translateX() {
        return _translateX;
    }

    public int translateY() {
        return _translateY;
    }

    public void translateTo(int x, int y) {
        _translateX = x;
        _translateY = y;
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

    public void obtainView(View view) {

        if (_view != null && _view.getParent() == view) {
            obtainChildrenView();
            return;
        }

        recycleView();

        View vv = null;

        String reuse = reuse();

        if (reuse != null && !"".equals(reuse)) {

            Map<String, Queue<View>> dequeue = (Map<String, Queue<View>>) view.getTag(R.id.kk_view_dequeue);

            if (dequeue != null && dequeue.containsKey(reuse)) {
                Queue<View> queue = (Queue<View>) dequeue.get(reuse);
                if (!queue.isEmpty()) {
                    vv = queue.poll();
                }
            }

        }

        if (vv == null) {
            Class<?> viewClass = viewClass();
            if (viewClass != null) {
                try {
                    vv = (View) viewClass.getConstructor(Context.class).newInstance(viewContext.context);
                } catch (Throwable e) {
                    Log.d(Tag.Tag, Log.getStackTraceString(e));
                }
            }
        }

        if (vv == null) {
            vv = new ElementView(viewContext.context);
        }

        Element p = parent();

        if (p != null && p instanceof ViewElement) {
            ((ViewElement) p).addSubview(vv, this, view);
        } else if (view instanceof ViewGroup) {
            ((ViewGroup) view).addView(vv);
        }

        onObtainView(vv);
        onLayout(vv);
        setView(vv);

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

                if (reuse != null && !"".equals(reuse)) {

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

        if (_view != null) {

            Element p = firstChild();

            while (p != null) {
                if (p instanceof ViewElement) {
                    ViewElement e = (ViewElement) p;
                    if (isChildrenVisible(e)) {
                        e.obtainView(_view);
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

        int l = Math.max(element.left() + element.translateX(), contentOffsetX());
        int t = Math.max(element.top() + element.translateY(), contentOffsetY());
        int r = Math.min(element.right() + element.translateX(), contentOffsetX() + width());
        int b = Math.min(element.bottom() + element.translateY(), contentOffsetY() + height());

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

    public void onLayout() {
        if (_view != null) {
            onLayout(_view);
        }
        obtainChildrenView();
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
        if ("background-color".equals(key)) {
            view.setBackgroundColor(Color.valueOf(value, 0));
        } else if ("border-color".equals(key)) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setStroke(2, Color.valueOf(value, 0));
            view.setBackgroundDrawable(drawable);
        } else if ("border-width".equals(key)) {

        } else if ("border-radius".equals(key)) {
            GradientDrawable drawable=new GradientDrawable();
            drawable.setCornerRadius(4);
            view.setBackgroundDrawable(drawable);
            int borderWidth = 0;
            float[] outerRadius = new float[8];
            float[] innerRadius = new float[8];
            for (int i = 0; i < 8; i++) {
                outerRadius[i] = radius + borderWidth;
                innerRadius[i] = radius;
            }
            ShapeDrawable shapeDrawable =
                    new ShapeDrawable(
                            new RoundRectShape(outerRadius,
                                    new RectF(borderWidth, borderWidth, borderWidth, borderWidth),
                                    innerRadius));
            shapeDrawable.getPaint().setColor(Color.valueOf(value, 0));
            view.setBackgroundDrawable(shapeDrawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(shapeDrawable);
            } else {
                view.setBackgroundDrawable(shapeDrawable);
            }
        } else if ("opacity".equals(key)) {
            view.setAlpha(V.floatValue(value, 1.0f));
        } else if ("hidden".equals(key)) {
            if (V.booleanValue(value, false)) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        } else if ("background-image".equals(key)) {
            //Drawable image = viewContext.getImage(value);
        }else if ("padding".equals(key)){
            padding.set(value);
            // TODO: 2018/2/8 这个地方有一个问题，百分比基于父view的大小，当父view没有渲染在屏幕上时，获得的父view的大小为0
            view.setPadding((int)padding.left.floatValue(width(), 0),
                    (int)padding.top.floatValue(height(), 0),
                    (int)padding.right.floatValue(width(), 0),
                    (int)padding.bottom.floatValue(height(), 0));
        }

        if (view instanceof IElementView) {
            ((IElementView) view).setProperty(view, this, key, value);
        }
    }

    protected void onLayout(View view) {
        view.requestLayout();
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

    @Override
    protected ViewElement clone() {
        ViewElement element = null;
        try {
            element = (ViewElement) super.clone();

        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        if (element != null){
            element._view = null;
        }
        return element;
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
