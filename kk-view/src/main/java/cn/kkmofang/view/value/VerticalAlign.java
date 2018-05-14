package cn.kkmofang.view.value;

/**
 * Created by zhanghailong on 2018/1/17.
 */
//垂直对齐
public enum VerticalAlign {
    Top,Middle,Bottom;

    public static VerticalAlign valueOfString(String v) {

        if("middle".equals(v)) {
            return Middle;
        }

        if("bottom".equals(v)) {
            return Bottom;
        }

        return Top;
    }
}
