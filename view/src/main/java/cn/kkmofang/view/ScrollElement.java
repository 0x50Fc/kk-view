package cn.kkmofang.view;


import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class ScrollElement extends ViewElement {

    public ScrollElement() {
        super();
        set("#view",RecyclerView.class.getName());
    }

    public RecyclerView recyclerView() {
        return (RecyclerView) view();
    }

    public void setView(View view) {
        if(view == null || view instanceof RecyclerView) {
            RecyclerView v = recyclerView();
            if(v != null) {
                v.setLayoutManager(null);
                v.setAdapter(null);
            }
            super.setView(view);
            v = recyclerView();
            if(v != null) {
                v.setLayoutManager(null);
                v.setAdapter(null);
            }
        }
    }

    public static class LayoutManager extends RecyclerView.LayoutManager {

        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            return null;
        }
    }
}
