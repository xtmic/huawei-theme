package com.huawei.superwallpaper.engine.videoplayer;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.view.Surface;
import com.huawei.superwallpaper.engine.util.LogUtil;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

/* JADX INFO: loaded from: classes.dex */
public class MediaCodecWrapper {
    public static final int Configured = 1;
    public static final int EndOfStream = 4;
    public static final int Error = -1;
    public static final int Flushed = 2;
    public static final int Null = -2;
    public static final int Released = 5;
    public static final int Running = 3;
    private static final String TAG = "MediaCodecWrapper";
    public static final int Uninitialized = 0;
    private MediaCodec mediaDecoder;
    private final MediaExtractorWrapper mediaExtractorWrapper;
    private OnPreloadListener preloadListener;
    private Surface surface;
    private int mState = -2;
    private long initSampleTime = 0;
    private volatile boolean preload = false;
    private final Queue<Integer> availableInputBuffers = new ArrayDeque();
    private final Queue<OutputBuffer> availableOutputBuffers = new ArrayDeque();
    private final MediaCodec.Callback callback = new MediaCodec.Callback() { // from class: com.huawei.superwallpaper.engine.videoplayer.MediaCodecWrapper.1
        @Override // android.media.MediaCodec.Callback
        public void onInputBufferAvailable(MediaCodec mediaCodec, int i) {
            MediaCodecWrapper.this.availableInputBuffers.add(Integer.valueOf(i));
        }

        @Override // android.media.MediaCodec.Callback
        public void onOutputBufferAvailable(MediaCodec mediaCodec, int i, MediaCodec.BufferInfo bufferInfo) {
            MediaCodecWrapper.this.availableOutputBuffers.add(new OutputBuffer(bufferInfo.presentationTimeUs, i));
            if (MediaCodecWrapper.this.preload) {
                MediaCodecWrapper.this.preload = false;
                if (MediaCodecWrapper.this.preloadListener != null) {
                    MediaCodecWrapper.this.preloadListener.onPreloadFinish();
                }
            }
        }

        @Override // android.media.MediaCodec.Callback
        public void onError(MediaCodec mediaCodec, MediaCodec.CodecException codecException) {
            LogUtil.e(MediaCodecWrapper.TAG, "onError", codecException);
            if ((codecException.isRecoverable() || codecException.isTransient()) && MediaCodecWrapper.this.mediaDecoder != null) {
                MediaCodecWrapper.this.stop();
            } else {
                MediaCodecWrapper.this.mState = -1;
            }
            MediaCodecWrapper.this.prepare();
        }

        @Override // android.media.MediaCodec.Callback
        public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
            LogUtil.i(MediaCodecWrapper.TAG, "onOutputFormatChanged", new Object[0]);
        }
    };

    public interface OnPreloadListener {
        void onPreloadFinish();
    }

    public void setOnPreloadListener(OnPreloadListener onPreloadListener) {
        this.preloadListener = onPreloadListener;
    }

    public MediaCodecWrapper(MediaExtractorWrapper mediaExtractorWrapper) {
        this.mediaExtractorWrapper = mediaExtractorWrapper;
    }

    public boolean hasBufferCache() {
        return this.availableInputBuffers.size() > 0 || this.availableOutputBuffers.size() > 0;
    }

    public void preloadReady() {
        this.preload = true;
    }

    public void prepare() {
        try {
            LogUtil.i(TAG, "prepare start state : %d", Integer.valueOf(this.mState));
            if (this.mState == -1) {
                LogUtil.i(TAG, "1 release MediaCodec", new Object[0]);
                release();
            }
            if (this.mediaDecoder == null) {
                LogUtil.i(TAG, "2 create MediaCodec", new Object[0]);
                MediaCodec mediaCodecCreateDecoderByType = MediaCodec.createDecoderByType(this.mediaExtractorWrapper.getVideoFormat().getString("mime"));
                this.mediaDecoder = mediaCodecCreateDecoderByType;
                mediaCodecCreateDecoderByType.setCallback(this.callback);
                this.mState = 0;
            }
            if (this.mState == 0) {
                LogUtil.i(TAG, "3 configure MediaCodec, startToSampleTime", new Object[0]);
                this.mediaDecoder.configure(this.mediaExtractorWrapper.getVideoFormat(), this.surface, (MediaCrypto) null, 0);
                startToSampleTime();
                this.mState = 3;
            } else if (this.mState == 1) {
                LogUtil.i(TAG, "4 startToSampleTime", new Object[0]);
                startToSampleTime();
                this.mState = 3;
            } else if (this.mState == 3 || this.mState == 4) {
                LogUtil.i(TAG, "5 flush, startToSampleTime", new Object[0]);
                flush();
                startToSampleTime();
                this.mState = 3;
            }
            LogUtil.i(TAG, "prepare end", new Object[0]);
        } catch (Exception e) {
            this.mState = -1;
            e.printStackTrace();
            LogUtil.e(TAG, "prepare error", e);
            release();
        }
    }

    public void startToSampleTime() {
        MediaExtractorWrapper mediaExtractorWrapper = this.mediaExtractorWrapper;
        if (mediaExtractorWrapper != null && this.initSampleTime > 0) {
            try {
                mediaExtractorWrapper.getMediaExtractor().seekTo(this.initSampleTime, 2);
                LogUtil.i(TAG, "initSeek mLastSampleTime = " + this.initSampleTime, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, "initSeek exception: message = " + e.getMessage());
            }
        }
        this.mediaDecoder.start();
    }

    public boolean writeSample() {
        int sampleData;
        if (!this.availableInputBuffers.isEmpty()) {
            try {
                MediaExtractor mediaExtractor = this.mediaExtractorWrapper.getMediaExtractor();
                long sampleTime = mediaExtractor.getSampleTime();
                int sampleFlags = mediaExtractor.getSampleFlags();
                int iIntValue = this.availableInputBuffers.remove().intValue();
                ByteBuffer inputBuffer = this.mediaDecoder.getInputBuffer(iIntValue);
                if (inputBuffer == null || (sampleData = mediaExtractor.readSampleData(inputBuffer, 0)) < 0) {
                    return false;
                }
                this.mediaDecoder.queueInputBuffer(iIntValue, 0, sampleData, sampleTime, sampleFlags);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, "writeSample error", e);
            }
        }
        return false;
    }

    public OutputBuffer popSample() {
        if (this.availableOutputBuffers.isEmpty()) {
            return null;
        }
        OutputBuffer outputBufferRemove = this.availableOutputBuffers.remove();
        try {
            this.mediaDecoder.releaseOutputBuffer(outputBufferRemove.index, true);
            return outputBufferRemove;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void release() {
        if (this.mediaDecoder != null) {
            LogUtil.i(TAG, "mDecoder release start", new Object[0]);
            try {
                this.mediaDecoder.release();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, "release error", e);
            }
            this.mediaDecoder = null;
        }
        this.mState = 5;
        this.availableInputBuffers.clear();
        this.availableOutputBuffers.clear();
        LogUtil.i(TAG, "mDecoder release end", new Object[0]);
    }

    public void flush() {
        LogUtil.i(TAG, "flush", new Object[0]);
        if (this.mediaDecoder != null) {
            this.availableInputBuffers.clear();
            this.availableOutputBuffers.clear();
            this.mediaDecoder.flush();
            this.mState = 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stop() {
        if (this.mediaDecoder != null) {
            this.availableInputBuffers.clear();
            this.availableOutputBuffers.clear();
            this.mediaDecoder.stop();
            this.mState = 0;
        }
    }

    public MediaCodec getDecoder() {
        return this.mediaDecoder;
    }

    public Surface getSurface() {
        return this.surface;
    }

    public void setInitSampleTime(long j) {
        if (j >= 0) {
            this.initSampleTime = j;
            LogUtil.i(TAG, "setInitSampleTime = " + this.initSampleTime, new Object[0]);
        }
    }

    public long getSampleTime() {
        return this.initSampleTime;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }
}
