package cn.kkmofang.view.value;

import android.graphics.Typeface;

/**
 * 字体
 * Created by wangchao15 on 2018/2/27.
 */

public enum Font {
    NORAML,BOLD,ITALIC;
    public static Font fvalueOf(Object v){
        if ("bold".equals(v)){
            return BOLD;
        }
        if ("italic".equals(v)){
            return ITALIC;
        }
        return NORAML;
    }

    public static Typeface createTypeface(Font font){
        switch (font){
            case BOLD:
                return Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
            case ITALIC:
                return Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
            case NORAML:
            default:
                return Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
        }
    }
}
