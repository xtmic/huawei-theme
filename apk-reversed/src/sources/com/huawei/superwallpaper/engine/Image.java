package com.huawei.superwallpaper.engine;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.opengl.GLES32;
import com.huawei.superwallpaper.engine.util.LogUtil;
import com.huawei.superwallpaper.engine.util.Utils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes.dex */
public class Image extends Node {
    private static final String TAG = Image.class.getSimpleName();
    private int mAlphaHandle;
    private double mAnchorX;
    private double mAnchorY;
    private final Context mContext;
    private int mDirection;
    private String mFragmentShader;
    private int mHeight;
    private final String mPath;
    private int mProgram;
    private int mTextureId;
    private int mVaoId;
    private String mVertexShader;
    private int mWidth;
    private int maPositionHandle;
    private int maTexCoorHandle;
    private int muMVPMatrixHandle;
    private int vCount;

    public Image(Context context, String str) {
        this(context, str, null);
    }

    public Image(Context context, String str, String str2) {
        this.mVaoId = 0;
        this.vCount = 0;
        this.mAnchorX = 0.5d;
        this.mAnchorY = 0.5d;
        this.mDirection = 1;
        this.mContext = context;
        this.mPath = str;
        this.mName = str2;
    }

    public void setAnchorPoint(double d, double d2) {
        this.mAnchorX = d;
        this.mAnchorY = d2;
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void initGLContext() {
        super.initGLContext();
        Point point = new Point();
        if (this.mPath.endsWith(".astc")) {
            int iCompressedTexture = Utils.compressedTexture(this.mContext, Uri.parse("assets://" + this.mPath), "astc", point);
            this.mTextureId = iCompressedTexture;
            this.mDirection = 1;
            LogUtil.i(TAG, "texture id : %d, width : %d, height : %d", Integer.valueOf(iCompressedTexture), Integer.valueOf(point.x), Integer.valueOf(point.y));
        } else if (this.mPath.endsWith(".png") || this.mPath.endsWith(".jpg") || this.mPath.endsWith("jpeg")) {
            this.mTextureId = Utils.texture(this.mContext, Uri.parse("assets://" + this.mPath), point);
            this.mDirection = -1;
        }
        LogUtil.i(TAG, "texture id : %d, width : %d, height : %d", Integer.valueOf(this.mTextureId), Integer.valueOf(point.x), Integer.valueOf(point.y));
        this.mWidth = point.x;
        this.mHeight = point.y;
        initShader(this.mContext, "vertex.vert", "fragment.frag");
        initVertexData();
    }

    public void initVertexData() {
        int[] iArr = new int[2];
        GLES32.glGenBuffers(2, iArr, 0);
        int i = iArr[0];
        int i2 = iArr[1];
        this.vCount = 6;
        int i3 = this.mWidth;
        double d = this.mAnchorX;
        int i4 = this.mHeight;
        double d2 = this.mAnchorY;
        float f = (float) (((double) (i4 * 1.0f)) * d2);
        float f2 = (float) (((double) (i3 * 1.0f)) * (1.0d - d));
        float f3 = (float) (((double) (i4 * 1.0f)) * (1.0d - d2));
        float f4 = ((float) (((double) (i3 * 1.0f)) * d)) * (-1.0f);
        int i5 = this.mDirection;
        float f5 = f2 * 1.0f;
        float[] fArr = {f4, i5 * f, 0.0f, f4, (-i5) * f3, 0.0f, f5, (-i5) * f3, 0.0f, f5, (-i5) * f3, 0.0f, f5, i5 * f, 0.0f, f4, i5 * f, 0.0f};
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
        floatBufferAsFloatBuffer2.put(new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f});
        floatBufferAsFloatBuffer2.position(0);
        GLES32.glBindBuffer(34962, i2);
        GLES32.glBufferData(34962, 48, floatBufferAsFloatBuffer2, 35044);
        GLES32.glBindBuffer(34962, 0);
        int[] iArr2 = new int[1];
        GLES32.glGenVertexArrays(1, iArr2, 0);
        int i6 = iArr2[0];
        this.mVaoId = i6;
        GLES32.glBindVertexArray(i6);
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
        this.mVertexShader = ShaderUtil.loadFromAssetsFile(str, context.getResources());
        String strLoadFromAssetsFile = ShaderUtil.loadFromAssetsFile(str2, context.getResources());
        this.mFragmentShader = strLoadFromAssetsFile;
        int iCreateProgram = ShaderUtil.createProgram(this.mVertexShader, strLoadFromAssetsFile);
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
        GLES32.glActiveTexture(33984);
        GLES32.glBindTexture(3553, this.mTextureId);
        GLES32.glUniform1f(this.mAlphaHandle, this.alpha);
        GLES32.glDrawArrays(4, 0, this.vCount);
        GLES32.glBindVertexArray(0);
    }
}
