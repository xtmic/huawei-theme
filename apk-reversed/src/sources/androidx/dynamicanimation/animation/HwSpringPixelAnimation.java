package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.OscarDynamicAnimation;

/* JADX INFO: loaded from: classes.dex */
public class HwSpringPixelAnimation extends HWSpringAnimation {
    private float lastValue;

    public HwSpringPixelAnimation(OscarFloatValueHolder oscarFloatValueHolder) {
        super(oscarFloatValueHolder);
        this.lastValue = 0.0f;
    }

    public <K> HwSpringPixelAnimation(K k, OscarFloatPropertyCompat<K> oscarFloatPropertyCompat) {
        super(k, oscarFloatPropertyCompat);
        this.lastValue = 0.0f;
    }

    public <K> HwSpringPixelAnimation(K k, OscarFloatPropertyCompat<K> oscarFloatPropertyCompat, SpringModel springModel) {
        super(k, oscarFloatPropertyCompat, springModel);
        this.lastValue = 0.0f;
    }

    public <K> HwSpringPixelAnimation(K k, OscarFloatPropertyCompat<K> oscarFloatPropertyCompat, float f, float f2, float f3, float f4) {
        super(k, oscarFloatPropertyCompat, f, f2, f3, f4);
        this.lastValue = 0.0f;
    }

    public <K> HwSpringPixelAnimation(K k, OscarFloatPropertyCompat<K> oscarFloatPropertyCompat, float f, float f2, float f3, float f4, float f5) {
        super(k, oscarFloatPropertyCompat, f, f2, f3, f4, f5);
        this.lastValue = 0.0f;
    }

    public <K> HwSpringPixelAnimation(OscarFloatValueHolder oscarFloatValueHolder, float f, float f2, float f3, float f4) {
        super(oscarFloatValueHolder, f, f2, f3, f4);
        this.lastValue = 0.0f;
    }

    @Override // androidx.dynamicanimation.animation.HWSpringAnimation
    boolean updateValueAndVelocity(long j) {
        OscarDynamicAnimation.MassState massStateUpdateValues = getSpringModel().updateValues(j);
        float startValue = massStateUpdateValues.mValue + getStartValue();
        float f = startValue - this.lastValue;
        if (Math.abs(f) >= 1.0f) {
            this.mValue = startValue;
        } else {
            this.mValue += Math.signum(f) * 1.0f;
        }
        this.lastValue = startValue;
        this.mVelocity = massStateUpdateValues.mVelocity;
        if (!isAtEquilibrium(this.mValue - getStartValue(), this.mVelocity) && !isAtEquilibrium(startValue - getStartValue(), this.mVelocity)) {
            return false;
        }
        this.mValue = getSpringModel().getEndPosition() + getStartValue();
        this.mVelocity = 0.0f;
        return true;
    }
}
