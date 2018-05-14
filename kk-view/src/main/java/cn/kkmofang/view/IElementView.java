package cn.kkmofang.view;

import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by zhanghailong on 2018/1/18.
 */

public interface IElementView {

    public void setProperty(View view,ViewElement element, String key, String value);
    public void layout(View view,ViewElement element);
    public void obtainView(View view,ViewElement element);
    public void recycleView(View view,ViewElement element);

}
