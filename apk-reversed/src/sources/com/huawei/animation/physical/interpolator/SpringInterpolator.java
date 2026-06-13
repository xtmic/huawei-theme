package com.huawei.animation.physical.interpolator;

import android.util.Log;
import com.huawei.animation.physical.FloatPropertyCompat;
import com.huawei.animation.physical.FloatValueHolder;
import com.huawei.animation.physical.OutputData;
import com.huawei.animation.physical.PhysicalModelBase;
import com.huawei.animation.physical.SpringModelBase;
import com.huawei.animation.physical.util.Utils;

/* JADX INFO: loaded from: classes.dex */
public class SpringInterpolator extends PhysicalInterpolatorBase<SpringInterpolator> {
    private static final int CURRENT_TIME = -1;
    private static final float ONE_SECOND = 1000.0f;
    private static final String TAG = SpringInterpolator.class.getSimpleName();

    public SpringInterpolator(FloatValueHolder floatValueHolder) {
        super(floatValueHolder, (PhysicalModelBase) null);
        SpringModelBase springModelBase = new SpringModelBase(800.0f, 15.0f, getValueThreshold());
        springModelBase.setValueThreshold(Math.abs(1.0f) * 0.001f);
        springModelBase.snap(0.0f);
        springModelBase.setEndPosition(1.0f, 0.0f, -1L);
        setModel(springModelBase);
    }

    public SpringInterpolator() {
        this(new FloatValueHolder(0.0f));
    }

    public SpringInterpolator(FloatValueHolder floatValueHolder, float f, float f2) {
        super(floatValueHolder, (PhysicalModelBase) null);
        SpringModelBase springModelBase = new SpringModelBase(f, f2, getValueThreshold());
        springModelBase.setValueThreshold(Math.abs(1.0f) * 0.001f);
        springModelBase.snap(0.0f);
        springModelBase.setEndPosition(1.0f, 0.0f, -1L);
        setModel(springModelBase);
    }

    public SpringInterpolator(float f, float f2) {
        this(new FloatValueHolder(0.0f), f, f2);
    }

    public SpringInterpolator(FloatValueHolder floatValueHolder, float f, float f2, float f3) {
        super(floatValueHolder, (PhysicalModelBase) null);
        SpringModelBase springModelBase = new SpringModelBase(f, f2, getValueThreshold());
        springModelBase.setValueThreshold(Math.abs(f3 - 0.0f) * 0.001f);
        springModelBase.snap(0.0f);
        springModelBase.setEndPosition(f3, 0.0f, -1L);
        setModel(springModelBase);
    }

    public SpringInterpolator(float f, float f2, float f3) {
        this(new FloatValueHolder(0.0f), f, f2, f3);
    }

    public SpringInterpolator(FloatValueHolder floatValueHolder, float f, float f2, float f3, float f4) {
        super(floatValueHolder, (PhysicalModelBase) null);
        SpringModelBase springModelBase = new SpringModelBase(f, f2, getValueThreshold());
        springModelBase.setValueThreshold(Math.abs(f3 - 0.0f) * 0.001f);
        springModelBase.snap(0.0f);
        springModelBase.setEndPosition(f3, f4, -1L);
        setModel(springModelBase);
    }

    public SpringInterpolator(float f, float f2, float f3, float f4) {
        this(new FloatValueHolder(0.0f), f, f2, f3, f4);
    }

    public SpringInterpolator(FloatValueHolder floatValueHolder, float f, float f2, float f3, float f4, float f5, float f6) {
        super(floatValueHolder, (PhysicalModelBase) null);
        SpringModelBase springModelBase = new SpringModelBase(f, f2, f3, f6 * 0.75f);
        springModelBase.snap(0.0f);
        springModelBase.setEndPosition(f4, f5, -1L);
        setModel(springModelBase);
    }

    public SpringInterpolator(float f, float f2, float f3, float f4, float f5) {
        this(new FloatValueHolder(0.0f), 1.0f, f, f2, f3, f4, f5);
    }

    public SpringInterpolator(float f, float f2, float f3, float f4, float f5, float f6) {
        this(new FloatValueHolder(0.0f), f, f2, f3, f4, f5, f6);
    }

    public <K> SpringInterpolator(FloatValueHolder floatValueHolder, SpringModelBase springModelBase) {
        super(floatValueHolder, springModelBase);
        setModel(springModelBase);
    }

    public <K> SpringInterpolator(FloatPropertyCompat<K> floatPropertyCompat) {
        super(floatPropertyCompat, (PhysicalModelBase) null);
        SpringModelBase springModelBase = new SpringModelBase(800.0f, 15.0f, getValueThreshold());
        springModelBase.snap(0.0f);
        springModelBase.setEndPosition(1.0f, 0.0f, -1L);
        setModel(springModelBase);
    }

    public <K> SpringInterpolator(FloatPropertyCompat<K> floatPropertyCompat, float f, float f2) {
        super(floatPropertyCompat, (PhysicalModelBase) null);
        SpringModelBase springModelBase = new SpringModelBase(f, f2, getValueThreshold());
        springModelBase.snap(0.0f);
        springModelBase.setEndPosition(1.0f, 0.0f, -1L);
        setModel(springModelBase);
    }

    public <K> SpringInterpolator(FloatPropertyCompat<K> floatPropertyCompat, float f, float f2, float f3) {
        super(floatPropertyCompat, (PhysicalModelBase) null);
        SpringModelBase springModelBase = new SpringModelBase(f, f2, getValueThreshold());
        springModelBase.snap(0.0f);
        springModelBase.setEndPosition(f3, 0.0f, -1L);
        setModel(springModelBase);
    }

    public <K> SpringInterpolator(FloatPropertyCompat<K> floatPropertyCompat, float f, float f2, float f3, float f4) {
        super(floatPropertyCompat, (PhysicalModelBase) null);
        SpringModelBase springModelBase = new SpringModelBase(f, f2, getValueThreshold());
        springModelBase.snap(0.0f);
        springModelBase.setEndPosition(f3, f4, -1L);
        setModel(springModelBase);
    }

    public <K> SpringInterpolator(FloatPropertyCompat<K> floatPropertyCompat, SpringModelBase springModelBase) {
        super(floatPropertyCompat, springModelBase);
        setModel(springModelBase);
    }

    @Override // com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase
    public SpringInterpolator setValueThreshold(float f) {
        PhysicalModelBase model = getModel();
        if (model == null) {
            return this;
        }
        model.setValueThreshold(f * 0.75f);
        return this;
    }

    @Override // com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase
    public float getEndOffset() {
        return getModel().getEndPosition() - getModel().getStartPosition();
    }

    public OutputData getInterpolateData(float f) {
        float duration = (f * getDuration()) / ONE_SECOND;
        return new OutputData(duration, getModel().getX(duration), getModel().getDX(duration), getModel().getDDX(duration));
    }

    @Override // com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase, android.animation.TimeInterpolator
    public float getInterpolation(float f) {
        super.getInterpolation(f);
        if (Float.compare(f, 1.0f) == 0) {
            return 1.0f;
        }
        float duration = (f * getDuration()) / ONE_SECOND;
        float x = getModel().getX(duration);
        if (getModel().isAtEquilibrium(duration)) {
            Log.d(TAG, "done at" + duration + "");
        }
        float fAbs = Math.abs(((SpringModelBase) getModel()).getFirstExtremumX());
        float endPosition = getModel().getEndPosition() - getModel().getStartPosition();
        float f2 = fAbs + endPosition;
        return Utils.isFloatZero(endPosition) ? (x + f2) / f2 : x / endPosition;
    }
}
