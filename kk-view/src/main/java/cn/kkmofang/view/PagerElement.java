package cn.kkmofang.view;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import cn.kkmofang.view.event.EventEmitter;
import cn.kkmofang.view.value.V;

/**
 * Created by zhanghailong on 2018/4/19.
 */

public class PagerElement extends ViewElement {

    private PagerElementAdapter _adapter;
    private final Handler _handler;
    private long _pagerInterval;
    private boolean isLooping;
    private boolean isLoop = true;
    private  Runnable autoRunnable;
    private int currentPosition;
    public PagerElement() {
        super();
        _handler = new Handler();
        set("#view",KKViewPager.class.getName());
    }

    public KKViewPager viewPager() {
        View v = view();
        if(v != null && v instanceof KKViewPager){
            return (KKViewPager) v;
        }
        return null;
    }

    private ViewPager.OnPageChangeListener _OnPageChangeListener;

    public void setView(View view) {

        KKViewPager _viewPager = viewPager();
        if(_viewPager != null && _OnPageChangeListener != null) {
            _viewPager.removeOnPageChangeListener(_OnPageChangeListener);
            _viewPager.setAdapter(null);
        }
        super.setView(view);
        _viewPager = viewPager();
        if(_viewPager != null ) {

            if(_OnPageChangeListener == null) {
                final WeakReference<PagerElement> e = new WeakReference<>(this);

                _OnPageChangeListener = new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        PagerElement v = e.get();
                        if(v != null) {
                            v.onPageScrolled(position,positionOffset,positionOffsetPixels);
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {
                        PagerElement v = e.get();
                        if(v != null) {
                            v.onPageSelected(position);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        PagerElement v = e.get();
                        if(v != null) {
                            v.onPageScrollStateChanged(state);
                        }
                    }
                };
            }

            _viewPager.addOnPageChangeListener(_OnPageChangeListener);

            if(_adapter == null) {
                _adapter = new PagerElementAdapter(this);
            }

            _viewPager.setAdapter(_adapter);

        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        Map<String,Object> data =new TreeMap<>();
        if(isLoop) {
            pageSelected(position);
            data.put("pageIndex",currentPosition-1);
            data.put("pageCount",_adapter.getCount()-2);
        }else{
            data.put("pageIndex",position);
            data.put("pageCount",_adapter.getCount());
        }
        Element.Event e = new Event(PagerElement.this);
        e.setData(data);

        emit("pagechange",e);
    }

    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE && isLoop) {
            KKViewPager _viewPager = viewPager();
            if (_viewPager != null){
                _viewPager.setCurrentItem(currentPosition, false);
            }
        }
    }

    private void pageSelected(int position) {
        if(isLoop) {
            if (position == 0) {    //判断当切换到第0个页面时把currentPosition设置为list.size(),即倒数第二个位置，小圆点位置为length-1
                currentPosition = _adapter.getCount() - 2;
            } else if (position == _adapter.getCount() - 1) {    //当切换到最后一个页面时currentPosition设置为第一个位置，小圆点位置为0
                currentPosition = 1;
            } else {
                currentPosition = position;
            }
        } else {
            currentPosition = position;
        }

    }


    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        if("interval".equals(key)) {
            _pagerInterval = V.longValue(get(key), 0);
        }else if("loop".equals(key)){
            isLoop = V.booleanValue(get(key), true);
        }
    }

    @Override
    public void obtainChildrenView() {

        if(_adapter != null) {
            _adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDidAddChildren(Element element) {
        super.onDidAddChildren(element);
        setNeedDisplay();
    }


    @Override
    protected void onWillRemoveChildren(Element element) {
        setNeedDisplay();
        super.onWillRemoveChildren(element);
    }

    @Override
    protected void onLayout(View view) {
        setNeedDisplay();
        super.onLayout(view);
    }


    private boolean _displaying = false;

    public void setNeedDisplay() {

        if(view() == null) {
            return;
        }

        if(_displaying) {
            return;
        }

        _displaying = true;

        _handler.post(new Runnable() {
            @Override
            public void run() {
                if(_displaying) {
                    display();
                }
            }
        });

    }

    public void display() {

        _displaying = false;
        if(_adapter != null) {
            if(isLoop) {
                initAutoPlayPagerViewAndPlay();
            }else {
                _adapter.notifyDataSetChanged();
            }
        }
    }



    private void startLoop() {
        KKViewPager _viewPager = viewPager();
        if(_viewPager==null){
            return;
        }
        if (!isLooping){
            // 每两秒执行一次runnable.
            if(_handler==null){
                return;
            }
            _handler.postDelayed(autoRunnable, _pagerInterval);
            isLooping = true;
        }
    }

    private void stopLoop() {
        KKViewPager _viewPager = viewPager();
        if (isLooping && _viewPager != null) {
            if(_handler==null){
                return;
            }
            _handler.removeCallbacks(autoRunnable);
            isLooping = false;
        }
    }

    /**
     * 需要自动轮播时，初始化相应的事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initAutoPlayPagerViewAndPlay(){
        currentPosition=1;
        KKViewPager _viewPager = viewPager();

        if(_viewPager==null || _adapter==null){
            return;
        }

        //需要自动轮播
        if(_pagerInterval>0){
            if(autoRunnable==null ) {
                final WeakReference<PagerElement> e = new WeakReference<>(this);
                autoRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PagerElement v = e.get();
                        if (v != null){
                            KKViewPager view = v.viewPager();
                            if (view != null && view.getChildCount() > 1) {
                                v._handler.postDelayed(this, v._pagerInterval);
                                v.currentPosition++;
                                view.setCurrentItem(v.currentPosition, true);
                            }
                        }
                    }
                };
                _viewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        PagerElement v = e.get();
                        if (v != null){
                            int action = event.getAction();
                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                case MotionEvent.ACTION_MOVE:
                                    v.isLooping = true;
                                    v.stopLoop();
                                    break;
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    v.isLooping = false;
                                    v.startLoop();
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });
            }
            stopLoop();
            startLoop();
        }else {
            _viewPager.setOnTouchListener(null);
            stopLoop();
        }

        _adapter.notifyDataSetChanged();
        _viewPager.setCurrentItem(currentPosition);

    }

    private static class PagerElementAdapter extends PagerAdapter {

        private final WeakReference<PagerElement> _element;

        private final Queue<DocumentView> _documentViews;
        private List<ViewElement> _elements;

        public PagerElementAdapter(PagerElement element) {
            _element = new WeakReference<>(element);
            _documentViews = new LinkedList<>();
        }

        @Override
        public int getCount() {
            return elements().size();
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            ViewElement element = (ViewElement) object;
            return ((DocumentView) view).obtainElement() == element;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ViewElement element = elements().get(position);

            DocumentView documentView = _documentViews.size() > 0 ? _documentViews.poll() : null;

            if(documentView == null) {
                documentView = new DocumentView(container.getContext());
                documentView.setLayoutParams(new ViewPager.LayoutParams());
            }

            documentView.setObtainElement(element);

            container.addView(documentView);

            return element;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewElement element = elements().get(position);

            View  view = element.view();

            if(view != null) {

                DocumentView documentView = (DocumentView) view.getParent();

                if(documentView != null) {
                    documentView.setObtainElement(null);
                    _documentViews.add(documentView);
                    container.removeView(documentView);
                } else {
                    element.recycleView();
                }
            }


        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            _elements = null;
            super.notifyDataSetChanged();
        }

        protected List<ViewElement> elements() {

            if(_elements == null) {

                _elements = new ArrayList<>();

                PagerElement p = _element.get();

                if(p != null) {
                    Element e = p.firstChild();
                    while(e != null) {
                        if(e instanceof ViewElement) {
                            _elements.add((ViewElement) e);
                        }
                        e = e.nextSibling();
                    }
                }
            }

            return _elements;
        }
    }

    @Override
    public void recycleView() {
        stopLoop();
        super.recycleView();
    }
}
