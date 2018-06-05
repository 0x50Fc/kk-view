package cn.kkmofang.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Created by zhanghailong on 2018/4/19.
 */

public class PagerElement extends ViewElement {

    private PagerElementAdapter _adapter;
    private final Handler _handler;
    private int _pagerInterval=0;
    private KKViewPager _viewPager=null;
    private boolean isLooping=false;
    private boolean isLoop=true;
    private  Runnable autoRunnable=null;
    private int currentPosition;
    public PagerElement() {
        super();
        _handler = new Handler();
        set("#view",KKViewPager.class.getName());
    }


    @Override
    public Class<?> viewClass() {
        return super.viewClass();
    }

    public KKViewPager viewPager() {
        View v = view();
        if(v != null && v instanceof KKViewPager){
            return (KKViewPager) v;
        }
        return null;
    }

    private KKViewPager.OnPageChangeListener _OnPageChangeListener;

    public void setView(View view) {
        _viewPager = viewPager();
        if(_viewPager != null && _OnPageChangeListener != null) {
            _viewPager.removeOnPageChangeListener(_OnPageChangeListener);
            _viewPager.setAdapter(null);
        }
        super.setView(view);
        _viewPager = viewPager();
        if(_viewPager != null ) {
            if(_OnPageChangeListener == null) {

                _OnPageChangeListener = new KKViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
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

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (state == KKViewPager.SCROLL_STATE_IDLE && isLoop) {
                            _viewPager.setCurrentItem(currentPosition, false);
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

    private void pageSelected(int position) {
        if (position == 0) {    //判断当切换到第0个页面时把currentPosition设置为list.size(),即倒数第二个位置，小圆点位置为length-1
            currentPosition = _adapter.getCount()-2;
        } else if (position ==_adapter.getCount() -1) {    //当切换到最后一个页面时currentPosition设置为第一个位置，小圆点位置为0
            currentPosition = 1;
        } else {
            currentPosition = position;
        }
    }


    @Override
    public void changedKey(String key) {
        String v = get(key);
        if("interval".equals(key)) {
            if(v!=null && !TextUtils.isEmpty(v)){
                try {
                    _pagerInterval = Integer.parseInt(v);
                }catch (Exception e){
                    _pagerInterval =0;
                }
            }
        }else if("loop".equals(key)){
            if(v!=null && !TextUtils.isEmpty(v)){
                try {
                    isLoop = Boolean.valueOf(v);
                }catch (Exception e){
                    isLoop =true;
                }
            }
        }

        super.changedKey(key);
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
                _adapter.reloadElements();
            }
        }
    }



    private void startLoop() {
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
    private void initAutoPlayPagerViewAndPlay(){
        currentPosition=1;
        if(_viewPager==null || _adapter==null){
            return;
        }
        _adapter.resetElements();
        List<ViewElement> _elements= _adapter.elements();
        if (_elements==null || _elements.size()<=1){
            return;
        }
//        ViewElement fe = _elements.get(_elements.size()-1);
//        _elements.add(0,fe);
//        _elements.add(_elements.get(1));
        //需要自动轮播
        if(_pagerInterval>0){
            View pview = getParentView();
            if(pview!=null && pview instanceof ViewGroup){
                _viewPager.setParentView((ViewGroup) pview);
            }
            if(autoRunnable==null ) {
                autoRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (_viewPager != null && _viewPager.getChildCount() > 1) {
                            _handler.postDelayed(this, _pagerInterval);
                            currentPosition++;
                            _viewPager.setCurrentItem(currentPosition, true);
                        }
                    }
                };
                _viewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                            case MotionEvent.ACTION_MOVE:
                                isLooping = true;
                                stopLoop();
                                break;
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                isLooping = false;
                                startLoop();
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }
        }
        _adapter.notifyDataSetChanged();
        _viewPager.setCurrentItem(currentPosition);
        if(_pagerInterval>0) {
            stopLoop();
            startLoop();
        }
    }

    /**
     * 回去viewpager的下拉滚动父view
     * @return
     */
    private View getParentView(){
        if(_viewPager==null){
            return null;
        }
        View parent= (View) _viewPager.getParent();
        do {
            if (parent != null) {
                if (parent instanceof ContainerView) {
                    ((ContainerView) parent).setOnScrollListener(new ContainerView.OnScrollListener() {
                        @Override
                        public void onScroll(int x, int y) {

                        }

                        @Override
                        public void onStartTracking() {
                            stopLoop();
                        }

                        @Override
                        public void onStopTracking() {
                            startLoop();
                        }
                    });
                    break;
                } else {//TODO 这里可以拓展其他的父view的设置

                }
                parent = (View) parent.getParent();
            }
        }while (parent!=null);
        return parent;
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
                documentView.setLayoutParams(new KKViewPager.LayoutParams());
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

        public void reloadElements() {
            _elements = null;
            notifyDataSetChanged();
        }
        public void resetElements(){
            _elements=null;
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
