package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.OscarDynamicAnimation;
import com.huawei.animation.physical.SpringModelBase;

/* JADX INFO: loaded from: classes.dex */
public class SpringModel extends SpringModelBase {
    private final OscarDynamicAnimation.MassState mMassState;
    private float mTotalT;

    public SpringModel(float f, float f2) {
        super(f, f2);
        this.mTotalT = 0.0f;
        this.mTotalT = 0.0f;
        this.mMassState = new OscarDynamicAnimation.MassState();
    }

    public OscarDynamicAnimation.MassState updateValues(long j) {
        float f = this.mTotalT + j;
        this.mTotalT = f;
        float f2 = f / 1000.0f;
        this.mMassState.mValue = getX(f2);
        this.mMassState.mVelocity = getDX(f2);
        return this.mMassState;
    }
}
