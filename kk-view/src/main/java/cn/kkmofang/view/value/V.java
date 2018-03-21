package cn.kkmofang.view.value;

import android.util.Log;

import cn.kkmofang.view.Tag;

/**
 * Created by hailong11 on 2018/1/18.
 */

public final class V {

    public final static boolean booleanValue(Object v,boolean defaultValue) {
        if(v == null) {
            return defaultValue;
        }
        if(v instanceof Boolean) {
            return (Boolean) v;
        }
        return "true".equals(v) || "yes".equals(v) || "1".equals(v);
    }

    public final static float floatValue(Object v,float defaultValue) {
        if(v == null) {
            return defaultValue;
        }
        if(v instanceof Float) {
            return (Float) v;
        }
        if(v instanceof Number) {
            return ((Number) v).floatValue();
        }
        if(v instanceof String) {
            try {
                return Float.parseFloat((String)v);
            }
            catch(Throwable e) {
                Log.d(Tag.Tag,Log.getStackTraceString(e));
            }
        }
        return defaultValue;
    }

}
