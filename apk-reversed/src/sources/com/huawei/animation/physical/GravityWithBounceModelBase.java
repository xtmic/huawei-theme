package com.huawei.animation.physical;

/* JADX INFO: loaded from: classes.dex */
public class GravityWithBounceModelBase extends GravityModelBase {
    private float mAbsorb;
    private long mDtThreshold;
    private boolean mReboundedLast;
    private float mValueThreshold;
    private float mVelocityThreshold;

    public GravityWithBounceModelBase(float f, float f2) {
        super(f, 0.0f);
        this.mDtThreshold = (long) ((1.2f - f2) * 300.0f);
        this.mAbsorb = f2 == 0.0f ? 0.8f : f2;
        this.mReboundedLast = false;
    }

    public void reconfigure(float f, float f2) {
        super.reconfigure(f);
        if (f2 == 0.0f) {
            f2 = 0.8f;
        }
        this.mAbsorb = f2;
    }

    @Override // com.huawei.animation.physical.GravityModelBase, com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getX(float f) {
        float x = super.getX(f);
        if (x > 0.0f) {
            if (this.mReboundedLast) {
                return 0.0f;
            }
            this.mReboundedLast = true;
            setXV(0.0f, this.mV * this.mAbsorb);
            return 0.0f;
        }
        this.mReboundedLast = false;
        return x;
    }

    @Override // com.huawei.animation.physical.GravityModelBase, com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium() {
        return getX() > 1.0f;
    }

    @Override // com.huawei.animation.physical.GravityModelBase, com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public GravityWithBounceModelBase setValueThreshold(float f) {
        float fAbs = Math.abs(f) / 100.0f;
        this.mValueThreshold = fAbs;
        this.mVelocityThreshold = fAbs * 62.5f;
        return this;
    }

    @Override // com.huawei.animation.physical.GravityModelBase, com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium(float f, float f2) {
        return ((double) Math.abs(f2)) < ((double) this.mVelocityThreshold) && ((double) Math.abs(f - this.mEndPosition)) < ((double) this.mValueThreshold);
    }
}
