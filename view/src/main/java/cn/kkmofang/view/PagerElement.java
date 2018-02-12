package cn.kkmofang.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.kkmofang.view.view.FViewPager;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class PagerElement extends ViewElement {
    public List<ViewElement> _elements = new ArrayList<>();

    public PagerElement() {
        super();
        set("#view", FViewPager.class.getName());
    }

    private FViewPager fetchView(){
        return (FViewPager) view();
    }

    @Override
    public void setView(View view) {
        if (view == null || view instanceof ViewPager){
            FViewPager v = fetchView();
            if (v != null){
                v.setPagerElement(null);
                v.setAdapter(null);
            }
            super.setView(view);
            v = fetchView();
            if (v != null){
                v.setPagerElement(this);
                v.setAdapter(new FPagerAdapter(_elements, viewContext.context));
            }

        }
    }

    @Override
    public void append(Element element) {
        super.append(element);
        if (element instanceof ViewElement){
            _elements.add((ViewElement) element);
            element.setParent(this);
        }
    }

    public class FPagerAdapter extends PagerAdapter{
        private List<ViewElement> mElements;
        private Context mContext;

        public FPagerAdapter(List<ViewElement> mElements, Context context) {
            this.mElements = mElements;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mElements.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {//判断是否是同一个view
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mElements.get(position).view());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewElement p = mElements.get(position);
            View view = p.view();
            if (view == null){
                p.obtainView(container);
            }
            view = p.view();
//            view.setLayoutParams(new FrameLayout.LayoutParams(100, 100));
//            int left = (int) p.left.floatValue(container.getWidth(), 0);
//            int right = (int) p.right.floatValue(container.getWidth(), 0);
//            int top = (int) p.top.floatValue(container.getHeight(), 0);
//            int bottom = (int) p.bottom.floatValue(container.getHeight(), 0);
//            view.layout(left, top, right, bottom);

            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
