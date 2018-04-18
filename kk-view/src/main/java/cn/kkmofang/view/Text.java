package cn.kkmofang.view;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.SpannableStringBuilder;
import android.util.Size;

/**
 * Created by hailong11 on 2018/4/18.
 */

public class Text extends SpannableStringBuilder {

    private int _maxWidth;
    private float _width = Float.MAX_VALUE;
    private float _height = Float.MAX_VALUE;

    public float width() {
        return _width;
    }

    public float height() {
        return _height;
    }

    public void contentSize(int maxWidth,int lineSpacing,Paint paint) {

        if(_width == Float.MAX_VALUE || _height == Float.MAX_VALUE || _maxWidth != maxWidth) {

            _width = 0;
            _height = 0;
            _maxWidth = maxWidth;

            String text = toString();

            float[] widths = new float[1];

            int start = 0;
            int end = text.length();
            int len;

            while(start < end ){

                if(start != 0) {
                    _height += lineSpacing;
                }

                len = paint.breakText(text, start, end, false, maxWidth, widths);

                if(widths[0] > _width){
                    _width = widths[0];
                }

                _height = _height - paint.ascent() + paint.descent();

                start += len;
            }

            if(_height > 0) {
                _height += 6;
            }
        }

    }

    public void change() {
        _width = _height = Float.MAX_VALUE;
    }

}
