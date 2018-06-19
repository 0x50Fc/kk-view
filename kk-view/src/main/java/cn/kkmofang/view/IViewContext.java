package cn.kkmofang.view;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.File;

import cn.kkmofang.image.ImageStyle;

/**
 * Created by zhanghailong on 2018/3/13.
 */

public interface IViewContext {

    Context getContext();

    ImageTask getImage(String url, ImageStyle style, ImageCallback callback);

    Drawable getImage(String url, ImageStyle style);

    AudioTask downLoadFile(String url, AudioElement.IAudioLoadCallback callback);
}
