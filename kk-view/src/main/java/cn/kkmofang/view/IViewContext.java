package cn.kkmofang.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import cn.kkmofang.image.ImageStyle;

/**
 * Created by hailong11 on 2018/3/13.
 */

public interface IViewContext {

    Context getContext();

    ImageTask getImage(String url, ImageStyle style, ImageCallback callback);

    Drawable getImage(String url, ImageStyle style);

}
