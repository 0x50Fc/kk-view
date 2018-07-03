package cn.kkmofang.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.Log;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Pixel;

public class StrokeSpanColor extends ReplacementSpan {
    private int color;
    private int alpha;
    private boolean hasStroke;
    private Pixel strokeWidth;
    private int strokeColor;
    private int strokeAlpha;


    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        int width = (int) paint.measureText(text, start, end);
        if (fm != null){
            Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
            fm.top = metrics.top;
            fm.ascent = metrics.ascent;
            fm.descent = metrics.descent;
            fm.bottom = metrics.bottom;
            fm.leading = metrics.leading;
        }
        return width;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setStroke(String textStroke){
        if (!TextUtils.isEmpty(textStroke)){
            hasStroke = true;
            String[] vv = textStroke.split(" ");
            if (vv.length >= 1){
                if (strokeWidth == null){
                    strokeWidth = new Pixel();
                }
                strokeWidth.set(vv[0]);
            }

            if (vv.length >= 2){
                strokeColor = Color.valueOf(vv[1], 0xff000000);
                strokeAlpha = 0x0ff & (strokeColor >> 24);
            }
        }
    }


    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {


        if (hasStroke && strokeWidth != null){
            paint.setAlpha(strokeAlpha);
            paint.setColor(strokeColor);
            paint.setStrokeWidth(strokeWidth.floatValue(0, 0));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawText(text, start, end, x, y, paint);
        }

        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, start, end, x, y, paint);
    }
}
