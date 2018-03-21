package cn.kkmofang.view.event;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hailong11 on 2018/1/17.
 */

public class EventEmitter {

    private final List<EventCallback> _funcs;

    public EventEmitter() {
        _funcs = new LinkedList<EventCallback>();
    }

    public void on(String name, EventFunction func) {
        _funcs.add(new EventCallback(name,func));
    }

    public void off(String name,EventFunction func) {

        Iterator<EventCallback> i = _funcs.iterator();

        while(i.hasNext()) {
            EventCallback cb = i.next()   ;
            if((name == null || name.equals(cb.name))
                    && (func == null || func == cb.func)) {
                i.remove();
            }
        }
    }

    public void emit(String name, Event event) {
        List<EventCallback> cbs = new LinkedList<EventCallback>();

        Iterator<EventCallback> i = _funcs.iterator();

        while(i.hasNext()) {
            EventCallback cb = i.next();
            if(cb.name.equals(name)) {
                cbs.add(cb);
            }
        }

        i = cbs.iterator();

        while(i.hasNext()) {
            EventCallback cb = i.next();
            cb.func.onEvent(event);
        }
    }

    private static class EventCallback {

        public final String name;
        public final EventFunction func;

        public EventCallback(String name,EventFunction func) {
            this.name = name;
            this.func = func;
        }
    }
}
