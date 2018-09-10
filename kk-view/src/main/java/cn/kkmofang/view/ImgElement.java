package cn.kkmofang.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;


import java.lang.ref.WeakReference;

import cn.kkmofang.image.Image;
import cn.kkmofang.image.ImageStyle;
import cn.kkmofang.view.value.Edge;
import cn.kkmofang.view.value.Pixel;

/**
 * Created by zhanghailong on 2018/1/20.
 */

public class ImgElement extends Element {
    private String _src;
    private Pixel _height = new Pixel();
    private Pixel _width = new Pixel();
    private Edge _margin = new Edge();
    private IViewContext viewContext;

    public ImgElement() {
        super();
        _height.type = Pixel.Type.Auto;
        _width.type = Pixel.Type.Auto;
        viewContext = ViewContext.current();
    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        String value = get(key);
        switch (key){
            case "width":
                _width.set(value);
                break;
            case "height":
                _height.set(value);
                break;
            case "src":
                _src = value;
                break;
            case "margin":
                _margin.set(value);
                break;
        }
    }

    public SpannableString obtainContent(){
        if (TextUtils.isEmpty(_src))return null;

        final WeakReference<ImgElement> v = new WeakReference<ImgElement>(this);

        SpannableString span = new SpannableString("...");

        span.setSpan(new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BOTTOM) {
            @Override
            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                ImgElement e = v.get();
                if (e != null){
                    int sc = canvas.save();
                    canvas.translate((int) Math.ceil(e._margin.left.floatValue(0, 0)), (int) Math.ceil(e._margin.top.floatValue(0, 0)));

                    Drawable b = getCachedDrawable();

                    int transY = ((bottom - top) - b.getBounds().bottom) / 2 + top ;

                    canvas.translate(x, transY);
                    b.draw(canvas);

                    canvas.restoreToCount(sc);
                }
            }

            int initialDescent = 0;
            int extraspace = 0;
            @Override
            public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
                Drawable d = getCachedDrawable();
                Rect rect = d.getBounds();
                if (fm != null){
                    if (rect.bottom - (fm.descent - fm.ascent) >= 0){
                        initialDescent = fm.descent;
                        extraspace = rect.bottom - (fm.descent - fm.ascent);
                    }
                    fm.descent = extraspace / 2 + initialDescent;
                    fm.bottom = fm.descent;

                    fm.ascent = -rect.bottom + fm.descent;
                    fm.top = fm.ascent;
                }
                return rect.right;
            }


            private Drawable getCachedDrawable() {
                WeakReference<Drawable> wr = mDrawableRef;
                Drawable d = null;

                if (wr != null)
                    d = wr.get();

                if (d == null) {
                    d = getDrawable();
                    mDrawableRef = new WeakReference<>(d);
                }

                return d;
            }

            private WeakReference<Drawable> mDrawableRef;

            @Override
            public Drawable getDrawable() {

                ImgElement e = v.get();

                if(e != null) {
                    ImageStyle style = new ImageStyle(e.viewContext.getContext());
                    style.marginLeft = (int) Math.ceil(e._margin.left.floatValue(0, 0));
                    style.marginRight = (int) Math.ceil(e._margin.right.floatValue(0, 0));
                    style.marginTop = (int) Math.ceil(e._margin.top.floatValue(0, 0));
                    style.marginBottom = (int) Math.ceil(e._margin.bottom.floatValue(0, 0));
                    Drawable image = viewContext.getImage(_src, style);

                    if(image != null) {

                        int r=0,b = 0;

                        if(e._width.type == Pixel.Type.Auto || e._height.type == Pixel.Type.Auto) {

                            if(image instanceof Image) {
                                r = (int) Math.ceil( ((Image) image).width() * Pixel.UnitPX );
                                b = (int) Math.ceil( ((Image) image).height() * Pixel.UnitPX );
                            } else if(image instanceof BitmapDrawable) {
                                Bitmap bm = ((BitmapDrawable) image).getBitmap();
                                if (bm != null){
                                    r = bm.getWidth();
                                    b = bm.getHeight();
                                }
                            }

                            if(e._width.type == Pixel.Type.Auto && e._height.type == Pixel.Type.Auto) {

                            } else if(e._width.type == Pixel.Type.Auto) {
                                float v = e._height.floatValue(0, 0);
                                r = (int) Math.ceil(r *  v / b);
                                b = (int) Math.ceil(v);
                            } else if(e._height.type == Pixel.Type.Auto) {
                                float v = e._width.floatValue(0, 0);
                                b = (int) Math.ceil(b *  v / r);
                                r = (int) Math.ceil(v);
                            }

                        } else {
                            r = (int) Math.ceil(e._width.floatValue(0, 0));
                            b = (int) Math.ceil(e._height.floatValue(0, 0));
                        }
                        image.setBounds(0,0, r + style.marginLeft + style.marginRight,
                                b + style.marginTop + style.marginBottom);
                    }

                    return image;

                }

                return null;
            }
        }, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }
}
