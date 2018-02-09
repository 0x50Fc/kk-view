package cn.kkmofang.view;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.TextAlign;
import cn.kkmofang.view.value.V;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class TextElement extends ViewElement {
    public TextElement() {
        super();
        set("#view", TextView.class.getName());
    }
    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view, key, value);
        if (view instanceof TextView){
            TextView textView = (TextView) view;
            if ("color".equals(key)){
                textView.setTextColor(Color.valueOf(value,0));
            }else if ("font".equals(key)) {
                if ("serif".equals(value)){
                    textView.setTypeface(Typeface.SERIF);
                }else if (("sans_serif").equals(value)){
                    textView.setTypeface(Typeface.SANS_SERIF);
                }else if (("monospace").equals(value)){
                    textView.setTypeface(Typeface.MONOSPACE);
                }
            }else if("line-spacing".equals(key)){
                 textView.setLineSpacing(V.floatValue(value,2.0f),V.floatValue(value,1.0f));
            }else if ("paragraph-spacing".equals(key)){
            }else if ("letter-spacing".equals(key)){
               textView.setTextScaleX(V.floatValue(value,3.0f));
            }else if ("text-align".equals(key)){
                if (TextAlign.Left.equals("left")){
                    textView.setGravity(Gravity.LEFT);
                }else if (TextAlign.Center.equals("center")){
                    textView.setGravity(Gravity.CENTER);
                }else if (TextAlign.Right.equals("right")){
                    textView.setGravity(Gravity.RIGHT);
                }else if  (TextAlign.Justify.equals("justify")){
                    textView.setGravity(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);
                }
            }else if ("#text".equals(key)){
                textView.setText(value);
            }
        }
    }
}
