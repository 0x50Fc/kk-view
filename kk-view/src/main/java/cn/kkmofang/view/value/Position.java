package cn.kkmofang.view.value;

/**
 * Created by zhanghailong on 2018/1/17.
 */
//位置
public enum Position {
    None {
        @Override
        public int intValue() {
            return 0;
        }
    },Top {
        @Override
        public int intValue() {
            return 1;
        }
    },Bottom {
        @Override
        public int intValue() {
            return 2;
        }
    },Left {
        @Override
        public int intValue() {
            return 3;
        }
    },Right {
        @Override
        public int intValue() {
            return 4;
        }
    },Pull {
        @Override
        public int intValue() {
            return 5;
        }
    };

    public static Position valueOfString(String v) {

        if("top".equals(v)) {
            return Top;
        }

        if("bottom".equals(v)) {
            return Bottom;
        }

        if("left".equals(v)) {
            return Left;
        }

        if("right".equals(v)) {
            return Right;
        }

        if("pull".equals(v)) {
            return Pull;
        }

        return None;
    }

    public abstract int intValue();
}
