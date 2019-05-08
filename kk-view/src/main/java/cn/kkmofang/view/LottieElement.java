package cn.kkmofang.view;

import android.animation.Animator;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.lang.ref.WeakReference;
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
        if ("autoplay".equals(key) || "src".equals(key)){
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
        if (view != null){

            if (!TextUtils.isEmpty(src)){
                String v = get("gravity");
                if ("resize".equals(v) || "resizeAspect".equals(v)){
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
            view.setAnimation(viewContext.getAbsolutePath(filePath));
            view.playAnimation();
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
