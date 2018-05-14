package cn.kkmofang.view.value;

/**
 * Created by zhanghailong on 2018/1/17.
 */
//对齐方式
public enum TextAlign {
    Left,Center,Right,Justify;

    public static TextAlign valueOfString(String v) {

        if("center".equals(v)) {
            return Center;
        }

        if("right".equals(v)) {
            return Right;
        }

        if("justify".equals(v)) {
            return Justify;
        }

        return Left;
    }
}
