package com.huawei.superwallpaper.engine;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.opengl.GLES32;
import com.huawei.superwallpaper.engine.util.Utils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes.dex */
public class ImageColor extends Node {
    private final int height;
    private int mAlphaHandle;
    private final Context mContext;
    private int mProgram;
    private int mTextureId;
    private int maPositionHandle;
    private int maTexCoorHandle;
    private int muMVPMatrixHandle;
    private final int width;
    private int mVaoId = 0;
    private int vCount = 0;

    public ImageColor(Context context, String str, int i, int i2) {
        this.mContext = context;
        this.mName = str;
        this.width = i;
        this.height = i2;
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void initGLContext() {
        super.initGLContext();
        this.mTextureId = Utils.compressedTexture(this.mContext, Uri.parse("assets://mask.astc"), "astc", (Point) null);
        initShader(this.mContext, "vertex.vert", "fragMask.frag");
        initVertexData();
    }

    public void initVertexData() {
        int[] iArr = new int[2];
        GLES32.glGenBuffers(2, iArr, 0);
        int i = iArr[0];
        int i2 = iArr[1];
        this.vCount = 6;
        int i3 = this.width;
        int i4 = this.height;
        float f = i3 * 1.0f * 0.5f * (-1.0f);
        float f2 = i4 * 1.0f * 0.5f * 1.0f;
        float f3 = i4 * 1.0f * 0.5f * (-1.0f);
        float f4 = i3 * 1.0f * 0.5f * 1.0f;
        float[] fArr = {f, f2, 0.0f, f, f3, 0.0f, f4, f3, 0.0f, f4, f3, 0.0f, f4, f2, 0.0f, f, f2, 0.0f};
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(72);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
        floatBufferAsFloatBuffer.put(fArr);
        floatBufferAsFloatBuffer.position(0);
        GLES32.glBindBuffer(34962, i);
        GLES32.glBufferData(34962, 72, floatBufferAsFloatBuffer, 35044);
        ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(48);
        byteBufferAllocateDirect2.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer2 = byteBufferAllocateDirect2.asFloatBuffer();
        floatBufferAsFloatBuffer2.put(new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f});
        floatBufferAsFloatBuffer2.position(0);
        GLES32.glBindBuffer(34962, i2);
        GLES32.glBufferData(34962, 48, floatBufferAsFloatBuffer2, 35044);
        GLES32.glBindBuffer(34962, 0);
        int[] iArr2 = new int[1];
        GLES32.glGenVertexArrays(1, iArr2, 0);
        int i5 = iArr2[0];
        this.mVaoId = i5;
        GLES32.glBindVertexArray(i5);
        GLES32.glEnableVertexAttribArray(this.maPositionHandle);
        GLES32.glEnableVertexAttribArray(this.maTexCoorHandle);
        GLES32.glBindBuffer(34962, i);
        GLES32.glVertexAttribPointer(this.maPositionHandle, 3, 5126, false, 12, 0);
        GLES32.glBindBuffer(34962, i2);
        GLES32.glVertexAttribPointer(this.maTexCoorHandle, 2, 5126, false, 8, 0);
        GLES32.glBindBuffer(34962, 0);
        GLES32.glBindVertexArray(0);
    }

    public void initShader(Context context, String str, String str2) {
        int iCreateProgram = ShaderUtil.createProgram(ShaderUtil.loadFromAssetsFile(str, context.getResources()), ShaderUtil.loadFromAssetsFile(str2, context.getResources()));
        this.mProgram = iCreateProgram;
        this.maPositionHandle = GLES32.glGetAttribLocation(iCreateProgram, "aPosition");
        this.maTexCoorHandle = GLES32.glGetAttribLocation(this.mProgram, "aTexCoor");
        this.muMVPMatrixHandle = GLES32.glGetUniformLocation(this.mProgram, "uMVPMatrix");
        this.mAlphaHandle = GLES32.glGetUniformLocation(this.mProgram, "uAlpha");
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void draw() {
        super.draw();
        GLES32.glUseProgram(this.mProgram);
        GLES32.glUniformMatrix4fv(this.muMVPMatrixHandle, 1, false, Camera.getMVPMatrix(this.mWorldMatrix.getFloatValues()), 0);
        GLES32.glBindVertexArray(this.mVaoId);
        GLES32.glEnableVertexAttribArray(this.maPositionHandle);
        GLES32.glEnableVertexAttribArray(this.maTexCoorHandle);
        GLES32.glActiveTexture(33984);
        GLES32.glBindTexture(3553, this.mTextureId);
        GLES32.glUniform1f(this.mAlphaHandle, this.alpha);
        GLES32.glDrawArrays(4, 0, this.vCount);
        GLES32.glBindVertexArray(0);
    }
}
