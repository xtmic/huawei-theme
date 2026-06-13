package com.huawei.animation.physical;

/* JADX INFO: loaded from: classes.dex */
public abstract class PhysicalModelBase implements IPhysicalModel {
    public static final float DEFAULT_END_POSITION0 = 0.0f;
    public static final float DEFAULT_END_POSITION1 = 1.0f;
    public static final float DEFAULT_INITIAL_VELOCITY = 0.0f;
    public static final float MAXIMUM_END_POSITION0 = 0.0f;
    public static final float MAXIMUM_END_POSITION1 = 99999.0f;
    public static final float MAXIMUM_INITIAL_VELOCITY = 99999.0f;
    public static final float MINIMUM_END_POSITION0 = 0.0f;
    public static final float MINIMUM_END_POSITION1 = -99999.0f;
    public static final float MINIMUM_INITIAL_VELOCITY = -99999.0f;
    protected static final float VELOCITY_THRESHOLD_MULTIPLIER = 62.5f;
    protected float mStartPosition = 0.0f;
    protected float mEndPosition = 0.0f;
    protected long mStartTime = 0;
    protected float mStartVelocity = 0.0f;
    protected float mValueThreshold = Float.MIN_VALUE;
    protected float mVelocityThreshold = Float.MIN_VALUE;

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract float getDDX();

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract float getDDX(float f);

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract float getDX();

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract float getDX(float f);

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract float getEstimatedDuration();

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract float getMaxAbsX();

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract float getX();

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract float getX(float f);

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract boolean isAtEquilibrium();

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract boolean isAtEquilibrium(float f);

    @Override // com.huawei.animation.physical.IPhysicalModel
    public abstract boolean isAtEquilibrium(float f, float f2);

    @Override // com.huawei.animation.physical.IPhysicalModel
    public PhysicalModelBase setValueThreshold(float f) {
        float fAbs = Math.abs(f);
        this.mValueThreshold = fAbs;
        this.mVelocityThreshold = fAbs * 62.5f;
        return this;
    }

    @Override // com.huawei.animation.physical.IPhysicalModel
    public float getEndPosition() {
        return this.mEndPosition;
    }

    @Override // com.huawei.animation.physical.IPhysicalModel
    public PhysicalModelBase setEndPosition(float f) {
        this.mEndPosition = f;
        return this;
    }

    @Override // com.huawei.animation.physical.IPhysicalModel
    public float getStartTime() {
        return this.mStartTime;
    }

    @Override // com.huawei.animation.physical.IPhysicalModel
    public float getStartPosition() {
        return this.mStartPosition;
    }

    @Override // com.huawei.animation.physical.IPhysicalModel
    public float getStartVelocity() {
        return this.mStartVelocity;
    }
}
