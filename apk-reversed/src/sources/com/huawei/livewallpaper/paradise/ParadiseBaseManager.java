package com.huawei.livewallpaper.paradise;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.superwallpaper.engine.ImageList;
import com.huawei.superwallpaper.engine.animation.Listener;
import com.huawei.superwallpaper.engine.util.LogUtil;
import com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class ParadiseBaseManager extends BaseWallpaperManager {
    protected final ImageList imageList;

    public ParadiseBaseManager(Context context) {
        super(context);
        this.aodAlignment = false;
        this.imageList = (ImageList) this.scene.getRootNode().getChildByName("main");
        setAnimationListener(new Listener() { // from class: com.huawei.livewallpaper.paradise.ParadiseBaseManager.1
            @Override // com.huawei.superwallpaper.engine.animation.Listener
            public void onAnimationEnd() {
                if (ParadiseBaseManager.this.onEventListener != null) {
                    ParadiseBaseManager.this.onEventListener.onEventPause();
                }
            }
        });
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    protected void stateChange(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogUtil.e("WallpaperManager", "stateChange with empty cAction");
        } else {
            super.stateChange(str, str2);
            this.imageList.setEndAction(str2);
        }
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    protected void aod() {
        super.aod();
        this.imageList.setFrameIndex(0.0f);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    protected void launcher() {
        super.launcher();
        this.imageList.setFrameIndex(1.0f);
    }
}
