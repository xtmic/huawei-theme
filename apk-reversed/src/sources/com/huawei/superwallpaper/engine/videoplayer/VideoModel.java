package com.huawei.superwallpaper.engine.videoplayer;

import android.content.Context;
import android.opengl.GLES32;
import com.huawei.superwallpaper.engine.util.LogUtil;
import com.huawei.superwallpaper.engine.util.Utils;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes.dex */
public class VideoModel {
    private static final String TAG = "TM";
    private Context mContext;
    private MatrixUtil mMatrixUtil;
    private int mProgram;
    private int mTextureId;
    private float[] mTransformMatrix;
    private int[] mVBOId;
    private FloatBuffer mVertexBuffer;
    private int muAlpha;
    private int muMVPMatrixHandler;
    private int muTexture;
    private int muTransformM;
    private float[] mVertexes = {-1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f};
    private float[] mTexCoords = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};

    public VideoModel(Context context, MatrixUtil matrixUtil) {
        this.mContext = context;
        this.mMatrixUtil = matrixUtil;
    }

    public void initGLData() {
        initVertexData();
        initShader();
    }

    public void setTextureId(int i) {
        this.mTextureId = i;
    }

    public void setTransformMatrix(float[] fArr) {
        this.mTransformMatrix = fArr;
    }

    protected void initVertexData() {
        this.mVertexBuffer = Utils.floatArray2FloatBuffer(this.mVertexes);
        FloatBuffer floatBufferFloatArray2FloatBuffer = Utils.floatArray2FloatBuffer(this.mTexCoords);
        int[] iArr = new int[2];
        this.mVBOId = iArr;
        GLES32.glGenBuffers(iArr.length, iArr, 0);
        GLES32.glBindBuffer(34962, this.mVBOId[0]);
        GLES32.glBufferData(34962, this.mVertexes.length * 4, this.mVertexBuffer, 35044);
        LogUtil.d(TAG, "vertex capacity : %d, length : %d", Integer.valueOf(this.mVertexBuffer.capacity()), Integer.valueOf(this.mVertexes.length));
        GLES32.glBindBuffer(34962, this.mVBOId[1]);
        GLES32.glBufferData(34962, this.mTexCoords.length * 4, floatBufferFloatArray2FloatBuffer, 35044);
    }

    protected void initShader() {
        int iCreateProgram = Utils.createProgram(this.mContext, "texture.vert", "texture.frag");
        this.mProgram = iCreateProgram;
        this.muMVPMatrixHandler = GLES32.glGetUniformLocation(iCreateProgram, "uMVPMatrix");
        this.muTransformM = GLES32.glGetUniformLocation(this.mProgram, "uTransformM");
        this.muTexture = GLES32.glGetUniformLocation(this.mProgram, "uTexture");
        this.muAlpha = GLES32.glGetUniformLocation(this.mProgram, "uAlpha");
    }

    public void draw() {
        GLES32.glUseProgram(this.mProgram);
        GLES32.glBindBuffer(34962, this.mVBOId[0]);
        GLES32.glEnableVertexAttribArray(0);
        GLES32.glVertexAttribPointer(0, 3, 5126, false, 0, 0);
        GLES32.glBindBuffer(34962, this.mVBOId[1]);
        GLES32.glEnableVertexAttribArray(1);
        GLES32.glVertexAttribPointer(1, 2, 5126, false, 0, 0);
        GLES32.glUniformMatrix4fv(this.muMVPMatrixHandler, 1, false, this.mMatrixUtil.getFinalMatrix(), 0);
        GLES32.glUniformMatrix4fv(this.muTransformM, 1, false, this.mTransformMatrix, 0);
        GLES32.glUniform1f(this.muAlpha, 1.0f);
        GLES32.glActiveTexture(33984);
        GLES32.glBindTexture(36197, this.mTextureId);
        GLES32.glUniform1i(this.muTexture, 0);
        GLES32.glDrawArrays(4, 0, this.mVertexBuffer.capacity() / 3);
    }
}
