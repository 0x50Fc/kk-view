package cn.kkmofang.view;

import android.text.TextUtils;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Pixel;

public class TextStyle {
    public Pixel strokeWidth = new Pixel();
    public int color;
    public int alpha;

    public boolean hasStroke;

    public int strokeColor;
    public int strokeAlpha;

    public void setColor(String value){
        if (!TextUtils.isEmpty(value)){
            color = Color.valueOf(value,0xff000000);
            alpha = 0x0ff & (color >> 24);
        }
    }

    public void setStroke(String value){
        if (!TextUtils.isEmpty(value)){
            hasStroke = true;
            String[] vv = value.split(" ");
            if (vv.length >= 1){
                strokeWidth.set(vv[0]);
            }

            if (vv.length >= 2){
                strokeColor = Color.valueOf(vv[1], 0xff000000);
                strokeAlpha = 0x0ff & (strokeColor >> 24);
            }
        }
    }
}
