package cn.kkmofang.view;

import android.animation.Animator;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieListener;
import com.airbnb.lottie.LottieTask;
import com.airbnb.lottie.OnCompositionLoadedListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import cn.kkmofang.view.value.V;

public class LottieElement extends ViewElement {
    private boolean _playing;
    private String filePath;
    private boolean _stoping;

    @Override
    public Class<?> viewClass() {
        return LottieAnimationView.class;
    }

    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view, key, value);
        if (("autoplay".equals(key) &&  V.booleanValue(value, false)) || "src".equals(key)){
            setNeedsPlaying();
        }
    }

    private void setNeedsPlaying(){

        if (_playing){
            return;
        }

        _playing = true;
        View v = view();
        if (v != null){
            Handler handler = v.getHandler();
            if (handler != null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        play();
                    }
                });
            }
        }

    }

    private void stop(){
        LottieAnimationView v = (LottieAnimationView) view();
        if (v != null){
            _stoping = true;
            v.cancelAnimation();
            filePath = null;
            _stoping = false;
        }
    }

    private void play(){
        _playing = false;
        String src = get("src");
        if (TextUtils.isEmpty(src) || !src.equals(filePath)){
            stop();
        }

        LottieAnimationView view = (LottieAnimationView) view();
        if (view != null && !TextUtils.isEmpty(src)){
            filePath = src;

            {
                String v = get("gravity");
                if ("resize".equals(v) || "resizeAspect".equals(v)) {
                    view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            }

            {
                String v = get("loop");
                view.setRepeatCount(V.booleanValue(v, false) ? -1:0);
            }

            {
                view.setRepeatMode(V.booleanValue(get("autoreverse"), false)? LottieDrawable.REVERSE:LottieDrawable.RESTART);
            }

            {
                String v = get("speed");
                if (v != null){
                    view.setSpeed(V.floatValue(v, 1.0f));
                } else {
                    view.setSpeed(1.0f);
                }
            }

            if (view.isAnimating()){
                view.cancelAnimation();
            }

            Event event = new Event(this);
            event.setData(this.data());
            emit("play", event);

            {
                final WeakReference<LottieElement> e = new WeakReference<>(this);


                view.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        LottieElement element = e.get();
                        if (element != null){
                            Event event = new Event(element);
                            Map<String, Object> data = element.data();
                            data.put("finished", true);
                            event.setData(data);
                            element.emit("done", event);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
            InputStream stream = null;
            if (filePath.startsWith("http://") || filePath.startsWith("https://")){
                try {
                    URL url = new URL(viewContext.getAbsolutePath(filePath));
                    stream = url.openStream();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String absolutePath  = viewContext.getAbsolutePath(filePath);
                try {
                    stream = new FileInputStream(absolutePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            final WeakReference<LottieAnimationView> v = new WeakReference<>(view);
            if (stream != null){
                LottieTask<LottieComposition> task = LottieCompositionFactory.fromJsonInputStream(stream, filePath);
                task.addListener(new LottieListener<LottieComposition>() {
                    @Override
                    public void onResult(LottieComposition result) {
                        LottieAnimationView lottieAnimationView = v.get();
                        if (lottieAnimationView != null){
                            lottieAnimationView.setComposition(result);
                            lottieAnimationView.playAnimation();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void recycleView() {
        stop();
        super.recycleView();
    }

    @Override
    public void obtainView(View view) {
        super.obtainView(view);
    }
}
