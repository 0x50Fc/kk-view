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
import java.util.Map;

import cn.kkmofang.view.utils.WeakHandler;
import cn.kkmofang.view.view.FViewPager;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class PagerElement extends ViewElement {
    public List<ViewElement> _elements = new ArrayList<>();
    private int _interval;//默认3s轮播

    private int count;
    private int currentItem;
    private WeakHandler handler = new WeakHandler();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            FViewPager fViewPager = fetchView();
            if (count > 1 && _interval > 0 && fViewPager != null){
                currentItem = currentItem % (count + 1) + 1;
                if (currentItem == 1){
                    fViewPager.setCurrentItem(currentItem ,false);
                    handler.post(task);
                }else {
                    fViewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, _interval);
                }
            }
        }
    };

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
                if (_elements.size() < 3)return;
                count = _elements.size() - 2;//realCount
                v.setPagerElement(this);
                v.addOnPageChangeListener(new FPageChangeListener());
                v.setOffscreenPageLimit(count);//此处必须设置为item size，否则报错，可以考虑作优化
                v.setAdapter(new FPagerAdapter(_elements, viewContext.context));
                //默认设置当前页面为0
                v.setCurrentItem(1, false);
                //页面大于1时才可以滑动
                v.setScrollable(count > 1);
                if (count > 1 && _interval > 0){//自动滑动
                    startAutoPlay();
                }
            }

        }
    }

    public void startAutoPlay(){
        handler.removeCallbacks(task);
        handler.postDelayed(task, _interval);
    }

    public void stopAtuoPlay(){
        handler.removeCallbacks(task);
    }

    public boolean isAtuoPlay(){
        return count > 1 && _interval > 0;
    }

    @Override
    public void remove() {
        super.remove();
        stopAtuoPlay();
    }

    @Override
    public void recycleView() {
        super.recycleView();
        if (view() != null){
            _elements.clear();
        }
    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        if ("interval".equals(key)){
            try {
                _interval = Integer.parseInt(get(key));
            }catch (NumberFormatException e){
                _interval = 0;
                e.printStackTrace();
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


    class FPagerAdapter extends PagerAdapter{
        private List<ViewElement> mElements;

        public FPagerAdapter(List<ViewElement> mElements, Context context) {
            this.mElements = mElements;
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
            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    class FPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            emitPageChangeEvent(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            FViewPager fViewPager = fetchView();
            if (fViewPager != null){
                switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        if (currentItem == 0){
                            fViewPager.setCurrentItem(count, false);
                        }else if (currentItem == count + 1){
                            fViewPager.setCurrentItem(1, false);
                        }
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                }
            }

        }

        private void emitPageChangeEvent(int position){
            if (position < 1 || position > count)return;
            Event event = new Element.Event(PagerElement.this);
            Map<String, String> map = data();
            map.put("pageCount", String.valueOf(count));
            map.put("pageIndex", String.valueOf(position));
            event.setData(map);
            emit("pagechange", event);
        }
    }
}
