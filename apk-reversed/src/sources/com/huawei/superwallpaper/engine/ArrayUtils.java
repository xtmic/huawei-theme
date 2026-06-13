package com.huawei.superwallpaper.engine;

/* JADX INFO: loaded from: classes.dex */
public class ArrayUtils {
    public static float[] convertDoublesToFloats(double[] dArr) {
        float[] fArr = new float[16];
        if (dArr == null) {
            return null;
        }
        for (int i = 0; i < dArr.length; i++) {
            fArr[i] = (float) dArr[i];
        }
        return fArr;
    }
}
