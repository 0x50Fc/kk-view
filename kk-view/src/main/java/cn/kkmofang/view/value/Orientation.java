package cn.kkmofang.view.value;

/**
 * 滚动容器方向,默认使用垂直滚动
 * Created by wangchao15 on 2018/2/5.
 */

public enum  Orientation {
    VERTICAL,//overflow-y
    HORIZONTAL;//overflow-x

    public static Orientation fValueOf(String o){
        if ("overflow-x".equals(o)){
            return HORIZONTAL;
        }
        return VERTICAL;
    }

    public String getVString(){
        if (this == HORIZONTAL){
            return "overflow-x";
        }
        return "overflow-y";
    }
}
