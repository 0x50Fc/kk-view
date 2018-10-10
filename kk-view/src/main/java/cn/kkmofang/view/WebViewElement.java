package cn.kkmofang.view;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zhanghailong on 2018/7/3.
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

    private WebViewClient _webViewClient = null;
    private WebChromeClient _webChromeClient = null;

    @Override
    public void setView(View view) {
        View webView = view();
        if (webView != null && webView instanceof WebView && _webViewClient != null){
            ((WebView) webView).setWebViewClient(null);
        }
        super.setView(view);
        if(view != null && view instanceof WebView) {
            WebView v = (WebView) view;
            v.getSettings().setJavaScriptEnabled(true);
            v.getSettings().setDefaultTextEncodingName("utf-8");
            v.setBackgroundColor(0);
            if (_webViewClient == null){
                _webViewClient = new Client();
            }
            if (_webChromeClient == null){
                _webChromeClient = new ChromeClient();
            }
            v.setWebViewClient(_webViewClient);
            v.setWebChromeClient(_webChromeClient);
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


    @Override
    public void recycleView() {
        View v =  view();
        if (v != null && v instanceof WebView){
            ((WebView) v).clearHistory();
            ((WebView) v).destroy();
        }
        super.recycleView();
    }

    /**
     *
     * @param value 0~1
     */
    protected void onProgress(double value) {

        Element.Event event = new Element.Event(this);

        Map<String,Object> data = this.data();

        data.put("value",value);

        event.setData(data);

        emit("progress",event);

    }

    protected void onLoad() {

        Element.Event event = new Element.Event(this);

        Map<String,Object> data = this.data();

        event.setData(data);

        emit("load",event);

    }

    protected void onError(String errmsg) {

        Element.Event event = new Element.Event(this);

        Map<String,Object> data = this.data();

        data.put("errmsg",errmsg);

        event.setData(data);

        emit("error",event);

    }

    protected void onLoading() {

        Element.Event event = new Element.Event(this);

        Map<String,Object> data = this.data();

        event.setData(data);

        emit("loading",event);

    }

    protected void onClose() {

        Element.Event event = new Element.Event(this);

        Map<String,Object> data = this.data();

        event.setData(data);

        emit("close",event);

    }

    protected void onAction(String name,String url,Map<String,Object> actionData) {

        Element.Event event = new Element.Event(this);

        Map<String,Object> data = this.data();

        data.putAll(actionData);

        data.put("url",url);

        event.setData(data);

        emit(name,event);

    }

    private class Client extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Element p = WebViewElement.this.firstChild();

            while(p != null) {

                if("action".equals(p.get("#name"))) {

                    {
                        String v = p.get("prefix");
                        if(v != null && url.startsWith(v)) {
                            break;
                        }
                    }

                    {
                        String v = p.get("suffix");
                        if(v != null && url.endsWith(v)) {
                            break;
                        }
                    }

                    {
                        String v = p.get("pattern");

                        if(v != null) {
                            Pattern pattern = Pattern.compile(v);

                            if(pattern.matcher(url).find()) {
                                break;
                            }

                        }
                    }
                }

                p = p.nextSibling();
            }

            if(p != null) {

                String name = p.get("name");

                if(name == null || "".equals(name)) {
                    name = "action";
                }

                WebViewElement.this.onAction(name,url,p.data());

                if("allow".equals(p.get("policy"))) {
                    return true;
                }

                return false;
            }

            return super.shouldOverrideUrlLoading(view,url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            WebViewElement.this.onLoading();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            WebViewElement.this.onLoad();
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                WebViewElement.this.onError(error.getDescription().toString());
            }
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                WebViewElement.this.onError(description);
            }
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private class ChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            WebViewElement.this.onProgress(newProgress);
        }

    }

}
