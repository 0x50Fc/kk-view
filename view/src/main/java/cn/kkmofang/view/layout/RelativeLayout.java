package cn.kkmofang.view.layout;

import cn.kkmofang.view.Element;
import cn.kkmofang.view.ViewElement;
import cn.kkmofang.view.value.Pixel;

/**
 * 相对布局 "relative"
 * Created by hailong11 on 2018/1/18.
 */

public class RelativeLayout implements ViewElement.Layout {

    @Override
    public void layout(ViewElement element) {

        float width = element.width();
        float height = element.height();
        float paddingLeft = element.padding.left.floatValue(width,0);
        float paddingRight = element.padding.right.floatValue(width,0);
        float paddingTop = element.padding.top.floatValue(height,0);
        float paddingBottom = element.padding.bottom.floatValue(height,0);
        float inWidth = width - paddingLeft - paddingRight;
        float inHeight = height - paddingTop - paddingBottom;

        float contentWidth = 0;
        float contentHeight = 0;

        Element p = element.firstChild();

        while(p != null) {

            if(p instanceof ViewElement) {

                ViewElement e = (ViewElement) p;

                float mleft = e.margin.left.floatValue(width,0);
                float mright = e.margin.right.floatValue(width,0);
                float mtop = e.margin.top.floatValue(width,0);
                float mbottom = e.margin.bottom.floatValue(width,0);

                float w = e.width.floatValue(inWidth, Pixel.Auto);
                float h = e.height.floatValue(inHeight,Pixel.Auto);

                e.setWidth((int) Math.ceil(w));
                e.setHeight((int) Math.ceil(w));

                e.layoutChildren();

                if(w == Pixel.Auto) {
                    w = e.contentWidth();
                    float min = e.minWidth.floatValue(inWidth,0);
                    float max = e.minWidth.floatValue(inWidth,Pixel.Auto);
                    if(w < min) {
                        w = min;
                    }
                    if(w > max) {
                        w = max;
                    }
                    e.setWidth((int) Math.ceil(w));
                }

                if(h == Pixel.Auto) {
                    h = e.contentHeight();
                    float min = e.minHeight.floatValue(inHeight,0);
                    float max = e.maxHeight.floatValue(inHeight,Pixel.Auto);
                    if(h < min) {
                        h = min;
                    }
                    if(h > max) {
                        h = max;
                    }
                    e.setHeight((int) Math.ceil(h));
                }

                float left = e.left.floatValue(inWidth,Pixel.Auto);
                float right = e.right.floatValue(inWidth,Pixel.Auto);
                float top = e.top.floatValue(inHeight,Pixel.Auto);
                float bottom = e.bottom.floatValue(inHeight,Pixel.Auto);

                if(left == Pixel.Auto) {
                    if(width == Pixel.Auto) {
                        left = paddingLeft + mleft;
                    } else if(right == Pixel.Auto) {
                        left = paddingLeft + mleft + (inWidth - w - mleft - mright) * 0.5f;
                    } else {
                        left = paddingLeft + (inWidth - right - mright - w);
                    }
                } else {
                    left = paddingLeft + left + mleft;
                }

                if(top == Pixel.Auto) {
                    if(height == Pixel.Auto) {
                        top = paddingTop + mtop;
                    } else if(bottom == Pixel.Auto) {
                        top = paddingTop + mtop + (inHeight - height - mtop - mbottom) * 0.5f;
                    } else {
                        top = paddingTop + (inHeight - height - mbottom -bottom);
                    }
                } else {
                    top = paddingTop + top + mtop;
                }

                e.setX((int) Math.ceil(left));
                e.setY((int) Math.ceil(top));

                if(left + paddingRight + mright + w > contentWidth) {
                    contentWidth = left + paddingRight + mright + w;
                }

                if(top + paddingBottom + mbottom + h > contentHeight) {
                    contentHeight = top + paddingBottom + mbottom + h;
                }

                e.onLayout();

            }

            p = p.nextSibling();
        }

        element.setContentSize((int) Math.ceil(contentWidth),(int) Math.ceil(contentHeight));

    }
}
