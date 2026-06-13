package com.huawei.superwallpaper.engine;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.opengl.GLES32;
import android.text.TextUtils;
import android.view.Surface;
import com.huawei.superwallpaper.engine.animation.Constant;
import com.huawei.superwallpaper.engine.util.LogUtil;
import com.huawei.superwallpaper.engine.util.Utils;
import com.huawei.superwallpaper.engine.videoplayer.TimeLimitVideoPlayer;
import com.huawei.superwallpaper.engine.videoplayer.VideoPlayer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes.dex */
public class VideoImageNode extends Node {
    public static final float END_IMAGE_INDEX = 1.0f;
    public static final float START_IMAGE_INDEX = 0.0f;
    private static final String TAG = VideoImageNode.class.getSimpleName();
    public static final float VIDEO_IMAGE_INDEX = 0.5f;
    protected int mAlphaHandle;
    protected Context mContext;
    protected String mEndImageAssetsPath;
    protected int mEndImageTextureHandle;
    protected int mHeight;
    protected float mNormedTime;
    protected int mNormedTimeHandle;
    protected String mPath;
    protected int mProgram;
    protected String mStartImageAssetsPath;
    protected int mStartImageTextureHandle;
    protected Surface mSurface;
    protected SurfaceTexture mSurfaceTexture;
    protected int mTexCoorBufferId;
    protected int mTextureId;
    protected int mTextureIndexHandle;
    protected int mVertexBufferId;
    protected VideoPlayer.OnPlayListener mVideoListener;
    protected TimeLimitVideoPlayer mVideoPlayer;
    protected int mWidth;
    protected int maPositionHandle;
    protected int maTexCoorHandle;
    protected int muMVPMatrixHandle;
    protected int muTexture;
    protected int muTransformM;
    protected int mVaoId = 0;
    protected int vCount = 0;
    protected float mTextureIndex = 1.0f;
    protected int mStartImageTextureId = 0;
    protected int mEndImageTextureId = 0;
    protected int mDirection = 1;
    protected final float[] mVideoTransformMatrix = new float[16];
    protected boolean hasStartImage = true;
    protected boolean hasEndImage = true;
    protected boolean asyncDecode = false;

    @Override // com.huawei.superwallpaper.engine.Node
    public boolean couldDraw() {
        return true;
    }

    public VideoImageNode(Context context, String str, String str2) {
        this.mContext = context;
        this.mPath = str;
        this.mName = str2;
    }

    public void setVideoListener(VideoPlayer.OnPlayListener onPlayListener) {
        this.mVideoListener = onPlayListener;
    }

    public void setVideoSize(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    public void setDecodeAsync(boolean z) {
        this.asyncDecode = z;
    }

    public void setStartImagePath(String str) {
        this.mStartImageAssetsPath = str;
    }

    public void setEndImagePath(String str) {
        this.mEndImageAssetsPath = str;
    }

    public void setTextureIndex(float f) {
        this.mTextureIndex = f;
    }

    public void setMaskAnimateParams(float f) {
        this.mNormedTime = f;
        if (f <= 0.0f || f >= 1.0f) {
            return;
        }
        LogUtil.i(TAG, "mNormedTime : %f", Float.valueOf(f));
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void initGLContext() {
        LogUtil.i(TAG, "initGLContext....", new Object[0]);
        if (this.mPath.endsWith(".mp4")) {
            this.mTextureId = Utils.surfaceTexture();
            this.mDirection = 1;
        }
        SurfaceTexture surfaceTexture = new SurfaceTexture(this.mTextureId);
        this.mSurfaceTexture = surfaceTexture;
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() { // from class: com.huawei.superwallpaper.engine.-$$Lambda$VideoImageNode$Lv9sdlSCb8geDrLEOnvKsn4pKow
            @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
            public final void onFrameAvailable(SurfaceTexture surfaceTexture2) {
                this.f$0.lambda$initGLContext$0$VideoImageNode(surfaceTexture2);
            }
        });
        this.mSurface = new Surface(this.mSurfaceTexture);
        TimeLimitVideoPlayer timeLimitVideoPlayer = new TimeLimitVideoPlayer(this.mContext, this.mPath, this.mSurface, this.asyncDecode);
        this.mVideoPlayer = timeLimitVideoPlayer;
        timeLimitVideoPlayer.setPlayListener(new VideoPlayer.OnPlayListener() { // from class: com.huawei.superwallpaper.engine.VideoImageNode.1
            @Override // com.huawei.superwallpaper.engine.videoplayer.VideoPlayer.OnPlayListener
            public void onRenderStart() {
                if (VideoImageNode.this.mTextureIndex != 0.5f) {
                    VideoImageNode.this.mTextureIndex = 0.5f;
                    LogUtil.i(VideoImageNode.TAG, "onRenderStart", new Object[0]);
                }
                if (VideoImageNode.this.mVideoListener != null) {
                    VideoImageNode.this.mVideoListener.onRenderStart();
                }
            }

            @Override // com.huawei.superwallpaper.engine.videoplayer.VideoPlayer.OnPlayListener
            public void onFrameUpdate(long j) {
                if (VideoImageNode.this.mVideoListener != null) {
                    VideoImageNode.this.mVideoListener.onFrameUpdate(j);
                }
            }

            @Override // com.huawei.superwallpaper.engine.videoplayer.VideoPlayer.OnPlayListener
            public void onArriveFirstFrame() {
                if (VideoImageNode.this.mTextureIndex != 0.0f) {
                    VideoImageNode.this.mTextureIndex = 0.0f;
                    LogUtil.i(VideoImageNode.TAG, "onArriveFirstFrame", new Object[0]);
                }
                if (VideoImageNode.this.mVideoListener != null) {
                    VideoImageNode.this.mVideoListener.onArriveFirstFrame();
                }
            }

            @Override // com.huawei.superwallpaper.engine.videoplayer.VideoPlayer.OnPlayListener
            public void onArriveLastFrame() {
                if (VideoImageNode.this.mTextureIndex != 1.0f) {
                    VideoImageNode.this.mTextureIndex = 1.0f;
                    VideoImageNode.this.mNormedTime = 1.0f;
                    LogUtil.i(VideoImageNode.TAG, "onArriveLastFrame", new Object[0]);
                }
                if (VideoImageNode.this.mVideoListener != null) {
                    VideoImageNode.this.mVideoListener.onArriveLastFrame();
                }
            }

            @Override // com.huawei.superwallpaper.engine.videoplayer.VideoPlayer.OnPlayListener
            public void onDestroy() {
                LogUtil.i(VideoImageNode.TAG, "onDestroy", new Object[0]);
                if (VideoImageNode.this.mSurface != null) {
                    VideoImageNode.this.mSurface.release();
                    VideoImageNode.this.mSurface = null;
                }
                if (VideoImageNode.this.mSurfaceTexture != null) {
                    VideoImageNode.this.mSurfaceTexture.release();
                    VideoImageNode.this.mSurfaceTexture = null;
                }
                if (VideoImageNode.this.mVideoListener != null) {
                    VideoImageNode.this.mVideoListener.onDestroy();
                }
            }

            @Override // com.huawei.superwallpaper.engine.videoplayer.VideoPlayer.OnPlayListener
            public void onVideoPlayStart() {
                if (VideoImageNode.this.mVideoListener != null) {
                    VideoImageNode.this.mVideoListener.onVideoPlayStart();
                }
            }

            @Override // com.huawei.superwallpaper.engine.videoplayer.VideoPlayer.OnPlayListener
            public void onVideoPlayEnd() {
                if (VideoImageNode.this.mVideoListener != null) {
                    VideoImageNode.this.mVideoListener.onVideoPlayEnd();
                }
            }
        });
        LogUtil.i(TAG, "texture id : %d, width : %d, height : %d", Integer.valueOf(this.mTextureId), Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight));
        initShader(this.mContext, "video_image_texture.vert", "video_image_texture.frag");
        initVertexData();
        if (TextUtils.isEmpty(this.mStartImageAssetsPath)) {
            LogUtil.i(TAG, "do not find startImage", new Object[0]);
            this.hasStartImage = false;
        } else {
            this.hasStartImage = true;
            this.mStartImageTextureId = Utils.compressedTexture(this.mContext, Uri.parse("assets://" + this.mStartImageAssetsPath), "astc", (Point) null);
        }
        if (TextUtils.isEmpty(this.mEndImageAssetsPath)) {
            LogUtil.i(TAG, "do not find endImage", new Object[0]);
            this.hasEndImage = false;
            return;
        }
        this.hasEndImage = true;
        this.mEndImageTextureId = Utils.compressedTexture(this.mContext, Uri.parse("assets://" + this.mEndImageAssetsPath), "astc", (Point) null);
    }

    public /* synthetic */ void lambda$initGLContext$0$VideoImageNode(SurfaceTexture surfaceTexture) {
        VideoPlayer.OnPlayListener onPlayListener = this.mVideoListener;
        if (onPlayListener != null) {
            onPlayListener.onRequestRender();
        }
    }

    private void initVertexData() {
        int[] iArr = new int[2];
        GLES32.glGenBuffers(2, iArr, 0);
        this.mVertexBufferId = iArr[0];
        this.mTexCoorBufferId = iArr[1];
        this.vCount = 6;
        int i = this.mWidth;
        int i2 = this.mHeight;
        float f = (float) (((double) (i2 * 1.0f)) * 0.5d);
        float f2 = (float) (((double) (i2 * 1.0f)) * 0.5d);
        float f3 = ((float) (((double) (i * 1.0f)) * 0.5d)) * (-1.0f);
        int i3 = this.mDirection;
        float f4 = ((float) (((double) (i * 1.0f)) * 0.5d)) * 1.0f;
        float[] fArr = {f3, i3 * f, 0.0f, f3, (-i3) * f2, 0.0f, f4, (-i3) * f2, 0.0f, f4, (-i3) * f2, 0.0f, f4, i3 * f, 0.0f, f3, i3 * f, 0.0f};
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(72);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
        floatBufferAsFloatBuffer.put(fArr);
        floatBufferAsFloatBuffer.position(0);
        GLES32.glBindBuffer(34962, this.mVertexBufferId);
        GLES32.glBufferData(34962, 72, floatBufferAsFloatBuffer, 35044);
        ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(48);
        byteBufferAllocateDirect2.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer2 = byteBufferAllocateDirect2.asFloatBuffer();
        floatBufferAsFloatBuffer2.put(new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f});
        floatBufferAsFloatBuffer2.position(0);
        GLES32.glBindBuffer(34962, this.mTexCoorBufferId);
        GLES32.glBufferData(34962, 48, floatBufferAsFloatBuffer2, 35044);
        GLES32.glBindBuffer(34962, 0);
        int[] iArr2 = new int[1];
        GLES32.glGenVertexArrays(1, iArr2, 0);
        int i4 = iArr2[0];
        this.mVaoId = i4;
        GLES32.glBindVertexArray(i4);
        GLES32.glEnableVertexAttribArray(this.maPositionHandle);
        GLES32.glEnableVertexAttribArray(this.maTexCoorHandle);
        GLES32.glBindBuffer(34962, this.mVertexBufferId);
        GLES32.glVertexAttribPointer(this.maPositionHandle, 3, 5126, false, 12, 0);
        GLES32.glBindBuffer(34962, this.mTexCoorBufferId);
        GLES32.glVertexAttribPointer(this.maTexCoorHandle, 2, 5126, false, 8, 0);
        GLES32.glBindBuffer(34962, 0);
        GLES32.glBindVertexArray(0);
    }

    private void initShader(Context context, String str, String str2) {
        int iCreateProgram = ShaderUtil.createProgram(ShaderUtil.loadFromAssetsFile(str, context.getResources()), ShaderUtil.loadFromAssetsFile(str2, context.getResources()));
        this.mProgram = iCreateProgram;
        this.maPositionHandle = GLES32.glGetAttribLocation(iCreateProgram, "aPosition");
        this.maTexCoorHandle = GLES32.glGetAttribLocation(this.mProgram, "aTexCoor");
        this.muMVPMatrixHandle = GLES32.glGetUniformLocation(this.mProgram, "uMVPMatrix");
        this.mAlphaHandle = GLES32.glGetUniformLocation(this.mProgram, "uAlpha");
        this.muTransformM = GLES32.glGetUniformLocation(this.mProgram, "uTransformM");
        this.muTexture = GLES32.glGetUniformLocation(this.mProgram, "uTexture");
        this.mStartImageTextureHandle = GLES32.glGetUniformLocation(this.mProgram, "uStartImageTexture");
        this.mEndImageTextureHandle = GLES32.glGetUniformLocation(this.mProgram, "uEndImageTexture");
        this.mTextureIndexHandle = GLES32.glGetUniformLocation(this.mProgram, "uTextureIndex");
        this.mNormedTimeHandle = GLES32.glGetUniformLocation(this.mProgram, "uNormedTime");
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void draw() {
        super.draw();
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.updateTexImage();
            this.mSurfaceTexture.getTransformMatrix(this.mVideoTransformMatrix);
        }
        if (super.couldDraw()) {
            GLES32.glUseProgram(this.mProgram);
            GLES32.glUniformMatrix4fv(this.muMVPMatrixHandle, 1, false, Camera.getMVPMatrix(this.mWorldMatrix.getFloatValues()), 0);
            GLES32.glUniformMatrix4fv(this.muTransformM, 1, false, this.mVideoTransformMatrix, 0);
            if (!this.hasStartImage && this.mTextureIndex == 0.0f) {
                this.mTextureIndex = 0.5f;
            }
            if (!this.hasEndImage && this.mTextureIndex == 1.0f) {
                this.mTextureIndex = 0.5f;
            }
            float f = this.mTextureIndex;
            if (f == 0.0f || f == 1.0f) {
                LogUtil.i(TAG, "mTextureIndex = " + this.mTextureIndex, new Object[0]);
            }
            GLES32.glUniform1f(this.mTextureIndexHandle, this.mTextureIndex);
            GLES32.glUniform1f(this.mNormedTimeHandle, this.mNormedTime);
            GLES32.glBindVertexArray(this.mVaoId);
            GLES32.glActiveTexture(33984);
            GLES32.glBindTexture(36197, this.mTextureId);
            GLES32.glUniform1i(this.muTexture, 0);
            GLES32.glActiveTexture(33985);
            GLES32.glBindTexture(3553, this.mStartImageTextureId);
            GLES32.glUniform1i(this.mStartImageTextureHandle, 1);
            GLES32.glActiveTexture(33986);
            GLES32.glBindTexture(3553, this.mEndImageTextureId);
            GLES32.glUniform1i(this.mEndImageTextureHandle, 2);
            GLES32.glUniform1f(this.mAlphaHandle, this.alpha);
            GLES32.glDrawArrays(4, 0, this.vCount);
            GLES32.glBindVertexArray(0);
        }
    }

    public boolean isPlaying() {
        TimeLimitVideoPlayer timeLimitVideoPlayer = this.mVideoPlayer;
        if (timeLimitVideoPlayer == null) {
            return false;
        }
        return timeLimitVideoPlayer.isPlaying();
    }

    public void playIn(long j, long j2) {
        TimeLimitVideoPlayer timeLimitVideoPlayer = this.mVideoPlayer;
        if (timeLimitVideoPlayer != null) {
            timeLimitVideoPlayer.playIn(j, j2);
        }
    }

    public void playTo(long j) {
        TimeLimitVideoPlayer timeLimitVideoPlayer = this.mVideoPlayer;
        if (timeLimitVideoPlayer != null) {
            timeLimitVideoPlayer.playTo(j);
        }
    }

    public void setSurPlusAnimateTime(long j) {
        TimeLimitVideoPlayer timeLimitVideoPlayer = this.mVideoPlayer;
        if (timeLimitVideoPlayer != null) {
            timeLimitVideoPlayer.setSurPlusAnimateTime(j);
        }
    }

    public void preload() {
        TimeLimitVideoPlayer timeLimitVideoPlayer = this.mVideoPlayer;
        if (timeLimitVideoPlayer != null) {
            timeLimitVideoPlayer.preloadFirstFrame();
            this.mTextureIndex = 0.0f;
        }
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        TimeLimitVideoPlayer timeLimitVideoPlayer = this.mVideoPlayer;
        if (timeLimitVideoPlayer != null) {
            timeLimitVideoPlayer.destroy();
        }
    }

    public void initState(String str) {
        boolean z = false;
        LogUtil.i(TAG, "initState : " + str, new Object[0]);
        if (this.mVideoPlayer != null) {
            if ((this.hasStartImage && Constant.STATE_AOD.equals(str)) || ((this.hasEndImage && Constant.STATE_LOCK.equals(str)) || (this.hasEndImage && Constant.STATE_LAUNCHER.equals(str)))) {
                z = true;
            }
            this.mVideoPlayer.initState(str, z);
        }
        if (str == null || Constant.STATE_AOD.equals(str)) {
            this.mTextureIndex = 0.0f;
        } else {
            this.mTextureIndex = 1.0f;
        }
    }
}
