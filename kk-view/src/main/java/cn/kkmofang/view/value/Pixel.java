package cn.kkmofang.view.value;

import android.util.Log;

import cn.kkmofang.view.Tag;

/**
 * Created by zhanghailong on 2018/1/17.
 */
//像素
public final class Pixel implements IValue<Pixel> {

    public static float UnitPX = 1.0f;
    public static float UnitRPX = 1.0f;
    public static float UnitVH = 1.0f;
    public static float UnitVW = 1.0f;

    public final static float Auto = (float) Integer.MAX_VALUE;

    public Type type;
    public float value;


    public float floatValue(float baseOf,float defaultValue) {

        if(type == Type.Percent) {
            return value * baseOf * 0.01f;
        }

        if(type == Type.PX) {
            return value * UnitPX;
        }

        if(type == Type.RPX) {
            return value * UnitRPX;
        }

        if(type == Type.VH) {
            return value * UnitVH;
        }

        if(type == Type.VW) {
            return value * UnitVW;
        }

        return defaultValue;
    }

    public static boolean is(String v) {
        if(v == null) {
            return false;
        }
        return v.endsWith("px") || v.endsWith("vh") || v.endsWith("vw") || v.endsWith("%") || v.equals("auto");
    }

    @Override
    public void set(String v) {

        if(v != null) {

            if(v.endsWith("rpx")) {

                type = Type.RPX;

                try {
                    value = Float.valueOf(v.substring(0, v.length() - 3));
                }
                catch (Throwable e) {
                    value = 0;
                    Log.d(Tag.Tag,Log.getStackTraceString(e));
                }

                return ;

            } else if(v.endsWith("px")) {

                type = Type.PX;

                try {
                    value = Float.valueOf(v.substring(0, v.length() - 2));
                }
                catch (Throwable e) {
                    value = 0;
                    Log.d(Tag.Tag,Log.getStackTraceString(e));
                }

                return ;
            } else if(v.endsWith("vh")) {

                type = Type.VH;

                try {
                    value = Float.valueOf(v.substring(0, v.length() - 2));
                }
                catch (Throwable e) {
                    value = 0;
                    Log.d(Tag.Tag,Log.getStackTraceString(e));
                }

                return ;
            } else if(v.endsWith("vw")) {

                type = Type.VW;

                try {
                    value = Float.valueOf(v.substring(0, v.length() - 2));
                }
                catch (Throwable e) {
                    value = 0;
                    Log.d(Tag.Tag,Log.getStackTraceString(e));
                }

                return ;
            } else if(v.endsWith("%")) {

                type = Type.Percent;

                try {
                    value = Float.valueOf(v.substring(0, v.length() - 1));
                }
                catch (Throwable e) {
                    value = 0;
                    Log.d(Tag.Tag,Log.getStackTraceString(e));
                }

                return ;
            }

        }

        type = Type.Auto;
        value = 0f;
    }

    @Override
    public void set(Pixel v) {
        type = v.type;
        value = v.value;
    }

    @Override
    public String toString() {

        if(type == Type.Auto) {
            return "auto";
        }

        if(type == Type.Percent) {
            return value + "%";
        }

        if(type == Type.RPX) {
            return value + "rpx";
        }

        if(type == Type.VH) {
            return value + "vh";
        }

        if(type == Type.VW) {
            return value + "vw";
        }

        return value + "px";
    }

    public static enum Type {
        Auto,Percent,PX,RPX,VH,VW
    }
}
