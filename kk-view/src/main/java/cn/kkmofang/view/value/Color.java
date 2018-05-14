package cn.kkmofang.view.value;

import android.util.Log;

import cn.kkmofang.view.Tag;

/**
 * Created by zhanghailong on 2018/1/18.
 */

public class Color {

    public static int valueOf(String v,int defaultValue) {

        if(v != null && v.startsWith("#")) {

            if(v.length() == 4) {
                try {
                    int vv = Integer.valueOf(v.substring(1), 16);
                    int r = (vv >> 8) & 0x0f;
                    int g = (vv >> 4) & 0x0f;
                    int b = (vv) & 0x0f;

                    r = (r << 4) | r;
                    g = (g << 4) | g;
                    b = (b << 4) | b;

                    return 0x0ff000000 | (r << 16) | (g << 8) | b;
                }
                catch(Throwable e) {
                    Log.d(Tag.Tag,Log.getStackTraceString(e));
                }
            } else if(v.length() == 7) {
                try {
                    int vv = Long.valueOf(v.substring(1), 16).intValue();
                    return 0x0ff000000 | vv;
                }
                catch(Throwable e) {
                    Log.d(Tag.Tag,Log.getStackTraceString(e));
                }
            } else if(v.length() == 9) {
                try {
                    int vv = Long.valueOf(v.substring(1), 16).intValue();
                    return vv;
                }
                catch(Throwable e) {
                    Log.d(Tag.Tag,Log.getStackTraceString(e));
                }
            }
        }

        return defaultValue;
    }
}
