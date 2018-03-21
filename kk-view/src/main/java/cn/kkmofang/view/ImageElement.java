package cn.kkmofang.view;

import android.graphics.drawable.Drawable;
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
                ImageStyle style = new ImageStyle();
                style.radius = (int) borderRadius.floatValue(0, 0);

                final ImageView imageView = (ImageView) view;

                viewContext.getImage(url, style, new ImageCallback() {

                    @Override
                    public void onImage(Drawable image) {
                        imageView.setImageDrawable(image);
                    }

                    @Override
                    public void onException(Exception exception) {

                    }
                });
            }
        }
    }
}
