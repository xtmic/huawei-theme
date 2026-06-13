package com.huawei.superwallpaper.engine.util;

import android.view.Surface;
import android.view.SurfaceHolder;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class SurfaceUtils {
    private static final String TAG = SurfaceUtils.class.getSimpleName();

    public static void setDynamicBufSize(SurfaceHolder surfaceHolder) {
        Surface surface = surfaceHolder.getSurface();
        try {
            Method declaredMethod = surface.getClass().getDeclaredMethod("syncDynamicBufSize", Integer.TYPE, Boolean.TYPE);
            LogUtil.i(TAG, "edit syncDynamicBufSize." + declaredMethod, new Object[0]);
            declaredMethod.invoke(surface, 3, false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i(TAG, "syncDynamicBufSize Exception" + e.toString(), new Object[0]);
        }
    }
}
