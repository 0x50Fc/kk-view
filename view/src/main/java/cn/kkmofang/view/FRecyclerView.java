package cn.kkmofang.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import cn.kkmofang.view.value.Orientation;

/**
 * Created by wangchao15 on 2018/2/6.
 */

public class FRecyclerView extends RecyclerView implements IElementView{
    private ScrollElement _element;

    public FRecyclerView(Context context) {
        this(context, null);
    }

    public FRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewElement element() {
        return _element;
    }

    public void setElement(ScrollElement element) {

        if(_element != element) {

            if(_element != null) {
                _element.recycleView();
            }

            _element = element;

            if(_element != null) {
                _element.obtainView(this);
            }

            requestLayout();
        }

    }

    @Override
    public void setProperty(View view, ViewElement element, String key, String value) {
        if ("scroll".equals(key)){
            Orientation orientation = Orientation.fValueOf(value);
            if (_element != null){
                _element.setmOrientation(orientation);
            }
        }
    }

    @Override
    public void layout(View view, ViewElement element) {

    }

    @Override
    public void obtainView(View view, ViewElement element) {

    }

    @Override
    public void recycleView(View view, ViewElement element) {

    }
}
