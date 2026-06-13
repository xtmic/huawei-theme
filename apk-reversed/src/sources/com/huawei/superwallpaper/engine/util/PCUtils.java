package com.huawei.superwallpaper.engine.util;

/* JADX INFO: loaded from: classes.dex */
public class PCUtils {
    public static boolean isInPCCastMode() {
        try {
            Class<?> cls = Class.forName("android.util.HwPCUtils");
            if (cls == null) {
                return false;
            }
            Object objInvoke = cls.getMethod("isInWindowsCastMode", new Class[0]).invoke(cls, new Object[0]);
            if (objInvoke instanceof Boolean) {
                return ((Boolean) objInvoke).booleanValue();
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }
}
