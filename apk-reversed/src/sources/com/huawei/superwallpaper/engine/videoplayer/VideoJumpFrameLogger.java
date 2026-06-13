package com.huawei.superwallpaper.engine.videoplayer;

import android.os.SystemClock;
import com.huawei.superwallpaper.engine.util.LogUtil;

/* JADX INFO: loaded from: classes.dex */
public class VideoJumpFrameLogger {
    private static final String TAG = "VideoJumpFrameLogger";
    private long videoFrameIntervalUs;
    private long currentValue = -1;
    private long currentTimeStamp = -1;
    private int frameCount = 0;
    private int overTimeFrameCount = 0;
    private long maxTimeCost = 0;
    private long totalOverTimeCost = 0;

    public VideoJumpFrameLogger(long j) {
        this.videoFrameIntervalUs = 16666L;
        this.videoFrameIntervalUs = j;
    }

    public void reset() {
        this.currentValue = -1L;
        this.currentTimeStamp = -1L;
        this.frameCount = 0;
        this.overTimeFrameCount = 0;
        this.maxTimeCost = 0L;
        this.totalOverTimeCost = 0L;
    }

    public void updateValue(long j) {
        int iAbs;
        long j2 = this.currentValue;
        if (j2 < 0) {
            this.currentValue = j;
            this.currentTimeStamp = SystemClock.elapsedRealtime();
            return;
        }
        if (Math.abs(j - j2) > this.videoFrameIntervalUs && (iAbs = ((int) (Math.abs(j - this.currentValue) + 2)) / ((int) this.videoFrameIntervalUs)) > 1) {
            LogUtil.i(TAG, "video jumpFrame : " + iAbs, new Object[0]);
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j3 = jElapsedRealtime - this.currentTimeStamp;
        if (j3 > 17) {
            LogUtil.d(TAG, "video frame " + j + " cost time : " + j3);
            this.overTimeFrameCount = this.overTimeFrameCount + 1;
            if (j3 > this.maxTimeCost) {
                this.maxTimeCost = j3;
            }
            this.totalOverTimeCost += j3;
        }
        this.currentValue = j;
        this.currentTimeStamp = jElapsedRealtime;
        this.frameCount++;
    }

    public void finish() {
        int i = this.overTimeFrameCount;
        LogUtil.e(TAG, "%d / %d, max = %d, ave = %d", Integer.valueOf(this.overTimeFrameCount), Integer.valueOf(this.frameCount), Long.valueOf(this.maxTimeCost), Long.valueOf(i > 0 ? this.totalOverTimeCost / ((long) i) : 0L));
        reset();
    }
}
