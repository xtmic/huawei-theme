package com.huawei.superwallpaper.engine.util;

import android.content.Context;
import android.view.SceneUtil;

/* JADX INFO: loaded from: classes.dex */
public class RefreshRateUtil {
    private static final String SCENE = "UNLOCK_INTO_LAUNCHER";
    private static final String TAG = "RefreshRateUtil";

    public static void setRefreshRate(Context context, int i) {
        LogUtil.i(TAG, "setRefreshRate state = " + i, new Object[0]);
        SceneUtil.notifyAnimationState(SCENE, i, -1, context.getApplicationContext().getPackageName().hashCode());
    }
}
