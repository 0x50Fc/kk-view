package cn.kkmofang.view;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;

import java.lang.ref.WeakReference;

import cn.kkmofang.image.Image;
import cn.kkmofang.image.ImageStyle;
import cn.kkmofang.view.value.Pixel;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class ImgElement extends Element {
    private String _src;
    private Pixel _height = new Pixel();
    private Pixel _width = new Pixel();
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
        }
    }

    public SpannableString obtainContent(){
        if (TextUtils.isEmpty(_src))return null;

        final WeakReference<ImgElement> v = new WeakReference<ImgElement>(this);

        SpannableString span = new SpannableString("...");

        span.setSpan(new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BASELINE) {
            @Override
            public Drawable getDrawable() {

                ImgElement e = v.get();

                if(e != null) {

                    ImageStyle style = new ImageStyle(e.viewContext.getContext());
                    Drawable image = viewContext.getImage(_src, style);

                    if(image != null) {

                        int r=0,b = 0;

                        if(e._width.type == Pixel.Type.Auto || e._height.type == Pixel.Type.Auto) {

                            if(image instanceof Image) {
                                r = ((Image) image).getBitmap().getWidth();
                                b = ((Image) image).getBitmap().getHeight();
                            } else if(image instanceof BitmapDrawable) {
                                r = ((BitmapDrawable) image).getBitmap().getWidth();
                                b = ((BitmapDrawable) image).getBitmap().getHeight();
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
                        image.setBounds(0,0, r,b);
                    }

                    return image;

                }

                return null;
            }
        }, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }
}
