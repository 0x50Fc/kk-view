package cn.kkmofang.view;

import cn.kkmofang.view.value.V;

/**
 * Created by zhanghailong on 2018/6/12.
 */

public class AudioElement extends Element {

    protected boolean _loading = false;
    protected boolean _loaded = false;
    protected boolean _playing = false;
    protected boolean _loop = false;

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        if("src".equals(key)) {
            stopPlaying();
            stopLoading();
            startLoading();
        } else if("loop".equals(key)) {
            _loop = V.booleanValue(get(key),false);
        } else if("autoplay".equals(key)) {
            if(V.booleanValue(get(key),false)) {
                startPlaying();
            }
        }
    }

    protected void startLoading() {

    }

    protected void stopLoading() {

    }

    protected void startPlaying() {

        if(_playing) {
            return;
        }

        if(_loading) {
            return;
        }

        if(!_loaded) {
            startLoading();
            return;
        }

        //TODO play
    }

    protected void stopPlaying() {


    }

}
