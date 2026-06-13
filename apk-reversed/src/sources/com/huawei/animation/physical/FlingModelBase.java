package com.huawei.animation.physical;

import android.util.Log;
import com.huawei.animation.physical.util.Utils;

/* JADX INFO: loaded from: classes.dex */
public class FlingModelBase extends PhysicalModelBase {
    private static final float DEFAULT_VALUE_THRESHOLD = 0.75f;
    private static final float FRICTION_SCALE = -4.2f;
    private static final int ONR_SECOND = 1000;
    private static final String TAG = FlingModelBase.class.getSimpleName();
    private float currentT;
    private float estimateTime;
    private float estimateValue;
    private float initVelocity;
    private boolean isDirty;
    private float mFriction;
    private float signum;

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDDX() {
        return 0.0f;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDDX(float f) {
        return 0.0f;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium(float f) {
        return false;
    }

    public FlingModelBase(float f, float f2) {
        this(f, f2, DEFAULT_VALUE_THRESHOLD);
    }

    public FlingModelBase(float f, float f2, float f3) {
        this.currentT = 0.0f;
        this.isDirty = true;
        super.setValueThreshold(f3);
        setInitVelocity(f);
        setFriction(f2);
    }

    private void reset() {
        if (this.isDirty) {
            sanityCheck();
            float fLog = ((float) (Math.log(this.mVelocityThreshold / this.initVelocity) / ((double) this.mFriction))) * 1000.0f;
            this.estimateTime = fLog;
            float fMax = Math.max(fLog, 0.0f);
            this.estimateTime = fMax;
            this.estimateValue = getX(fMax / 1000.0f);
            this.isDirty = false;
            Log.i(TAG, "reset: estimateTime=" + this.estimateTime + ",estimateValue=" + this.estimateValue);
        }
    }

    public void sanityCheck() {
        if (Utils.isFloatZero(this.initVelocity)) {
            throw new UnsupportedOperationException("InitVelocity should be set and can not be 0!!");
        }
        if (Utils.isFloatZero(this.mFriction)) {
            throw new UnsupportedOperationException("Friction should be set and can not be 0!!");
        }
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getX(float f) {
        this.currentT = f;
        return this.signum * ((float) (((double) (this.initVelocity / this.mFriction)) * (Math.exp(r2 * f) - 1.0d)));
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getX() {
        return getX(this.currentT);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDX(float f) {
        return this.signum * ((float) (((double) this.initVelocity) * Math.exp(this.mFriction * f)));
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDX() {
        return getDX(this.currentT);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium() {
        return this.initVelocity < this.mVelocityThreshold;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium(float f, float f2) {
        return Math.abs(f - getEndPosition()) < this.mValueThreshold && Math.abs(f2) < this.mVelocityThreshold;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getEstimatedDuration() {
        reset();
        return this.estimateTime;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getEndPosition() {
        reset();
        return this.estimateValue;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public final PhysicalModelBase setValueThreshold(float f) {
        super.setValueThreshold(f);
        this.isDirty = true;
        return this;
    }

    public final <T extends PhysicalModelBase> T setInitVelocity(float f) {
        this.initVelocity = Math.abs(f);
        this.signum = Math.signum(f);
        this.isDirty = true;
        return this;
    }

    public final <T extends PhysicalModelBase> T setFriction(float f) {
        this.mFriction = f * FRICTION_SCALE;
        this.isDirty = true;
        return this;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getMaxAbsX() {
        reset();
        return this.estimateValue;
    }
}
