package com.huawei.animation.physical.interpolator;

import android.view.animation.Interpolator;
import com.huawei.animation.physical.GravityWithBounceModelBase;

/* JADX INFO: loaded from: classes.dex */
public class GravityWithBounceInterpolator implements Interpolator {
    private GravityWithBounceModelBase bounceModel;
    private float width;

    public GravityWithBounceInterpolator(GravityWithBounceModelBase gravityWithBounceModelBase, float f) {
        this.bounceModel = gravityWithBounceModelBase;
        this.width = f;
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f) {
        return this.bounceModel.getX(f) / this.width;
    }
}
