package cn.kkmofang.view.value;

import android.util.Log;

import cn.kkmofang.view.Tag;

/**
 * Created by hailong11 on 2018/1/17.
 */

public final class Pixel implements IValue<Pixel> {

    public static float UnitPX = 1.0f;
    public static float UnitRPX = 1.0f;
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
        return defaultValue;
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

        return value + "px";
    }

    public static enum Type {
        Auto,Percent,PX,RPX
    }
}
