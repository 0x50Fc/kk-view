package cn.kkmofang.view;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import cn.kkmofang.image.Image;
import cn.kkmofang.image.ImageStyle;
import cn.kkmofang.view.value.Pixel;

/**
 * Created by zhanghailong on 2018/1/20.
 */

public class ImageElement extends ViewElement {

    public final Pixel borderRadius = new Pixel();

    private final Handler _handler;
    private ImageStyle _imageStyle;
    private Drawable _image;
    private Drawable _defaultImage;

    public ImageElement() {
        super();
        set("#view", ImageView.class.getName());
        setLayout(ImageLayout);
        setViewLayer(View.LAYER_TYPE_HARDWARE);
        _handler = new Handler();
    }

    public ImageStyle imageStyle() {

        if(_imageStyle== null) {
            _imageStyle = new ImageStyle(viewContext.getContext());
            _imageStyle.radius = borderRadius.floatValue(0,0);
        }

        return _imageStyle;
    }

    public Drawable getImage() {

        if(_image == null) {
            _image = viewContext.getImage(get("src"),imageStyle());
        }

        return _image;
    }

    public Drawable getDefaultImage() {
        if(_defaultImage == null) {
            String v = get("default-src");
            if(v != null) {
                ImageStyle style = new ImageStyle(viewContext.getContext());
                style.radius = borderRadius.floatValue(0,0);
                _defaultImage = viewContext.getImage(v,style);
            }
        }
        return _defaultImage;
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

        if(_image != null) {

            if(width.type == Pixel.Type.Auto
                    || height.type == Pixel.Type.Auto) {
                emit("layout",new Event(this));
            }
        }
    }

    @Override
    public void changedKey(String key) {
        if("src".equals(key)) {
            _image = null;
        } else if("border-radius".equals(key)) {
            borderRadius.set(get(key));
            if(_imageStyle != null) {
                _imageStyle.radius = borderRadius.floatValue(0,0);
            }
        } else if("default-src".equals(key)) {
            _defaultImage = null;
        }
        super.changedKey(key);
    }

    public ImageView imageView() {
        View v = view();
        if(v != null && v instanceof ImageView) {
            return (ImageView) v;
        }
        return null;
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        setNeedDisplay();
    }

    private boolean _displaying = false;

    public void setNeedDisplay() {


        if(imageView() == null) {
            return;
        }

        if(_displaying) {
            return;
        }

        _displaying = true;

        _handler.post(new Runnable() {
            @Override
            public void run() {
                display();
            }
        });

    }

    protected void display() {

        ImageView v = imageView();

        if(v != null) {
            if(_image != null) {
                v.setImageDrawable(_image);
            } else {
                v.setImageDrawable(getDefaultImage());
            }
        }
        _displaying = false;
    }

    private long imageId = 0;

    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view, key, value);

        if ("src".equals(key)){

            final WeakReference<ImageElement> v = new WeakReference<ImageElement>(this);

            imageId ++;

            final long id = imageId;

            loadImage(new ImageCallback() {

                @Override
                public void onImage(Drawable drawable) {

                    ImageElement e = v.get();

                    if(e != null && id == e.imageId) {
                        e.setImage(drawable);
                        e.display();
                    }
                }

                @Override
                public void onException(Exception exception) {

                }
            });
        } else if("overflow".equals(key)) {
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
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
                    r = (int) Math.ceil( ((Image) image).width() * Pixel.UnitPX);
                    b = (int) Math.ceil( ((Image) image).height() * Pixel.UnitPX );
                } else if(image instanceof BitmapDrawable) {
                    r = (int) Math.ceil( ((BitmapDrawable) image).getBitmap().getWidth() );
                    b = (int) Math.ceil( ((BitmapDrawable) image).getBitmap().getHeight() );
                }

                if(width == Pixel.Auto && height == Pixel.Auto) {

                } else if(width == Pixel.Auto) {
                    r = (int) Math.ceil(r *  height / b);
                    b = (int) Math.ceil(height);
                } else if(height == Pixel.Auto) {
                    b = (int) Math.ceil(b *  width / r);
                    r = (int) Math.ceil(width);
                }

                element.setContentSize(r, b);

            } else {
                element.setContentSize(width,height);
            }

        }
    };

}
