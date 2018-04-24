package cn.kkmofang.view.value;

import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * 字体
 * Created by zhanghailong on 2018/2/27.
 */

public class Font {

    public static void valueOf(String value,Paint paint) {

        if(value == null) {
            return ;
        }

        Pixel fontSize = new Pixel();
        boolean bold = false;
        boolean italic= false;
        String name = null;

        String[] vs = value.split(" ");

        for(String v : vs) {

            if(Pixel.is(v)) {
                fontSize.set(v);
            } else if("bold".equals(v)) {
                bold = true;
            } else if("italic".equals(v)) {
                italic = true;
            } else {
                name = v;
            }
        }

        paint.setTextSize(fontSize.floatValue(0,0));
        paint.setFakeBoldText(bold);

        if(name == null) {
            if(italic) {
                paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.ITALIC));
            } else if(bold) {
                paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
            } else {
                paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
            }
        } else {
            if(italic) {
                paint.setTypeface(Typeface.create(name,Typeface.ITALIC));
            } else if(bold) {
                paint.setTypeface(Typeface.create(name,Typeface.BOLD));
            } else {
                paint.setTypeface(Typeface.create(name,Typeface.NORMAL));
            }
        }

    }

}
