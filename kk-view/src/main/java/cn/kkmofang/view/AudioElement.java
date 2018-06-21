package cn.kkmofang.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.kkmofang.view.value.V;

/**
 * Created by zhanghailong on 2018/6/12.
 */

public class AudioElement extends Element {
    private boolean _playing = false;
    private boolean _loop = false;
    private boolean _autopaly = false;
    private String _src;
    private MediaPlayer _player;

    public AudioElement() {
        super();
    }


    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        if("src".equals(key)) {
            _src = get(key);
            stopPlaying();
            if (_autopaly){
                startPlaying();
            }
        } else if("loop".equals(key)) {
            _loop = V.booleanValue(get(key),false);
        } else if("autoplay".equals(key)) {
            _autopaly = V.booleanValue(get(key),false);
            if(_autopaly) {
                startPlaying();
            }
        }
    }

    private void startPlaying() {


        if(_playing) {
            return;
        }

        if (_player == null){
            _player = new MediaPlayer();
        }
        try {
            if (!TextUtils.isEmpty(_src)){
                _player.setDataSource(_src);
                final WeakReference<AudioElement> p = new WeakReference<>(this);
                _player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        AudioElement e = p.get();
                        if (e != null){
                            if (e._loop && e._player != null){
                                e._player.start();
                                e._player.setLooping(true);
                            }

                        }
                    }
                });
                _player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        AudioElement e = p.get();
                        if (e != null && e._player != null){
                            e._player.start();
                            e._playing = true;
                        }
                    }
                });


                _player.prepareAsync();
            }
        } catch (IOException e) {
            _playing = false;
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (_player != null){
            _player.stop();
            _player.release();
            _player.setOnPreparedListener(null);
            _player.setOnCompletionListener(null);
            _player = null;
        }
        _playing = false;
    }

    private void pausePlaying(){
        if (_player != null){
            if (_player.isPlaying()){
                _playing = false;
                _player.pause();
            }
        }
    }

    private void resumePlaying(){
        if (_player != null){
            if (!_player.isPlaying()){
                _playing = true;
                _player.start();
            }
        }
    }


    @Override
    public void onResume(Activity activity) {
        super.onResume(activity);
        resumePlaying();
    }

    @Override
    public void onPause(Activity activity) {
        super.onStop(activity);
        pausePlaying();
    }

    @Override
    public void onDestroy(Activity activity) {
        super.onDestroy(activity);
        stopPlaying();
    }

    public interface IAudioLoadCallback{
        void onStart();

        void onFinish(String source, Exception error);

    }

}
