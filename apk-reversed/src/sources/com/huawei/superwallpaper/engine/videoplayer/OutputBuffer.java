package com.huawei.superwallpaper.engine.videoplayer;

/* JADX INFO: loaded from: classes.dex */
public class OutputBuffer {
    public static final long UNKNOWN_TIME = -1;
    public int flags;
    public int index;
    public long presentationTimeUs;
    public int rotation;

    public OutputBuffer() {
        this.presentationTimeUs = -1L;
        this.rotation = 0;
        this.flags = 0;
        this.presentationTimeUs = -1L;
        this.index = -1;
        this.rotation = 0;
    }

    public OutputBuffer(long j, int i) {
        this.presentationTimeUs = -1L;
        this.rotation = 0;
        this.flags = 0;
        this.presentationTimeUs = j;
        this.index = i;
        this.rotation = 0;
        this.flags = 0;
    }

    public OutputBuffer(long j, int i, int i2, int i3) {
        this.presentationTimeUs = -1L;
        this.rotation = 0;
        this.flags = 0;
        this.presentationTimeUs = j;
        this.index = i;
        this.rotation = i2;
        this.flags = i3;
    }

    public boolean isMatch(OutputBuffer outputBuffer) {
        return Math.abs(this.presentationTimeUs - outputBuffer.presentationTimeUs) < 1000 && this.rotation == outputBuffer.rotation;
    }

    public void resetTime() {
        this.presentationTimeUs = -1L;
    }

    public String toString() {
        return "{t=" + this.presentationTimeUs + ", i=" + this.index + ", r=" + this.rotation + "}";
    }
}
