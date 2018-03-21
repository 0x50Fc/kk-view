package cn.kkmofang.demo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.kkmofang.view.ButtonElement;
import cn.kkmofang.view.DocumentView;
import cn.kkmofang.view.Element;
import cn.kkmofang.view.ElementView;
import cn.kkmofang.view.IViewContext;
import cn.kkmofang.view.ImageElement;
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

        Point size = new Point();

        getWindowManager().getDefaultDisplay().getSize(size);

        Pixel.UnitRPX = size.x / 750.0f;

        IViewContext viewContext = new MainViewContext(getApplicationContext());


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

                    {

                        TextElement element2 = new TextElement();
                        element2.setAttrs("width","100%","height","100%","background-color","#999",
                                "color", "#000", "#text", "我是第四页", "text-align", "center", "font", "40rpx italic",
                                "line-spacing", "", "letter-spacing", "0rpx");
                        pagerElement.append(element2);

                    }

                    ViewElement elementclone0 = new ViewElement();
                    elementclone0.setAttrs("width","100%","height","100%","background-color","#f00");
                    pagerElement.append(elementclone0);

//                    ViewElement element111 = new ViewElement();
//                    element111.setAttrs("width","100%","height","100%","background-color","#fff");

                    TextElement element = new TextElement();
                    element.setAttrs("width","100%","height","100%","background-color","#999",
                            "color", "#000", "#text", "你好\nni\nnfdsfjsl\nfjslfjslf", "text-align", "center", "font", "40rpx italic",
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
                    element1.setAttrs("width","100%","height","100%","background-color","#fff");

                    {
                        TextElement element2 = new TextElement();
                        element2.setAttrs("width","100%","height","100%","background-color","#999",
                                "color", "#000", "#text", "你好\nni\nnfdsfjsl\nfjslfjslf", "text-align", "center", "font", "40rpx italic",
                                "line-spacing", "", "letter-spacing", "0rpx");
                        element1.append(element2);
                    }


                    pagerElement.append(element1);

                    {

                            TextElement element2 = new TextElement();
                            element2.setAttrs("width","100%","height","100%","background-color","#999",
                                    "color", "#000", "#text", "我是第三页", "text-align", "center", "font", "40rpx italic",
                                    "line-spacing", "", "letter-spacing", "0rpx");
                            pagerElement.append(element2);

                    }

                    {

                        TextElement element2 = new TextElement();
                        element2.setAttrs("width","100%","height","100%","background-color","#999",
                                "color", "#000", "#text", "我是第四页", "text-align", "center", "font", "40rpx italic",
                                "line-spacing", "", "letter-spacing", "0rpx");
                        pagerElement.append(element2);

                    }

                    ViewElement elementclone5 = new ViewElement();
                    elementclone5.setAttrs("width","100%","height","100%","background-color","#f00");
                    pagerElement.append(elementclone5);

                }

                ViewElement group = new ViewElement();
                group.setAttrs("width","100%", "height", "50%");

//                group.append(pagerElement);
                scrollElement.append(pagerElement);
            }

//            {
//                TextElement element2 = new TextElement();
//                element2.setAttrs("width","100%","height","100%","background-color","#999",
//                        "color", "#000", "#text", "你好\nni\nnfdsfjsl\nfjslfjslf", "text-align", "center", "font", "40rpx italic",
//                        "line-spacing", "", "letter-spacing", "0rpx");
//                scrollElement.append(element2);
//            }

            {
                ScrollElement selement = new ScrollElement();
                selement.setAttrs("width","100%","height","50%","background-color","#1f0","left","50px", "right", "100px");
                {
                    ImageElement element1 = new ImageElement();
                    element1.setAttrs("width","200rpx","height","200rpx","left","50px", "right", "100px","background-color", "#ddd",
                            "color", "#000",
                            "border-width", "10rpx", "border-radius", "100rpx", "border-color", "#fff",
                            "#text", "button", "text-align", "center", "font", "32rpx italic", "status", "hover",
                            "src", "http://tvax1.sinaimg.cn/crop.0.0.750.750.180/006AkuvSly8fnzregrc91j30ku0kugmm.jpg");
                    element1.setCSSStyle("border-width:20rpx;border-radius:20rpx;border-color:#000", "hover");

                    selement.append(element1);
                }

                {
                    ButtonElement element1 = new ButtonElement();
                    element1.setAttrs("width","200rpx","height","200rpx","left","250rpx", "right", "100px", "top", "250rpx", "background-color", "#ddd",
                            "color", "#000",
                            "border-width", "10rpx", "border-radius", "100rpx", "border-color", "#fff",
                            "#text", "button", "text-align", "center", "font", "32rpx italic", "status", "hover",
                            "src", "http://tvax1.sinaimg.cn/crop.0.0.750.750.180/006AkuvSly8fnzregrc91j30ku0kugmm.jpg");
                    element1.setCSSStyle("border-width:20rpx;border-radius:20rpx;border-color:#000", "hover");

                    selement.append(element1);
                }

                scrollElement.append(selement);
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

        documentView.setElement(root);


        ViewContext.pop();


    }
}

