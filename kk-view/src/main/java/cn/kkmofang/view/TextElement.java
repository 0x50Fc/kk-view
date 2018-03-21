package cn.kkmofang.view;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Font;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.TextAlign;
import cn.kkmofang.view.view.FTextView;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class TextElement extends ViewElement implements Cloneable{
    private Pixel fontSize = new Pixel();
    private Pixel lineSpacing = new Pixel();
    private Pixel letterSpacing = new Pixel();
    private Font font;
    private String content;

    private TextAlign textAlign = TextAlign.Left;

    public TextElement() {
        super();
        set("#view", FTextView.class.getName());

    }
    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view, key, value);
        if (view instanceof FTextView){
            FTextView textView = (FTextView) view;
            if ("color".equals(key)){
                textView.setTextColor(Color.valueOf(value,0));
            }else if ("font".equals(key)) {
                setTextSizeAndFont(value, textView);
                //重新设置字间距，防止字体大小还没有设置
                setLetterSpacing(get("line-spacing"), textView);
            }else if("line-spacing".equals(key)){
                setLineSpacing(value, textView);
            }else if ("paragraph-spacing".equals(key)){
                //暂时无法实现，保留
            }else if ("letter-spacing".equals(key)){
                setLetterSpacing(value, textView);
            }else if ("text-align".equals(key)){
                setTextAlign(value, textView);
            }else if ("#text".equals(key)){
                if (firstChild() == null){
                    content = value;
                    textView.setText(content);
                }
            }
        }
    }

    @Override
    public void obtainChildrenView() {
        FTextView tv = (FTextView) view();
        if (tv == null || firstChild() == null)return;
        Element p = firstChild();
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        while (p != null){
            if (p instanceof SpanElement){
                SpannableString ss = ((SpanElement) p).obtainContent();
                if (ss != null)
                    ssb.append(ss);
            }

            if (p instanceof ImgElement){
                SpannableString ss = ((ImgElement) p).obtainContent();
                if (ss != null){
                    ssb.append(ss);
                }
            }
            p = p.nextSibling();
        }

        tv.setText(ssb);
    }

    /**
     * 设置字体大小和是否加粗
     * @param value 如30rpx bold
     * @param tv
     */
    private void setTextSizeAndFont(String value, TextView tv){
        String[] values = value.split(" ");
        fontSize.set(values[0]);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize.floatValue(0, 0));

        if (values.length > 1){
            font = Font.valueOfString(values[1]);
            tv.setTypeface(Font.createTypeface(font));
        }
    }

    /**
     * 设置行间距
     * @param value 行间距如10rpx,-10%,20px
     * @param tv
     */
    private void setLineSpacing(String value, TextView tv){
        lineSpacing.set(value);
        //mult默认为1.0,不进行操作
        tv.setLineSpacing(
                lineSpacing.floatValue(0, 0), 1.0f);
    }

    /**
     * 设置字间距
     * @param value 字间距如10rpx
     * @param tv
     */
    private void setLetterSpacing(String value, TextView tv){
        letterSpacing.set(value);
        float fontMeasure = fontSize.value;
        float space = 0;
        if (fontMeasure != 0){
            space = letterSpacing.value / fontMeasure;
        }
        tv.setLetterSpacing(space);
    }

    /**
     * 设置文本对齐方式
     * @param value left（默认） center right justify
     * @param tv
     */
    private void setTextAlign(String value, TextView tv){
        textAlign = TextAlign.valueOfString(value);
        switch (textAlign){
            case Left:
                tv.setGravity(Gravity.LEFT);
                break;
            case Right:
                tv.setGravity(Gravity.RIGHT);
                break;
            case Center:
                tv.setGravity(Gravity.CENTER);
                break;
            case Justify://俩端对齐，textview原生不支持，后续可以考虑手动实现
                break;
        }
    }

    @Override
    public TextElement clone() {
        TextElement element = (TextElement) super.clone();
        element.font = font;
        return element;
    }
}
