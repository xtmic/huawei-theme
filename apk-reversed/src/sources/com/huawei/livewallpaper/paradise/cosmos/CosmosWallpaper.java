package com.huawei.livewallpaper.paradise.cosmos;

import android.content.Context;
import android.view.SurfaceHolder;
import com.huawei.superwallpaper.engine.ImageList;
import com.huawei.superwallpaper.engine.Node;
import com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView;
import com.huawei.superwallpaper.engine.wallpaper.BaseSuperWallpaperService;
import com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager;
import com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: classes.dex */
public class CosmosWallpaper extends BaseSuperWallpaperService {
    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseSuperWallpaperService
    public String getAssertFileName() {
        return "Cosmos/lock.jpg";
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService
    protected String getLogTag() {
        return "CosmosWallpaper";
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseSuperWallpaperService
    protected String getProviderPackageName() {
        return "cosmos.superwallpaper";
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseSuperWallpaperService
    public String getShareFileName() {
        return "cosmos_lock.jpg";
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService
    protected String getVersionName() {
        return "1.0_2023081601";
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService
    protected BaseGLSurfaceView createWallpaperView(Context context) {
        return new DelightingWView(context);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService
    protected BaseWallpaperManager createWallpaperManager(Context context) {
        return new CosmosManager(context);
    }

    public class DelightingWView extends BaseWallpaperService.WallpaperView {
        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView
        public boolean openP3EglSurfaceFactory() {
            return false;
        }

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView
        public boolean preserveEGLContextOnPause() {
            return true;
        }

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService.WallpaperView, android.view.SurfaceView
        public /* bridge */ /* synthetic */ SurfaceHolder getHolder() {
            return super.getHolder();
        }

        public DelightingWView(Context context) {
            super(context);
        }

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView, android.opengl.GLSurfaceView.Renderer
        public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
            super.onSurfaceCreated(gl10, eGLConfig);
            queueEvent(new Runnable() { // from class: com.huawei.livewallpaper.paradise.cosmos.CosmosWallpaper.DelightingWView.1
                @Override // java.lang.Runnable
                public void run() {
                    if (DelightingWView.this.getWallpaperManager() == null || DelightingWView.this.getWallpaperManager().getScene() == null || DelightingWView.this.getWallpaperManager().getScene().getRootNode() == null) {
                        return;
                    }
                    Node childByName = DelightingWView.this.getWallpaperManager().getScene().getRootNode().getChildByName("main");
                    if (childByName instanceof ImageList) {
                        ((ImageList) childByName).initOtherImages();
                    }
                }
            });
        }
    }
}
