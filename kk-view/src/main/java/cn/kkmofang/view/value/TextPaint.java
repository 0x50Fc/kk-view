package cn.kkmofang.view.value;

import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by nwangchao15 on 2018/5/3.
 */

public class TextPaint {
    Paint paint;

    boolean colorSet;

    boolean fontSet;

    public TextPaint() {
        this.paint = new Paint();
    }

    public void setFont(String value){
        Font.valueOf(value, paint);
        fontSet = true;
    }

    public void setColor(String value){
        int v = Color.valueOf(value, 0xff000000);
        paint.setColor(v);
        paint.setAlpha(0x0ff & (v >> 24));
        colorSet = true;
    }

    public boolean isSetColor(){
        return colorSet;
    }

    public boolean isSetFont(){
        return fontSet;
    }

    public Paint getPaint() {
        return paint;
    }
}
