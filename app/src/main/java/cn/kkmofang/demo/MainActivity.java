package cn.kkmofang.demo;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

import cn.kkmofang.view.DocumentView;
import cn.kkmofang.view.Element;
import cn.kkmofang.view.ElementView;
import cn.kkmofang.view.ImgElement;
import cn.kkmofang.view.PagerElement;
import cn.kkmofang.view.ScrollElement;
import cn.kkmofang.view.SpanElement;
import cn.kkmofang.view.TextElement;
import cn.kkmofang.view.ViewContext;
import cn.kkmofang.view.ViewElement;
import cn.kkmofang.view.value.Orientation;
import cn.kkmofang.view.value.Pixel;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private Orientation orientation = Orientation.VERTICAL;
//    private ScrollElement scrollElement;
//    private List<Element> elementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        getWindowManager().getDefaultDisplay().getSize(ViewContext.windowPoint);

        Pixel.UnitRPX = ViewContext.windowPoint.x / 750.0f;

        ViewContext viewContext = new ViewContext(getApplicationContext());

        ViewContext.push(viewContext);


        DocumentView documentView = (DocumentView) findViewById(R.id.documentView);

        ViewElement root = new ViewElement();

        root.setAttrs("width","100%","height","100%","background-color","#ddd");

        {
            ScrollElement scrollElement = new ScrollElement();
            scrollElement.setAttrs("width","100%","height","100%","background-color","#f00", "scroll", "overflow-y", "background-image", "fsd");
            {
                PagerElement pagerElement = new PagerElement();
                pagerElement.setAttrs("width", "100%", "height", "50%", "background-color", "#ddd", "interval", "0", "hidden", "false");

                {
//                    ViewElement elementclone0 = new ViewElement();
//                    elementclone0.setAttrs("width","100%","height","100%","background-color","#fff");
//                    pagerElement.append(elementclone0);

                    TextElement element = new TextElement();
                    element.setAttrs("width","100%","height","100%","background-color","#999",
                            "color", "#000", "#text", "你好\nni\nnfdsfjsl\nfjslfjslf", "text-align", "left", "font", "40rpx italic",
                            "line-spacing", "", "letter-spacing", "0rpx");
                    {
                        SpanElement spanElement = new SpanElement();
                        spanElement.setAttrs("#text", "我是文本", "color", "#f00", "font", "80rpx bold");
                        element.append(spanElement);
                    }

                    {
                        SpanElement spanElement = new SpanElement();
                        spanElement.setAttrs("#text", "我是第二行内容了", "color", "#4f0", "font", "40rpx italic");
                        element.append(spanElement);
                    }

                    {
                        ImgElement imgElement = new ImgElement();
                        imgElement.setAttrs("src", "ic_launcher", "width", "100rpx", "height", "100rpx");
                        element.append(imgElement);
                    }

                    {
                        SpanElement spanElement = new SpanElement();
                        spanElement.setAttrs("#text", "我是第二行内容了", "color", "#4f0", "font", "40rpx italic");
                        element.append(spanElement);
                    }
                    pagerElement.append(element);

                    ViewElement element1 = new ViewElement();
                    element1.setAttrs("width","100%","height","100%","background-color","#555");
                    pagerElement.append(element1);

                }
                scrollElement.append(pagerElement);
            }

            {
                ViewElement element = new ViewElement();
                element.setAttrs("width","100%","height","50%","background-color","#1f0","left","50px", "right", "100px");
                {
                    ViewElement element1 = new ViewElement();
                    element1.setAttrs("width","50%","height","50%","left","50px", "right", "100px","background-color", "#ddd",
                            "border-width", "10rpx", "border-radius", "10rpx", "border-color", "#fff", "background-image", "#ddd");
                    element.append(element1);
                }
                scrollElement.append(element);
            }

            {
                ScrollElement hScrollElement = new ScrollElement();
                hScrollElement.setAttrs("width", "100%", "height", "30%", "background-color", "#fff", "scroll", "overflow-x");
                {
                    ViewElement element = new ViewElement();
                    element.setAttrs("width", "50%", "height", "100%", "background-color", "#444");
                    hScrollElement.append(element);
                }

                {
                    ViewElement element = new ViewElement();
                    element.setAttrs("width", "50%", "height", "100%", "background-color", "#888");
                    hScrollElement.append(element);
                }
                {
                    ViewElement element = new ViewElement();
                    element.setAttrs("width", "50%", "height", "100%", "background-color", "#666");
                    hScrollElement.append(element);
                }

                scrollElement.append(hScrollElement);
            }

            {
                ScrollElement vScrollElement = new ScrollElement();
                vScrollElement.setAttrs("width", "100%", "height", "50%", "background-color", "#ff0", "scroll", "overflow-y");
                {
                    ViewElement element = new ViewElement();
                    element.setAttrs("width", "50%", "height", "50%", "background-color", "#763");
                    vScrollElement.append(element);
                }

                {
                    ViewElement element = new ViewElement();
                    element.setAttrs("width", "50%", "height", "50%", "background-color", "#888");
                    vScrollElement.append(element);
                }
                {
                    ViewElement element = new ViewElement();
                    element.setAttrs("width", "50%", "height", "50%", "background-color", "#666");
                    vScrollElement.append(element);
                }

                scrollElement.append(vScrollElement);
            }

            {
                ViewElement element = new ViewElement();
                element.setAttrs("width","100%","height","50%","background-color","#000","left","50px", "right", "100px");
                scrollElement.append(element);
            }

            {
                ViewElement element = new ViewElement();
                element.setAttrs("width","100%","height","50%","background-color","#1f0","left","50px", "right", "100px");
                scrollElement.append(element);
            }
            root.append(scrollElement);
        }


//        PagerElement pagerElement = new PagerElement();
//        pagerElement.setAttrs("width", "100%", "height", "50%", "background-color", "#000");
//
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","100%","height","100%","background-color","#999");
//            pagerElement.append(element);
//
//            ViewElement element1 = new ViewElement();
//            element1.setAttrs("width","100%","height","100%","background-color","#555");
//            pagerElement.append(element1);
//
//            ViewElement element2 = new ViewElement();
//            element2.setAttrs("width","100%","height","100%","background-color","#fff");
//            pagerElement.append(element2);
//
//            root.append(pagerElement);
//
//        }

        Element.Event e = new Element.Event(root);

        e.setData(root.data());

        root.emit("tap",e);

        documentView.setElement(root);


        ViewContext.pop();

    }
}

