package com.huawei.livewallpaper.paradise.cosmos;

import android.content.Context;
import com.huawei.livewallpaper.paradise.ParadiseBaseManager;
import com.huawei.superwallpaper.engine.animation.Constant;
import com.huawei.superwallpaper.engine.util.LogUtil;

/* JADX INFO: loaded from: classes.dex */
public class CosmosManager extends ParadiseBaseManager {
    public CosmosManager(Context context) {
        super(context);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    protected void initItem() {
        LogUtil.i("WallpaperManager", "initItem start", new Object[0]);
        this.scene.setTimeRatio(1.0f);
        this.scene.setCameraInfo("Cosmos/data/base_data.json");
        this.scene.setSceneElement("Cosmos/data/scene.json");
        this.scene.setAodData("Cosmos/data/aod_data.json");
        this.scene.setLockData("Cosmos/data/lock_data.json");
        this.scene.setLauncherData("Cosmos/data/launcher_data.json");
        LogUtil.i("WallpaperManager", "initItem end", new Object[0]);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    protected void initAod2LockAnimatorSet() {
        this.animatorController.setStateAnimatorSet(this.scene.setAnimatorSet("Cosmos/data/aod_lock_animation.json", this.propertySet), Constant.STATE_AOD, Constant.STATE_LOCK);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    protected void initAod2LauncherAnimatorSet() {
        this.animatorController.setStateAnimatorSet(this.scene.setAnimatorSet("Cosmos/data/aod_home_animation.json", this.propertySet), Constant.STATE_AOD, Constant.STATE_LAUNCHER);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    protected void initLock2AodAnimatorSet() {
        this.animatorController.setStateAnimatorSet(this.scene.setAnimatorSet("Cosmos/data/lock_aod_animation.json", this.propertySet), Constant.STATE_LOCK, Constant.STATE_AOD);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    protected void initLock2LauncherAnimatorSet() {
        this.animatorController.setStateAnimatorSet(this.scene.setAnimatorSet("Cosmos/data/lock_home_animation.json", this.propertySet), Constant.STATE_LOCK, Constant.STATE_LAUNCHER);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    protected void initLauncher2AodAnimatorSet() {
        this.animatorController.setStateAnimatorSet(this.scene.setAnimatorSet("Cosmos/data/home_aod_animation.json", this.propertySet), Constant.STATE_LAUNCHER, Constant.STATE_AOD);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    protected void lock() {
        super.lock();
        this.imageList.setFrameIndex(0.35f);
    }

    @Override // com.huawei.superwallpaper.engine.wallpaper.BaseWallpaperManager
    public void onSurfaceCreate() {
        super.onSurfaceCreate();
        if (Constant.STATE_AOD.equals(this.currentAction)) {
            this.imageList.setFrameIndex(0.0f);
        } else if (Constant.STATE_LOCK.equals(this.currentAction)) {
            this.imageList.setFrameIndex(0.35f);
        } else {
            this.imageList.setFrameIndex(1.0f);
        }
        this.imageList.setLockIndex(0.35f);
    }
}
