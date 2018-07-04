package cn.kkmofang.view;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Font;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.TextDecoration;

/**
 * Created by zhanghailong on 2018/1/20.
 */

public class SpanElement extends Element {

    public final TextPaint paint = new TextPaint();
    private StrokeSpan _styleSpan;

    public SpanElement() {
        super();
    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);

        if("#text".equals(key)) {
            Element p = parent();
            if(p instanceof TextElement) {
                ((TextElement) p).setNeedDisplay();
            }
        } else if("font".equals(key)) {
            Font.valueOf(get(key),paint);
            Element p = parent();
            if(p instanceof TextElement) {
                ((TextElement) p).setNeedDisplay();
            }
        } else if("color".equals(key)) {
            int v = Color.valueOf(get(key),0xff000000);
            paint.setColor(v);
            paint.setAlpha(0x0ff & (v >> 24));
            Element p = parent();
            if(p instanceof TextElement) {
                ((TextElement) p).setNeedDisplay();
            }
        } else if("text-decoration".equals(key)) {
            TextDecoration.valueOf(get(key),paint);
        }

    }

    public Paint getPaint(){
        Element p = parent();
        if (p instanceof TextElement){
            if (TextUtils.isEmpty(get("color"))){
                int v = Color.valueOf(p.get("color"),0xff000000);
                paint.setColor(v);
                paint.setAlpha(0x0ff & (v >> 24));
            }

            if (TextUtils.isEmpty(get("font"))){
                Font.valueOf(p.get("font"),paint);

            }
        }

        return paint;
    }

    public StrokeSpan obtainStyle(){
        return _styleSpan;
    }

    public SpannableString obtainContent(){
        Paint paint = getPaint();

        String text = get("#text");

        if (TextUtils.isEmpty(text))return null;

        SpannableString span = new SpannableString(text);

        int length = text.length();

        span.setSpan(new AbsoluteSizeSpan((int) Math.ceil( paint.getTextSize())), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        String v = get("text-stroke");
        if (!TextUtils.isEmpty(v)){
            _styleSpan = new StrokeSpan();
            _styleSpan.setAlpha(paint.getAlpha());
            _styleSpan.setColor(paint.getColor());
            _styleSpan.setStroke(v);
            span.setSpan(_styleSpan, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        if(paint.isFakeBoldText()) {
            span.setSpan(new StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            Typeface f = paint.getTypeface();

            if(f != null) {
                span.setSpan(new StyleSpan(f.getStyle()), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return span;
    }
}
