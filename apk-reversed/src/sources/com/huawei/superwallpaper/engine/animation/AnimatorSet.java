package com.huawei.superwallpaper.engine.animation;

import com.huawei.superwallpaper.engine.util.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AnimatorSet {
    private static final String TAG = AnimatorSet.class.getSimpleName();
    private final List<Animator> mAnimators = new ArrayList();

    public void addAnimator(Animator animator) {
        this.mAnimators.add(animator);
    }

    public void addAllAnimator(List<Animator> list) {
        this.mAnimators.addAll(list);
    }

    public void clearAnimators() {
        this.mAnimators.clear();
    }

    public List<Animator> getAnimators() {
        return this.mAnimators;
    }

    public boolean step(long j) {
        boolean z = false;
        if (this.mAnimators.isEmpty()) {
            LogUtil.i(TAG, "mAnimators is empty", new Object[0]);
            return false;
        }
        Iterator<Animator> it = this.mAnimators.iterator();
        while (it.hasNext()) {
            if (it.next().step(j)) {
                z = true;
            }
        }
        return z;
    }
}
