package com.huawei.animation.physical.interpolator;

import android.view.animation.Interpolator;
import com.huawei.animation.physical.GravityModelBase;

/* JADX INFO: loaded from: classes.dex */
public class GravityInterpolator implements Interpolator {
    private GravityModelBase gravityModel;
    private float width;

    public GravityInterpolator(GravityModelBase gravityModelBase, float f) {
        this.gravityModel = gravityModelBase;
        this.width = f;
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f) {
        return this.gravityModel.getX(f) / this.width;
    }
}
