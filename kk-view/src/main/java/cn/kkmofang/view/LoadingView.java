package cn.kkmofang.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.kk.view.R;

import java.lang.ref.WeakReference;

public class LoadingView extends ImageView {
    private LoadingDrawable _drawable;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        _drawable = new LoadingDrawable(context);
        setImageDrawable(_drawable);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE){
            start();
        }else {
            stop();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public void start(){
        if (_drawable != null){
            _drawable.start();
        }
    }

    public void stop(){
        if (_drawable != null){
            _drawable.stop();
        }
    }

    public static class LoadingDrawable extends Drawable implements Animatable {
        private static final int default_divide_count = 12;
        private static final int default_duration = 1200;

        private Context _context;
        private Bitmap _source;
        private Handler _handler;
        private Matrix _matrix = new Matrix();
        private int _currentDegree = 0;
        private int _duration = default_duration;
        private int _averageCount = default_divide_count;

        private boolean isAnimRunning = false;
        private LoadingTask _loadingTask;

        private int _alpha;
        private int _centerXY;
        private float _scaleX = 1;
        private float _scaleY = 1;
        private int _offset = -1;

        public LoadingDrawable(Context context) {
            this(context, R.drawable.loading_drawable);
        }

        public LoadingDrawable(Context context, @DrawableRes int resDrawable) {
            _context = context;
            _handler = new Handler();
            _loadingTask = new LoadingTask(this);
            _source = BitmapFactory.decodeResource(_context.getResources(), resDrawable);

        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            int w = bounds.width();
            int h = bounds.height();
            if (w != h){
                throw new IllegalStateException("view must have same width and height, now w =" +w+"h ="+h);
            }
            _centerXY = w / 2;
            if (w <= _source.getWidth()){
                _scaleX = w / (float)_source.getWidth();
                _scaleY = h / (float)_source.getHeight();
            }else {
                _offset = (w - _source.getWidth()) / 2;
            }
            setCurrentDegree(0);


        }

        private void setDuration(int duration){
            _duration = duration;
        }

        @Override
        public void start() {
            if (!isRunning()){
                _handler.post(_loadingTask);
                isAnimRunning = true;

            }
        }

        @Override
        public void stop() {
            if (isRunning()){
                _handler.removeCallbacks(_loadingTask);
                isAnimRunning = false;
            }
        }

        @Override
        public boolean isRunning() {
            return isAnimRunning;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            canvas.drawBitmap(_source, _matrix, null);
        }

        @Override
        public void setAlpha(int alpha) {
            _alpha = alpha;
        }

        @Override
        public int getAlpha() {
            return _alpha;
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }

        @Override
        public int getIntrinsicHeight() {
            return _source.getHeight();
        }

        @Override
        public int getIntrinsicWidth() {
            return _source.getWidth();
        }

        private void setCurrentDegree(int cDegree){
            _currentDegree = cDegree;
            doRotateLoading(_currentDegree);
        }

        public void setGapCount(int gapCount){
            _averageCount = gapCount;
        }


        public int getRotateGap(){
            return 360 / _averageCount;
        }

        private int getRotateGapTime(){
            return _duration / _averageCount;
        }

        public void doRotateLoading(int degree){
            _matrix.reset();
            _matrix.postScale(_scaleX, _scaleY);
            if (_offset >= 0){
                _matrix.postTranslate(_offset, _offset);
            }
            _matrix.postRotate(degree, _centerXY, _centerXY);
            invalidateSelf();
        }

        private static class LoadingTask implements Runnable{
            WeakReference<LoadingDrawable> _loadingDrawable;

            public LoadingTask(LoadingDrawable drawable) {
                this._loadingDrawable = new WeakReference<>(drawable);
            }

            @Override
            public void run() {
                LoadingDrawable _drawable = _loadingDrawable.get();
                if (_drawable == null){
                    return;
                }
                _drawable._currentDegree += _drawable.getRotateGap();
                if (_drawable._currentDegree >= 360){
                    _drawable._currentDegree = 0;
                }
                _drawable.doRotateLoading(_drawable._currentDegree);
                if (_drawable.isAnimRunning){
                    _drawable._handler.postDelayed(this, _drawable.getRotateGapTime());
                }


            }
        }

    }


}
