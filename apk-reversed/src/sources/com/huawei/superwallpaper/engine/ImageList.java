package com.huawei.superwallpaper.engine;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.opengl.GLES32;
import com.huawei.superwallpaper.engine.animation.Constant;
import com.huawei.superwallpaper.engine.util.LogUtil;
import com.huawei.superwallpaper.engine.util.Utils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public class ImageList extends Node {
    private static final String TAG = ImageList.class.getSimpleName();
    private boolean isPlayByOrder;
    private int lockIndex;
    private int mAlphaHandle;
    private double mAnchorX;
    private double mAnchorY;
    private final Context mContext;
    private int mCount;
    private int mDirection;
    private String mEndAction;
    private String mFragmentShader;
    private int mHeight;
    private int mIndex;
    private int mNormedTimeHandle;
    public float mOriginIndex;
    private final String mPath;
    private int mProgram;
    private int[] mTextureIds;
    private int mVaoId;
    private String mVertexShader;
    private int mWidth;
    private int maPositionHandle;
    private int maTexCoorHandle;
    private int muMVPMatrixHandle;
    private float normedTime;
    private ArrayList<String> path;
    private Point point;
    private int vCount;

    public ImageList(Context context, String str) {
        this(context, str, null);
    }

    public ImageList(Context context, String str, String str2) {
        this.mVaoId = 0;
        this.vCount = 0;
        this.mAnchorX = 0.5d;
        this.mAnchorY = 0.5d;
        this.mDirection = 1;
        this.mOriginIndex = 0.0f;
        this.mIndex = 0;
        this.mCount = 0;
        this.normedTime = 1.0f;
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
        String[] list;
        int i;
        super.initGLContext();
        this.point = new Point();
        try {
            list = this.mContext.getAssets().list(this.mPath);
        } catch (IOException e) {
            e.printStackTrace();
            list = null;
        }
        if (list == null || list.length == 0) {
            LogUtil.e(TAG, "could not found image list in path : " + this.mPath);
            return;
        }
        int length = list.length;
        this.mCount = length;
        this.mTextureIds = new int[length];
        this.path = new ArrayList<>();
        int i2 = 0;
        int i3 = 0;
        while (true) {
            i = this.mCount;
            if (i3 >= i) {
                break;
            }
            this.path.add(list[i3]);
            Collections.sort(this.path);
            i3++;
        }
        if (i > 60) {
            while (true) {
                int i4 = this.mCount;
                if (i2 >= i4) {
                    break;
                }
                if (i2 == i4 - 1 || i2 % 2 == 0) {
                    String str = this.path.get(i2);
                    if (str.endsWith(".astc")) {
                        this.mTextureIds[i2] = Utils.compressedTexture(this.mContext, Uri.parse("assets://" + this.mPath + "/" + str), "astc", this.point);
                        this.mDirection = 1;
                    } else if (str.endsWith(".png") || str.endsWith(".jpg") || str.endsWith("jpeg")) {
                        this.mTextureIds[i2] = Utils.texture(this.mContext, Uri.parse("assets://" + this.mPath + "/" + str), this.point);
                        this.mDirection = -1;
                    }
                    this.mWidth = Math.max(this.mWidth, this.point.x);
                    this.mHeight = Math.max(this.mHeight, this.point.y);
                }
                i2++;
            }
        } else {
            while (i2 < this.mCount) {
                String str2 = this.path.get(i2);
                if (str2.endsWith(".astc")) {
                    this.mTextureIds[i2] = Utils.compressedTexture(this.mContext, Uri.parse("assets://" + this.mPath + "/" + str2), "astc", this.point);
                    this.mDirection = 1;
                } else if (str2.endsWith(".png") || str2.endsWith(".jpg") || str2.endsWith("jpeg")) {
                    this.mTextureIds[i2] = Utils.texture(this.mContext, Uri.parse("assets://" + this.mPath + "/" + str2), this.point);
                    this.mDirection = -1;
                }
                this.mWidth = Math.max(this.mWidth, this.point.x);
                this.mHeight = Math.max(this.mHeight, this.point.y);
                i2++;
            }
        }
        initShader(this.mContext, "image_list.vert", "image_list.frag");
        initVertexData();
    }

    public void initOtherImages() {
        if (this.mCount <= 60 || this.mTextureIds == null || this.path == null) {
            return;
        }
        int i = 0;
        LogUtil.i(TAG, "initOtherImages---->", new Object[0]);
        while (true) {
            int i2 = this.mCount;
            if (i >= i2) {
                return;
            }
            if (i != i2 - 1 && i % 2 != 0 && this.mTextureIds[i] == 0) {
                String str = this.path.get(i);
                if (str.endsWith(".astc")) {
                    this.mTextureIds[i] = Utils.compressedTexture(this.mContext, Uri.parse("assets://" + this.mPath + "/" + str), "astc", this.point);
                    this.mDirection = 1;
                } else if (str.endsWith(".png") || str.endsWith(".jpg") || str.endsWith("jpeg")) {
                    this.mTextureIds[i] = Utils.texture(this.mContext, Uri.parse("assets://" + this.mPath + "/" + str), this.point);
                    this.mDirection = -1;
                }
            }
            i++;
        }
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
        this.mNormedTimeHandle = GLES32.glGetUniformLocation(this.mProgram, "uNormedTime");
    }

    private int findIndex() {
        int[] iArr = this.mTextureIds;
        int i = this.mIndex;
        if (iArr[i] != 0) {
            return iArr[i];
        }
        if (this.isPlayByOrder) {
            while (true) {
                int[] iArr2 = this.mTextureIds;
                if (i >= iArr2.length) {
                    break;
                }
                if (iArr2[i] != 0) {
                    return iArr2[i];
                }
                i++;
            }
        } else {
            while (i >= 0) {
                int[] iArr3 = this.mTextureIds;
                if (iArr3[i] != 0) {
                    return iArr3[i];
                }
                i--;
            }
        }
        if (this.isPlayByOrder) {
            return this.mTextureIds[r0.length - 1];
        }
        return this.mTextureIds[0];
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void draw() {
        super.draw();
        GLES32.glUseProgram(this.mProgram);
        GLES32.glUniformMatrix4fv(this.muMVPMatrixHandle, 1, false, Camera.getMVPMatrix(this.mWorldMatrix.getFloatValues()), 0);
        GLES32.glBindVertexArray(this.mVaoId);
        GLES32.glActiveTexture(33984);
        GLES32.glBindTexture(3553, findIndex());
        GLES32.glUniform1f(this.mAlphaHandle, this.alpha);
        GLES32.glUniform1f(this.mNormedTimeHandle, this.normedTime);
        LogUtil.i(TAG, "mIndex = " + this.mIndex + " alpha = " + this.alpha + " normedTime = " + this.normedTime, new Object[0]);
        GLES32.glDrawArrays(4, 0, this.vCount);
        GLES32.glBindVertexArray(0);
    }

    public void setLockIndex(float f) {
        this.lockIndex = Math.round((this.mCount - 1) * f);
        LogUtil.i(TAG, "setLockIndex to " + this.lockIndex, new Object[0]);
    }

    public void setEndAction(String str) {
        this.mEndAction = str;
        LogUtil.i(TAG, "setEndAction to " + str, new Object[0]);
    }

    public void setFrameIndex(float f) {
        int i;
        int i2;
        LogUtil.i(TAG, "setFrameIndex to " + f, new Object[0]);
        if (f > this.mOriginIndex) {
            this.isPlayByOrder = true;
        } else {
            this.isPlayByOrder = false;
        }
        this.mOriginIndex = f;
        int iRound = Math.round((this.mCount - 1) * f);
        if (iRound == 0 || iRound == this.mCount - 1 || this.lockIndex == iRound) {
            this.mIndex = Math.max(0, Math.min(iRound, this.mCount - 1));
        } else {
            if (this.isPlayByOrder && iRound <= (i2 = this.mIndex)) {
                this.mIndex = i2 + 1;
                LogUtil.d(TAG, "reset FrameIndex to " + this.mIndex + " when frameIndex = " + iRound);
            } else if (!this.isPlayByOrder && iRound >= (i = this.mIndex)) {
                this.mIndex = i - 1;
                LogUtil.d(TAG, "reset FrameIndex to " + this.mIndex + " when frameIndex = " + iRound);
            } else {
                this.mIndex = iRound;
            }
            this.mIndex = Math.max(0, Math.min(this.mIndex, this.mCount - 1));
        }
        if (Constant.STATE_LOCK.equals(this.mEndAction)) {
            int i3 = this.mIndex;
            int i4 = this.lockIndex;
            if (i3 <= i4 || !this.isPlayByOrder) {
                return;
            }
            this.mIndex = i4;
        }
    }

    public void setNormedTime(float f) {
        this.normedTime = Math.max(0.0f, Math.min(f, 1.0f));
    }
}
