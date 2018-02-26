package cn.kkmofang.view;

import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Pixel;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class SpanElement extends Element {
    public SpanElement() {
        super();
        set("#textview", SpanElement.class.getName());
    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        String s = get(key);
        TextElement textElement = new TextElement();
        View view = textElement.view();
        if (view instanceof TextView){
            TextView textView = (TextView) view;
            if ("color".equals(key)){
                textView.setTextColor(Color.valueOf(s,0));
            }else if ("font".equals(key)){
                if (Pixel.Type.valueOf(s).equals(Pixel.UnitRPX)){
                    textView.setTextSize(32);
                    TextPaint tp = textView.getPaint();
                    tp.setFakeBoldText(true);
            }
            }else if ("letter-spacing".equals(key)){
                textView.setLineSpacing(Pixel.UnitRPX,Pixel.UnitRPX);
            }else if ("#text".equals(key)){
                textView.setText(s);
            }
        }
    }

    @Override
    public void set(String key, String value) {
        super.set(key, value);
    }

    @Override
    protected void onDidAddChildren(Element element) {
        super.onDidAddChildren(element);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    protected void onWillRemoveChildren(Element element) {
        super.onWillRemoveChildren(element);
    }
}
