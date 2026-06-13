package com.huawei.animation.physical;

/* JADX INFO: loaded from: classes.dex */
public class OutputData {
    private float mAcceleration;
    private float mTime;
    private float mVelocity;
    private float mX;

    public OutputData() {
    }

    public OutputData(float f, float f2, float f3, float f4) {
        this.mTime = f;
        this.mX = f2;
        this.mVelocity = f3;
        this.mAcceleration = f4;
    }

    public float getT() {
        return this.mTime;
    }

    public void setT(float f) {
        this.mTime = f;
    }

    public float getX() {
        return this.mX;
    }

    public void setX(float f) {
        this.mX = f;
    }

    public float getV() {
        return this.mVelocity;
    }

    public void setV(float f) {
        this.mVelocity = f;
    }

    public float getA() {
        return this.mAcceleration;
    }

    public void setA(float f) {
        this.mAcceleration = f;
    }

    public String toString() {
        return "OutputData{time=" + this.mTime + ", x=" + this.mX + ", v=" + this.mVelocity + ", a=" + this.mAcceleration + '}';
    }
}
