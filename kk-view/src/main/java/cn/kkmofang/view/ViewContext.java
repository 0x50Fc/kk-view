package cn.kkmofang.view;


import java.util.Stack;

/**
 * Created by zhanghailong on 2018/1/17.
 */

public class ViewContext {

    private final static ThreadLocal<Stack<IViewContext>> _viewContexts = new ThreadLocal<>();

    public final static void push(IViewContext viewContext) {

        Stack<IViewContext> v = _viewContexts.get();

        if(v == null) {
            v = new Stack<>();
            _viewContexts.set(v);
        }

        v.push(viewContext);
    }

    public final static void pop() {

        Stack<IViewContext> v = _viewContexts.get();

        if(v != null && !v.empty()) {
            v.pop();
        }
    }

    public final static IViewContext current() {

        Stack<IViewContext> v = _viewContexts.get();

        if(v != null && !v.empty()) {
            return v.peek();
        }

        return null;
    }
}
