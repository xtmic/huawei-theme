package com.huawei.animation.physical;

import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class ParamsTransferImpl implements IParamTransfer<Float> {
    public static final float CURVE_COEFFICIENT = 0.18f;
    private static final float DEFAULT_K = 0.0f;
    private float kVal;

    public ParamsTransferImpl() {
        this(0.0f);
    }

    public ParamsTransferImpl(float f) {
        this.kVal = f;
    }

    public float getK() {
        return this.kVal;
    }

    public ParamsTransferImpl setK(float f) {
        this.kVal = f;
        return this;
    }

    @Override // com.huawei.animation.physical.IParamTransfer
    public Float transfer(Float f, int i) {
        return Float.valueOf(((float) Math.pow(i + 1, (-this.kVal) * 0.18f)) * f.floatValue());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && Float.compare(((ParamsTransferImpl) obj).kVal, this.kVal) == 0;
    }

    public int hashCode() {
        return Objects.hash(Float.valueOf(this.kVal));
    }
}
