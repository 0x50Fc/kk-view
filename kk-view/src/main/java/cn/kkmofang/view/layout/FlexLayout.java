package cn.kkmofang.view.layout;


import java.util.LinkedList;
import java.util.List;

import cn.kkmofang.view.Element;
import cn.kkmofang.view.ViewElement;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.VerticalAlign;

/**
 * 流式布局 "flex" 左到右 上到下
 * Created by zhanghailong on 2018/1/18.
 */

public class FlexLayout implements ViewElement.Layout {

    public static void layoutLineElements(List<ViewElement> elements, float inWidth, float inHeight, float lineHeight) {

        for(ViewElement element : elements) {

            VerticalAlign v = element.verticalAlign;

            if(v == VerticalAlign.Bottom) {
                float y = element.y();
                float mtop = element.margin.top.floatValue(inHeight,0);
                float mbottom = element.margin.bottom.floatValue(inHeight,0);
                element.setY(y + (lineHeight - mtop - mbottom - element.height()));
            } else if(v == VerticalAlign.Middle) {
                float y = element.y();
                float mtop = element.margin.top.floatValue(inHeight,0);
                float mbottom = element.margin.bottom.floatValue(inHeight,0);
                element.setY( y + (lineHeight - mtop - mbottom - element.height()) * 0.5f);
            }

            element.onLayout();
        }
    }

    protected boolean isAutoWarp() {
        return true;
    }

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

        float y = paddingTop;
        float x = paddingLeft;
        float maxWidth = paddingLeft + paddingRight;
        float lineHeight = 0;

        List<ViewElement> lineElements = new LinkedList<>();

        Element p = element.firstChild();

        while(p != null) {

            if(p instanceof ViewElement) {

                ViewElement e = (ViewElement) p;

                if(e.isHidden()) {
                    p = p.nextSibling();
                    continue;
                }

                e.willLayout();

                float mleft = e.margin.left.floatValue(width,0);
                float mright = e.margin.right.floatValue(width,0);
                float mtop = e.margin.top.floatValue(height,0);
                float mbottom = e.margin.bottom.floatValue(height,0);

                float w = e.width.floatValue(inWidth - mleft - mright,Pixel.Auto);
                float h = e.height.floatValue(inHeight - mtop - mbottom,Pixel.Auto);

                e.setWidth(w);
                e.setHeight(h);

                e.layoutChildren();

                if(w == Pixel.Auto) {
                    w = e.contentWidth();
                    float min = e.minWidth.floatValue(inWidth,0);
                    float max = e.maxWidth.floatValue(inHeight,Pixel.Auto);
                    if(w < min) {
                        w = min;
                    }
                    if(w > max) {
                        w = max;
                    }
                    e.setWidth(w);
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
                    e.setHeight(h);
                }


                if(isAutoWarp() && x + w + mleft + mright + paddingRight > width) {
                    if(lineElements.size() >0) {
                        layoutLineElements(lineElements,inWidth,inHeight,lineHeight);
                        lineElements.clear();
                    }
                    y += lineHeight;
                    lineHeight = 0;
                    x = paddingLeft;
                }

                float left = x + mleft;
                float top = y + mtop;

                x += w + mleft + mright;

                if(lineHeight < h + mtop + mbottom) {
                    lineHeight = h + mtop + mbottom;
                }

                e.setX(left);
                e.setY(top);

                if(x + paddingRight  > maxWidth) {
                    maxWidth = x + paddingRight ;
                }

                lineElements.add(e);

            }

            p = p.nextSibling();
        }

        if(lineElements.size() > 0) {
            layoutLineElements(lineElements,inWidth,inHeight,lineHeight);
        }

        element.setContentSize(maxWidth,y + lineHeight + paddingBottom);

    }
}
