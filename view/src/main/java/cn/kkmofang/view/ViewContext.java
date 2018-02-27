package cn.kkmofang.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.kk.view.R;

import java.util.Stack;

/**
 * Created by hailong11 on 2018/1/17.
 */

public class ViewContext {

    public final Context context;

    public static final Point windowPoint = new Point();

    public ViewContext(Context context) {
        this.context = context;
    }

    private String _basePath;

    /**
     *
     * @param uri
     * @return
     */
    public Drawable getImage(String uri) {
//        String path;
//        if(uri.startsWith("http") || uri.startsWith("https")) {
//
//        } else {
//            path = _basePath + uri;
//        }

//        if(path.startsWith("assets://")) {
//
//        } else {
//
//        }
        return context.getResources().getDrawable(R.mipmap.ic_launcher);
    }

    public boolean getImage(String url,ImageCallback callback) {
        return false;
    }

    public static interface ImageCallback {
        public void onImage(Drawable image);
    }

    private final static ThreadLocal<Stack<ViewContext>> _viewContexts = new ThreadLocal<>();

    public final static void push(ViewContext viewContext) {

        Stack<ViewContext> v = _viewContexts.get();

        if(v == null) {
            v = new Stack<>();
            _viewContexts.set(v);
        }

        v.push(viewContext);
    }

    public final static void pop() {

        Stack<ViewContext> v = _viewContexts.get();

        if(v != null && !v.empty()) {
            v.pop();
        }
    }

    public final static ViewContext current() {

        Stack<ViewContext> v = _viewContexts.get();

        if(v != null && !v.empty()) {
            return v.peek();
        }

        return null;
    }
}
