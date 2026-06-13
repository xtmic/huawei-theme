package com.huawei.superwallpaper.engine.wallpaper;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.PixelCopy;
import android.view.SurfaceHolder;
import com.huawei.superwallpaper.engine.util.FileUtil;
import com.huawei.superwallpaper.engine.util.LogUtil;
import com.huawei.superwallpaper.engine.util.SurfaceUtils;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseWallpaperService extends WallpaperService {
    protected boolean screenShot = false;
    protected int screenShotWidth = 1220;
    protected int screenShotHeight = 2700;
    protected WallpaperService.Engine wallpaperEngine = null;

    protected abstract WallpaperService.Engine createEngine(Context context);

    protected abstract BaseWallpaperManager createWallpaperManager(Context context);

    protected abstract String getLogTag();

    protected abstract String getVersionName();

    @Override // android.service.wallpaper.WallpaperService, android.app.Service
    public void onCreate() {
        super.onCreate();
        LogUtil.i(getLogTag(), "LiveWallpaper onCreate", new Object[0]);
    }

    @Override // android.service.wallpaper.WallpaperService
    public WallpaperService.Engine onCreateEngine() {
        this.wallpaperEngine = createEngine(this);
        LogUtil.i(getLogTag(), "onCreateEngine", new Object[0]);
        return this.wallpaperEngine;
    }

    @Override // android.app.Service, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        WallpaperService.Engine engine = this.wallpaperEngine;
        if (engine instanceof BaseWallpaperEngine) {
            BaseWallpaperManager baseWallpaperManager = ((BaseWallpaperEngine) engine).wallpaperManager;
            if (baseWallpaperManager != null) {
                baseWallpaperManager.onConfigurationChanged(configuration);
            }
            BaseGLSurfaceView baseGLSurfaceView = ((BaseWallpaperEngine) this.wallpaperEngine).wallpaperView;
            if (baseGLSurfaceView != null) {
                baseGLSurfaceView.requestRender();
            }
        }
    }

    @Override // android.service.wallpaper.WallpaperService, android.app.Service
    public void onDestroy() {
        LogUtil.i(getLogTag(), "LiveWallpaper onDestroy", new Object[0]);
        super.onDestroy();
    }

    protected BaseGLSurfaceView createWallpaperView(Context context) {
        return new WallpaperView(context);
    }

    protected class BaseWallpaperEngine extends WallpaperService.Engine {
        protected final Context context;
        protected BaseWallpaperManager wallpaperManager;
        protected BaseGLSurfaceView wallpaperView;

        public BaseWallpaperEngine(Context context) {
            super(BaseWallpaperService.this);
            this.context = context;
        }

        public boolean supportsLocalColorExtraction() {
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "supportsLocalColorExtraction", new Object[0]);
            return true;
        }

        protected boolean isReceiveTouchEvent() {
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "isReceiveTouchEvent: false", new Object[0]);
            return false;
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "onCreate", new Object[0]);
            this.wallpaperView = BaseWallpaperService.this.createWallpaperView(this.context);
            BaseWallpaperManager baseWallpaperManagerCreateWallpaperManager = BaseWallpaperService.this.createWallpaperManager(this.context);
            this.wallpaperManager = baseWallpaperManagerCreateWallpaperManager;
            BaseGLSurfaceView baseGLSurfaceView = this.wallpaperView;
            if (baseGLSurfaceView != null && baseWallpaperManagerCreateWallpaperManager != null) {
                baseGLSurfaceView.setWallpaperManager(baseWallpaperManagerCreateWallpaperManager);
            }
            if (BaseWallpaperService.this.screenShot) {
                new Handler().postDelayed(new Runnable() { // from class: com.huawei.superwallpaper.engine.wallpaper.-$$Lambda$BaseWallpaperService$BaseWallpaperEngine$TQSJFeC7fjjPFShJpEmn_-C1L-8
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onCreate$1$BaseWallpaperService$BaseWallpaperEngine();
                    }
                }, 5000L);
            }
        }

        public /* synthetic */ void lambda$onCreate$1$BaseWallpaperService$BaseWallpaperEngine() {
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "run PixelCopy.request", new Object[0]);
            final Bitmap bitmapCreateBitmap = Bitmap.createBitmap(BaseWallpaperService.this.screenShotWidth, BaseWallpaperService.this.screenShotHeight, Bitmap.Config.ARGB_8888);
            PixelCopy.request(this.wallpaperView, bitmapCreateBitmap, new PixelCopy.OnPixelCopyFinishedListener() { // from class: com.huawei.superwallpaper.engine.wallpaper.-$$Lambda$BaseWallpaperService$BaseWallpaperEngine$bFjXn66zJpG0XFU_mWK_IvaVFt4
                @Override // android.view.PixelCopy.OnPixelCopyFinishedListener
                public final void onPixelCopyFinished(int i) throws Throwable {
                    this.f$0.lambda$onCreate$0$BaseWallpaperService$BaseWallpaperEngine(bitmapCreateBitmap, i);
                }
            }, new Handler());
        }

        public /* synthetic */ void lambda$onCreate$0$BaseWallpaperService$BaseWallpaperEngine(Bitmap bitmap, int i) throws Throwable {
            String str = BaseWallpaperService.this.getExternalFilesDir(null).getPath() + File.separator + "lock.jpg";
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "onPixelCopyFinished copyResult : %d, path : %s", Integer.valueOf(i), str);
            FileUtil.saveBitmap(bitmap, str);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "onVisibilityChanged visible : %b, version name : %s", Boolean.valueOf(z), BaseWallpaperService.this.getVersionName());
            BaseGLSurfaceView baseGLSurfaceView = this.wallpaperView;
            if (baseGLSurfaceView != null) {
                baseGLSurfaceView.onVisibilityChanged(z);
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "onSurfaceCreated", new Object[0]);
            SurfaceUtils.setDynamicBufSize(surfaceHolder);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "onSurfaceChanged format : %d, width : %d, height : %d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
            BaseGLSurfaceView baseGLSurfaceView = this.wallpaperView;
            if (baseGLSurfaceView != null) {
                baseGLSurfaceView.refreshRenderBuffer(i2, i3);
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder) {
            super.onSurfaceDestroyed(surfaceHolder);
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "onSurfaceDestroyed", new Object[0]);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
            super.onSurfaceRedrawNeeded(surfaceHolder);
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "onSurfaceRedrawNeeded", new Object[0]);
            BaseGLSurfaceView baseGLSurfaceView = this.wallpaperView;
            if (baseGLSurfaceView != null) {
                baseGLSurfaceView.onSurfaceRedrawNeeded();
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onDestroy() {
            super.onDestroy();
            LogUtil.i(BaseWallpaperService.this.getLogTag(), "onDestroy", new Object[0]);
            BaseGLSurfaceView baseGLSurfaceView = this.wallpaperView;
            if (baseGLSurfaceView != null) {
                baseGLSurfaceView.onDetachedFromWindow();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public class WallpaperView extends BaseGLSurfaceView {
        public WallpaperView(Context context) {
            super(context);
        }

        @Override // android.view.SurfaceView
        public SurfaceHolder getHolder() {
            if (BaseWallpaperService.this.wallpaperEngine != null) {
                return BaseWallpaperService.this.wallpaperEngine.getSurfaceHolder();
            }
            return super.getHolder();
        }
    }
}
