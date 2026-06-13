package com.huawei.superwallpaper.engine.wallpaper;

import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.service.wallpaper.WallpaperService;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import androidx.core.content.FileProvider;
import com.huawei.superwallpaper.engine.animation.Constant;
import com.huawei.superwallpaper.engine.util.FileUtil;
import com.huawei.superwallpaper.engine.util.LogUtil;
import com.huawei.superwallpaper.engine.util.VipThreadUtil;
import com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView;
import com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService;
import java.io.File;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseSuperWallpaperService extends BaseWallpaperService {
    public abstract String getAssertFileName();

    public String getAssertNightFileName() {
        return null;
    }

    protected String getProviderPackageName() {
        return "paradise";
    }

    public abstract String getShareFileName();

    public String getShareNightFileName() {
        return null;
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService, android.service.wallpaper.WallpaperService, android.app.Service
    public void onCreate() {
        super.onCreate();
        Process.setThreadPriority(-20);
        LogUtil.i(getLogTag(), "SuperWallpaper onCreate build file provider start", new Object[0]);
        File filesDir = createDeviceProtectedStorageContext().getFilesDir();
        if (filesDir.exists()) {
            String shareFileName = getShareFileName();
            String assertFileName = getAssertFileName();
            if (!TextUtils.isEmpty(shareFileName) && !TextUtils.isEmpty(assertFileName)) {
                shareFile(filesDir.getPath() + File.separator + shareFileName, assertFileName);
            }
            String shareNightFileName = getShareNightFileName();
            String assertNightFileName = getAssertNightFileName();
            if (!TextUtils.isEmpty(shareNightFileName) && !TextUtils.isEmpty(assertNightFileName)) {
                shareFile(filesDir.getPath() + File.separator + shareNightFileName, assertNightFileName);
            }
        }
        LogUtil.i(getLogTag(), "build file provider end", new Object[0]);
        System.gc();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v7, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v8 */
    private void shareFile(String str, String str2) {
        ?? Open = 0;
        Open = 0;
        try {
            try {
                try {
                    Open = getAssets().open(str2);
                    FileUtil.saveInputStream(Open, str);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (Open != 0) {
                        Open.close();
                    }
                }
                if (Open != 0) {
                    Open.close();
                }
            } catch (Throwable th) {
                if (Open != 0) {
                    try {
                        Open.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        Open = "com.android.systemui";
        grantUriPermission("com.android.systemui", FileProvider.getUriForFile(this, "com.huawei.livewallpaper." + getProviderPackageName() + ".fileprovider", new File(str)), 1);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService
    protected WallpaperService.Engine createEngine(Context context) {
        return new BaseSuperEngine(this);
    }

    public class BaseSuperEngine extends BaseWallpaperService.BaseWallpaperEngine {
        private final Object lock;
        private CommandData tempCommand;

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService.BaseWallpaperEngine, android.service.wallpaper.WallpaperService.Engine
        public /* bridge */ /* synthetic */ void onDestroy() {
            super.onDestroy();
        }

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService.BaseWallpaperEngine, android.service.wallpaper.WallpaperService.Engine
        public /* bridge */ /* synthetic */ void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
        }

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService.BaseWallpaperEngine, android.service.wallpaper.WallpaperService.Engine
        public /* bridge */ /* synthetic */ void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
        }

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService.BaseWallpaperEngine, android.service.wallpaper.WallpaperService.Engine
        public /* bridge */ /* synthetic */ void onSurfaceDestroyed(SurfaceHolder surfaceHolder) {
            super.onSurfaceDestroyed(surfaceHolder);
        }

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService.BaseWallpaperEngine, android.service.wallpaper.WallpaperService.Engine
        public /* bridge */ /* synthetic */ void onSurfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
            super.onSurfaceRedrawNeeded(surfaceHolder);
        }

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService.BaseWallpaperEngine, android.service.wallpaper.WallpaperService.Engine
        public /* bridge */ /* synthetic */ void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
        }

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService.BaseWallpaperEngine
        public /* bridge */ /* synthetic */ boolean supportsLocalColorExtraction() {
            return super.supportsLocalColorExtraction();
        }

        public BaseSuperEngine(Context context) {
            super(context);
            this.lock = new Object();
            this.tempCommand = null;
        }

        @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperService.BaseWallpaperEngine, android.service.wallpaper.WallpaperService.Engine
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            if (this.wallpaperView == null || this.wallpaperManager == null) {
                return;
            }
            this.wallpaperView.setOnSurfaceCreateListener(new BaseGLSurfaceView.OnSurfaceCreateListener() { // from class: com.huawei.superwallpaper.engine.wallpaper.-$$Lambda$BaseSuperWallpaperService$BaseSuperEngine$vfWc3O7kCPi13R0zjtqDDbJTwhw
                @Override // com.huawei.superwallpaper.engine.wallpaper.BaseGLSurfaceView.OnSurfaceCreateListener
                public final void onSurfaceCreate() {
                    this.f$0.lambda$onCreate$0$BaseSuperWallpaperService$BaseSuperEngine();
                }
            });
        }

        public /* synthetic */ void lambda$onCreate$0$BaseSuperWallpaperService$BaseSuperEngine() {
            LogUtil.i(BaseSuperWallpaperService.this.getLogTag(), "wallpaperView onSurfaceCreate callback", new Object[0]);
            synchronized (this.lock) {
                if (this.tempCommand != null) {
                    LogUtil.i(BaseSuperWallpaperService.this.getLogTag(), "get tempCommand:[%s, %s, %d, %d, %d, %d]", this.tempCommand.oldAction, this.tempCommand.newAction, Integer.valueOf(this.tempCommand.x), Integer.valueOf(this.tempCommand.y), Integer.valueOf(this.tempCommand.aodDefaultX), Integer.valueOf(this.tempCommand.aodDefaultY));
                    this.wallpaperManager.onCommand(this.tempCommand.oldAction, this.tempCommand.newAction, this.tempCommand.x, this.tempCommand.y, this.tempCommand.aodDefaultX, this.tempCommand.aodDefaultY);
                    this.tempCommand = null;
                }
            }
        }

        protected boolean isActionValid(String str) {
            return Constant.STATE_AOD.equals(str) || Constant.STATE_LOCK.equals(str) || Constant.STATE_LAUNCHER.equals(str);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public Bundle onCommand(String str, int i, int i2, int i3, Bundle bundle, boolean z) throws Throwable {
            String string;
            int i4;
            int i5;
            Object obj;
            if (isActionValid(str)) {
                VipThreadUtil.setPriority();
                if (bundle != null) {
                    string = bundle.containsKey("oldState") ? bundle.getString("oldState") : null;
                    i4 = Settings.Secure.getInt(this.context.getContentResolver(), "bg_init_picture_position_x", 0);
                    i5 = Settings.Secure.getInt(this.context.getContentResolver(), "bg_init_picture_position_y", 0);
                } else {
                    string = null;
                    i4 = 0;
                    i5 = 0;
                }
                LogUtil.i(BaseSuperWallpaperService.this.getLogTag(), "onCommand old state : %s, new state : %s, x : %d, y : %d, z : %d, aodDefaultX : %d, aodDefaultY : %d, resultRequested : %b, isVisible : %b", string, str, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Boolean.valueOf(z), Boolean.valueOf(isVisible()));
                Object obj2 = this.lock;
                synchronized (obj2) {
                    try {
                        try {
                            if (this.wallpaperView == null || this.wallpaperManager == null) {
                                obj = obj2;
                            } else {
                                this.tempCommand = null;
                                if (this.wallpaperView.isSurfaceCreated() || this.wallpaperView.preserveEGLContextOnPause()) {
                                    obj = obj2;
                                    this.wallpaperView.resumeGLContext();
                                    this.wallpaperManager.onCommand(string, str, i, i2, i4, i5);
                                } else {
                                    if (string == null && Constant.STATE_AOD.equals(str)) {
                                        obj = obj2;
                                        this.wallpaperManager.onCommand(string, str, i, i2, i4, i5);
                                    } else {
                                        obj = obj2;
                                        this.tempCommand = new CommandData(string, str, i, i2, i4, i5);
                                        LogUtil.e(BaseSuperWallpaperService.this.getLogTag(), "set this command to tempCommand: [%s, %s, %d, %d, %d, %d]", string, str, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i4), Integer.valueOf(i5));
                                    }
                                    if (!this.wallpaperView.isSurfaceCreated()) {
                                        this.wallpaperManager.beforeSurfaceCreated(this.tempCommand);
                                    }
                                    this.wallpaperView.resumeGLContext();
                                }
                            }
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
                throw th;
            }
            return super.onCommand(str, i, i2, i3, bundle, z);
        }
    }
}
