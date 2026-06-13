package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.OscarDynamicAnimation;

/* JADX INFO: loaded from: classes.dex */
public class HWSpringAnimation extends OscarDynamicAnimation<HWSpringAnimation> {
    private float mEndValue;
    private SpringModel mSpringModel;
    private float mStartValue;

    float getAcceleration(float f, float f2) {
        return 0.0f;
    }

    public HWSpringAnimation(OscarFloatValueHolder oscarFloatValueHolder) {
        super(oscarFloatValueHolder);
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
    }

    public <K> HWSpringAnimation(K k, OscarFloatPropertyCompat<K> oscarFloatPropertyCompat) {
        super(k, oscarFloatPropertyCompat);
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
    }

    public <K> HWSpringAnimation(K k, OscarFloatPropertyCompat<K> oscarFloatPropertyCompat, SpringModel springModel) {
        super(k, oscarFloatPropertyCompat);
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
        this.mSpringModel = springModel;
    }

    public <K> HWSpringAnimation(K k, OscarFloatPropertyCompat<K> oscarFloatPropertyCompat, float f, float f2, float f3, float f4) {
        super(k, oscarFloatPropertyCompat);
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
        setStartVelocity(f4);
        this.mEndValue = f3;
        this.mStartValue = oscarFloatPropertyCompat.getValue(k);
        SpringModel springModel = new SpringModel(f, f2);
        this.mSpringModel = springModel;
        springModel.setValueThreshold(getValueThreshold()).snap(0.0f).setEndPosition(f3 - this.mStartValue, f4, -1L);
    }

    public <K> HWSpringAnimation(K k, OscarFloatPropertyCompat<K> oscarFloatPropertyCompat, float f, float f2, float f3, float f4, float f5) {
        super(k, oscarFloatPropertyCompat);
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

    public <K> HWSpringAnimation(OscarFloatValueHolder oscarFloatValueHolder, float f, float f2, float f3, float f4) {
        super(oscarFloatValueHolder);
        this.mStartValue = 0.0f;
        this.mEndValue = 0.0f;
        setStartVelocity(f4);
        this.mEndValue = f3;
        this.mStartValue = oscarFloatValueHolder.getValue();
        SpringModel springModel = new SpringModel(f, f2);
        this.mSpringModel = springModel;
        springModel.setValueThreshold(Math.abs(f3 - oscarFloatValueHolder.getValue()) * 0.001f);
        this.mSpringModel.snap(0.0f);
        this.mSpringModel.setEndPosition(f3 - this.mStartValue, f4, -1L);
    }

    boolean updateValueAndVelocity(long j) {
        OscarDynamicAnimation.MassState massStateUpdateValues = this.mSpringModel.updateValues(j);
        this.mValue = massStateUpdateValues.mValue + this.mStartValue;
        this.mVelocity = massStateUpdateValues.mVelocity;
        if (!isAtEquilibrium(this.mValue - this.mStartValue, this.mVelocity)) {
            return false;
        }
        this.mValue = this.mSpringModel.getEndPosition() + this.mStartValue;
        this.mVelocity = 0.0f;
        return true;
    }

    void setValueThreshold(float f) {
        this.mSpringModel.setValueThreshold(f);
    }

    public void setSpringModel(SpringModel springModel) {
        this.mSpringModel = springModel;
    }

    public void start() {
        super.start();
    }

    boolean isAtEquilibrium(float f, float f2) {
        return this.mSpringModel.isAtEquilibrium(f, f2);
    }

    /* JADX INFO: renamed from: setStartValue, reason: merged with bridge method [inline-methods] */
    public HWSpringAnimation m4setStartValue(float f) {
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
}
