package cn.kkmofang.demo;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import cn.kkmofang.view.DocumentView;
import cn.kkmofang.view.Element;
import cn.kkmofang.view.ElementView;
import cn.kkmofang.view.ScrollElement;
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

        ViewContext viewContext = new ViewContext(getApplicationContext());

        ViewContext.push(viewContext);



        DocumentView documentView = (DocumentView) findViewById(R.id.documentView);

        ViewElement root = new ViewElement();

        root.setAttrs("width","100%","height","100%","background-color","#ddd");


        {
            ScrollElement scrollElement = new ScrollElement();
            scrollElement.setAttrs("width","80%","height","80%","background-color","#f00");
            {
                ViewElement element = new ViewElement();
                element.setAttrs("width","100%","height","50%","background-color","#1f0","left","50px", "right", "100px");
                {
                    ViewElement element1 = new ViewElement();
                    element1.setAttrs("width","50%","height","50%","background-color","#000","left","50px", "right", "100px");
                    element.append(element1);
                }
                scrollElement.append(element);
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
//
//            {
//                ViewElement element = new ViewElement();
//                element.setAttrs("width","100%","height","50%","background-color","#4f0","left","50px", "right", "100px");
//                scrollElement.append(element);
//            }
            root.append(scrollElement);
        }

        documentView.setElement(root);


        ViewContext.pop();

    }
}
