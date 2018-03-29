package cn.kkmofang.view;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;

import cn.kkmofang.view.view.FTextView;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class ButtonElement extends ViewElement {

    public ButtonElement(){
        super();
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
                    ButtonElement e = element.get();
                    if(e != null) {
                        Element.Event event = new Element.Event(e);
                        event.setData(e.data());
                        e.emit("tap",event);
                    }
                }
            });

            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ButtonElement e = element.get();
                    if(e != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                e.setStatus("hover");
                                break;
                            case MotionEvent.ACTION_MOVE:
                                System.out.println("Location:" + event.getX()+":"+v.getLeft()+":"+v.getWidth());
                                if (event.getX() > v.getWidth() || event.getX() < 0){
                                    e.setStatus("");
                                }else if (event.getY() > v.getHeight() || event.getY() < 0){
                                    e.setStatus("");
                                }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                e.setStatus("");
                                return true;
                            case MotionEvent.ACTION_UP:
                                e.setStatus("");
                                break;
                        }
                    }
                    return false;
                }
            });
        }
    }

}
