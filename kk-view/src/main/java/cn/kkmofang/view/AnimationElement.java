package cn.kkmofang.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.V;

/**
 * Created by hailong11 on 2018/6/12.
 */

public class AnimationElement extends Element {

    public long duration;
    public long repeatCount;
    public boolean autoreverses;
    public long delay;

    public void startAnimation(View view, Animator.AnimatorListener listener) {
        ObjectAnimator animator = new ObjectAnimator();
        valueOf(animator);

        List<PropertyValuesHolder> holders = new ArrayList<>();

        Element e = firstChild();
        while (e != null){
            if (e instanceof Item){
                ((Item) e).ofAnimation(animator, view, holders);
            }
            e = e.nextSibling();
        }

        animator.setTarget(view);

        PropertyValuesHolder[] vv = new PropertyValuesHolder[holders.size()];
        animator.setValues(holders.toArray(vv));

        animator.addListener(listener);
        animator.start();

    }

    public void valueOf(ObjectAnimator anim) {

        anim.setStartDelay(delay);
        anim.setDuration(duration);
        anim.setRepeatCount((int) repeatCount);
        anim.setInterpolator(new LinearInterpolator());

        if(autoreverses) {
            anim.setRepeatMode(ObjectAnimator.REVERSE);
        } else {
            anim.setRepeatMode(ObjectAnimator.RESTART);
        }

    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        if("duration".equals(key)) {
            duration = V.longValue(get(key),0);
        } else if("repeat-count".equals(key)) {
            repeatCount = V.longValue(get(key),0);
        } else if("autoreverses".equals(key)) {
            autoreverses = V.booleanValue(get(key),false);
        } else if("delay".equals(key)) {
            delay = V.longValue(get(key),0);
        }
    }



    public static abstract class Item extends Element {

       public abstract void ofAnimation(ObjectAnimator animator, View view, List<PropertyValuesHolder> holders);

    }

    public static class Transform extends Item {

        String from;
        String to;
        @Override
        public void changedKey(String key) {
            super.changedKey(key);
            if("from".equals(key)) {
                from = get(key);
            } else if("to".equals(key)) {
                to = get(key);
            }
        }


        @Override
        public void ofAnimation(ObjectAnimator animator, View view, List<PropertyValuesHolder> holders) {
            if (from == null || to == null){
                return;
            }

            String[] fromArray = from.split(" ");
            String[] toArray = to.split(" ");
            if (fromArray.length != toArray.length){
                return;
            }

            Pixel pFrom = new Pixel();
            Pixel pTo = new Pixel();
            for (int i = 0; i < fromArray.length; i++) {
                String fromItem = fromArray[i];
                String toItem = toArray[i];
                if (fromItem.startsWith("translate(") && fromItem.endsWith(")")){

                    String[] fromValue = fromItem.substring(10, fromItem.length() - 1).split(",");
                    String[] toValue = toItem.substring(10, toItem.length() - 1).split(",");
                    if (fromValue.length > 1 && toValue.length > 1){
                        pFrom.set(fromValue[0]);
                        pTo.set(toValue[0]);
                        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("translationX", pFrom.floatValue(0, 0), pTo.floatValue(0, 0));
                        holders.add(holderX);

                        pFrom.set(fromValue[1]);
                        pTo.set(toValue[1]);
                        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("translationY", pFrom.floatValue(0, 0), pTo.floatValue(0, 0));
                        holders.add(holderY);
                    }

                } else if (fromItem.startsWith("scale(") && fromItem.endsWith(")")){

                    String[] fromValue = fromItem.substring(6, fromItem.length() - 1).split(",");
                    String[] toValue = toItem.substring(6, toItem.length() - 1).split(",");
                    if (fromValue.length > 1 && toValue.length > 1){
                        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", V.floatValue(fromValue[0], 0), V.floatValue(toValue[0], 0));
                        holders.add(holderX);

                        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", V.floatValue(fromValue[1], 0), V.floatValue(toValue[1], 0));
                        holders.add(holderY);

                    }

                } else if (fromItem.startsWith("rotateX(") && fromItem.endsWith(")")){
                    float fromValue = V.floatValue(fromItem.substring(8, fromItem.length() - 1).split(","),0);
                    float toValue = V.floatValue(toItem.substring(8, toItem.length() - 1).split(","), 0);

                    PropertyValuesHolder holder = PropertyValuesHolder.ofFloat("rotationX", (float)( fromValue * Math.PI / 180f), (float)( toValue * Math.PI / 180f));
                    holders.add(holder);

                } else if (fromItem.startsWith("rotateY(") && fromItem.endsWith(")")){

                    float fromValue = V.floatValue(fromItem.substring(8, fromItem.length() - 1).split(","),0);
                    float toValue = V.floatValue(toItem.substring(8, toItem.length() - 1).split(","), 0);

                    PropertyValuesHolder holder = PropertyValuesHolder.ofFloat("rotationY", (float)( fromValue * Math.PI / 180f), (float)( toValue * Math.PI / 180f));
                    holders.add(holder);

                } else if (fromItem.startsWith("rotate(") && fromItem.endsWith(")")){
                    float fromValue = V.floatValue(fromItem.substring(7, fromItem.length() - 1).split(","),0);
                    float toValue = V.floatValue(toItem.substring(7, toItem.length() - 1).split(","), 0);

                    PropertyValuesHolder holder = PropertyValuesHolder.ofFloat("rotation", (float)( fromValue * Math.PI / 180f), (float)( toValue * Math.PI / 180f));
                    holders.add(holder);
                }

                final WeakReference<View> v = new WeakReference<>(view);
                animator.setInterpolator(new LinearInterpolator());
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        View view = v.get();
                        if(view != null) {
                            view.setTranslationX(0);
                            view.setTranslationY(0);
                            view.setScaleX(1.0f);
                            view.setScaleY(1.0f);
                            view.setRotation(0.0f);
                            view.setRotationX(0.0f);
                            view.setRotationY(0.0f);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        }
    }

    public static class Opacity extends Item {

        private float fromValue = 1;
        private float toValue = 1;

        @Override
        public void changedKey(String key) {
            super.changedKey(key);
            if("from".equals(key)) {
                fromValue = V.floatValue(get(key),1);
            } else if("to".equals(key)) {
                toValue = V.floatValue(get(key),1);
            }
        }

        @Override
        public void ofAnimation(ObjectAnimator animator, View view, List<PropertyValuesHolder> holders) {
            holders.add(PropertyValuesHolder.ofFloat("alpha", fromValue, toValue));
            final WeakReference<View> v = new WeakReference<>(view);
            animator.setInterpolator(new LinearInterpolator());
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    View view = v.get();
                    if (view != null){
                        view.setAlpha(1);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

}
