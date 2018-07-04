package cn.kkmofang.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ParagraphStyle;
import android.text.style.UpdateAppearance;
import android.text.style.UpdateLayout;

import cn.kkmofang.view.value.Pixel;

public class StrokeSpan extends CharacterStyle implements UpdateAppearance, UpdateLayout, ParagraphStyle {
    private int color;
    private int alpha;
    private Pixel strokeWidth = new Pixel();
    private int strokeColor;
    private int strokeAlpha;

    @Override
    public void updateDrawState(TextPaint paint) {
        if (update){
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(strokeColor);
            paint.setAlpha(strokeAlpha);
            paint.setStrokeWidth(strokeWidth.floatValue(0, 0));
        }else {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setAlpha(alpha);
        }

    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setStroke(String textStroke){
        if (!TextUtils.isEmpty(textStroke)){
            String[] vv = textStroke.split(" ");
            if (vv.length >= 1){
                strokeWidth.set(vv[0]);
            }

            if (vv.length >= 2){
                strokeColor = cn.kkmofang.view.value.Color.valueOf(vv[1], 0xff000000);
                strokeAlpha = 0x0ff & (strokeColor >> 24);
            }
        }
    }

    private boolean update = false;

    public void setUpdate(boolean update) {
        this.update = update;
    }

}
