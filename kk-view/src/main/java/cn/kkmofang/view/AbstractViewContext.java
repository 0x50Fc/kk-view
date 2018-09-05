package cn.kkmofang.view;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.lang.ref.WeakReference;

import cn.kkmofang.image.ImageStyle;

/**
 * Created by zhanghailong on 2018/3/23.
 */

public abstract class AbstractViewContext implements IViewContext {

    protected final Context _context;
    private WeakReference<IViewApplication> _app;


    public AbstractViewContext(Context context) {
        _context = context;
    }

    public IViewApplication getViewApplication() {
        return _app != null ? _app.get() : null;
    }

    public void setViewApplication(IViewApplication app) {
        if(app == null) {
            _app = null;
        } else {
            _app = new WeakReference<>(app);
        }
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
