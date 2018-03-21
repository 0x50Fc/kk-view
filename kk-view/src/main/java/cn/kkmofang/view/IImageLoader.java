package cn.kkmofang.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by wangchao15 on 2018/3/2.
 */

public interface IImageLoader {

    void loadImage(String url, ImageView view, Context context, int radius);
}
