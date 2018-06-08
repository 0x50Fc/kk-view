package cn.kkmofang.view;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

import cn.kkmofang.view.value.V;

/**
 * Created by zhanghailong on 2018/1/20.
 */

public class ButtonElement extends ViewElement {
    private boolean enabled = true;
    private boolean selected = false;
    private boolean hover = false;

    public ButtonElement(){
        super();
    }

    protected void setHover(boolean v) {
        if(hover != v) {
            hover = v;
            refreshStatus();
        }
    }

    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view, key, value);
        if ("enabled".equals(key)){
            enabled = V.booleanValue(get(key), true);
            refreshStatus();
        } else if ("selected".equals(key)){
            selected = V.booleanValue(get(key), false);
            refreshStatus();
        }
    }

    protected void refreshStatus() {
        if (enabled){
            if (selected){
                setStatus("selected");
            }else if (hover){
                setStatus("hover");
            }else {
                setStatus("");
            }
        }else {
            setStatus("disabled");
        }
    }

    @Override
    public void setView(final View view) {
        View v = this.view();
        if(v != null) {
            v.setOnClickListener(null);
        }
        super.setView(view);
        v = this.view();
        if(v != null) {

            final WeakReference<ButtonElement> element = new WeakReference<ButtonElement>(this);

            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (enabled){
                        ButtonElement e = element.get();
                        if(e != null) {
                            Element.Event event = new Element.Event(e);
                            event.setData(e.data());
                            e.emit("tap",event);
                        }
                    }
                }
            });

            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (enabled){
                        ButtonElement e = element.get();
                        if(e != null) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    setHover(true);
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (event.getX() > v.getWidth() || event.getX() < 0){
                                        setHover(false);
                                    }else if (event.getY() > v.getHeight() || event.getY() < 0){
                                        setHover(false);
                                    } else {
                                        setHover(true);
                                    }
                                    break;
                                case MotionEvent.ACTION_CANCEL:
                                    setHover(false);
                                    return true;
                                case MotionEvent.ACTION_UP:
                                    setHover(false);
                                    break;
                            }
                        }
                    }

                    return false;
                }
            });
        }
    }

}
