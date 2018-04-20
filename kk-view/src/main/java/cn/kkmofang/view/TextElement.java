package cn.kkmofang.view;

import android.app.Activity;
import android.app.Application;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Font;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.TextAlign;
import cn.kkmofang.view.view.FTextView;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class TextElement extends ViewElement{


    private Pixel fontSize = new Pixel();
    private Pixel lineSpacing = new Pixel();
    private Pixel letterSpacing = new Pixel();
    private Font font;
//    private SpannableStringBuilder content = new SpannableStringBuilder();

    private TextAlign textAlign = TextAlign.Left;

    private static final Layout TextLayout = new Layout(){
        @Override
        public void layout(ViewElement element) {

            float width = element.width();
            float height = element.height();


            if(width == Pixel.Auto || height == Pixel.Auto) {

                float paddingLeft = element.padding.left.floatValue(width,0);
                float paddingRight = element.padding.right.floatValue(width,0);
                float paddingTop = element.padding.top.floatValue(height,0);
                float paddingBottom = element.padding.bottom.floatValue(height,0);

                float maxWidth = width;
                float maxHeight = height;

                if(width != Pixel.Auto) {
                    maxWidth = width - paddingLeft - paddingRight;
                }else if (element instanceof TextElement){
                    maxWidth = (((TextElement) element).getTextRect()).width() + paddingLeft + paddingRight;
                }

                if (height != Pixel.Auto){
                    maxHeight = height - paddingTop - paddingBottom;
                }else if (element instanceof TextElement){
                    maxHeight = (((TextElement) element).getTextRect()).height() + paddingTop + paddingBottom;
                }

                element.setContentSize(maxWidth, maxHeight);

            } else {
                element.setContentSize(width,height);
            }

        }
    };

    public TextElement() {
        super();
        set("#view", FTextView.class.getName());
        setLayout(TextLayout);
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
                SpannableStringBuilder builder = obtainString();
                FTextView tv = (FTextView) view();
                if (tv != null){
                    tv.setText(builder.toString());
                }
            }
        }
    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
//        if ("#text".equals(key)){
//            if (firstChild() == null){
//
//                content = get(key);
//                FTextView tv = (FTextView) view();
//                if (tv != null)
//                    tv.setText(content);
//            }
//        }
    }


    /**
     * 获取文本宽高
     * @return
     */
    public Rect getTextRect() {
        Rect size = new Rect();
        TextPaint paint = new TextPaint();
        paint.setTextSize(getFontSize());
        String str = obtainString().toString();
        System.out.println("TextElement=" + str);

        if (str.length() > 0) {
            String[] strings = str.split("\\n");
            int line = strings.length;
            if (line > 0){
                int w = 0;
                line = 0;
                for (String curLineStr : strings) {
                    int len = curLineStr.length();
                    float[] widths = new float[len];
                    paint.getTextWidths(curLineStr, widths);
                    int width = 0;
                    for (int i = 0; i < len; i++) {
                        width += Math.ceil(widths[i]);
                    }
                    if (this.width.type != Pixel.Type.Auto) {
                        int mw = (int) this.width.floatValue(0, 0);
                        if(mw > 0)
                            line += Math.floor(((float)width) / mw);
                    }
                    line += 1;
                    if (width >= w)w=width;
                }

                int h = 0;
                Paint.FontMetrics metrics = paint.getFontMetrics();
                h += Math.ceil(metrics.bottom - metrics.top) * line + getLineSpacing() * (line - 1);
                size.set(0, 0, w, h);

            }


        }

        return size;
    }

    public SpannableStringBuilder obtainString(){
        Element p = firstChild();
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        if(p == null) {
            String text = get("#text");
            if (text != null)
                ssb.append(text);
        } else {
            while (p != null) {
                if (p instanceof SpanElement) {
                    SpannableString ss = ((SpanElement) p).obtainContent();
                    if (ss != null)
                        ssb.append(ss);
                }

                if (p instanceof ImgElement) {
                    SpannableString ss = ((ImgElement) p).obtainContent();
                    if (ss != null) {
                        ssb.append(ss);
                    }
                }
                p = p.nextSibling();
            }
        }
        return ssb;
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

    private int getFontSize(){
        String font = get("font");
        String[] values = font.split(" ");
        if (values.length > 0)
            fontSize.set(values[0]);
        return (int) fontSize.floatValue(0, 0);
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

    private int getLineSpacing(){
        String spaceStr = get("line-spacing");
        lineSpacing.set(spaceStr);
        return (int) (lineSpacing.floatValue(0, 0) + 12);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tv.setLetterSpacing(space);
        }
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
}
