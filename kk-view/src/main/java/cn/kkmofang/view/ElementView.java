package cn.kkmofang.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import java.lang.ref.WeakReference;
import cn.kkmofang.unity.R;
import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.V;


/**
 * Created by zhanghailong on 2018/1/18.
 */

public class ElementView extends FrameLayout implements IElementView{
    private Path _path;
    private Paint _paint;

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

        int parentWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
        int parentheightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v != null && v.getVisibility() != GONE){
                WeakReference<ViewElement> childElement = (WeakReference<ViewElement>) v.getTag(R.id.kk_view_element);
                if (childElement != null){
                    ViewElement element = childElement.get();
                    if (element != null){
                        int childWidthSpec;
                        int childHeightSpec;
                        if (element.width() == Pixel.Auto){
                            childWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
                        }else {
                            childWidthSpec = MeasureSpec.makeMeasureSpec((int) element.width(), MeasureSpec.EXACTLY);
                        }

                        if (element.height() == Pixel.Auto){
                            childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
                        }else {
                            childHeightSpec = MeasureSpec.makeMeasureSpec((int) element.height(), MeasureSpec.EXACTLY);
                        }
                        measureChild(v, childWidthSpec, childHeightSpec);
                        continue;
                    }
                }
                measureChild(v, parentWidthSpec, parentheightSpec);

            }
        }

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

        Handler handler = getHandler();
        if (handler != null){
            handler.post(new Runnable() {
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

    @Override
    public void draw(Canvas canvas) {
        int sc = canvas.save();

        WeakReference<ViewElement> e = (WeakReference<ViewElement>) getTag(R.id.kk_view_element);
        if (e != null){
            ViewElement element = e.get();

            if (element != null){
                float _borderRadius = element.borderRadius.floatValue(0 ,0 );
                float _borderWidth = element.borderWidth.floatValue(0 ,0 );

                if (_borderWidth > 0){
                    if (_paint == null){
                        _paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        _paint.setStyle(Paint.Style.STROKE);
                    }
                    _paint.setStrokeWidth(_borderWidth);
                    _paint.setColor(Color.valueOf(element.get("border-color"), 0));
                    canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), _borderRadius, _borderRadius, _paint);
                }

                if (_borderRadius > 0){
                    if (_path == null){
                        _path = new Path();
                        _path.setFillType(Path.FillType.EVEN_ODD);
                    }
                    _path.reset();
                    _path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), _borderRadius, _borderRadius, Path.Direction.CW);
                    canvas.clipPath(_path);
                }

            }
        }

        super.draw(canvas);

        canvas.restoreToCount(sc);

    }
}
