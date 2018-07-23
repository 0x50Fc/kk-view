package cn.kkmofang.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.TextAlign;

/**
 * Created by zhanghailong on 2018/4/18.
 */

public class Text {

    public float maxWidth = 0;
    public float lineSpacing = 0;
    public float letterSpacing = 0;
    public TextAlign textAlign = TextAlign.Left;
    public final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    public float paddingLeft = 0;
    public float paddingTop = 0;

    private StaticLayout _layout;
    private float _width;
    private final WeakReference<TextContent> _textContent;

    public Text(TextContent textContent) {
        _textContent = new WeakReference<>(textContent);
    }

    public void setNeedDisplay() {
        _layout = null;
    }

    public boolean isNeedDisplay() {
        return _layout == null;
    }

    public void setMaxWidth(int v) {
        if(maxWidth != v) {
            maxWidth = v;
            setNeedDisplay();
        }
    }

    public void setLineSpacing(int v) {
        if(lineSpacing != v) {
            lineSpacing = v;
            setNeedDisplay();
        }
    }

    public void setLetterSpacing(int v) {
        if(letterSpacing != v) {
            letterSpacing = v;
            setNeedDisplay();
        }
    }


    public void setTextAlign(TextAlign v) {
        if(textAlign != v) {
            textAlign = v;
            setNeedDisplay();
        }
    }

    public void draw(Canvas canvas,int width,int height) {

        build();
        canvas.translate(paddingLeft,paddingTop + (height - _layout.getHeight()) * 0.5f);

        drawStyles(canvas);
        _layout.draw(canvas);

    }

    private void drawStyles(Canvas canvas){
        TextContent vv = _textContent.get();
        if (vv != null) {
            CharSequence charSequence = vv.textContent();
            if (charSequence instanceof Spannable) {
                StrokeSpan[] spans = ((Spannable) charSequence).getSpans(0, charSequence.length(), StrokeSpan.class);
                if (spans == null || spans.length <= 0)return;
                for (StrokeSpan span : spans) {
                    span.setUpdate(true);
                }
                _layout.draw(canvas);
                for (StrokeSpan span : spans) {
                    span.setUpdate(false);
                }
            }
        }

    }


    private void build() {

        if(_layout == null) {

            CharSequence textContent = "";

            TextContent vv = _textContent.get();


            if(vv != null) {
                textContent = vv.textContent();
            }



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if(paint.getTextSize() != 0.0f) {
                    paint.setLetterSpacing((letterSpacing + 1.3f) / paint.getTextSize());
                }
            }

            Layout.Alignment align = Layout.Alignment.ALIGN_NORMAL;

            if(textAlign == TextAlign.Center) {
                align = Layout.Alignment.ALIGN_CENTER;
            } else if(textAlign == TextAlign.Right) {
                align = Layout.Alignment.ALIGN_OPPOSITE;
            }

            if(maxWidth == Pixel.Auto) {
                align =  Layout.Alignment.ALIGN_NORMAL;
            }

            _layout = new StaticLayout(
                   textContent,
                    0,
                   textContent.length(),
                    paint,
                   (int) Math.ceil(maxWidth),
                    align,
                    1.0f,
                    0f,
                    false);

            _width = 0;

            for(int i=0;i<_layout.getLineCount();i++) {
                float v = _layout.getLineWidth(i);
                if(v > _width) {
                    _width = v;
                }
            }

        }

    }

    public float width() {
        build();
        return _width;
    }

    public float height() {
        build();
        return _layout.getHeight();
    }

    public static interface TextContent {
        CharSequence textContent();
    }
}
