package cn.kkmofang.demo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;

import cn.kkmofang.view.IImageLoader;

/**
 * Created by wangchao15 on 2018/3/2.
 */

public class ImageLoader implements IImageLoader {
    @Override
    public void loadImage(String url, ImageView view, Context context, int radius) {
        Glide.with(context)
                .load(url)
                .bitmapTransform(new RoundCornersTransformation(context, radius,
                        RoundCornersTransformation.CornerType.ALL))
                .into(view);
    }
}
