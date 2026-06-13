package com.huawei.animation.physical;

import com.huawei.animation.physical.DynamicAnimation;

/* JADX INFO: loaded from: classes.dex */
public class SpringModel extends SpringModelBase {
    private static final float ONE_SECOND = 1000.0f;
    private final DynamicAnimation.MassState mMassState;
    protected float mTotalT;

    public SpringModel(float f, float f2) {
        super(f, f2);
        this.mTotalT = 0.0f;
        this.mTotalT = 0.0f;
        this.mMassState = new DynamicAnimation.MassState();
    }

    public SpringModel reset() {
        this.mTotalT = 0.0f;
        this.mMassState.mValue = 0.0f;
        this.mMassState.mVelocity = 0.0f;
        return this;
    }

    public DynamicAnimation.MassState updateValues(long j) {
        float f = this.mTotalT + j;
        this.mTotalT = f;
        float f2 = f / ONE_SECOND;
        this.mMassState.mValue = getX(f2);
        this.mMassState.mVelocity = getDX(f2);
        return this.mMassState;
    }
}
