package com.huawei.superwallpaper.engine.wallpaper;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES32;
import android.opengl.Matrix;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import androidx.core.location.LocationRequestCompat;
import com.huawei.superwallpaper.engine.util.Utils;
import com.huawei.superwallpaper.engine.videoplayer.MatrixUtil;
import com.huawei.superwallpaper.engine.videoplayer.TimeLimitVideoPlayer;
import com.huawei.superwallpaper.engine.videoplayer.VideoModel;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: classes.dex */
public class BaseVideoSurfaceView extends BaseGLSurfaceView {
    protected MatrixUtil mMatrixUtil;
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private String mVideoAssetsPath;
    private int mVideoHeight;
    private VideoModel mVideoModel;
    private TimeLimitVideoPlayer mVideoPlayer;
    protected float[] mVideoTransformMatrix;
    private int mVideoWidth;

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView
    public boolean openP3EglSurfaceFactory() {
        return true;
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView
    public boolean preserveEGLContextOnPause() {
        return true;
    }

    public BaseVideoSurfaceView(Context context) {
        super(context);
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.mVideoAssetsPath = "";
        this.mVideoTransformMatrix = new float[16];
    }

    public BaseVideoSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.mVideoAssetsPath = "";
        this.mVideoTransformMatrix = new float[16];
    }

    public void setVideoWidth(int i) {
        this.mVideoWidth = i;
    }

    public void setVideoHeight(int i) {
        this.mVideoHeight = i;
    }

    public void setVideoAssetsPath(String str) {
        this.mVideoAssetsPath = str;
    }

    public boolean isPlaying() {
        TimeLimitVideoPlayer timeLimitVideoPlayer = this.mVideoPlayer;
        if (timeLimitVideoPlayer != null) {
            return timeLimitVideoPlayer.isPlaying();
        }
        return false;
    }

    public void playTo(long j) {
        TimeLimitVideoPlayer timeLimitVideoPlayer = this.mVideoPlayer;
        if (timeLimitVideoPlayer != null) {
            timeLimitVideoPlayer.playTo(j);
        }
    }

    public void playIn(long j, long j2) {
        TimeLimitVideoPlayer timeLimitVideoPlayer = this.mVideoPlayer;
        if (timeLimitVideoPlayer != null) {
            timeLimitVideoPlayer.playIn(j, j2);
        }
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView, android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        MatrixUtil matrixUtil = new MatrixUtil();
        this.mMatrixUtil = matrixUtil;
        matrixUtil.init();
        this.mVideoModel = new VideoModel(getContext(), this.mMatrixUtil);
        Matrix.setIdentityM(this.mVideoTransformMatrix, 0);
        int iSurfaceTexture = Utils.surfaceTexture();
        this.mSurfaceTexture = new SurfaceTexture(iSurfaceTexture);
        this.mSurface = new Surface(this.mSurfaceTexture);
        this.mVideoModel.setTextureId(iSurfaceTexture);
        this.mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() { // from class: com.huawei.superwallpaper.engine.wallpaper.-$$Lambda$BaseVideoSurfaceView$rf7RMvWG0fLotH_Mo81WznIWWwM
            @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
            public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
                this.f$0.lambda$onSurfaceCreated$0$BaseVideoSurfaceView(surfaceTexture);
            }
        });
        if (TextUtils.isEmpty(this.mVideoAssetsPath)) {
            throw new RuntimeException("mVideoAssetsPath is null");
        }
        TimeLimitVideoPlayer timeLimitVideoPlayer = new TimeLimitVideoPlayer(getContext(), this.mVideoAssetsPath, this.mSurface);
        this.mVideoPlayer = timeLimitVideoPlayer;
        if (this.mVideoWidth == 0) {
            this.mVideoWidth = timeLimitVideoPlayer.getVideoWidth();
        }
        if (this.mVideoHeight == 0) {
            this.mVideoHeight = this.mVideoPlayer.getVideoHeight();
        }
        this.mVideoModel.initGLData();
        this.mVideoModel.setTransformMatrix(this.mVideoTransformMatrix);
        this.mVideoPlayer.playTo(LocationRequestCompat.PASSIVE_INTERVAL);
    }

    public /* synthetic */ void lambda$onSurfaceCreated$0$BaseVideoSurfaceView(SurfaceTexture surfaceTexture) {
        requestRender();
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView, android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        GLES32.glViewport(0, 0, i, i2);
        setOrthoM(i, i2);
        if (i > i2) {
            this.mMatrixUtil.setLookAtM(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f);
        } else {
            this.mMatrixUtil.setLookAtM(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        }
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView, android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        GLES32.glClear(16384);
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.updateTexImage();
            this.mSurfaceTexture.getTransformMatrix(this.mVideoTransformMatrix);
        }
        this.mMatrixUtil.pushMatrix();
        this.mVideoModel.draw();
        this.mMatrixUtil.popMatrix();
    }

    protected void setOrthoM(int i, int i2) {
        float f;
        float f2;
        float fMin = Math.min(i, i2) / ((Math.max(i, i2) / this.mVideoHeight) * this.mVideoWidth);
        if (fMin > 1.0f) {
            f2 = 1.0f;
            f = 1.0f / fMin;
        } else {
            f = 1.0f;
            f2 = fMin;
        }
        this.mMatrixUtil.orthoM(-f2, f2, -f, f, 1.0f, 10.0f);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView, android.opengl.GLSurfaceView, android.view.SurfaceView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        TimeLimitVideoPlayer timeLimitVideoPlayer = this.mVideoPlayer;
        if (timeLimitVideoPlayer != null) {
            timeLimitVideoPlayer.releasePlayerSync();
        }
        Surface surface = this.mSurface;
        if (surface != null) {
            surface.release();
        }
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.release();
        }
    }
}
