package cn.kkmofang.view.utils;

public class Utils {

    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0L < timeD && timeD < 500L) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }
}
