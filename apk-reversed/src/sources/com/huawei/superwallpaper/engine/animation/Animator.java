package com.huawei.superwallpaper.engine.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Animator {
    private long mDelay;
    private long mDuration;
    private Interpolator mInterpolator;
    private String mInterpolatorStr;
    private final List<Property> mProperties = new ArrayList();

    public Animator() {
    }

    public Animator(long j, long j2, String str, Interpolator interpolator) {
        this.mDelay = j;
        this.mDuration = j2;
        this.mInterpolatorStr = str;
        this.mInterpolator = interpolator;
    }

    public Animator(long j, Interpolator interpolator) {
        this.mDuration = j;
        this.mInterpolator = interpolator;
    }

    public Animator(long j) {
        this.mDuration = j;
    }

    public String getInterpolatorStr() {
        return this.mInterpolatorStr;
    }

    public void setInterpolatorStr(String str) {
        this.mInterpolatorStr = str;
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }

    public long getDelay() {
        return this.mDelay;
    }

    public void setDelay(long j) {
        this.mDelay = j;
    }

    public void addProperty(Property property) {
        this.mProperties.add(property);
    }

    public void addProperty(List<Property> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        this.mProperties.addAll(list);
    }

    public List<Property> getProperties() {
        return this.mProperties;
    }

    public boolean step(long j) {
        if (!this.mProperties.isEmpty()) {
            long j2 = this.mDuration;
            if (j2 > 0) {
                long j3 = j - this.mDelay;
                z = j3 < j2;
                float fMin = Math.min(Math.max(0.0f, j3 / this.mDuration), 1.0f);
                Interpolator interpolator = this.mInterpolator;
                if (interpolator != null) {
                    fMin = interpolator.getInterpolation(fMin);
                }
                Iterator<Property> it = this.mProperties.iterator();
                while (it.hasNext()) {
                    it.next().calculateValue(fMin);
                }
            }
        }
        return z;
    }
}
