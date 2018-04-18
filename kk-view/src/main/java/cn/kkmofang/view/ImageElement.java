package cn.kkmofang.view;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import java.lang.ref.WeakReference;
import cn.kkmofang.image.Image;
import cn.kkmofang.image.ImageStyle;
import cn.kkmofang.view.value.Pixel;

/**
 * Created by zhanghailong on 2018/1/20.
 */

public class ImageElement extends ViewElement {

    public final Pixel borderRadius = new Pixel();

    private ImageStyle _imageStyle;
    private Drawable _image;

    public ImageElement() {
        super();
        setLayout(ImageLayout);
    }

    public ImageStyle imageStyle() {

        if(_imageStyle== null) {
            _imageStyle = new ImageStyle(viewContext.getContext());
            _imageStyle.radius = borderRadius.floatValue(0,0);
            if(_imageStyle.radius >0){
                Log.d("","");
            }
        }

        return _imageStyle;
    }
    public Drawable getImage() {

        if(_image == null) {
            _image = viewContext.getImage(get("src"),imageStyle());
        }

        return _image;
    }

    public void loadImage(ImageCallback cb) {
        if(_image != null){
            cb.onImage(_image);
        } else {
            viewContext.getImage(get("src"),imageStyle(),cb);
        }
    }

    public void setImage(Drawable image) {
        _image = image;
        View v = view();
        if(v != null) {
            v.setBackground(image);
        }
    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        if("src".equals(key)) {
            _image = null;
        } else if("border-radius".equals(key)) {
            borderRadius.set(get(key));
            if(_imageStyle != null) {
                _imageStyle.radius = borderRadius.floatValue(0,0);
            }
        }
    }

    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view, key, value);

        if ("src".equals(key)){

            final WeakReference<ImageElement> v = new WeakReference<ImageElement>(this);

            loadImage(new ImageCallback() {

                @Override
                public void onImage(Drawable drawable) {

                    ImageElement e = v.get();

                    if(e != null) {
                        e.setImage(drawable);
                    }
                }

                @Override
                public void onException(Exception exception) {

                }
            });
        }
    }

    private static final Layout ImageLayout = new Layout(){
        @Override
        public void layout(ViewElement element) {

            float width = element.width();
            float height = element.height();

            ImageElement e = (ImageElement) element;

            if(width == Pixel.Auto || height == Pixel.Auto) {

                Drawable image = e.getImage();

                int r = 0,b = 0;

                if(image instanceof Image) {
                    r = (int) Math.ceil( ((Image) image).getBitmap().getWidth() / Pixel.UnitPX );
                    b = (int) Math.ceil( ((Image) image).getBitmap().getHeight() / Pixel.UnitPX );
                } else if(image instanceof BitmapDrawable) {
                    r = (int) Math.ceil( ((BitmapDrawable) image).getBitmap().getWidth() / Pixel.UnitPX);
                    b = (int) Math.ceil( ((BitmapDrawable) image).getBitmap().getHeight() / Pixel.UnitPX);
                }

                if(width == Pixel.Auto && height == Pixel.Auto) {

                } else if(width == Pixel.Auto) {
                    r = (int) Math.ceil(r *  height / b);
                    b = (int) Math.ceil(height);
                } else if(height == Pixel.Auto) {
                    b = (int) Math.ceil(b *  width / r);
                    r = (int) Math.ceil(height);
                }

                element.setContentSize(r, b);

            } else {
                element.setContentSize(width,height);
            }

        }
    };

}
