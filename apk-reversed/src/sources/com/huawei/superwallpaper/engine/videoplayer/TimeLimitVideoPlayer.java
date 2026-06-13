package com.huawei.superwallpaper.engine.videoplayer;

import android.content.Context;
import android.view.Surface;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class TimeLimitVideoPlayer extends VideoPlayer {
    private static final String TAG = "TimeLimitVideoPlayer";
    private final AtomicInteger freeDecodeCount;
    private volatile long surPlusAnimateTime;

    public TimeLimitVideoPlayer(Context context, String str, Surface surface) {
        super(context, str, surface, false);
        this.surPlusAnimateTime = 0L;
        this.freeDecodeCount = new AtomicInteger(0);
    }

    public TimeLimitVideoPlayer(Context context, String str, Surface surface, boolean z) {
        super(context, str, surface, z);
        this.surPlusAnimateTime = 0L;
        this.freeDecodeCount = new AtomicInteger(0);
    }

    public void setSurPlusAnimateTime(long j) {
        this.surPlusAnimateTime = j * 1000;
    }

    @Override // com.huawei.superwallpaper.engine.videoplayer.VideoPlayer
    public void playIn(long j, long j2) {
        super.playIn(j, j2);
        this.freeDecodeCount.set(0);
    }

    @Override // com.huawei.superwallpaper.engine.videoplayer.VideoPlayer
    public void playTo(long j) {
        super.playTo(j);
        this.freeDecodeCount.set(0);
    }

    @Override // com.huawei.superwallpaper.engine.videoplayer.VideoPlayer
    protected void decodeNextFrame() {
        long j;
        long jMin;
        if (!this.startRender) {
            super.decodeNextFrame();
            this.freeDecodeCount.getAndAdd(1);
            return;
        }
        boolean z = this.currentPts < this.targetPts;
        long sampleTime = this.mediaExtractorWrapper.getMediaExtractor().getSampleTime();
        if (!z) {
            j = sampleTime - this.videoFrameIntervalUs;
        } else {
            j = sampleTime + this.videoFrameIntervalUs;
        }
        if (Math.abs(this.targetPts - this.currentPts) - this.surPlusAnimateTime > ((long) this.freeDecodeCount.get()) * this.videoFrameIntervalUs) {
            if (!z) {
                j -= this.videoFrameIntervalUs;
            } else {
                j += this.videoFrameIntervalUs;
            }
        }
        if (!z) {
            jMin = Math.max(0L, j);
        } else {
            jMin = Math.min(j, this.videoDuration - this.videoFrameIntervalUs);
        }
        this.mediaExtractorWrapper.seekTo(jMin);
    }
}
