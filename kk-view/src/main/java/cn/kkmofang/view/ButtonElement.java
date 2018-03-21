package cn.kkmofang.view;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

import cn.kkmofang.view.view.FTextView;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class ButtonElement extends TextElement {
    public ButtonElement(){
        super();
    }

    @Override
    public void obtainView(View view) {
        super.obtainView(view);
        final View bt = view();
        if (bt != null && bt instanceof FTextView){
            bt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    setBackgroundForHover(event.getAction(), bt);
                    return false;
                }
            });

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(viewContext.getContext(), "onclick", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setBackgroundForHover(int state, View view){
        if (state == MotionEvent.ACTION_MOVE)return;
        Map<String, String> hovers = getStyle("hover");
        if (hovers != null){
            switch (state){
                case MotionEvent.ACTION_DOWN:{
                    Set<String> keySet = hovers.keySet();
                    for (String key : keySet) {
                        setBackground(key, hovers.get(key), view);
                    }
                    break;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:{
                    for (String key : keys()) {
                        setBackground(key, get(key), view);
                    }
                    break;
                }
            }
        }

    }

}
