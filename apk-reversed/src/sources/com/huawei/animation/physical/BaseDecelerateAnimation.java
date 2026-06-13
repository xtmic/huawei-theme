package com.huawei.animation.physical;

import com.huawei.animation.physical.PhysicalModelBase;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseDecelerateAnimation<T extends PhysicalModelBase> extends DynamicAnimation<BaseDecelerateAnimation<T>> {
    private static final int ONE_SECOND = 1000;
    private long frameTime;
    private float lastValue;
    T model;

    @Override // com.huawei.animation.physical.DynamicAnimation
    float getAcceleration(float f, float f2) {
        return 0.0f;
    }

    abstract void sanityCheck();

    <K> BaseDecelerateAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat, T t) {
        super(k, floatPropertyCompat);
        this.lastValue = 0.0f;
        this.model = t;
        t.setValueThreshold(getValueThreshold());
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x004b  */
    @Override // com.huawei.animation.physical.DynamicAnimation
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean updateValueAndVelocity(long j) {
        boolean zIsAtEquilibrium;
        long j2 = this.frameTime + j;
        this.frameTime = j2;
        float x = this.model.getX(j2 / 1000.0f);
        this.mValue += x - this.lastValue;
        this.lastValue = x;
        this.mVelocity = this.model.getDX(this.frameTime / 1000.0f);
        if (this.mValue < this.mMinValue) {
            this.mValue = this.mMinValue;
        } else if (this.mValue > this.mMaxValue) {
            this.mValue = this.mMaxValue;
        } else {
            zIsAtEquilibrium = isAtEquilibrium(this.mValue, this.mVelocity);
            if (zIsAtEquilibrium) {
                reset();
            }
            return zIsAtEquilibrium;
        }
        zIsAtEquilibrium = true;
        if (zIsAtEquilibrium) {
        }
        return zIsAtEquilibrium;
    }

    @Override // com.huawei.animation.physical.DynamicAnimation
    boolean isAtEquilibrium(float f, float f2) {
        return this.model.isAtEquilibrium(f, f2);
    }

    @Override // com.huawei.animation.physical.DynamicAnimation
    void setValueThreshold(float f) {
        this.model.setValueThreshold(f);
    }

    @Override // com.huawei.animation.physical.DynamicAnimation
    public void cancel() {
        super.cancel();
        reset();
    }

    public void reset() {
        this.frameTime = 0L;
        this.lastValue = 0.0f;
    }

    public T getModel() {
        return this.model;
    }

    @Override // com.huawei.animation.physical.DynamicAnimation
    public void start() {
        if (this.model == null) {
            throw new UnsupportedOperationException("Incomplete Animation: Physical Model should be set!");
        }
        sanityCheck();
        this.model.setValueThreshold(getValueThreshold());
        super.start();
    }
}
