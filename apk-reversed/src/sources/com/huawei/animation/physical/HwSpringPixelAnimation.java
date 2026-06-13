package com.huawei.animation.physical;

import com.huawei.animation.physical.DynamicAnimation;

/* JADX INFO: loaded from: classes.dex */
public class HwSpringPixelAnimation extends HWSpringAnimation {
    private float mLastTheoryValue;
    private float mValueThreshold;

    public HwSpringPixelAnimation(FloatValueHolder floatValueHolder) {
        super(floatValueHolder);
        this.mLastTheoryValue = 0.0f;
        setValueThreshold(1.0f);
    }

    public <K> HwSpringPixelAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat) {
        super(k, floatPropertyCompat);
        this.mLastTheoryValue = 0.0f;
        setValueThreshold(1.0f);
    }

    public <K> HwSpringPixelAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat, SpringModel springModel) {
        super(k, floatPropertyCompat, springModel);
        this.mLastTheoryValue = 0.0f;
        setValueThreshold(1.0f);
    }

    public <K> HwSpringPixelAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat, float f, float f2, float f3, float f4) {
        super(k, floatPropertyCompat, f, f2, f3, f4);
        this.mLastTheoryValue = 0.0f;
        setValueThreshold(1.0f);
    }

    public <K> HwSpringPixelAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat, float f, float f2, float f3, float f4, float f5) {
        super(k, floatPropertyCompat, f, f2, f3, f4, f5);
        this.mLastTheoryValue = 0.0f;
        setValueThreshold(1.0f);
    }

    public <K> HwSpringPixelAnimation(FloatValueHolder floatValueHolder, float f, float f2, float f3, float f4) {
        super(floatValueHolder, f, f2, f3, f4);
        this.mLastTheoryValue = 0.0f;
        setValueThreshold(1.0f);
    }

    @Override // com.huawei.animation.physical.HWSpringAnimation
    public HwSpringPixelAnimation reset() {
        super.reset();
        this.mLastTheoryValue = Float.MAX_VALUE;
        setValueThreshold(1.0f);
        return this;
    }

    @Override // com.huawei.animation.physical.HWSpringAnimation, com.huawei.animation.physical.DynamicAnimation
    boolean updateValueAndVelocity(long j) {
        if (this.mPendingPosition != Float.MAX_VALUE) {
            return onPendingUpdate(j);
        }
        return onUpdate(j);
    }

    @Override // com.huawei.animation.physical.HWSpringAnimation, com.huawei.animation.physical.DynamicAnimation
    public void start() {
        super.start();
        this.mLastTheoryValue = this.mValue;
    }

    private boolean onPendingUpdate(long j) {
        this.mEndValue = this.mPendingPosition;
        setStartVelocity(this.mVelocity);
        this.mStartValue = this.mProperty.getValue(this.mTarget);
        this.mSpringModel.setEndValue(this.mEndValue - this.mStartValue, this.mVelocity);
        DynamicAnimation.MassState massStateUpdateValues = this.mSpringModel.updateValues((j * 7) / 24);
        this.mValue = getFixedValue(massStateUpdateValues.mValue);
        this.mVelocity = massStateUpdateValues.mVelocity;
        this.mPendingPosition = Float.MAX_VALUE;
        return false;
    }

    private boolean onUpdate(long j) {
        DynamicAnimation.MassState massStateUpdateValues = getSpringModel().updateValues(j);
        float startValue = massStateUpdateValues.mValue + getStartValue();
        this.mValue = getFixedValue(massStateUpdateValues.mValue);
        this.mVelocity = massStateUpdateValues.mVelocity;
        if (!isAtEquilibrium(this.mValue, this.mVelocity) && !isAtEquilibrium(startValue, this.mVelocity)) {
            return false;
        }
        this.mValue = getSpringModel().getEndPosition() + getStartValue();
        this.mVelocity = 0.0f;
        return true;
    }

    @Override // com.huawei.animation.physical.HWSpringAnimation, com.huawei.animation.physical.DynamicAnimation
    public void setValueThreshold(float f) {
        this.mValueThreshold = f;
    }

    @Override // com.huawei.animation.physical.HWSpringAnimation, com.huawei.animation.physical.DynamicAnimation
    boolean isAtEquilibrium(float f, float f2) {
        return ((double) Math.abs(f2)) < 62.5d && Math.abs(f - this.mEndValue) < this.mValueThreshold;
    }

    private float getFixedValue(float f) {
        float f2;
        float fSignum;
        float startValue = f + getStartValue();
        float f3 = startValue - this.mLastTheoryValue;
        this.mLastTheoryValue = startValue;
        float fAbs = Math.abs(this.mValue - this.mEndValue);
        if (Math.abs(fAbs) > 3.0f) {
            if (Math.abs(f3) >= 1.0f) {
                return startValue;
            }
            f2 = this.mValue;
            fSignum = Math.signum(f3) * 1.0f;
        } else {
            if (Math.abs(f3) > 1.0f) {
                return startValue;
            }
            float f4 = fAbs % this.mValueThreshold;
            f2 = this.mValue;
            fSignum = Math.signum(f3) * this.mValueThreshold * (f4 + 1.0f);
        }
        return f2 + fSignum;
    }
}
