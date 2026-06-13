package com.huawei.animation.physical.interpolator;

import com.huawei.animation.physical.DynamicAnimation;
import com.huawei.animation.physical.FlingModelBase;
import com.huawei.animation.physical.FloatPropertyCompat;
import com.huawei.animation.physical.FloatValueHolder;
import com.huawei.animation.physical.OutputData;

/* JADX INFO: loaded from: classes.dex */
public class FlingInterpolator extends PhysicalInterpolatorBase<FlingInterpolator> {
    private static final float ONE_SECOND = 1000.0f;

    public FlingInterpolator(FloatValueHolder floatValueHolder, FlingModelBase flingModelBase) {
        super(floatValueHolder, flingModelBase);
        flingModelBase.setValueThreshold(getValueThreshold());
    }

    public FlingInterpolator(float f, float f2) {
        super(DynamicAnimation.X, new FlingModelBase(f, f2));
        ((FlingModelBase) getModel()).setValueThreshold(getValueThreshold());
    }

    public <K> FlingInterpolator(FloatPropertyCompat<K> floatPropertyCompat, float f, float f2) {
        super(floatPropertyCompat, new FlingModelBase(f, f2));
        ((FlingModelBase) getModel()).setValueThreshold(getValueThreshold());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase
    public FlingInterpolator setValueThreshold(float f) {
        getModel().setValueThreshold(f * 0.75f);
        return this;
    }

    @Override // com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase
    protected float getDeltaX() {
        return getEndOffset();
    }

    public OutputData getInterpolateData(float f) {
        float duration = (f * getDuration()) / ONE_SECOND;
        return new OutputData(duration, getModel().getX(duration), getModel().getDX(duration), getModel().getDDX(duration));
    }
}
