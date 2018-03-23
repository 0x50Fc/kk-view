package cn.kkmofang.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

/**
 * Created by hailong11 on 2018/3/23.
 */

public class AbstractViewContext implements IViewContext {

    private final Context _context;

    public AbstractViewContext(Context context) {
        _context = context;
    }

    @Override
    public Context getContext() {
        return _context;
    }

    @Override
    public ImageTask getImage(String url, ImageStyle style, ImageCallback callback) {
        return null;
    }

    @Override
    public Drawable getImage(String url, ImageStyle style) {
        return null;
    }
}
