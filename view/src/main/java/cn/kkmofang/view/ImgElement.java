package cn.kkmofang.view;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;

import cn.kkmofang.view.value.Pixel;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class ImgElement extends Element {
    private String _src;
    private Pixel _height = new Pixel();
    private Pixel _width = new Pixel();
    private ViewContext viewContext;

    public ImgElement() {
        super();
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
        SpannableString span = new SpannableString("");
        span.setSpan(new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BOTTOM) {
            @Override
            public Drawable getDrawable() {
                Drawable d = viewContext.getImage(_src);
                int width = _width.type == Pixel.Type.Auto?d.getIntrinsicWidth():
                        (int) _width.floatValue(ViewContext.windowPoint.x, 0);
                int height = _height.type == Pixel.Type.Auto?d.getIntrinsicHeight():
                        (int) _height.floatValue(ViewContext.windowPoint.x, 0);
                d.setBounds(0, 0, width, height);
                return null;
            }
        }, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
}
