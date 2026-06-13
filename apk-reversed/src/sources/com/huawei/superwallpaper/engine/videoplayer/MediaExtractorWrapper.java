package com.huawei.superwallpaper.engine.videoplayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import com.huawei.superwallpaper.engine.util.LogUtil;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class MediaExtractorWrapper {
    private static final String TAG = "MediaExtractorWrapper";
    private static final int VALUE_INVALID = -1;
    private final Context context;
    private MediaExtractor mediaExtractor;
    private MediaFormat videoFormat;
    private String videoMime;
    private int trackCount = 0;
    private int videoTrack = -1;
    private long duration = 0;
    private int frameRate = -1;
    private int frameCount = -1;

    public MediaExtractorWrapper(Context context) {
        this.context = context;
    }

    public void setDataSource(String str) {
        LogUtil.i(TAG, "setDataSource path : %s", str);
        this.mediaExtractor = new MediaExtractor();
        try {
            AssetFileDescriptor assetFileDescriptorOpenFd = this.context.getAssets().openFd(str);
            this.mediaExtractor.setDataSource(assetFileDescriptorOpenFd.getFileDescriptor(), assetFileDescriptorOpenFd.getStartOffset(), assetFileDescriptorOpenFd.getLength());
            assetFileDescriptorOpenFd.close();
            this.trackCount = this.mediaExtractor.getTrackCount();
            for (int i = 0; i < this.trackCount; i++) {
                this.mediaExtractor.unselectTrack(i);
            }
            findVideoTrack();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.i(TAG, "setDataSource end", new Object[0]);
    }

    public void findVideoTrack() {
        int trackCount = this.mediaExtractor.getTrackCount();
        LogUtil.i(TAG, "findVideoTrack track count : %d", Integer.valueOf(this.trackCount));
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = this.mediaExtractor.getTrackFormat(i);
            String string = trackFormat.getString("mime");
            if (string.contains("video/")) {
                this.mediaExtractor.selectTrack(i);
                this.videoTrack = i;
                this.videoFormat = trackFormat;
                this.videoMime = string;
                return;
            }
        }
    }

    public int getVideoTrack() {
        return this.videoTrack;
    }

    public MediaFormat getVideoFormat() {
        return this.videoFormat;
    }

    public String getVideoMime() {
        return this.videoMime;
    }

    public MediaExtractor getMediaExtractor() {
        return this.mediaExtractor;
    }

    public int getFrameRate() {
        int i = this.frameRate;
        if (i > 0) {
            return i;
        }
        MediaFormat mediaFormat = this.videoFormat;
        if (mediaFormat != null) {
            if (mediaFormat.containsKey("frame-rate")) {
                this.frameRate = this.videoFormat.getInteger("frame-rate");
            } else {
                LogUtil.e(TAG, "KEY_FRAME_RATE not found");
            }
        }
        return this.frameRate;
    }

    public void seekToIndex(int i) {
        if (this.mediaExtractor != null) {
            long frameTime = getFrameTime(i);
            LogUtil.i(TAG, "seekToIndex : index = %d, frameTime = %d", Integer.valueOf(i), Long.valueOf(frameTime));
            this.mediaExtractor.seekTo(frameTime, 2);
        }
    }

    public void seekTo(long j) {
        if (this.mediaExtractor != null) {
            LogUtil.i(TAG, "seekTo : frameTime = %d", Long.valueOf(j));
            this.mediaExtractor.seekTo(j, 2);
        }
    }

    public int getVideoWidth() {
        MediaFormat mediaFormat = this.videoFormat;
        if (mediaFormat == null) {
            return -1;
        }
        if (mediaFormat.containsKey("width")) {
            return this.videoFormat.getInteger("width");
        }
        LogUtil.e(TAG, "KEY_WIDTH not found");
        return -1;
    }

    public int getVideoHeight() {
        MediaFormat mediaFormat = this.videoFormat;
        if (mediaFormat == null) {
            return -1;
        }
        if (mediaFormat.containsKey("height")) {
            return this.videoFormat.getInteger("height");
        }
        LogUtil.e(TAG, "KEY_HEIGHT not found");
        return -1;
    }

    public int getMaxInputSize() {
        MediaFormat mediaFormat = this.videoFormat;
        if (mediaFormat == null) {
            return -1;
        }
        if (mediaFormat.containsKey("max-input-size")) {
            return this.videoFormat.getInteger("max-input-size");
        }
        LogUtil.e(TAG, "KEY_MAX_INPUT_SIZE not found");
        return -1;
    }

    public long getDuration() {
        long j = this.duration;
        if (j > 0) {
            return j;
        }
        MediaFormat mediaFormat = this.videoFormat;
        if (mediaFormat != null) {
            if (mediaFormat.containsKey("durationUs")) {
                this.duration = this.videoFormat.getLong("durationUs");
            } else {
                LogUtil.e(TAG, "KEY_DURATION not found");
            }
        }
        return this.duration;
    }

    public int getRotation() {
        MediaFormat mediaFormat = this.videoFormat;
        if (mediaFormat == null) {
            return 0;
        }
        if (mediaFormat.containsKey("rotation-degrees")) {
            return this.videoFormat.getInteger("rotation-degrees");
        }
        LogUtil.e(TAG, "KEY_ROTATION not found");
        return 0;
    }

    public int getSampleRate() {
        MediaFormat mediaFormat = this.videoFormat;
        if (mediaFormat == null) {
            return -1;
        }
        if (mediaFormat.containsKey("sample-rate")) {
            return this.videoFormat.getInteger("sample-rate");
        }
        LogUtil.e(TAG, "KEY_SAMPLE_RATE not found");
        return -1;
    }

    public int getBitRate() {
        MediaFormat mediaFormat = this.videoFormat;
        if (mediaFormat == null) {
            return -1;
        }
        if (mediaFormat.containsKey("bitrate")) {
            return this.videoFormat.getInteger("bitrate");
        }
        LogUtil.e(TAG, "KEY_BIT_RATE not found");
        return -1;
    }

    public long getFrameInterval() {
        if (getDuration() <= 0 || getFrameCount() <= 0) {
            return -1L;
        }
        return getDuration() / ((long) getFrameCount());
    }

    public int getFrameCount() {
        int i = this.frameCount;
        if (i > 0) {
            return i;
        }
        if (getFrameRate() <= 0 || getDuration() <= 0) {
            return 0;
        }
        int duration = (int) (((getDuration() / 1000000.0f) * getFrameRate()) + 0.1f);
        this.frameCount = duration;
        return duration;
    }

    public long getFrameTime(int i) {
        if (getDuration() <= 0 || getFrameCount() <= 0) {
            return 0L;
        }
        return (long) (((getDuration() / getFrameCount()) * i) + 0.5f);
    }

    public int getIndex(long j) {
        if (this.frameRate == -1) {
            getFrameRate();
        }
        return Math.round((j * ((long) this.frameRate)) / 1000000.0f);
    }

    public void setRotation(int i) {
        LogUtil.i(TAG, "setRotation : %d", Integer.valueOf(i));
        this.videoFormat.setInteger("rotation-degrees", i);
    }

    public void release() {
        LogUtil.i(TAG, "release start", new Object[0]);
        if (this.mediaExtractor != null) {
            LogUtil.i(TAG, "release extractor", new Object[0]);
            this.mediaExtractor.release();
            this.mediaExtractor = null;
        }
        this.trackCount = 0;
        this.videoTrack = -1;
        this.frameRate = -1;
        LogUtil.i(TAG, "release end", new Object[0]);
    }
}
