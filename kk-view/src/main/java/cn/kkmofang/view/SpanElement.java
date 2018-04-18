package cn.kkmofang.view;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Font;
import cn.kkmofang.view.value.Pixel;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class SpanElement extends Element {

    public final Paint paint = new Paint();

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
        }

    }


    public SpannableString obtainContent(){

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
