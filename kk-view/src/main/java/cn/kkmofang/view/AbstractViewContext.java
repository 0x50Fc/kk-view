package cn.kkmofang.view;

import android.content.Context;
import android.graphics.drawable.Drawable;

import cn.kkmofang.image.ImageStyle;

/**
 * Created by zhanghailong on 2018/3/23.
 */

public abstract class AbstractViewContext implements IViewContext {

    private final Context _context;

    public AbstractViewContext(Context context) {
        _context = context;
    }

    @Override
    public Context getContext() {
        return _context;
    }

    @Override
    abstract public ImageTask getImage(String url, ImageStyle style, ImageCallback callback) ;

    @Override
    abstract public Drawable getImage(String url, ImageStyle style);
}
