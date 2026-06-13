package com.huawei.animation.physical;

import com.huawei.animation.physical.DynamicAnimation;

/* JADX INFO: loaded from: classes.dex */
public abstract class ChainListener implements DynamicAnimation.OnAnimationUpdateListener {
    @Override // com.huawei.animation.physical.DynamicAnimation.OnAnimationUpdateListener
    public abstract void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2);
}
