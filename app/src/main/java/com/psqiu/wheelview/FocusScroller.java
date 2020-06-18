package com.psqiu.wheelview;

import android.content.Context;
import android.graphics.Rect;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class FocusScroller {
    private int mMode;
    private FocusScroller.SplineFocusScroller mScroller;
    private Interpolator mInterpolator;
    private static final int DEFAULT_DURATION = 250;
    private static final int SCROLL_MODE = 0;

    public FocusScroller(Context context) {
        this(context, new DecelerateInterpolator());
    }

    public FocusScroller(Context context, Interpolator interpolator) {
        this.setInterpolator(interpolator);
        this.mScroller = new FocusScroller.SplineFocusScroller(context);
    }

    public void setInterpolator(Interpolator interpolator) {
        if (interpolator == null) {
            this.mInterpolator = new DecelerateInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }

    }

    public final boolean isFinished() {
        return this.mScroller.mFinished;
    }

    public final void forceFinished(boolean finished) {
        this.mScroller.mFinished = finished;
    }

    public final Rect getCurrRect() {
        return this.mScroller.mCurrentPosition;
    }

    public final Rect getStartRect() {
        return this.mScroller.mStart;
    }

    public final Rect getFinalRect() {
        return this.mScroller.mFinal;
    }

    public boolean computeScrollOffset() {
        if (this.isFinished()) {
            return false;
        } else {
            switch(this.mMode) {
                case 0:
                    long time = AnimationUtils.currentAnimationTimeMillis();
                    long elapsedTime = time - this.mScroller.mStartTime;
                    int duration = this.mScroller.mDuration;
                    if (elapsedTime < (long)duration) {
                        float q = this.mInterpolator.getInterpolation((float)elapsedTime / (float)duration);
                        this.mScroller.updateScroll(q);
                    } else {
                        this.abortAnimation();
                    }
                default:
                    return true;
            }
        }
    }

    public void startScroll(Rect start, Rect end) {
        this.startScroll(start, end, 250);
    }

    public void startScroll(Rect start, Rect end, int duration) {
        this.mMode = 0;
        this.mScroller.startScroll(start, end, duration);
    }

    public void appendScroll(Rect end) {
        this.mScroller.appendScroll(end, 250);
    }

    public void abortAnimation() {
        this.mScroller.finish();
    }

    static class SplineFocusScroller {
        private Rect mStart = new Rect();
        private Rect mCurrentPosition = new Rect();
        private Rect mFinal = new Rect();
        private long mStartTime;
        private int mDuration;
        private boolean mFinished = true;

        SplineFocusScroller(Context context) {
        }

        public void startScroll(Rect start, Rect distance, int duration) {
            this.mFinished = false;
            this.mStart.set(start);
            this.mFinal.set(distance);
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = duration;
        }

        public void appendScroll(Rect distance, int duration) {
            this.mFinal.set(distance);
            this.mDuration = duration;
        }

        public void updateScroll(float q) {
            this.mCurrentPosition.left = calculateCurrentPos(q, this.mStart.left, this.mFinal.left);
            this.mCurrentPosition.top = calculateCurrentPos(q, this.mStart.top, this.mFinal.top);
            this.mCurrentPosition.right = calculateCurrentPos(q, this.mStart.right, this.mFinal.right);
            this.mCurrentPosition.bottom = calculateCurrentPos(q, this.mStart.bottom, this.mFinal.bottom);
        }

        public void finish() {
            this.mCurrentPosition.set(this.mFinal);
            this.mFinished = true;
        }

        static int calculateCurrentPos(float q, int _start, int _final) {
            return _start + Math.round(q * (float)(_final - _start));
        }
    }
}
