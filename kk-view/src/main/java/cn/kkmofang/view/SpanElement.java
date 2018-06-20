package cn.kkmofang.view;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import cn.kkmofang.view.value.TextDecoration;
import cn.kkmofang.view.value.TextPaint;

/**
 * Created by zhanghailong on 2018/1/20.
 */

public class SpanElement extends Element {

    public final TextPaint paint = new TextPaint();

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
            paint.setFont(get(key));
            Element p = parent();
            if(p instanceof TextElement) {
                ((TextElement) p).setNeedDisplay();
            }
        } else if("color".equals(key)) {
            paint.setColor(get(key));
            Element p = parent();
            if(p instanceof TextElement) {
                ((TextElement) p).setNeedDisplay();
            }
        } else if("text-decoration".equals(key)) {
            TextDecoration.valueOf(get(key),paint.getPaint());
        }

    }

    private Paint getPaint(){
        Element p = parent();
        if (p instanceof TextElement){
            if (!paint.isSetColor()){
                paint.setColor(p.get("color"));
            }

            if (!paint.isSetFont()){
                paint.setFont(p.get("font"));
            }
        }

        return paint.getPaint();
    }

    public SpannableString obtainContent(){
        Paint paint = getPaint();

        String text = get("#text");

        if (TextUtils.isEmpty(text))return null;

        SpannableString span = new SpannableString(text);

        int length = text.length();

        span.setSpan(new ForegroundColorSpan(paint.getColor()), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new AbsoluteSizeSpan((int) Math.ceil( paint.getTextSize())), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


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
