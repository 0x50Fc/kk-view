package cn.kkmofang.view;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import cn.kkmofang.view.event.EventEmitter;

/**
 * Created by zhanghailong on 2018/1/17.
 */

public class Element extends EventEmitter {

    private Element _firstChild;
    private Element _lastChild;
    private Element _nextSibling;
    private WeakReference<Element> _prevSibling;
    private WeakReference<Element> _parent;
    private long _levelId;
    private long _depth;
    private long _levelChildId;
    private final Map<String,String> _attributes;
    private final Map<String,Map<String,String>> _styles;

    public Element() {
        _attributes = new TreeMap<>();
        _styles = new TreeMap<>();
    }

    public Element firstChild() {
        return _firstChild;
    }

    private void setFirstChild(Element element) {
        _firstChild = element;
    }

    public Element lastChild() {
        return _lastChild;
    }

    private void setLastChild(Element element) {
        _lastChild = element;
    }

    public Element nextSibling() {
        return _nextSibling;
    }

    private void setNextSibling(Element element) {
        _nextSibling = element;
    }

    public Element prevSibling() {
        return _prevSibling == null ? null : _prevSibling.get();
    }

    private void setPrevSibling(Element element) {
        _prevSibling = element == null ? null : new WeakReference<Element>(element);
    }

    public Element parent() {
        return _parent == null ? null : _parent.get();
    }

    protected void setParent(Element element) {
        _parent = element == null ? null : new WeakReference<Element>(element);
    }


    public long levelId() {
        return _levelId;
    }

    public long depth() {
        return _depth;
    }

    public void append(Element element) {

        element.remove();

        if(_lastChild != null) {
            _lastChild.setNextSibling(element);
            element.setPrevSibling(_lastChild);
            setLastChild(element);
            element.setParent(this);
        } else {
            setFirstChild(element);
            setLastChild(element);
            element.setParent(this);
        }

        onDidAddChildren(element);
    }

    public void before(Element element) {

        element.remove();

        Element prev = prevSibling();
        Element parent = parent();

        if(prev != null) {
            prev.setNextSibling(element);
            element.setPrevSibling(prev);
            element.setNextSibling(this);
            element.setParent(parent);
            setPrevSibling(element);
        } else if(parent != null) {
            element.setNextSibling(this);
            element.setParent(parent);
            setPrevSibling(element);
            parent.setFirstChild(element);
        }

        if(parent != null) {
            parent.onDidAddChildren(element);
        }
    }

    public void after(Element element) {

        element.remove();

        Element next = nextSibling();
        Element parent = parent();

        if(next != null) {
            next.setPrevSibling(element);
            element.setNextSibling(next);
            element.setPrevSibling(this);
            element.setParent(parent);
            setNextSibling(element);
        } else if(parent != null) {
            element.setPrevSibling(this);
            element.setParent(parent);
            setNextSibling(element);
            parent.setLastChild(element);
        }

        if(parent != null) {
            parent.onDidAddChildren(element);
        }
    }

    public void remove() {

        Element e = this;
        Element prev = prevSibling();
        Element next = nextSibling();
        Element parent = parent();

        if(prev != null) {

            parent.onWillRemoveChildren(e);

            prev.setNextSibling(next);

            if(next != null) {
                next.setPrevSibling(prev);
            } else {
                parent.setLastChild(prev);
            }

            parent.onDidRemoveChildren(e);
        }
        else if(parent != null) {

            parent.onWillRemoveChildren(e);

            parent.setFirstChild(next);
            if(next != null) {
                next.setPrevSibling(null);
            } else {
                parent.setLastChild(null);
            }

            parent.onDidRemoveChildren(e);
        }
    }


    public void appendTo(Element element) {
        element.append(this);
    }

    public void beforeTo(Element element) {
        element.before(this);
    }

    public void afterTo(Element element) {
        element.after(this);
    }

    protected void onDidRemoveChildren(Element element) {

    }
    protected void onWillRemoveChildren(Element element) {
        element._levelId = 0;
        element._depth = 0;
    }

    protected void onDidAddChildren(Element element) {
        element._levelId = ++ _levelChildId;
        element._depth = _depth + 1;
    }

    public void changedKeys(Set<String> keys) {

        for(String key : keys) {
            changedKey(key);
        }

    }

    public void changedKey(String key) {

    }

    public Set<String> keys() {

        Set<String> keys = new TreeSet<>();

        keys.addAll(_attributes.keySet());

        String v = status();

        if(v == null) {
            v = "";
        }

        if(_styles.containsKey(v)) {
            keys.addAll(_styles.get(v).keySet());
        }

        if(!"".equals(v) && _styles.containsKey("")) {
            keys.addAll(_styles.get("").keySet());
        }

        return keys;
    }

    public String get(String key) {

        if(_attributes.containsKey(key)) {
            return _attributes.get(key);
        }

        String v = status();

        if(v == null) {
            v = "";
        }

        if(_styles.containsKey(v)) {
            Map<String,String> attrs = _styles.get(v);
            if(attrs.containsKey(key)) {
                return attrs.get(key);
            }
        }

        if(!"".equals(v) && _styles.containsKey("")) {
            Map<String,String> attrs = _styles.get("");
            if(attrs.containsKey(key)) {
                return attrs.get(key);
            }
        }

        return null;
    }

    public Map<String, String> getAttributes() {
        return _attributes;
    }

    public void set(String key, String value) {

        if(value == null) {
            if(_attributes.containsKey(key)) {
                _attributes.remove(key);
            }
        } else {
            _attributes.put(key, value);
        }

        if("status".equals(key) || "in-status".equals(key)) {

            Set<String> keys = new TreeSet<>();

            if(_styles.containsKey("")) {
                keys.addAll(_styles.get("").keySet());
            }

            String v = status();

            if(v != null && !"".equals(v) && _styles.containsKey(v)) {
                keys.addAll(_styles.get(v).keySet());
            }

            changedKeys(keys);

            Element e = firstChild();

            while(e != null) {
                e.set("in-status",v);
                e = e.nextSibling();
            }

        } else {
            Set<String> keys = new TreeSet<>();
            keys.add(key);
            changedKeys(keys);
        }
    }

    public void setAttrs(Map<String,String> attrs) {
        _attributes.putAll(attrs);
        changedKeys(attrs.keySet());
    }

    public void setAttrs(String key, String value, String ... keyValues) {
        Set<String> keys = new TreeSet<>();
        if(key != null && value != null) {
            keys.add(key);
            _attributes.put(key,value);
        }
        if(keyValues != null) {
            for(int i=0;i+1<keyValues.length;i+=2) {
                keys.add(keyValues[i]);
                _attributes.put(keyValues[i],keyValues[i+1]);
            }
        }
        changedKeys(keys);
    }

    public void setStyle(Map<String,String> attrs ,String status) {

        if(status == null) {
            status = "";
        }

        Map<String,String> v;

        if(_styles.containsKey(status)) {
            v = _styles.get(status);
        } else {
            v = new TreeMap<>();
            _styles.put(status,v);
        }

        v.putAll(attrs);
    }

    protected Map<String, String> getStyle(String status){
        if (status == null){
           status = "";
        }
        return _styles.get(status);
    }

    public void setCSSStyle(String cssStyle,String status) {

        String[] vs = cssStyle.split(";");

        Map<String,String> attrs = new TreeMap<>();

        for(String v : vs) {
            String[] kv = v.split(":");
            String key = kv[0].trim();
            String value = null;
            if(kv.length > 1) {
                value = kv[1].trim();
            }
            if(key != null && value != null) {
                attrs.put(key,value);
            }
        }

        setStyle(attrs,status);

    }

    public String status() {

        if(_attributes.containsKey("status")) {
            return _attributes.get("status");
        }

        if(_attributes.containsKey("in-status")) {
            return _attributes.get("in-status");
        }

        return null;
    }

    public void setStatus(String status) {
        set("status",status);
        Element p = firstChild();
        while(p != null) {
            p.set("in-status",status);
            p = p.nextSibling();
        }
    }

    public Map<String,Object> data() {

        Map<String,Object> data = new TreeMap<>();

        for(String key : keys()) {
            if(key.startsWith("data-")) {
                String v = get(key);
                if(v != null) {
                    data.put(key.substring(5),v);
                }
            }
        }

        return data;
    }

    @Override
    public Element clone() throws CloneNotSupportedException{
        return (Element) super.clone();
    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();

        {
            for (int i = 0; i < _depth; i++) {
                sb.append("\t");
            }
        }

        String name = get("#name");

        if(name == null) {
            name = "element";
        }

        sb.append("<").append(name);

        {
            for(String key : _attributes.keySet()) {

                if(!key.startsWith("#")) {
                    String v = _attributes.get(key);
                    v = v.replace("&","&amp;");
                    v = v.replace("\n","\\n");
                    v = v.replace("\t","\\t");
                    v = v.replace("\"","\\\"");
                    sb.append(" ").append(key).append("=\"").append(v).append("\"");
                }

            }
        }

        {
            for(String status : _styles.keySet()) {

                Map<String,String> attrs = _styles.get(status);

                if("".equals(status)) {
                    sb.append(" style=\"");
                } else {
                    sb.append(" style:").append(status).append("=\"");
                }

                for(String key : attrs.keySet()) {
                    String v = attrs.get(key);
                    sb.append(key).append(": ").append(v).append(";");
                }

                sb.append("\"");
            }
        }

        sb.append(">");

        Element p = firstChild();

        if(p != null) {

            sb.append("\r\n");

            while(p != null) {
                sb.append(p.toString());
                p = p.nextSibling();
            }

            for(int i=0;i<_depth;i++) {
                sb.append("\t");
            }

            sb.append("</").append(name).append(">\r\n");

        } else {
            String v = get("#text");
            if(v != null) {
                v = v.replace("&","&amp;");
                v = v.replace("<","&lt;");
                v = v.replace(">","&gt;");
                sb.append(v);
            }
            sb.append("</").append(name).append(">\r\n");
        }

        return sb.toString();
    }

    public static class Event extends cn.kkmofang.view.event.Event {

        private final Element _element;
        private boolean _cancelBubble;
        private Object _data;

        public Event(Element element) {
            _element = element;
        }

        public Element element() {
            return _element;
        }

        public boolean isCancelBubble() {
            return _cancelBubble;
        }

        public void setCancelBubble(boolean v) {
            _cancelBubble = v;
        }

        public Object data() {
            return _data;
        }

        public void setData(Object data) {
            _data = data;
        }
    }
}
