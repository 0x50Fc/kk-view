package cn.kkmofang.view;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by zhanghailong on 2018/4/19.
 */

public class PagerElement extends ViewElement {

    private PagerElementAdapter _adapter;
    private final Handler _handler;

    public PagerElement() {
        super();
        _handler = new Handler();
        set("#view",ViewPager.class.getName());
    }

    public ViewPager viewPager() {
        View v = view();
        if(v != null && v instanceof ViewPager){
            return (ViewPager) v;
        }
        return null;
    }

    private ViewPager.OnPageChangeListener _OnPageChangeListener;

    public void setView(View view) {
        ViewPager v = viewPager();
        if(v != null && _OnPageChangeListener != null) {
            v.removeOnPageChangeListener(_OnPageChangeListener);
            v.setAdapter(null);
        }
        super.setView(view);
        v = viewPager();
        if(v != null ) {
            if(_OnPageChangeListener == null) {

                _OnPageChangeListener = new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                };
            }
            v.addOnPageChangeListener(_OnPageChangeListener);

            if(_adapter == null) {
                _adapter = new PagerElementAdapter(this);
            }

            v.setAdapter(_adapter);
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
            _adapter.reloadElements();
        }
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
}
