package cn.kkmofang.view.value;

import android.view.View;

/**
 * Created by hailong11 on 2018/6/29.
 */

public class Transform {

    public static void valueOf(View view , String value) {

        view.setTranslationX(0);
        view.setTranslationY(0);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        view.setRotationX(0);
        view.setRotationY(0);
        view.setRotation(0);

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
                    view.setTranslationX(x.floatValue(0,0));
                    view.setTranslationY(y.floatValue(0,0));
                }
            } else if(v.startsWith("scale(") && v.endsWith(")")) {
                String[] vv = v.substring(6,v.length() - 1).split(",");
                if(vv.length >1) {
                    x.set(vv[0]);
                    y.set(vv[1]);
                    view.setScaleX(x.floatValue(0,0));
                    view.setScaleY(y.floatValue(0,0));
                }
            } else if(v.startsWith("rotateX(") && v.endsWith(")")) {
                float vv = V.floatValue(v.substring(8,v.length() - 1).split(","),0);
                view.setRotationX((float)( vv * Math.PI / 180f));
            } else if(v.startsWith("rotateY(") && v.endsWith(")")) {
                float vv = V.floatValue(v.substring(8,v.length() - 1).split(","),0);
                view.setRotationY((float)( vv * Math.PI / 180f));
            } else if(v.startsWith("rotateZ(") && v.endsWith(")")) {
                float vv = V.floatValue(v.substring(8,v.length() - 1).split(","),0);
                view.setRotation((float)( vv * Math.PI / 180f));
            } else if(v.startsWith("rotate(") && v.endsWith(")")) {
                float vv = V.floatValue(v.substring(7,v.length() - 1).split(","),0);
                view.setRotation((float)( vv * Math.PI / 180f));
            }
        }

    }
}
