package com.huawei.animation.physical;

import android.util.Log;
import android.util.SparseArray;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractChainAdapter {
    private static final float HALF_FACTOR = 2.0f;
    private static final String TAG = "AbstractChainAdapter";
    private SpringChain springChain;
    protected int visibleRight;
    private boolean isTouchDown = false;
    protected int visibleLeft = 0;
    private SparseArray<Float> valuesArr = new SparseArray<>();

    protected abstract int getMaxVisibleCount();

    public AbstractChainAdapter(int i, SpringChain springChain) {
        this.visibleRight = i - 1;
        this.springChain = springChain;
    }

    public boolean isObjVisible(int i) {
        int i2 = this.visibleLeft;
        int i3 = this.visibleRight;
        if (i2 < i3) {
            return i > i2 - getMaxVisibleCount() && ((double) i) < ((double) this.visibleRight) + Math.ceil((double) (((float) getMaxVisibleCount()) / HALF_FACTOR));
        }
        if (i2 > i3) {
            return ((double) i) > ((double) i3) - Math.ceil((double) (((float) getMaxVisibleCount()) / HALF_FACTOR)) && i < this.visibleLeft + getMaxVisibleCount();
        }
        boolean z = i > i2 - getMaxVisibleCount() && i < this.visibleRight + getMaxVisibleCount();
        Log.i(TAG, String.format("isObjVisible: l=%s, r=%s, index=%s, result=%s", Integer.valueOf(this.visibleLeft), Integer.valueOf(this.visibleRight), Integer.valueOf(i), Boolean.valueOf(z)));
        return z;
    }

    public AbstractChainAdapter setVisibleRange(int i, int i2) {
        Log.i(TAG, String.format("setVisibleRange: left=%s, right=%s", Integer.valueOf(i), Integer.valueOf(i2)));
        this.visibleLeft = i;
        this.visibleRight = i2;
        return this;
    }

    public AbstractChainAdapter setTouchDown(boolean z) {
        this.isTouchDown = z;
        return this;
    }

    public boolean isTouchDown() {
        return this.isTouchDown;
    }

    public AbstractChainAdapter record(int i, float f) {
        this.valuesArr.put(i, Float.valueOf(f));
        return this;
    }

    public AbstractChainAdapter recover() {
        List<HWSpringAnimation> allSpringAnimation = this.springChain.getAllSpringAnimation();
        for (int i = 0; i < this.valuesArr.size(); i++) {
            int iKeyAt = this.valuesArr.keyAt(i);
            allSpringAnimation.get(iKeyAt).setAnimateValue(this.valuesArr.valueAt(i).floatValue());
        }
        this.valuesArr.clear();
        return this;
    }
}
