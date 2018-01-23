package cn.kkmofang.view.value;

/**
 * Created by hailong11 on 2018/1/17.
 */

public enum VerticalAlign {
    Top,Middle,Bottom;

    public static VerticalAlign valueOf(Object v) {

        if("middle".equals(v)) {
            return Middle;
        }

        if("bottom".equals(v)) {
            return Bottom;
        }

        return Top;
    }
}
