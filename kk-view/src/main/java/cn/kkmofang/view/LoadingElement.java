package cn.kkmofang.view;

import android.view.View;

import cn.kkmofang.view.value.V;

/**
 * Created by zhanghailong on 2018/1/20.
 */

public class LoadingElement extends ViewElement {

    public LoadingElement() {
        super();
        set("#view", LoadingView.class.getName());
    }

    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view, key, value);

        if("hidden".equals(key)) {
            boolean v = V.booleanValue(value,false);
            if (view != null && view instanceof LoadingView){
                if (v){
                    ((LoadingView) view).stop();
                }else {
                    ((LoadingView) view).start();

                }
            }
        }
    }
}
