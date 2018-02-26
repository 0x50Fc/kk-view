package cn.kkmofang.demo;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;

import cn.kkmofang.view.DocumentView;
import cn.kkmofang.view.SpanElement;
import cn.kkmofang.view.TextElement;
import cn.kkmofang.view.ViewContext;
import cn.kkmofang.view.ViewElement;
import cn.kkmofang.view.value.Pixel;

public class MainActivity extends Activity {

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

        root.setAttrs("width","100%","height","100%","background-color","#fff");
        {
            TextElement element = new TextElement();
            element.setAttrs("width","200rpx","height","200rpx","background-color","#f00",
                    "#text", "nihao","text-align","right");
            SpanElement spanElement = new SpanElement();
            spanElement.setAttrs("","");
            element.append(spanElement);
            root.append(element);
        }

        documentView.setElement(root);

        ViewContext.pop();

    }
}
