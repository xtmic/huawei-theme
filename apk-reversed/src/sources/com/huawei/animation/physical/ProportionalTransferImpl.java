package com.huawei.animation.physical;

import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class ProportionalTransferImpl implements IParamTransfer<Float> {
    private static final float DEFAULT_K = 0.0f;
    private float kVal;

    public ProportionalTransferImpl() {
        this.kVal = 0.0f;
    }

    public ProportionalTransferImpl(float f) {
        this.kVal = f;
    }

    public float getK() {
        return this.kVal;
    }

    public void setK(float f) {
        this.kVal = f;
    }

    @Override // com.huawei.animation.physical.IParamTransfer
    public Float transfer(Float f, int i) {
        return Float.valueOf(f.floatValue() - (this.kVal * i));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && Float.compare(((ProportionalTransferImpl) obj).kVal, this.kVal) == 0;
    }

    public int hashCode() {
        return Objects.hash(Float.valueOf(this.kVal));
    }
}
