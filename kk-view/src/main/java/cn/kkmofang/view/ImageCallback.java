package cn.kkmofang.view;

import android.graphics.drawable.Drawable;

/**
 * Created by zhanghailong on 2018/3/21.
 */

public interface ImageCallback {
    void onImage(Drawable image);
    void onException(Exception exception);
}
