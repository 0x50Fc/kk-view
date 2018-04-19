package cn.kkmofang.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.View;

/**
 * Created by zhanghailong on 2018/4/19.
 */

public class TextView extends View {

    private Text _text;

    public Text text() {
        return _text;
    }

    public void setText(Text v) {
        _text = v;
        invalidate();
    }

    public TextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(_text != null) {
            _text.draw(canvas);
        }
    }
}
