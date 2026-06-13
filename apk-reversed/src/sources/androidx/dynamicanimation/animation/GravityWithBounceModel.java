package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.OscarDynamicAnimation;
import com.huawei.animation.physical.GravityWithBounceModelBase;

/* JADX INFO: loaded from: classes.dex */
public class GravityWithBounceModel extends GravityWithBounceModelBase {
    private final OscarDynamicAnimation.MassState mMassState;
    private float mTotalT;

    public GravityWithBounceModel(float f, float f2) {
        super(f, f2);
        this.mTotalT = 0.0f;
        this.mTotalT = 0.0f;
        this.mMassState = new OscarDynamicAnimation.MassState();
    }

    public OscarDynamicAnimation.MassState updateValues(double d, double d2, long j) {
        float f = this.mTotalT + j;
        this.mTotalT = f;
        float f2 = f / 1000.0f;
        this.mMassState.mValue = getX(f2);
        if (d == 0.0d) {
            this.mMassState.mVelocity = this.mV;
        } else {
            OscarDynamicAnimation.MassState massState = this.mMassState;
            massState.mVelocity = (float) Math.abs((((double) massState.mValue) - d) / ((double) f2));
        }
        return this.mMassState;
    }
}
