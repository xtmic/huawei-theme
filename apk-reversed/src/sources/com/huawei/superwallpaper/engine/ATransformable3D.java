package com.huawei.superwallpaper.engine;

import com.huawei.superwallpaper.engine.math.Matrix4;
import com.huawei.superwallpaper.engine.math.Quaternion;
import com.huawei.superwallpaper.engine.math.Vector3;

/* JADX INFO: loaded from: classes.dex */
public abstract class ATransformable3D {
    protected final Matrix4 mMMatrix = new Matrix4();
    protected final Vector3 mPosition = new Vector3();
    protected final Vector3 mScale = new Vector3(1.0d, 1.0d, 1.0d);
    protected final Quaternion mOrientation = new Quaternion();

    public void calculateModelMatrix() {
        this.mMMatrix.setAll(this.mPosition, this.mScale, this.mOrientation);
    }

    public void setX(double d) {
        this.mPosition.x = d;
    }

    public void setY(double d) {
        this.mPosition.y = d;
    }

    public void setZ(double d) {
        this.mPosition.z = d;
    }

    public double getX() {
        return this.mPosition.x;
    }

    public double getY() {
        return this.mPosition.y;
    }

    public double getZ() {
        return this.mPosition.z;
    }

    public void setRotation(Vector3.Axis axis, double d) {
        this.mOrientation.fromAngleAxis(axis, d);
    }

    public void setScaleX(double d) {
        this.mScale.x = d;
    }

    public void setScaleY(double d) {
        this.mScale.y = d;
    }

    public void setScaleZ(double d) {
        this.mScale.z = d;
    }

    public double getScaleX() {
        return this.mScale.x;
    }

    public double getScaleY() {
        return this.mScale.y;
    }
}
