package cn.kkmofang.demo;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import cn.kkmofang.view.DocumentView;
import cn.kkmofang.view.Element;
import cn.kkmofang.view.FRecyclerView;
import cn.kkmofang.view.ScrollElement;
import cn.kkmofang.view.ViewContext;
import cn.kkmofang.view.ViewElement;
import cn.kkmofang.view.value.Orientation;
import cn.kkmofang.view.value.Pixel;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private Orientation orientation = Orientation.VERTICAL;
    private ScrollElement scrollElement;
    private List<Element> elementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        findViewById(R.id.btnChangeProperty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换方向
                orientation = orientation == Orientation.VERTICAL?Orientation.HORIZONTAL:Orientation.VERTICAL;
                scrollElement.setAttrs("scroll", orientation.getVString());

                //删除所有view
//                scrollElement.remove();

//                if (elementList.size() > 5){
//                    int position = 5;
//                    elementList.remove(position);
//                    scrollElement.removeItem(position);
//                }
            }
        });

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        Pixel.UnitRPX = size.x / 750.0f;

        ViewContext viewContext = new ViewContext(getApplicationContext());

        ViewContext.push(viewContext);

        FRecyclerView recyclerView = (FRecyclerView) findViewById(R.id.myRecyclerView);

        scrollElement = new ScrollElement();
        recyclerView.setElement(scrollElement);
        elementList = new ArrayList<>();


        scrollElement.setAttrs("width", "100%", "height", "100%", "background-color", "#ddd", "scroll", orientation.getVString());
        int left = 0;
        int color = 0;
        for (int i = 0; i < 50; i++) {
            ViewElement element = new ViewElement();
            element.setAttrs("width","50%","height","15%","background-color", "#" + color + "00" ,
                    "left",left + "px", "right", "100px",
                    "top","50px", "bottom", "10px",
                    "reuse", "0");
            elementList.add(element);
            left += 50;
            color ++;
            if (color >= 10){
                color = 0;
            }
        }
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","50%","height","30%","background-color","#000",
//                    "left","50px", "right", "100px",
//                    "top","50px", "bottom", "10px",
//                    "reuse", "1");
//            elementList.add(element);
//        }
//
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","50%","height","30%","background-color","#ff0",
//                    "left","50px", "right", "100px",
//                    "reuse", "1");
//            elementList.add(element);
//        }
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","50%","height","30%","background-color","#f00","left","50px", "right", "100px", "reuse", "3");
//            elementList.add(element);
//        }
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","100%","height","30%","background-color","#ff0","left","50px", "right", "100px");
//            elementList.add(element);
//        }
//
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","50%","height","30%","background-color","#f00","left","50px", "right", "100px");
//            elementList.add(element);
//
//        }
//
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","50%","height","30%","background-color","#ff0","left","50px", "right", "100px", "reuse", "6");
//            elementList.add(element);
//        }
//
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","50%","height","30%","background-color","#f00","left","50px", "right", "100px", "reuse", "7");
//            elementList.add(element);
//        }
//
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","50%","height","30%","background-color","#ff0","left","50px", "right", "100px", "reuse", "8");
//            elementList.add(element);
//        }
        scrollElement.appendList(elementList);


//        DocumentView documentView = (DocumentView) findViewById(R.id.documentView);
//
//        DocumentElement root = new DocumentElement();
//
//        root.setAttrs("width","100%","height","100%","background-color","#ddd");
//
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","50%","height","50%","background-color","#f00","left","50px", "right", "100px");
//            root.append(element);
//        }
//
//        {
//            ViewElement element = new ViewElement();
//            element.setAttrs("width","100px","height","100px","background-color","#ff0");
//            root.append(element);
//        }
//
//        documentView.setElement(root);

        ViewContext.pop();

    }
}
