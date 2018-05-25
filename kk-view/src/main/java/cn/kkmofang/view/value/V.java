package cn.kkmofang.view.value;

import android.util.Log;

import java.util.Map;

import cn.kkmofang.view.Tag;

/**
 * Created by zhanghailong on 2018/1/18.
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

    public final static String stringValue(Object v,String defaultValue) {

        if(v == null){
            return defaultValue;
        }

        if(v instanceof String) {
            return (String) v;
        }

        return v.toString();
    }

    public final static Object get(Object object,String key) {
        if(object == null) {
            return null;
        }
        if(object instanceof Map){
            Map m = (Map) object;
            if(m.containsKey(key)) {
                return m.get(key);
            }
        }
        return null;
    }

}
