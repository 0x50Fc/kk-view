package cn.kkmofang.view;

import android.graphics.drawable.BitmapDrawable;
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
        SpannableString span = new SpannableString("加载图片");
        span.setSpan(new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BASELINE) {
            @Override
            public Drawable getDrawable() {
                Drawable d = viewContext.getImage(_src);
                //取不到宽高，暂时使用100作为默认值
                int width = _width.type == Pixel.Type.Auto?100:
                        (int) _width.floatValue(ViewContext.windowPoint.x, 0);
                int height = _height.type == Pixel.Type.Auto?100:
                        (int) _height.floatValue(ViewContext.windowPoint.x, 0);
                d.setBounds(0, 0, width, height);
                return d;
            }
        }, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
}
