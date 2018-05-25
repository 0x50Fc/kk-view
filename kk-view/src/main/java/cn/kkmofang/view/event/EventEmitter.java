package cn.kkmofang.view.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhanghailong on 2018/1/17.
 */

public class EventEmitter {

    private final Map<String,List<EventFunction>> _funcs ;

    public EventEmitter() {
        _funcs = new TreeMap<>();
    }

    public void on(String name, EventFunction func) {
        List<EventFunction> cbs;
        if(_funcs.containsKey(name)) {
            cbs = _funcs.get(name);
        } else {
            cbs = new LinkedList<>();
            _funcs.put(name,cbs);
        }
        cbs.add(func);
    }

    public void off(String name,EventFunction func) {

        if(name == null && func == null) {
            _funcs.clear();
        } else if(name == null) {
            for(String key : _funcs.keySet()) {

                List<EventFunction> cbs = _funcs.get(key);

                Iterator<EventFunction> i = cbs.iterator();

                while(i.hasNext()) {
                    EventFunction cb = i.next()   ;
                    if( func == cb) {
                        i.remove();
                    }
                }

            }
        } else if(_funcs.containsKey(name)){

            List<EventFunction> cbs = _funcs.get(name);

            Iterator<EventFunction> i = cbs.iterator();

            while(i.hasNext()) {
                EventFunction cb = i.next()   ;
                if( func == cb) {
                    i.remove();
                }
            }
        }

    }

    public void emit(String name, Event event) {

        if(_funcs.containsKey(name)) {

            List<EventFunction> cbs = _funcs.get(name);

            for(EventFunction fn : new ArrayList<>(cbs)) {
                fn.onEvent(event);
            }

        }

    }

    public boolean hasEvent(String name) {
        return _funcs.containsKey(name) && _funcs.get(name).size() > 0;
    }


}
