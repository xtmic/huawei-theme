package com.huawei.animation.physical;

import android.os.SystemClock;

/* JADX INFO: loaded from: classes.dex */
public class GravityModelBase extends PhysicalModelBase {
    protected float mA;
    protected float mAcceleration;
    protected float mTerminate;
    private float mValueThreshold;
    private float mVelocityThreshold;
    protected float mX = 0.0f;
    protected float mV = 0.0f;

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDDX() {
        return 0.0f;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDDX(float f) {
        return 0.0f;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getEstimatedDuration() {
        return 0.0f;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getMaxAbsX() {
        return 0.0f;
    }

    public GravityModelBase(float f, float f2) {
        this.mAcceleration = f;
        this.mTerminate = f2;
        this.mA = f;
        this.mStartTime = 0L;
    }

    public void setXV(float f, float f2) {
        this.mX = f;
        this.mV = f2;
        this.mStartTime = SystemClock.elapsedRealtime();
    }

    public void reconfigure(float f) {
        setXV(getX(), getDX());
        this.mA = f;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getX(float f) {
        float fElapsedRealtime = SystemClock.elapsedRealtime();
        if (f < 0.0f) {
            f = (fElapsedRealtime - this.mStartTime) / 1000.0f;
        }
        double d = f;
        return (float) (((double) (this.mX + (this.mV * f))) + (((double) this.mA) * 0.5d * d * d));
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getX() {
        return getX(-1.0f);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDX(float f) {
        if (f < 0.0f) {
            f = (SystemClock.elapsedRealtime() - this.mStartTime) / 1000.0f;
        }
        return this.mV + (f * this.mA);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDX() {
        return getDX(-1.0f);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium(float f) {
        return isAtEquilibrium();
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium() {
        return Math.abs(getX()) > this.mTerminate;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium(float f, float f2) {
        return ((double) Math.abs(f2)) < ((double) this.mVelocityThreshold) && ((double) Math.abs(f - this.mEndPosition)) < ((double) this.mValueThreshold);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public GravityModelBase setValueThreshold(float f) {
        float fAbs = Math.abs(f) / 100.0f;
        this.mValueThreshold = fAbs;
        this.mVelocityThreshold = fAbs * 62.5f;
        return this;
    }
}
