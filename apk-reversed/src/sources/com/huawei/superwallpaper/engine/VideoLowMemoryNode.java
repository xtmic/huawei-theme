package com.huawei.superwallpaper.engine;

import android.content.Context;
import com.huawei.superwallpaper.engine.animation.Constant;

/* JADX INFO: loaded from: classes.dex */
public class VideoLowMemoryNode extends VideoImageNode {
    public VideoLowMemoryNode(Context context, String str, String str2) {
        super(context, str, str2);
    }

    @Override // com.huawei.superwallpaper.engine.VideoImageNode, com.huawei.superwallpaper.engine.Node
    public void initGLContext() {
        super.initGLContext();
        if (!this.hasStartImage || !this.hasEndImage) {
            throw new RuntimeException("you must set startImage and endImage when use VideoLowMemoryNode");
        }
        if (this.mTextureIndex == 1.0f) {
            this.mVideoPlayer.initState(Constant.STATE_LAUNCHER, this.hasEndImage);
        } else if (this.mTextureIndex == 0.0f) {
            this.mVideoPlayer.initState(Constant.STATE_AOD, this.hasStartImage);
        }
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void onPause() {
        super.onPause();
        if (this.mVideoPlayer != null) {
            this.mVideoPlayer.setPlayListener(null);
            this.mVideoPlayer.destroySync();
            this.mVideoPlayer = null;
        }
        if (this.mSurface != null) {
            this.mSurface.release();
            this.mSurface = null;
        }
        if (this.mSurfaceTexture != null) {
            this.mSurfaceTexture.release();
            this.mSurfaceTexture = null;
        }
    }
}
