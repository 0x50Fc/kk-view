package cn.kkmofang.view;

import android.animation.Animator;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

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

    public Animation getAnimation() {

        AnimationSet anim = new AnimationSet(true);

        valueOf(anim);

        Element e = firstChild();

        while(e != null) {
            if(e instanceof Item) {
                ((Item) e).addAnimation(anim);
            }
            e = e.nextSibling();
        }

        return anim;
    }

    public void valueOf(Animation anim) {

        anim.setStartOffset(delay);
        anim.setDuration(duration);
        anim.setRepeatCount((int) repeatCount);

        if(autoreverses) {
            anim.setRepeatMode(Animation.REVERSE);
        } else {
            anim.setRepeatMode(Animation.RESTART);
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

       public abstract void addAnimation(AnimationSet animSet);

    }

    public static class TransformAnimation extends Animation {

        private final float[] _fromValue = new float[9];
        private final float[] _toValue = new float[9];
        private final float[] _byValue = new float[9];
        private final float[] _vs = new float[9];

        public TransformAnimation(Matrix fromValue,Matrix toValue) {
            fromValue.getValues(_fromValue);
            toValue.getValues(_toValue);
            for(int i=0;i<9;i++) {
                _byValue[i] = _toValue[i] - _fromValue[i];
            }
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            t.setTransformationType(Transformation.TYPE_MATRIX);

            for(int i=0;i<9;i++) {
                _vs[i] = _fromValue[i] + _byValue[i] * interpolatedTime;
            }

            Matrix m = t.getMatrix();
            m.setValues(_vs);

        }
    }

    public static class Transform extends Item {

        public static void valueOf(Matrix matrix,String value) {

            if(value == null) {
                return;
            }

            Pixel x = new Pixel();
            Pixel y = new Pixel();

            String[] vs = value.split(" ");

            for(String v : vs) {
                if(v.startsWith("translate(") && v.endsWith(")")) {
                    String[] vv = v.substring(10,v.length() - 1).split(",");
                    if(vv.length >1) {
                        x.set(vv[0]);
                        y.set(vv[1]);
                        matrix.preTranslate(x.floatValue(0,0),y.floatValue(0,0));
                    }
                } else if(v.startsWith("scale(") && v.endsWith(")")) {
                    String[] vv = v.substring(6,v.length() - 1).split(",");
                    if(vv.length >1) {
                        x.set(vv[0]);
                        y.set(vv[1]);
                        matrix.preScale(x.floatValue(0,0),y.floatValue(0,0));
                    }
                } else if(v.startsWith("rotateX(") && v.endsWith(")")) {
                    float vv = V.floatValue(v.substring(8,v.length() - 1).split(","),0);
                    matrix.preRotate((float)( vv * Math.PI / 180f),1,0);
                } else if(v.startsWith("rotateY(") && v.endsWith(")")) {
                    float vv = V.floatValue(v.substring(8,v.length() - 1).split(","),0);
                    matrix.preRotate((float)( vv * Math.PI / 180f),0,1);
                } else if(v.startsWith("rotateZ(") && v.endsWith(")")) {
                    float vv = V.floatValue(v.substring(8,v.length() - 1).split(","),0);
                    matrix.preRotate((float)( vv * Math.PI / 180f));
                } else if(v.startsWith("rotate(") && v.endsWith(")")) {
                    float vv = V.floatValue(v.substring(7,v.length() - 1).split(","),0);
                    matrix.preRotate((float)( vv * Math.PI / 180f));
                }
            }
        }

        public final Matrix fromValue = new Matrix();
        public final Matrix toValue  = new Matrix();

        @Override
        public void changedKey(String key) {
            super.changedKey(key);
            if("from".equals(key)) {
                fromValue.reset();
                valueOf(fromValue,get(key));
            } else if("to".equals(key)) {
                valueOf(fromValue,get(key));
            }
        }

        @Override
        public void addAnimation(AnimationSet animSet) {
            animSet.addAnimation(new TransformAnimation(fromValue,toValue));
        }
    }

    public static class Opacity extends Item {

        public float fromValue = 1;
        public float toValue = 1;

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
        public void addAnimation(AnimationSet animSet) {
            animSet.addAnimation(new AlphaAnimation(fromValue,toValue));
        }
    }

}
