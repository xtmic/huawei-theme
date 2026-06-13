package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.OscarDynamicAnimation;
import com.huawei.animation.physical.GravityModelBase;

/* JADX INFO: loaded from: classes.dex */
public class GravityModel extends GravityModelBase {
    private final OscarDynamicAnimation.MassState mMassState;
    private float mTotalT;

    public GravityModel(float f, float f2) {
        super(f, f2);
        this.mMassState = new OscarDynamicAnimation.MassState();
        this.mTotalT = 0.0f;
    }

    public OscarDynamicAnimation.MassState updateValues(double d, double d2, long j) {
        float f = this.mTotalT + j;
        this.mTotalT = f;
        float f2 = (float) (((double) f) / 1000.0d);
        this.mMassState.mValue = getX(f2);
        OscarDynamicAnimation.MassState massState = this.mMassState;
        massState.mVelocity = (float) Math.abs((((double) massState.mValue) - d) / ((double) f2));
        return this.mMassState;
    }
}
