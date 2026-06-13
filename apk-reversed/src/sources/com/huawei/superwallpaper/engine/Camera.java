package com.huawei.superwallpaper.engine;

import android.opengl.Matrix;

/* JADX INFO: loaded from: classes.dex */
public class Camera {
    private static final float[] mProjMatrix = new float[16];
    private static final float[] mVMatrix = new float[16];

    public static void setCameraLookAt(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        Matrix.setLookAtM(mVMatrix, 0, f, f2, f3, f4, f5, f6, f7, f8, f9);
    }

    public static void setPerspective(float f, float f2, float f3, float f4) {
        Matrix.perspectiveM(mProjMatrix, 0, f3, f4, f, f2);
    }

    public static float[] getMVPMatrix(float[] fArr) {
        float[] fArr2 = new float[16];
        Matrix.multiplyMM(fArr2, 0, mVMatrix, 0, fArr, 0);
        Matrix.multiplyMM(fArr2, 0, mProjMatrix, 0, fArr2, 0);
        return fArr2;
    }
}
