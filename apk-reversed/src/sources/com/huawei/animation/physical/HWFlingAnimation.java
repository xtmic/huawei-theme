package com.huawei.animation.physical;

/* JADX INFO: loaded from: classes.dex */
public class HWFlingAnimation extends BaseDecelerateAnimation<FlingModelBase> {
    public <K> HWFlingAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat, float f, float f2) {
        super(k, floatPropertyCompat, new FlingModelBase(f, f2));
        getModel().setValueThreshold(getValueThreshold());
    }

    @Override // com.huawei.animation.physical.BaseDecelerateAnimation
    void sanityCheck() {
        ((FlingModelBase) this.model).sanityCheck();
    }

    public HWFlingAnimation setInitVelocity(float f) {
        ((FlingModelBase) this.model).setInitVelocity(f);
        return this;
    }

    public HWFlingAnimation setFriction(float f) {
        ((FlingModelBase) this.model).setFriction(f);
        return this;
    }
}
