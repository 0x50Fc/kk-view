package cn.kkmofang.demo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;


import cn.kkmofang.view.IViewContext;
import cn.kkmofang.view.ImageCallback;
import cn.kkmofang.view.ImageStyle;
import cn.kkmofang.view.ImageTask;

/**
 * Created by zhanghailong on 2018/3/21.
 */

public class MainViewContext implements IViewContext {

    private final Context _context;

    public MainViewContext(Context context) {
        _context = context;
    }

    @Override
    public Context getContext() {
        return _context;
    }

    @Override
    public ImageTask getImage(String url, ImageStyle style, final ImageCallback callback) {

        Glide.with(_context)
                .load(url)
                .bitmapTransform(new RoundCornersTransformation(_context, style.radius,
                        RoundCornersTransformation.CornerType.ALL))
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        callback.onImage(glideDrawable);
                    }
                });

        return null;
    }

    @Override
    public Drawable getImage(String url, ImageStyle style) {


        return null;
    }
}
