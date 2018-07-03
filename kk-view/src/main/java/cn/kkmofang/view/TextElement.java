package cn.kkmofang.view;

import android.graphics.Paint;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Font;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.TextAlign;
import cn.kkmofang.view.value.TextDecoration;

/**
 * Created by zhanghailong on 2018/1/20.
 */

public class TextElement extends ViewElement implements Text.TextContent{

    public final Pixel lineSpacing = new Pixel();
    public final Pixel letterSpacing = new Pixel();
    private final Text _text = new Text(this);

    private SpannableStringBuilder _string = null;

    private final Handler _handler;

    @Override
    public CharSequence textContent() {

        if(_string == null) {

            _string = new SpannableStringBuilder();

            Element p = firstChild();

            if(p == null) {

                String v = get("#text");
                if (!TextUtils.isEmpty(v)){
                    int lenght = v.length();
                    SpannableString span = new SpannableString(v);
                    span.setSpan(new AbsoluteSizeSpan((int) Math.ceil( _text.paint.getTextSize())), 0, lenght, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    StrokeSpanColor stroke = new StrokeSpanColor();
                    stroke.setAlpha(_text.paint.getAlpha());
                    stroke.setColor(_text.paint.getColor());

                    stroke.setStroke(get("text-stroke"));

                    span.setSpan(stroke, 0, lenght, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                    _string.append(span);

                }
            } else {
                while (p != null) {
                    if (p instanceof SpanElement) {
                        SpannableString ss = ((SpanElement) p).obtainContent();
                        if (ss != null)
                            _string.append(ss);
                    }

                    if (p instanceof ImgElement) {
                        SpannableString ss = ((ImgElement) p).obtainContent();
                        if (ss != null) {
                            _string.append(ss);
                        }
                    }
                    p = p.nextSibling();
                }
            }

        }

        return _string;
    }

    private static final Layout TextLayout = new Layout(){
        @Override
        public void layout(ViewElement element) {

            float width = element.width();
            float height = element.height();


            TextElement e = (TextElement) element;

            if(width == Pixel.Auto || height == Pixel.Auto) {

                float paddingLeft = element.padding.left.floatValue(width,0);
                float paddingRight = element.padding.right.floatValue(width,0);
                float paddingTop = element.padding.top.floatValue(width,0);
                float paddingBottom = element.padding.bottom.floatValue(width,0);

                float maxWidth = width;
                float maxHeight = height;

                e._text.paddingLeft = paddingLeft;
                e._text.paddingTop = paddingTop;

                if(width != Pixel.Auto) {
                    maxWidth = width - paddingLeft - paddingRight;
                }

                e._text.setMaxWidth((int) maxWidth);

                if(width == Pixel.Auto) {
                    width = e._text.width() + paddingLeft + paddingRight;
                }

                if(height == Pixel.Auto) {

                    height = e._text.height()
                        + paddingTop
                        + paddingBottom;

                    if(height > maxHeight) {
                        height = maxHeight;
                    }
                }

                element.setContentSize(width, height);

            } else {
                e._text.setMaxWidth((int) width);
                element.setContentSize(width,height);
            }

        }
    };

    public TextElement() {
        super();
        _handler = new Handler() ;
        set("#view", TextView.class.getName());
        setLayout(TextLayout);
    }

    public void setView(View v) {
        super.setView(v);
        if(v != null) {
            setNeedDisplay();
        }
    }

    private boolean _displaying = false;

    public void setNeedDisplay() {

        _string = null;
        _text.setNeedDisplay();

        if(view() == null) {
            return;
        }

        if(_displaying) {
            return;
        }

        _displaying = true;

        _handler.post(new Runnable() {
            @Override
            public void run() {
                TextView v = (TextView) view();
                if(v != null) {
                    v.setText(_text);
                }
                _displaying = false;
            }
        });

    }


    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        if("#text".equals(key)) {
            setNeedDisplay();
        } else if("font".equals(key)) {
            Font.valueOf(get(key),_text.paint);
            setNeedDisplay();
        } else if("color".equals(key)) {
            String value = get(key);
            if (!TextUtils.isEmpty(value)){
                int color = Color.valueOf(value, 0xff000000);
                _text.paint.setColor(color);
                _text.paint.setAlpha(0x0ff & (color >> 24));
                setNeedDisplay();
            }
        } else if("line-spacing".equals(key)) {
            lineSpacing.set(get(key));
            _text.setLineSpacing((int) lineSpacing.floatValue(0,0));
            setNeedDisplay();
        } else if("letter-spacing".equals(key)) {
            letterSpacing.set(get(key));
            _text.setLetterSpacing((int) lineSpacing.floatValue(0,0));
            setNeedDisplay();
        } else if("text-align".equals(key)) {
            String v = get(key);
            _text.setTextAlign(TextAlign.valueOfString(v));
            setNeedDisplay();
        } else if("text-decoration".equals(key)) {
            TextDecoration.valueOf(get(key),_text.paint);
        } else if ("text-stroke".equals(key)){
            setNeedDisplay();
        }

    }

    @Override
    protected void onLayout(View view) {
        setNeedDisplay();
        super.onLayout(view);
    }

    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view,key,value);

        if("padding".equals(key)) {
            view.setPadding((int) padding.left.floatValue(0,0),
                    (int) padding.top.floatValue(0,0),
                    (int) padding.right.floatValue(0,0),
                    (int) padding.bottom.floatValue(0,0));
        }
    }


}
