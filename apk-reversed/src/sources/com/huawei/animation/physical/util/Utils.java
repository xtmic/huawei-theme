package com.huawei.animation.physical.util;

/* JADX INFO: loaded from: classes.dex */
public class Utils {
    private static final float FLOAT_ZERO = 1.0E-5f;

    private Utils() {
    }

    public static boolean isFloatZero(float f) {
        return Math.abs(f) < FLOAT_ZERO;
    }
}
