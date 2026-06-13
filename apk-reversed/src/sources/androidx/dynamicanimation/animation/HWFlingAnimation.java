package androidx.dynamicanimation.animation;

import com.huawei.animation.physical.FlingModelBase;

/* JADX INFO: loaded from: classes.dex */
public class HWFlingAnimation extends BaseDecelerateAnimation<FlingModelBase> {
    public <K> HWFlingAnimation(K k, OscarFloatPropertyCompat<K> oscarFloatPropertyCompat, float f, float f2) {
        super(k, oscarFloatPropertyCompat, new FlingModelBase(f, f2));
        getModel().setValueThreshold(getValueThreshold());
    }

    @Override // androidx.dynamicanimation.animation.BaseDecelerateAnimation
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
