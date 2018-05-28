package cn.kkmofang.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by zhanghailong on 2018/1/29.
 */

public class DocumentView extends ElementView {

    private boolean _animated;
    private boolean _changed;
    private ViewElement _element;
    private ViewElement _obtainElement;

    public DocumentView(Context context) {
        super(context);
    }

    public DocumentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewElement element() {
        return _element;
    }

    public void setElement(ViewElement element) {

        if (_element != element) {

            if (_element != null) {
                _element.recycleView();
            }

            _element = element;
            _changed = true;
            requestLayout();
        }

    }

    public ViewElement obtainElement() {
        return _obtainElement;
    }

    public void setObtainElement(ViewElement element) {

        if (_element != null) {
            _element.recycleView();
            _element = null;
        }

        if (_obtainElement != element) {

            if (_obtainElement != null) {
                _obtainElement.recycleView();
            }

            _obtainElement = element;
            _changed = true;

            requestLayout();
        }

    }

    public void setNeedsLayout(boolean animated) {
        _changed = true;
        _animated = animated;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (_element != null && (changed || _changed)) {
            _element.layout(r - l, b - t);
            _element.obtainView(this);
        }

        if (_obtainElement != null && (changed || _changed)) {
            _obtainElement.setX(_obtainElement.margin.left.floatValue(0,0));
            _obtainElement.setY(_obtainElement.margin.top.floatValue(0,0));
            _obtainElement.obtainView(this);
        }

        _changed = false;
        _animated = false;

        super.onLayout(changed, l, t, r, b);
    }
}
