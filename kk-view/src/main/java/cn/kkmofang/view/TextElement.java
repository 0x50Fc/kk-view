package cn.kkmofang.view;

import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Font;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.TextAlign;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class TextElement extends ViewElement{


    public final Pixel lineSpacing = new Pixel();
    public final Paint paint = new Paint();
    public TextAlign textAlign = TextAlign.Left;
    public Text _text = null;

    private final Handler _handler;

    public Text text() {

        if(_text == null) {
            _text = new Text();

            Element p = firstChild();

            if(p == null) {
                String text = get("#text");
                if (text != null)
                    _text.append(text);
            } else {
                while (p != null) {
                    if (p instanceof SpanElement) {
                        SpannableString ss = ((SpanElement) p).obtainContent();
                        if (ss != null)
                            _text.append(ss);
                    }

                    if (p instanceof ImgElement) {
                        SpannableString ss = ((ImgElement) p).obtainContent();
                        if (ss != null) {
                            _text.append(ss);
                        }
                    }
                    p = p.nextSibling();
                }
            }
        }

        return _text;
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

                float maxWidth = width;
                float maxHeight = height;


                if(width != Pixel.Auto) {
                    maxWidth = width - paddingLeft - paddingRight;
                }

                Text text = e.text();

                text.contentSize((int) maxWidth,(int) e.lineSpacing.floatValue(0,0),e.paint);

                if(width == Pixel.Auto) {
                    width = text.width();
                }

                if(height == Pixel.Auto) {
                    height = text.height();
                    if(height > maxHeight) {
                        height = maxHeight;
                    }
                }

                element.setContentSize(width, height);

            } else {
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

    public void setView(View view){
        super.setView(view);
        if(view != null && view instanceof TextView) {
            TextView textView = (TextView) view;
        }
    }

    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view, key, value);

        if (view instanceof TextView){

            TextView textView = (TextView) view;

            if ("color".equals(key)){
                textView.setTextColor(Color.valueOf(value,0));
            }else if ("font".equals(key)) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX , paint.getTextSize());
                setLetterSpacing(get("letter-spacing"), textView);
                setNeedDisplay();
            }else if("line-spacing".equals(key)){
                setLineSpacing(value, textView);
                setNeedDisplay();
            }else if ("paragraph-spacing".equals(key)){
                //暂时无法实现，保留
            }else if ("letter-spacing".equals(key)){
                setLetterSpacing(value, textView);
                setNeedDisplay();
            }else if ("text-align".equals(key)){
                setTextAlign(value, textView);
            }else if ("#text".equals(key)){
                setNeedDisplay();
            }
        }
    }

    private boolean _displaying = false;

    public void setNeedDisplay() {

        _text = null;

        if(_displaying) {
            return;
        }

        _displaying = true;

        _handler.post(new Runnable() {
            @Override
            public void run() {
                TextView v = (TextView) view();
                if(v != null) {
                    v.setText(text());
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
            Font.valueOf(get(key),paint);
        } else if("color".equals(key)) {
            int v = Color.valueOf(get(key),0xff000000);
            paint.setColor(v);
            paint.setAlpha(0x0ff & (v >> 24));
        } else if("line-spacing".equals(key)) {
            lineSpacing.set(get(key));
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
        float fontMeasure = paint.getTextSize();
        float space = 0;
        Pixel p = new Pixel();
        p.set(value);
        if (fontMeasure != 0){
            space = p.floatValue(0,0) / fontMeasure;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paint.setLetterSpacing(space);
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
                tv.setGravity(Gravity.LEFT | Gravity.TOP);
                break;
            case Right:
                tv.setGravity(Gravity.RIGHT | Gravity.TOP);
                break;
            case Center:
                tv.setGravity(Gravity.CENTER | Gravity.TOP);
                break;
            case Justify://俩端对齐，textview原生不支持，后续可以考虑手动实现
                break;
        }
    }
}
