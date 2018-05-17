package cn.kkmofang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import java.lang.ref.WeakReference;

import cn.kkmofang.view.layout.FlexLayout;

/**
 * Created by zhanghailong on 2018/1/29.
 */

public class DocumentView extends ElementView {

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

            if (_obtainElement != null) {
                _obtainElement.setX(0);
                _obtainElement.setY(0);
                _obtainElement.obtainView(this);
            }

            requestLayout();
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (_element != null ) {
            _element.layout(r - l, b - t);
            _element.obtainView(this);
        }

        super.onLayout(changed, l, t, r, b);
    }
}
