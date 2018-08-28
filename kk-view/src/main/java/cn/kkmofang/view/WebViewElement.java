package cn.kkmofang.view;

import android.view.View;
import android.webkit.WebView;

import java.lang.ref.WeakReference;

/**
 * Created by hailong11 on 2018/7/3.
 */

public class WebViewElement extends ViewElement {

    @Override
    public Class<?> viewClass() {
        return WebView.class;
    }

    @Override
    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view,key,value);

        if(view instanceof WebView) {
            if("src".equals(key)) {
                setNeedDisplay();
            }
        }
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        if(view != null && view instanceof WebView) {
            WebView v = (WebView) view;
            v.getSettings().setJavaScriptEnabled(true);
            v.getSettings().setDefaultTextEncodingName("utf-8");
            v.setBackgroundColor(0);
            setNeedDisplay();
        }
    }

    protected boolean _displaying = false;

    public void display() {

        _displaying = false;

        WebView v = (WebView) this.view();

        if(v == null) {
            return;
        }

        String src = get("src");

        if(src != null && !"".equals(src)) {
            v.loadUrl(src);
        }

    }

    public void setNeedDisplay() {

        WebView v = (WebView) this.view();

        if(v == null) {
            return;
        }


        if(_displaying) {
            return;
        }

        _displaying = true;

        final WeakReference<WebViewElement> e = new WeakReference<WebViewElement>(this);

        v.post(new Runnable() {
            @Override
            public void run() {
                WebViewElement element = e.get();
                if(element != null) {
                    element.display();
                }
            }
        });
    }

}