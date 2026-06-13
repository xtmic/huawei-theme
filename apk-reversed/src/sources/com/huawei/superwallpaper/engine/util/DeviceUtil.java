package com.huawei.superwallpaper.engine.util;

import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public class DeviceUtil {
    public static boolean isKirin() {
        return Build.HARDWARE.matches("kirin");
    }

    public static boolean isQcom() {
        return Build.HARDWARE.matches("qcom");
    }
}
