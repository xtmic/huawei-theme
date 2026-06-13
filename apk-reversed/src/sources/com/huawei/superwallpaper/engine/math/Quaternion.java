package com.huawei.superwallpaper.engine.math;

import com.huawei.superwallpaper.engine.math.Vector3;

/* JADX INFO: loaded from: classes.dex */
public final class Quaternion implements Cloneable {
    private final Vector3 mTmpVec = new Vector3();
    public double w;
    public double x;
    public double y;
    public double z;

    public Quaternion() {
        identity();
    }

    public Quaternion(double d, double d2, double d3, double d4) {
        setAll(d, d2, d3, d4);
    }

    public void setAll(double d, double d2, double d3, double d4) {
        this.w = d;
        this.x = d2;
        this.y = d3;
        this.z = d4;
    }

    public void fromAngleAxis(Vector3.Axis axis, double d) {
        fromAngleAxis(Vector3.getAxisVector(axis), d);
    }

    public void fromAngleAxis(Vector3 vector3, double d) {
        if (vector3.isZero()) {
            identity();
            return;
        }
        this.mTmpVec.setAll(vector3);
        if (!this.mTmpVec.isUnit()) {
            this.mTmpVec.normalize();
        }
        double dDegreesToRadians = MathUtil.degreesToRadians(d) * 0.5d;
        double dSin = Math.sin(dDegreesToRadians);
        this.w = Math.cos(dDegreesToRadians);
        this.x = this.mTmpVec.x * dSin;
        this.y = this.mTmpVec.y * dSin;
        this.z = dSin * this.mTmpVec.z;
    }

    public void identity() {
        this.w = 1.0d;
        this.x = 0.0d;
        this.y = 0.0d;
        this.z = 0.0d;
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Quaternion m6clone() {
        return new Quaternion(this.w, this.x, this.y, this.z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Quaternion)) {
            return false;
        }
        Quaternion quaternion = (Quaternion) obj;
        return this.x == quaternion.x && this.y == quaternion.y && this.z == quaternion.z && this.w == quaternion.w;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Quaternion <w, x, y, z>: <");
        stringBuffer.append(this.w);
        stringBuffer.append(", ");
        stringBuffer.append(this.x);
        stringBuffer.append(", ");
        stringBuffer.append(this.y);
        stringBuffer.append(", ");
        stringBuffer.append(this.z);
        stringBuffer.append(">");
        return stringBuffer.toString();
    }
}
