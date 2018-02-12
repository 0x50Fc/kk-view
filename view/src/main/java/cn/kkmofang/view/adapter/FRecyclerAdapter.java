package cn.kkmofang.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.kkmofang.view.Element;
import cn.kkmofang.view.ElementView;
import cn.kkmofang.view.ScrollElement;
import cn.kkmofang.view.ViewElement;

/**
 * Created by wangchao15 on 2018/2/6.
 */

public class FRecyclerAdapter extends RecyclerView.Adapter<FRecyclerAdapter.MyHolder> {

    private List<ViewElement> _elements = new ArrayList<>();
    private Map<String,Integer> _reuseIds = new TreeMap<>();
    private Context mContext;
    private int viewType;

    public FRecyclerAdapter(ViewElement element, Context context) {
        this.mContext = context;
        viewType = 0;
        if (element instanceof ScrollElement){

            for (ViewElement e : ((ScrollElement) element)._elements) {
                String reuse = e.reuse();
                if(! _reuseIds.containsKey(reuse)) {
                    _reuseIds.put(reuse,viewType);
                    viewType ++;
                }
                _elements.add(e);
            }
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyHolder(new ElementView(mContext));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        if (_elements.size() > position){
            if (holder.itemView != null && holder.itemView instanceof ElementView){
                ElementView itemView = (ElementView) holder.itemView;
                _elements.get(position).obtainView(itemView);
            }
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
