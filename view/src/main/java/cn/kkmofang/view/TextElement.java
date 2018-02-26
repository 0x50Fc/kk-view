package cn.kkmofang.view;

import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.TextAlign;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class TextElement extends ViewElement {

    public TextElement() {
        super();
        set("#view", TextView.class.getName());
        Element p= firstChild();
        while (p != null){
            p= p.nextSibling();
        }

    }
    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view, key, value);
        if (view instanceof TextView){
            TextView textView = (TextView) view;
            if ("color".equals(key)){
                textView.setTextColor(Color.valueOf(value,0));
            }else if ("font".equals(key)) {
               if (Pixel.Type.valueOf(value).equals(Pixel.UnitRPX)){
                   textView.setTextSize(32);
                   TextPaint tp = textView.getPaint();
                   tp.setFakeBoldText(true);
               }
            }else if("line-spacing".equals(key)){
                 textView.setLineSpacing(Pixel.UnitRPX,Pixel.UnitRPX);
            }else if ("paragraph-spacing".equals(key)){
                // TODO: 2018\2\12 0012
            }else if ("letter-spacing".equals(key)){
               textView.setLetterSpacing(Pixel.UnitPX);
            }else if ("text-align".equals(key)){
                if (TextAlign.fvalueOf(value)==TextAlign.Left){
                    textView.setGravity(Gravity.LEFT);
                }else if (TextAlign.fvalueOf(value)==TextAlign.Center){
                    textView.setGravity(Gravity.CENTER);
                }else if (TextAlign.fvalueOf(value)==TextAlign.Right){
                    textView.setGravity(Gravity.RIGHT);
                }else if  (TextAlign.fvalueOf(value)==TextAlign.Justify){
                    textView.setGravity(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);
                }
            }else if ("#text".equals(key)){
                textView.setText(value);
            }
        }
    }
}
