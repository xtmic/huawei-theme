package com.huawei.superwallpaper.engine.wallpaper;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.AttributeSet;
import com.huawei.superwallpaper.engine.Camera;
import com.huawei.superwallpaper.engine.HandlerWrapper;
import com.huawei.superwallpaper.engine.util.LogUtil;
import com.huawei.superwallpaper.engine.util.P3EglSurfaceFactory;
import com.huawei.superwallpaper.engine.util.VipThreadUtil;
import com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: classes.dex */
public class BaseGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private static final int MESSAGE_PAUSE = 2;
    private static final int MESSAGE_REFRESH_RENDER = 4;
    private static final int MESSAGE_RENDER = 3;
    private static final int MESSAGE_RESUME = 1;
    private static final int PAUSE_DELAY_TIME = 3000;
    private static final int REFRESH_COUNT = 10;
    private static final String TAG = "WallPaperView";
    private HandlerWrapper mainHandler;
    public OnSurfaceCreateListener onSurfaceCreateListener;
    private int refreshRenderCount;
    private volatile boolean refreshRenderEnd;
    private volatile boolean surfaceCreated;
    private int surfaceHeight;
    private int surfaceWidth;
    private BaseWallpaperManager wallpaperManager;

    public interface OnSurfaceCreateListener {
        void onSurfaceCreate();
    }

    public boolean openP3EglSurfaceFactory() {
        return true;
    }

    public boolean preserveEGLContextOnPause() {
        return true;
    }

    static /* synthetic */ int access$008(BaseGLSurfaceView baseGLSurfaceView) {
        int i = baseGLSurfaceView.refreshRenderCount;
        baseGLSurfaceView.refreshRenderCount = i + 1;
        return i;
    }

    public BaseGLSurfaceView(Context context) {
        super(context);
        this.refreshRenderCount = 0;
        this.refreshRenderEnd = false;
        this.surfaceCreated = false;
        init();
    }

    public BaseGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.refreshRenderCount = 0;
        this.refreshRenderEnd = false;
        this.surfaceCreated = false;
        init();
    }

    private void init() {
        Process.setThreadPriority(-20);
        setEGLContextClientVersion(3);
        setPreserveEGLContextOnPause(preserveEGLContextOnPause());
        if (openP3EglSurfaceFactory() && getResources().getConfiguration().isScreenWideColorGamut()) {
            setEGLWindowSurfaceFactory(new P3EglSurfaceFactory());
        }
        setRenderer(this);
        setRenderMode(0);
        initHandler();
    }

    private void initHandler() {
        this.mainHandler = new HandlerWrapper(new Handler(Looper.getMainLooper()) { // from class: com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    BaseGLSurfaceView.this.onResume();
                    return;
                }
                if (i == 2) {
                    BaseGLSurfaceView.this.onPause();
                    return;
                }
                if (i == 3) {
                    BaseGLSurfaceView.this.requestRender();
                    return;
                }
                if (i != 4) {
                    return;
                }
                if (BaseGLSurfaceView.this.refreshRenderCount > 10) {
                    BaseGLSurfaceView.this.refreshRenderCount = 0;
                    BaseGLSurfaceView.this.refreshRenderEnd = false;
                    BaseGLSurfaceView.this.requestRender();
                    BaseGLSurfaceView.this.pauseGLContext();
                    return;
                }
                LogUtil.i(BaseGLSurfaceView.TAG, "refreshRender, count = " + BaseGLSurfaceView.this.refreshRenderCount, new Object[0]);
                BaseGLSurfaceView.access$008(BaseGLSurfaceView.this);
                BaseGLSurfaceView.this.refreshRenderEnd = true;
                BaseGLSurfaceView.this.requestRender();
                BaseGLSurfaceView.this.mainHandler.sendEmptyMessageDelayed(4, 16L);
            }
        });
    }

    public final BaseWallpaperManager getWallpaperManager() {
        return this.wallpaperManager;
    }

    public final void setWallpaperManager(BaseWallpaperManager baseWallpaperManager) {
        this.wallpaperManager = baseWallpaperManager;
        baseWallpaperManager.setOnEventListener(new BaseWallpaperManager.OnEventListener() { // from class: com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView.2
            @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager.OnEventListener
            public void onEventRender() {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    BaseGLSurfaceView.this.mainHandler.sendEmptyMessage(3);
                } else {
                    BaseGLSurfaceView.this.requestRender();
                }
            }

            @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager.OnEventListener
            public void onEventResume() {
                BaseGLSurfaceView.this.resumeGLContext();
            }

            @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager.OnEventListener
            public void onEventPause() {
                BaseGLSurfaceView.this.pauseGLContext();
            }
        });
    }

    public void resumeGLContext() {
        LogUtil.i(TAG, "resumeGLContext", new Object[0]);
        this.mainHandler.removeMessages(2);
        if (this.surfaceCreated) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            onResume();
        } else {
            this.mainHandler.sendEmptyMessage(1);
        }
    }

    public void pauseGLContext() {
        LogUtil.i(TAG, "pauseGLContext", new Object[0]);
        this.mainHandler.sendEmptyMessageDelayed(2, 3000L);
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        LogUtil.i(TAG, "onSurfaceCreated", new Object[0]);
        Process.setThreadPriority(-20);
        Thread.currentThread().setName("GLThreadOneTake");
        GLES32.glEnable(3042);
        GLES32.glBlendFunc(770, 771);
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES32.glClear(16640);
        BaseWallpaperManager baseWallpaperManager = this.wallpaperManager;
        if (baseWallpaperManager != null) {
            baseWallpaperManager.onSurfaceCreate();
        }
        this.surfaceCreated = true;
        OnSurfaceCreateListener onSurfaceCreateListener = this.onSurfaceCreateListener;
        if (onSurfaceCreateListener != null) {
            onSurfaceCreateListener.onSurfaceCreate();
        }
        VipThreadUtil.sRenderThreadId = Process.myTid();
    }

    public final void refreshRenderBuffer(int i, int i2) {
        if (i == this.surfaceWidth && i2 == this.surfaceHeight) {
            return;
        }
        LogUtil.i(TAG, "refreshRenderBuffer", new Object[0]);
        resumeGLContext();
        this.refreshRenderCount = 0;
        this.mainHandler.sendEmptyMessage(4);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        double degrees;
        double degrees2;
        LogUtil.i(TAG, "onSurfaceChanged, width : %d , height : %d", Integer.valueOf(i), Integer.valueOf(i2));
        GLES32.glViewport(0, 0, i, i2);
        if (this.wallpaperManager == null) {
            return;
        }
        if (i == this.surfaceWidth && i2 == this.surfaceHeight) {
            return;
        }
        this.surfaceWidth = i;
        this.surfaceHeight = i2;
        float f = i / i2;
        if (f <= 1.0f) {
            double d = f;
            if (d <= this.wallpaperManager.getScene().getCameraSceneWidth() / this.wallpaperManager.getScene().getCameraSceneHeight()) {
                degrees2 = Math.toDegrees(Math.atan((this.wallpaperManager.getScene().getCameraSceneHeight() * 0.5d) / this.wallpaperManager.getScene().getCameraZoom()) * 2.0d);
            } else {
                degrees2 = Math.toDegrees(Math.atan(((this.wallpaperManager.getScene().getCameraSceneWidth() * 0.5d) / d) / this.wallpaperManager.getScene().getCameraZoom()) * 2.0d);
            }
            Camera.setPerspective(2.0f, 3000.0f, (float) degrees2, f);
            Camera.setCameraLookAt((float) this.wallpaperManager.getScene().getCameraPosition().x, -((float) this.wallpaperManager.getScene().getCameraPosition().y), -((float) this.wallpaperManager.getScene().getCameraPosition().z), (float) this.wallpaperManager.getScene().getCameraTarget().x, -((float) this.wallpaperManager.getScene().getCameraTarget().y), -((float) this.wallpaperManager.getScene().getCameraTarget().z), 0.0f, 1.0f, 0.0f);
            return;
        }
        double d2 = f;
        if (d2 <= this.wallpaperManager.getScene().getCameraSceneWidth() / this.wallpaperManager.getScene().getCameraSceneHeight()) {
            degrees = Math.toDegrees(Math.atan(((this.wallpaperManager.getScene().getCameraSceneHeight() * 0.5d) / d2) / this.wallpaperManager.getScene().getCameraZoom()) * 2.0d);
        } else {
            degrees = Math.toDegrees(Math.atan((this.wallpaperManager.getScene().getCameraSceneWidth() * 0.5d) / this.wallpaperManager.getScene().getCameraZoom()) * 2.0d);
        }
        Camera.setPerspective(2.0f, 3000.0f, (float) degrees, f);
        Camera.setCameraLookAt((float) this.wallpaperManager.getScene().getCameraPosition().x, -((float) this.wallpaperManager.getScene().getCameraPosition().y), -((float) this.wallpaperManager.getScene().getCameraPosition().z), (float) this.wallpaperManager.getScene().getCameraTarget().x, -((float) this.wallpaperManager.getScene().getCameraTarget().y), -((float) this.wallpaperManager.getScene().getCameraTarget().z), 1.0f, 0.0f, 0.0f);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        BaseWallpaperManager baseWallpaperManager = this.wallpaperManager;
        if (baseWallpaperManager != null) {
            baseWallpaperManager.onDrawFrame();
        }
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mainHandler.removeCallbacksAndMessages();
        BaseWallpaperManager baseWallpaperManager = this.wallpaperManager;
        if (baseWallpaperManager != null) {
            baseWallpaperManager.onDetachedFromWindow();
        }
        this.onSurfaceCreateListener = null;
    }

    @Override // android.opengl.GLSurfaceView
    public void onResume() {
        super.onResume();
        if (preserveEGLContextOnPause()) {
            this.surfaceCreated = true;
        }
        LogUtil.i(TAG, "onResume", new Object[0]);
    }

    @Override // android.opengl.GLSurfaceView
    public void onPause() {
        super.onPause();
        this.surfaceCreated = false;
        BaseWallpaperManager baseWallpaperManager = this.wallpaperManager;
        if (baseWallpaperManager != null) {
            baseWallpaperManager.onPause();
        }
        LogUtil.i(TAG, "onPause", new Object[0]);
    }

    public boolean isSurfaceCreated() {
        return this.surfaceCreated;
    }

    public void onVisibilityChanged(boolean z) {
        LogUtil.i(TAG, "onVisibilityChanged visible = " + z, new Object[0]);
        BaseWallpaperManager baseWallpaperManager = this.wallpaperManager;
        if (baseWallpaperManager != null) {
            baseWallpaperManager.onVisibleChange(z);
        }
        if (z) {
            LogUtil.i(TAG, "onVisibilityChanged do nothing", new Object[0]);
        } else {
            requestRender();
            pauseGLContext();
        }
    }

    public final void onSurfaceRedrawNeeded() {
        if (this.surfaceCreated) {
            requestRender();
        } else {
            resumeGLContext();
        }
    }

    public void setOnSurfaceCreateListener(OnSurfaceCreateListener onSurfaceCreateListener) {
        this.onSurfaceCreateListener = onSurfaceCreateListener;
    }
}
