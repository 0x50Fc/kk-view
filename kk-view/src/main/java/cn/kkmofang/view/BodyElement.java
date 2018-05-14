package cn.kkmofang.view;

import android.view.View;

/**
 * Created by zhanghailong on 2018/1/30.
 */

public class BodyElement extends ViewElement {

    public void obtainView(View view) {

        if(view() == view) {
            obtainChildrenView();
            return;
        }

        recycleView();

        onObtainView(view);
        onLayout(view);
        setView(view);

        for(String key : keys()) {
            String v = get(key);
            onSetProperty(view,key,v);
        }

        obtainChildrenView();
    }
}
