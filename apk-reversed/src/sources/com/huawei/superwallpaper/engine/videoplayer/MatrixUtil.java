package com.huawei.superwallpaper.engine.videoplayer;

import android.opengl.Matrix;
import java.lang.reflect.Array;

/* JADX INFO: loaded from: classes.dex */
public class MatrixUtil {
    private static final int AMOUNT = 16;
    private float[] mCameraLocation;
    private float[] mProjectionM = new float[16];
    private float[] mLookAtM = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mCurrentMatrix = new float[16];
    private float[][] mStack = (float[][]) Array.newInstance((Class<?>) float.class, 10, 16);
    private int mStackTop = -1;

    public void init() {
        this.mProjectionM = new float[16];
        this.mLookAtM = new float[16];
        this.mMVPMatrix = new float[16];
        float[] fArr = new float[16];
        this.mCurrentMatrix = fArr;
        Matrix.setIdentityM(fArr, 0);
        this.mStack = (float[][]) Array.newInstance((Class<?>) float.class, 10, 16);
        this.mStackTop = -1;
    }

    public void frustumM(float f, float f2, float f3, float f4, float f5, float f6) {
        Matrix.frustumM(this.mProjectionM, 0, f, f2, f3, f4, f5, f6);
    }

    public void perspectiveM(float f, float f2, float f3, float f4) {
        Matrix.perspectiveM(this.mProjectionM, 0, f, f2, f3, f4);
    }

    public void orthoM(float f, float f2, float f3, float f4, float f5, float f6) {
        Matrix.orthoM(this.mProjectionM, 0, f, f2, f3, f4, f5, f6);
    }

    public void setLookAtM(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        Matrix.setLookAtM(this.mLookAtM, 0, f, f2, f3, f4, f5, f6, f7, f8, f9);
        this.mCameraLocation = new float[]{f, f2, f3};
    }

    public void translate(float f, float f2, float f3) {
        Matrix.translateM(this.mCurrentMatrix, 0, f, f2, f3);
    }

    public void rotate(float f, float f2, float f3, float f4) {
        Matrix.rotateM(this.mCurrentMatrix, 0, f, f2, f3, f4);
    }

    public void scale(float f, float f2, float f3) {
        Matrix.scaleM(this.mCurrentMatrix, 0, f, f2, f3);
    }

    public void pushMatrix() {
        int i = this.mStackTop + 1;
        this.mStackTop = i;
        System.arraycopy(this.mCurrentMatrix, 0, this.mStack[i], 0, 16);
    }

    public void popMatrix() {
        System.arraycopy(this.mStack[this.mStackTop], 0, this.mCurrentMatrix, 0, 16);
        this.mStackTop--;
    }

    public float[] getCameraLocation() {
        return this.mCameraLocation;
    }

    public float[] getCurrentMatrix() {
        return this.mCurrentMatrix;
    }

    public float[] getLookAtM() {
        return this.mLookAtM;
    }

    public float[] getProjectionM() {
        return this.mProjectionM;
    }

    public float[] getFinalMatrix() {
        Matrix.multiplyMM(this.mMVPMatrix, 0, this.mLookAtM, 0, this.mCurrentMatrix, 0);
        float[] fArr = this.mMVPMatrix;
        Matrix.multiplyMM(fArr, 0, this.mProjectionM, 0, fArr, 0);
        return this.mMVPMatrix;
    }
}
