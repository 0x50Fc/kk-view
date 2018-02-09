package cn.kkmofang.view;


import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.kkmofang.view.value.Edge;
import cn.kkmofang.view.value.Orientation;

/**
 * Created by hailong11 on 2018/1/20.
 */

public class ScrollElement extends ViewElement{
    //默认使用垂直滑动
    public Orientation mOrientation = Orientation.VERTICAL;
    private MyAdapter adapter;
    private LayoutManager manager;
    public List<ViewElement> _elements = new ArrayList<>();

    public ScrollElement() {
        super();
        set("#view",RecyclerView.class.getName());
    }

    private RecyclerView recyclerView() {
        return (RecyclerView) view();
    }

    @Override
    public void obtainView(View view) {
        super.obtainView(view);
    }

    @Override
    public void setView(View view) {
        if(view == null || view instanceof RecyclerView) {
            RecyclerView v = recyclerView();
            if (v != null){
                manager = null;
                adapter = null;
                v.setLayoutManager(null);
                v.setAdapter(null);
            }
            super.setView(view);
            v = recyclerView();
            if (v != null){
                manager = new LayoutManager(this);
                v.setLayoutManager(manager);
                adapter = new MyAdapter(this, viewContext.context);
                v.setAdapter(adapter);
            }
        }
    }


    @Override
    public void append(Element element) {
        if (element instanceof ViewElement){
            _elements.add((ViewElement) element);
            if (adapter != null){
                adapter.fNotifyItemInserted(element);
            }
        }
    }

    @Override
    public void recycleView() {
        super.recycleView();
        if (view() != null){
            _elements.clear();
        }
    }

    @Override
    public void remove() {
        super.remove();
    }

    public static class LayoutManager extends RecyclerView.LayoutManager {
        private int offset;
        private ScrollElement mElement;
        private Orientation mOrientation;
        private int totalHeight;
        private int totalWidth;


        public LayoutManager(@NonNull  ScrollElement element) {
            this.mElement = element;
            this.mOrientation = element.mOrientation;
        }

        public Orientation getOrientation() {
            return mOrientation;
        }

        public void setOrientation(Orientation mOrientation) {
            if (this.mOrientation == mOrientation)return;
            this.mOrientation = mOrientation;
            requestLayout();
        }

        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (getItemCount() == 0){
                detachAndScrapAttachedViews(recycler);
                return;
            }

            if (getChildCount() == 0 && state.isPreLayout()){
                return;
            }
            detachAndScrapAttachedViews(recycler);

            offset = 0;

            layoutItems(0, recycler, state);
        }

        public void layoutItems(int dy, RecyclerView.Recycler recycler, RecyclerView.State state){
            // TODO: 2018/2/7 进行优化，将不在屏幕中的remove
            Rect displayRect = new Rect(0, offset, getHorizontalSpace(), offset + getVerticalSpace());

            Rect childRect = new Rect();
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                childRect.left = getDecoratedLeft(child);
                childRect.right = getDecoratedRight(child);
                childRect.top = getDecoratedTop(child);
                childRect.bottom = getDecoratedBottom(child);

                if (!Rect.intersects(displayRect, childRect)){
                    removeAndRecycleView(child, recycler);
                }
            }


            int offsetY = 0;
            int offsetX = 0;
            ViewElement p;

            for (int i = 0; i < getItemCount() && i < mElement._elements.size(); i++) {
                p = mElement._elements.get(i);
                if (p == null)continue;
                View scrap = recycler.getViewForPosition(i);
                if (dy >= 0){
                    addView(scrap);
                }else {
                    addView(scrap, 0);
                }
                measureChildWithMargins(scrap, 0, 0);
                int marginLeft = (int) p.left.floatValue(getWidth(), 0);
                int marginRight = (int) p.right.floatValue(getWidth(), 0);
                int marginTop = (int) p.top.floatValue(getHeight(), 0);
                int marginBottom = (int) p.bottom.floatValue(getHeight(), 0);
                int width = (int) p.width.floatValue(getWidth() - marginLeft - marginRight, 0);
                int height = (int) p.height.floatValue(getHeight() - marginTop - marginBottom, 0);
                p.layout(width, height);
                if (mOrientation == Orientation.VERTICAL){
                    layoutDecorated(scrap,
                            marginLeft,
                            offsetY + marginTop - offset,
                            marginLeft + width,
                            offsetY + height + marginTop - offset);
                    offsetY += height + marginTop + marginBottom;
                }else {//水平方向
                    layoutDecorated(scrap, offsetX + marginLeft - offset, marginTop, offsetX + marginLeft + width - offset, height + marginTop);
                    offsetX += width + marginLeft + marginRight;
                }
            }
            totalWidth = Math.max(offsetX, getHorizontalSpace());
            totalHeight = Math.max(offsetY, getVerticalSpace());
        }

        private int getVerticalSpace() {
            return getHeight() - getPaddingBottom() - getPaddingTop();
        }

        private int getHorizontalSpace(){
            return getWidth() - getPaddingLeft() - getPaddingRight();
        }

        @Override
        public boolean canScrollHorizontally() {
            return mOrientation == Orientation.HORIZONTAL;
        }

        @Override
        public boolean canScrollVertically() {
            return mOrientation == Orientation.VERTICAL;
        }

        @Override
        public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (mOrientation == Orientation.HORIZONTAL)return 0;
            return scrollBy(dy, recycler, state);
        }

        @Override
        public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (mOrientation == Orientation.VERTICAL)return 0;
            return scrollBy(dx, recycler, state);
        }

        public int scrollBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state){
            if (getChildCount() == 0 || dy == 0)return 0;
            detachAndScrapAttachedViews(recycler);
            int size = mOrientation == Orientation.HORIZONTAL?totalWidth - getHorizontalSpace():
                    totalHeight - getVerticalSpace();

            if (offset + dy <0){
                dy = -offset;
            }else if (offset + dy > size){
                dy = size - offset;
            }
            offset += dy;

            if (mOrientation == Orientation.HORIZONTAL){
                offsetChildrenHorizontal(-dy);
            }else {
                offsetChildrenVertical(-dy);
            }

            layoutItems(dy, recycler, state);
            return dy;
        }
    }
}
