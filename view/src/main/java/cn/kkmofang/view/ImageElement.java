package cn.kkmofang.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import cn.kkmofang.view.value.Pixel;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class ImageElement extends ViewElement {
    private String url;

    public ImageElement() {
        super();
        set("#view", ImageView.class.getName());
    }

    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view, key, value);
        if (view instanceof ImageView){
            if ("src".equals(key)){
                IImageLoader loader = ViewContext.getImageLoader();
                if (loader != null){
                    int radius = (int) borderRadius.floatValue(ViewContext.windowPoint.x, 0);
                    if (radius < 0){
                        radius = 0;
                    }
                    loader.loadImage(get(key), (ImageView) view, viewContext.context, radius);
                }
            }
        }
    }
}
