package com.huawei.superwallpaper.engine.util;

import android.util.Log;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class LogUtil {
    public static final String LOG_PREFIX = "HWE_";

    public static void d(String str, String str2) {
        Log.d(LOG_PREFIX + str, str2);
    }

    public static void d(String str, String str2, Object... objArr) {
        Log.d(LOG_PREFIX + str, String.format(Locale.CHINA, str2, objArr));
    }

    public static void i(String str, String str2, Object... objArr) {
        Log.i(LOG_PREFIX + str, String.format(Locale.CHINA, str2, objArr));
    }

    public static void w(String str, String str2, Object... objArr) {
        Log.w(LOG_PREFIX + str, String.format(Locale.CHINA, str2, objArr));
    }

    public static void e(String str, String str2) {
        Log.e(LOG_PREFIX + str, str2);
    }

    public static void e(String str, String str2, Object... objArr) {
        Log.e(LOG_PREFIX + str, String.format(Locale.CHINA, str2, objArr));
    }

    public static void e(String str, String str2, Throwable th) {
        Log.e(LOG_PREFIX + str, str2, th);
    }
}
