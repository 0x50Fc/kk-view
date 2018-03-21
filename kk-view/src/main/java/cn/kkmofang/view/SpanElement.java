package cn.kkmofang.view;

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
    private int color;
    private Pixel fontSize = new Pixel();
    private Font font;
    private String letterSpacing;//字间距
    private String content;

    public SpanElement() {
        super();
    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        String value = get(key);
        switch (key){
            case "#text":
                content = value;
                break;
            case "color":
                color = Color.valueOf(value, 0);
                break;
            case "font":
                saveTextSizeAndFont(value);
                break;
        }
    }

    /**
     * 保存字体大小和是否加粗
     * @param value 如30rpx bold
     */
    private void saveTextSizeAndFont(String value){
        String[] values = value.split(" ");
        fontSize.set(values[0]);

        if (values.length > 1){
            font = Font.fvalueOf(values[1]);
        }
    }

    public SpannableString obtainContent(){
        if (TextUtils.isEmpty(content))return null;
        SpannableString span = new SpannableString(content);
        int length = content.length();
        span.setSpan(new ForegroundColorSpan(color), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new AbsoluteSizeSpan((int) fontSize.floatValue(ViewContext.windowPoint.x, 0)), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan styleSpan;
        switch (font){
            default:
            case NORAML:
                styleSpan = new StyleSpan(Typeface.NORMAL);
                break;
            case BOLD:
                styleSpan = new StyleSpan(Typeface.BOLD);
                break;
            case ITALIC:
                styleSpan = new StyleSpan(Typeface.ITALIC);
                break;
        }
        span.setSpan(styleSpan, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // TODO: 2018/2/27 字间距设置暂时保留，需要自定义span
        return span;
    }
}
