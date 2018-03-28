package cn.kkmofang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.kk.view.R;

import java.lang.ref.WeakReference;

import cn.kkmofang.view.layout.FlexLayout;

/**
 * Created by hailong11 on 2018/1/29.
 */

public class DocumentView extends ElementView {

    private ViewElement _element;


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

        if(_element != element) {

            if(_element != null) {
                _element.recycleView();
            }

            _element = element;

            requestLayout();
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if(_element != null) {
            _element.layout(r - l , b - t);
            _element.obtainView(this);
        }
        super.onLayout(changed,l,t,r,b);

    }


}
