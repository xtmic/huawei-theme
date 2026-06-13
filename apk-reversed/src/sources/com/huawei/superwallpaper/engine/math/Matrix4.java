package com.huawei.superwallpaper.engine.math;

import com.huawei.superwallpaper.engine.ArrayUtils;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class Matrix4 implements Cloneable {
    public static final int M00 = 0;
    public static final int M01 = 4;
    public static final int M02 = 8;
    public static final int M03 = 12;
    public static final int M10 = 1;
    public static final int M11 = 5;
    public static final int M12 = 9;
    public static final int M13 = 13;
    public static final int M20 = 2;
    public static final int M21 = 6;
    public static final int M22 = 10;
    public static final int M23 = 14;
    public static final int M30 = 3;
    public static final int M31 = 7;
    public static final int M32 = 11;
    public static final int M33 = 15;
    private final double[] m;
    private final double[] mTmp;

    public Matrix4() {
        this.m = new double[16];
        this.mTmp = new double[16];
        identity();
    }

    public Matrix4(Matrix4 matrix4) {
        this.m = new double[16];
        this.mTmp = new double[16];
        setAll(matrix4);
    }

    public void setAll(Matrix4 matrix4) {
        matrix4.toArray(this.m);
    }

    public void setAll(Vector3 vector3, Vector3 vector32, Quaternion quaternion) {
        double d = quaternion.x * quaternion.x;
        double d2 = quaternion.y * quaternion.y;
        double d3 = quaternion.z * quaternion.z;
        double d4 = (-quaternion.x) * quaternion.y;
        double d5 = (-quaternion.x) * quaternion.z;
        double d6 = quaternion.y * quaternion.z;
        double d7 = (-quaternion.w) * quaternion.x;
        double d8 = quaternion.w * quaternion.y;
        double d9 = quaternion.w * quaternion.z;
        this.m[0] = vector32.x * (1.0d - ((d2 + d3) * 2.0d));
        this.m[1] = vector32.y * 2.0d * (d4 - d9);
        this.m[2] = vector32.z * 2.0d * (d5 + d8);
        double[] dArr = this.m;
        dArr[3] = 0.0d;
        dArr[4] = vector32.x * 2.0d * (d4 + d9);
        this.m[5] = vector32.y * (1.0d - ((d + d3) * 2.0d));
        this.m[6] = vector32.z * 2.0d * (d6 - d7);
        double[] dArr2 = this.m;
        dArr2[7] = 0.0d;
        dArr2[8] = vector32.x * 2.0d * (d5 - d8);
        this.m[9] = vector32.y * 2.0d * (d6 + d7);
        this.m[10] = vector32.z * (1.0d - ((d + d2) * 2.0d));
        double[] dArr3 = this.m;
        dArr3[11] = 0.0d;
        dArr3[12] = vector3.x;
        this.m[13] = vector3.y;
        this.m[14] = vector3.z;
        this.m[15] = 1.0d;
    }

    public void identity() {
        double[] dArr = this.m;
        dArr[0] = 1.0d;
        dArr[1] = 0.0d;
        dArr[2] = 0.0d;
        dArr[3] = 0.0d;
        dArr[4] = 0.0d;
        dArr[5] = 1.0d;
        dArr[6] = 0.0d;
        dArr[7] = 0.0d;
        dArr[8] = 0.0d;
        dArr[9] = 0.0d;
        dArr[10] = 1.0d;
        dArr[11] = 0.0d;
        dArr[12] = 0.0d;
        dArr[13] = 0.0d;
        dArr[14] = 0.0d;
        dArr[15] = 1.0d;
    }

    public Matrix4 leftMultiply(Matrix4 matrix4) {
        System.arraycopy(this.m, 0, this.mTmp, 0, 16);
        Matrix.multiplyMM(this.m, 0, matrix4.getDoubleValues(), 0, this.mTmp, 0);
        return this;
    }

    public Vector3 getTranslation() {
        return getTranslation(new Vector3());
    }

    public Vector3 getTranslation(Vector3 vector3) {
        double[] dArr = this.m;
        return vector3.setAll(dArr[12], dArr[13], dArr[14]);
    }

    public float[] getFloatValues() {
        return ArrayUtils.convertDoublesToFloats(this.m);
    }

    public double[] getDoubleValues() {
        return this.m;
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Matrix4 m5clone() {
        return new Matrix4(this);
    }

    public void toArray(double[] dArr) {
        System.arraycopy(this.m, 0, dArr, 0, 16);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Arrays.equals(this.m, ((Matrix4) obj).m);
    }

    public int hashCode() {
        return Arrays.hashCode(this.m);
    }

    public String toString() {
        return "[\n" + this.m[0] + "|" + this.m[4] + "|" + this.m[8] + "|" + this.m[12] + "]\n[" + this.m[1] + "|" + this.m[5] + "|" + this.m[9] + "|" + this.m[13] + "]\n[" + this.m[2] + "|" + this.m[6] + "|" + this.m[10] + "|" + this.m[14] + "]\n[" + this.m[3] + "|" + this.m[7] + "|" + this.m[11] + "|" + this.m[15] + "]\n";
    }
}
