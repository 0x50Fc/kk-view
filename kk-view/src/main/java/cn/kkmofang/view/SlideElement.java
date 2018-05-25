package cn.kkmofang.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.kk.view.R;

import java.lang.ref.WeakReference;

/**
 * Created by hailong11 on 2018/5/25.
 */

public class SlideElement extends ScrollElement {

    private DocumentView _curElementView;

    public DocumentView curElementView() {
        if(_curElementView == null) {
            ViewElement e = curElement();
            if(e != null) {
                _curElementView = new DocumentView(viewContext.getContext());
                _curElementView.setElement(e);
            }
        }
        return _curElementView;
    }

    public ViewElement curElement() {
        Element e = firstChild();
        if(e != null && e instanceof  CurElement) {
            e = e.firstChild();
            if(e != null && e instanceof ViewElement) {
                return (ViewElement) e;
            }
        }
        return null;
    }

    public void setView(View view) {
        if(_curElementView != null) {
            ViewParent p = _curElementView.getParent();
            if(p != null && p instanceof ViewGroup) {
                ((ViewGroup) p).removeView(_curElementView);
            }
        }
        super.setView(view);
        if(view != null) {
            updateAnchor(false);
        }

    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        if("anchor".equals(key)) {
            if(view() != null) {
                updateAnchor(true);
            }
        }
    }

    public void updateAnchor(boolean animated) {

        String anchor = get("anchor");


        ViewElement element = null;

        if(anchor != null) {
            Element e = firstChild();

            while (e != null) {

                if (e instanceof ViewElement) {
                    String v = e.get("anchor");
                    if (v != null && v.equals(anchor)) {
                        element = (ViewElement) e;
                        break;
                    }
                }

                e = e.nextSibling();
            }
        }

        DocumentView cur = curElementView();

        if(cur != null && view() != null) {

            if(element == null) {
                ViewParent p = cur.getParent();
                if(p != null && p instanceof ViewGroup) {
                    ((ViewGroup) p).removeView(cur);
                }
            } else {

                ViewGroup p = (ViewGroup) cur.getParent();

                if(p != null) {
                    p.removeView(cur);
                }


                WeakReference<ViewElement> e = (WeakReference<ViewElement>) cur.getTag(R.id.kk_view_element);

                if(e == null || e.get() == null) {
                    animated = false;
                }

                cur.setTag(R.id.kk_view_element,new WeakReference<>(element));

                ScrollView pv = scrollView();

                if(pv != null) {
                    pv.contentView.addView(cur,0);
                }

            }
        }

    }

    public static class CurElement extends Element {

    }
}
