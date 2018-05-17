package cn.kkmofang.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

import cn.kkmofang.view.value.TextAlign;

/**
 * Created by zhanghailong on 2018/4/18.
 */

public class TextLayout {

    public float maxWidth = 0;
    public float lineSpacing = 0;
    public float letterSpacing = 0;
    public TextAlign textAlign = TextAlign.Left;
    public final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    public final SpannableStringBuilder string = new SpannableStringBuilder();

    public void setNeedDisplay() {
        _lines = null;
    }

    public boolean isNeedDisplay() {
        return _lines == null;
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

    public void draw(Canvas canvas) {
        build();

        int width = canvas.getWidth();

        Paint.FontMetrics metrics = paint.getFontMetrics();
        float dy = metrics.descent - metrics.ascent - (metrics.bottom - metrics.descent) + lineSpacing;
        Layout.Alignment align = Layout.Alignment.ALIGN_NORMAL;

        for(Line line : _lines) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                paint.setLetterSpacing(letterSpacing);
            }

            if(textAlign == TextAlign.Center) {
                align = Layout.Alignment.ALIGN_CENTER;
            } else if(textAlign == TextAlign.Right) {
                align = Layout.Alignment.ALIGN_OPPOSITE;
            } else if(textAlign == TextAlign.Justify) {
                int c = (line.end - line.start - 1);
                float dw = width - line.width;
                if(c > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        paint.setLetterSpacing(letterSpacing + dw / c);
                    }
                }
            }

            StaticLayout layout = new StaticLayout(
                    string,
                    line.start,
                    line.end,
                    paint,
                    width + 12,
                    align,
                    1.0f,
                    0f,
                    false);

            layout.draw(canvas);
            canvas.translate(0, dy);
        }
    }

    private Line[] _lines;
    private float _width;
    private float _height;

    private void build() {

        if(_lines == null) {

            List<Line> lines = new ArrayList<Line>(4);

            _height = 0;
            _width = 0;

            float[] widths = new float[1];

            int start = 0;
            int end = string.length();
            int len;

            while(start < end ){

                if(start != 0) {
                    _height += lineSpacing;
                }

                len = paint.breakText(string, start, end, false, maxWidth, widths);

                Line v = new Line();
                v.start = start;
                v.end = start + len;
                v.width = widths[0];

                lines.add(v);

                _height +=  - paint.ascent() + paint.descent();

                if(_width < widths[0]) {
                    _width = widths[0];
                }

                start += len;

            }

            _lines = lines.toArray(new Line[lines.size()]);
        }

    }

    public float width() {
        build();
        return _width;
    }

    public float height() {
        build();
        return _height;
    }

    private static class Line {
        public int start;
        public int end;
        public float width;
    }
}
