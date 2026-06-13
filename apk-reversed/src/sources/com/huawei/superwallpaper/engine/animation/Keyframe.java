package com.huawei.superwallpaper.engine.animation;

import android.view.animation.Interpolator;

/* JADX INFO: loaded from: classes.dex */
public class Keyframe {
    private float mFraction;
    private Interpolator mInterpolator;
    private float mValue;

    public Keyframe(float f, float f2) {
        this.mInterpolator = null;
        this.mFraction = f;
        this.mValue = f2;
    }

    public Keyframe(float f, float f2, Interpolator interpolator) {
        this.mInterpolator = null;
        this.mFraction = f;
        this.mValue = f2;
        this.mInterpolator = interpolator;
    }

    public float getFraction() {
        return this.mFraction;
    }

    public void setFraction(float f) {
        this.mFraction = f;
    }

    public float getValue() {
        return this.mValue;
    }

    public void setValue(float f) {
        this.mValue = f;
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }
}
