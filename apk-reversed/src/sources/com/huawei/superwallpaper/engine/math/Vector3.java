package com.huawei.superwallpaper.engine.math;

/* JADX INFO: loaded from: classes.dex */
public class Vector3 implements Cloneable {
    public static final Vector3 X = new Vector3(1.0d, 0.0d, 0.0d);
    public static final Vector3 Y = new Vector3(0.0d, 1.0d, 0.0d);
    public static final Vector3 Z = new Vector3(0.0d, 0.0d, 1.0d);
    public double x;
    public double y;
    public double z;

    public enum Axis {
        X,
        Y,
        Z
    }

    public Vector3() {
        this.x = 0.0d;
        this.y = 0.0d;
        this.z = 0.0d;
    }

    public Vector3(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    public Vector3 setAll(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
        return this;
    }

    public void setAll(Vector3 vector3) {
        this.x = vector3.x;
        this.y = vector3.y;
        this.z = vector3.z;
    }

    public void normalize() {
        double d = this.x;
        double d2 = this.y;
        double d3 = (d * d) + (d2 * d2);
        double d4 = this.z;
        double dSqrt = Math.sqrt(d3 + (d4 * d4));
        if (dSqrt == 0.0d || dSqrt == 1.0d) {
            return;
        }
        double d5 = 1.0d / dSqrt;
        this.x *= d5;
        this.y *= d5;
        this.z *= d5;
    }

    public double length() {
        double d = this.x;
        double d2 = this.y;
        double d3 = (d * d) + (d2 * d2);
        double d4 = this.z;
        return d3 + (d4 * d4);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Vector3 m7clone() {
        return new Vector3(this.x, this.y, this.z);
    }

    public boolean isUnit() {
        return isUnit(1.0E-8d);
    }

    public boolean isUnit(double d) {
        return Math.abs(length() - 1.0d) < d * d;
    }

    public boolean isZero() {
        return this.x == 0.0d && this.y == 0.0d && this.z == 0.0d;
    }

    /* JADX INFO: renamed from: com.huawei.superwallpaper.engine.math.Vector3$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$huawei$superwallpaper$engine$math$Vector3$Axis;

        static {
            int[] iArr = new int[Axis.values().length];
            $SwitchMap$com$huawei$superwallpaper$engine$math$Vector3$Axis = iArr;
            try {
                iArr[Axis.X.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$huawei$superwallpaper$engine$math$Vector3$Axis[Axis.Y.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$huawei$superwallpaper$engine$math$Vector3$Axis[Axis.Z.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static Vector3 getAxisVector(Axis axis) {
        int i = AnonymousClass1.$SwitchMap$com$huawei$superwallpaper$engine$math$Vector3$Axis[axis.ordinal()];
        if (i == 1) {
            return X;
        }
        if (i == 2) {
            return Y;
        }
        if (i == 3) {
            return Z;
        }
        throw new IllegalArgumentException("The specified Axis is not a valid choice.");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Vector3 vector3 = (Vector3) obj;
        return vector3.x == this.x && vector3.y == this.y && vector3.z == this.z;
    }

    public boolean equals(Vector3 vector3, double d) {
        return Math.abs(vector3.x - this.x) <= d && Math.abs(vector3.y - this.y) <= d && Math.abs(vector3.z - this.z) <= d;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Vector3 <x, y, z>: <");
        stringBuffer.append(this.x);
        stringBuffer.append(", ");
        stringBuffer.append(this.y);
        stringBuffer.append(", ");
        stringBuffer.append(this.z);
        stringBuffer.append(">");
        return stringBuffer.toString();
    }
}
