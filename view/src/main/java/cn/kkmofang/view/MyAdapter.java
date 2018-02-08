package cn.kkmofang.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by wangchao15 on 2018/2/6.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private List<ViewElement> _elements = new ArrayList<>();
    private Map<String,Integer> _reuseIds = new TreeMap<>();
    private Context mContext;
    private int viewType;

    public MyAdapter(ViewElement element, Context context) {
        this.mContext = context;
        Element p = element.firstChild();
        viewType = 0;
        while(p != null) {
            if(p instanceof ViewElement) {
                ViewElement e = (ViewElement) p;
                String reuse = e.reuse();
                if(! _reuseIds.containsKey(reuse)) {
                    _reuseIds.put(reuse,viewType);
                    viewType ++;
                }
                _elements.add(e);
            }
            p = p.nextSibling();
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(new DocumentView(mContext));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        if (holder.itemView instanceof DocumentView){
            DocumentView itemView = (DocumentView) holder.itemView;
            itemView.setElement(_elements.get(position));
        }

    }

    public void fNotifyItemInserted(Element e){
        if (e != null && e instanceof ViewElement){
            ViewElement element = (ViewElement) e;
            String reuse = element.reuse();
            if (!_reuseIds.containsKey(reuse)){
                _reuseIds.put(reuse,viewType);
                viewType ++;
            }
            _elements.add(element);
            notifyItemInserted(getItemCount());
        }
    }

    public void fNotifyItemRemoved(int position){
        if (_elements != null && _elements.size() >= position && position >= 0){
            _elements.remove(position);
            notifyItemRemoved(position);
            if (position != _elements.size()){
                notifyItemRangeChanged(position, _elements.size() - position);
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (_elements.size() < position)return 0;
        ViewElement element = _elements.get(position);
        if (element == null)return 0;
        return _reuseIds.get(_elements.get(position).reuse());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (_elements != null)return _elements.size();
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
