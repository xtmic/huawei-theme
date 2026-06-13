package com.huawei.superwallpaper.engine.math;

/* JADX INFO: loaded from: classes.dex */
public class Matrix {
    public static void multiplyMM(double[] dArr, int i, double[] dArr2, int i2, double[] dArr3, int i3) {
        String str;
        if (dArr == null) {
            str = "Result matrix can not be null.";
        } else if (dArr2 == null) {
            str = "Left hand side matrix can not be null.";
        } else if (dArr3 == null) {
            str = "Right hand side matrix can not be null.";
        } else if (i + 16 > dArr.length) {
            str = "Specified result offset would overflow the passed result matrix.";
        } else if (i2 + 16 > dArr2.length) {
            str = "Specified left hand side offset would overflow the passed lhs matrix.";
        } else {
            str = i3 + 16 > dArr3.length ? "Specified right hand side offset would overflow the passed rhs matrix." : null;
        }
        if (str != null) {
            throw new IllegalArgumentException(str);
        }
        for (int i4 = 0; i4 < 4; i4++) {
            for (int i5 = 0; i5 < 4; i5++) {
                double d = 0.0d;
                for (int i6 = 0; i6 < 4; i6++) {
                    d += dArr2[(i6 * 4) + i4 + i2] * dArr3[(i5 * 4) + i6 + i3];
                }
                dArr[(i5 * 4) + i4 + i] = d;
            }
        }
    }

    public static double length(double d, double d2, double d3) {
        return Math.sqrt((d * d) + (d2 * d2) + (d3 * d3));
    }
}
