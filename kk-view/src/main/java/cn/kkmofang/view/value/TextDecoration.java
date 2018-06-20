package cn.kkmofang.view.value;

import android.graphics.Paint;

/**
 * Created by hailong11 on 2018/6/20.
 */

public class TextDecoration {

    public static void valueOf(String value,Paint paint) {

        paint.setUnderlineText(false);
        paint.setStrikeThruText(false);

        if("underline".equals(value)) {
            paint.setUnderlineText(true);
            return ;
        }

        if("line-through".equals(value)) {
            paint.setStrikeThruText(true);
            return ;
        }


    }
}
