package com.huawei.animation.physical;

import com.huawei.animation.physical.DynamicAnimation;

/* JADX INFO: loaded from: classes.dex */
public class HWSpringAnimation extends DynamicAnimation<HWSpringAnimation> {
    private static final String TAG = "HWSpringAnimation";
    static final float UNSET = Float.MAX_VALUE;
    static final int USE_CURRENT_TIME = -1;
    float mEndValue;
    float mPendingPosition;
    SpringModel mSpringModel;
    float mStartValue;

    @Override // com.huawei.animation.physical.DynamicAnimation
    float getAcceleration(float f, float f2) {
        return 0.0f;
    }

    public HWSpringAnimation(FloatValueHolder floatValueHolder) {
        super(floatValueHolder);
        this.mPendingPosition = Float.MAX_VALUE;
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
    }

    public <K> HWSpringAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat) {
        super(k, floatPropertyCompat);
        this.mPendingPosition = Float.MAX_VALUE;
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
    }

    public <K> HWSpringAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat, SpringModel springModel) {
        super(k, floatPropertyCompat);
        this.mPendingPosition = Float.MAX_VALUE;
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
        this.mSpringModel = springModel;
        this.mStartValue = floatPropertyCompat.getValue(k);
        this.mSpringModel.setValueThreshold(getValueThreshold()).snap(0.0f);
    }

    public <K> HWSpringAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat, float f, float f2, float f3, float f4) {
        super(k, floatPropertyCompat);
        this.mPendingPosition = Float.MAX_VALUE;
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
        setStartVelocity(f4);
        this.mEndValue = f3;
        this.mStartValue = floatPropertyCompat.getValue(k);
        SpringModel springModel = new SpringModel(f, f2);
        this.mSpringModel = springModel;
        springModel.setValueThreshold(getValueThreshold()).snap(0.0f).setEndPosition(f3 - this.mStartValue, f4, -1L);
    }

    public HWSpringAnimation reset() {
        this.mTarget = null;
        this.mProperty = null;
        setStartVelocity(0.0f);
        this.mEndValue = 0.0f;
        this.mStartValue = 0.0f;
        this.mSpringModel.reset().snap(0.0f).setEndPosition(1.0f, 0.0f, -1L);
        AnimationHandler.getInstance().removeCallback(this);
        return (HWSpringAnimation) super.clearListeners();
    }

    public <K> HWSpringAnimation setObj(K k, FloatPropertyCompat<K> floatPropertyCompat, float f, float f2, float f3, float f4) {
        super.setObj(k, floatPropertyCompat);
        setStartVelocity(f4);
        this.mEndValue = f3;
        if (this.mTarget == null) {
            if (this.mProperty == null) {
                final FloatValueHolder floatValueHolder = new FloatValueHolder(0.0f);
                this.mProperty = new FloatPropertyCompat("FloatValueHolder") { // from class: com.huawei.animation.physical.HWSpringAnimation.1
                    @Override // com.huawei.animation.physical.FloatPropertyCompat
                    public float getValue(Object obj) {
                        return floatValueHolder.getValue();
                    }

                    @Override // com.huawei.animation.physical.FloatPropertyCompat
                    public void setValue(Object obj, float f5) {
                        floatValueHolder.setValue(f5);
                    }
                };
            } else {
                this.mProperty.setValue(this.mTarget, 0.0f);
            }
            this.mStartValue = 0.0f;
        } else {
            this.mStartValue = this.mProperty.getValue(this.mTarget);
        }
        this.mSpringModel.reset().setStiffness(f).setDamping(f2).snap(0.0f).setEndPosition(f3 - this.mStartValue, f4, -1L);
        return this;
    }

    public HWSpringAnimation endToPosition(float f, float f2) {
        if (this.mChainValueListener != null) {
            this.mChainValueListener.onChainValue(this, f, f2, true);
        }
        if (isRunning()) {
            this.mPendingPosition = f;
            updatePending(16L);
        } else {
            this.mPendingPosition = Float.MAX_VALUE;
            if (!this.mStartValueIsSet) {
                this.mStartValue = this.mProperty.getValue(this.mTarget);
            }
            setStartVelocity(f2);
            this.mEndValue = f;
            getSpringModel().reset().snap(0.0f).setEndPosition(this.mEndValue - this.mStartValue, f2, -1L);
            startImmediately();
        }
        return this;
    }

    public <K> HWSpringAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat, float f, float f2, float f3, float f4, float f5) {
        super(k, floatPropertyCompat);
        this.mPendingPosition = Float.MAX_VALUE;
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
        super.setStartValue(f3);
        setStartVelocity(f5);
        this.mStartValue = f3;
        this.mEndValue = f4;
        SpringModel springModel = new SpringModel(f, f2);
        this.mSpringModel = springModel;
        springModel.setValueThreshold(getValueThreshold()).snap(0.0f).setEndPosition(f4 - this.mStartValue, f5, -1L);
    }

    public <K> HWSpringAnimation(FloatValueHolder floatValueHolder, float f, float f2, float f3, float f4) {
        super(floatValueHolder);
        this.mPendingPosition = Float.MAX_VALUE;
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
        setStartVelocity(f4);
        this.mEndValue = f3;
        this.mStartValue = floatValueHolder.getValue();
        SpringModel springModel = new SpringModel(f, f2);
        this.mSpringModel = springModel;
        springModel.setValueThreshold(Math.abs(f3 - floatValueHolder.getValue()) * 0.001f);
        this.mSpringModel.snap(0.0f);
        this.mSpringModel.setEndPosition(f3 - this.mStartValue, f4, -1L);
    }

    private void updatePending(long j) {
        DynamicAnimation.MassState massStateUpdateValues = this.mSpringModel.updateValues(j / 2);
        this.mValue = massStateUpdateValues.mValue + this.mStartValue;
        this.mVelocity = massStateUpdateValues.mVelocity;
        this.mEndValue = this.mPendingPosition;
        this.mStartValue = this.mValue;
        this.mSpringModel.reset().setEndValue(this.mEndValue - this.mStartValue, this.mVelocity);
        DynamicAnimation.MassState massStateUpdateValues2 = this.mSpringModel.updateValues(j);
        this.mValue = massStateUpdateValues2.mValue + this.mStartValue;
        this.mVelocity = massStateUpdateValues2.mVelocity;
        this.mPendingPosition = Float.MAX_VALUE;
        setValue(this.mValue);
    }

    @Override // com.huawei.animation.physical.DynamicAnimation
    boolean updateValueAndVelocity(long j) {
        if (this.mPendingPosition != Float.MAX_VALUE) {
            updatePending(j);
            return false;
        }
        DynamicAnimation.MassState massStateUpdateValues = this.mSpringModel.updateValues(j);
        this.mValue = massStateUpdateValues.mValue + this.mStartValue;
        this.mVelocity = massStateUpdateValues.mVelocity;
        if (!isAtEquilibrium(this.mValue - this.mStartValue, this.mVelocity)) {
            return false;
        }
        this.mValue = this.mSpringModel.getEndPosition() + this.mStartValue;
        this.mVelocity = 0.0f;
        return true;
    }

    @Override // com.huawei.animation.physical.DynamicAnimation
    void setValueThreshold(float f) {
        this.mSpringModel.setValueThreshold(f);
    }

    public void setSpringModel(SpringModel springModel) {
        this.mSpringModel = springModel;
    }

    @Override // com.huawei.animation.physical.DynamicAnimation
    public void start() {
        super.start();
    }

    @Override // com.huawei.animation.physical.DynamicAnimation
    boolean isAtEquilibrium(float f, float f2) {
        return this.mSpringModel.isAtEquilibrium(f, f2);
    }

    @Override // com.huawei.animation.physical.DynamicAnimation
    public HWSpringAnimation setStartValue(float f) {
        super.setStartValue(f);
        this.mStartValue = f;
        float startVelocity = this.mSpringModel.getStartVelocity();
        this.mSpringModel.snap(0.0f);
        this.mSpringModel.setEndPosition(this.mEndValue - this.mStartValue, startVelocity, -1L);
        return this;
    }

    public SpringModel getSpringModel() {
        return this.mSpringModel;
    }

    public float getStartValue() {
        return this.mStartValue;
    }

    public float getEndValue() {
        return this.mEndValue;
    }
}
