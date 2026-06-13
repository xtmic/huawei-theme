package com.huawei.superwallpaper.engine.videoplayer;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.view.Surface;
import androidx.core.location.LocationRequestCompat;
import com.huawei.superwallpaper.engine.HandlerWrapper;
import com.huawei.superwallpaper.engine.animation.Constant;
import com.huawei.superwallpaper.engine.util.DeviceUtil;
import com.huawei.superwallpaper.engine.util.LogUtil;
import com.huawei.superwallpaper.engine.videoplayer.MediaCodecWrapper;

/* JADX INFO: loaded from: classes.dex */
public class VideoPlayer {
    private static final int MESSAGE_DECODE_VIDEO = 4;
    private static final int MESSAGE_PAUSE_VIDEO = 2;
    private static final int MESSAGE_PLAY_VIDEO = 1;
    private static final int MESSAGE_PRELOAD_FINISH = 7;
    private static final int MESSAGE_PRELOAD_VIDEO = 6;
    private static final int MESSAGE_RELEASE_VIDEO = 3;
    private static final int MESSAGE_RENDER_VIDEO = 5;
    private static final long PROTECTED_DELAY_TIME = 5000;
    private static final long RELEASE_DELAY_TIME = 2000;
    private static final String TAG = VideoPlayer.class.getSimpleName();
    private final String assetsPath;
    private final boolean async;
    protected HandlerWrapper handlerWrapper;
    protected MediaCodecWrapper mediaCodecWrapper;
    protected MediaExtractorWrapper mediaExtractorWrapper;
    private OnPlayListener playListener;
    private Handler protectedHandler;
    private Runnable protectedRunnable;
    private Surface surface;
    private HandlerThread videoThread;
    protected final Object lock = new Object();
    protected long currentPtsHope = 0;
    protected long currentPts = 0;
    protected long targetPts = 0;
    protected long videoDuration = 0;
    protected long videoFrameIntervalUs = 16666;
    protected long frameRenderIntervalUs = 16666;
    private volatile PlayState playState = PlayState.Stop;
    protected volatile boolean looping = false;
    protected volatile boolean startDecode = false;
    protected volatile boolean startRender = false;
    private volatile boolean destroy = false;
    protected VideoJumpFrameLogger videoJumpFrameLogger = new VideoJumpFrameLogger(this.videoFrameIntervalUs);

    public interface OnPlayListener {
        default void onArriveFirstFrame() {
        }

        default void onArriveLastFrame() {
        }

        default void onDecodeStart() {
        }

        default void onDestroy() {
        }

        default void onFrameUpdate(long j) {
        }

        default void onRenderStart() {
        }

        default void onRequestRender() {
        }

        default void onVideoPlayEnd() {
        }

        default void onVideoPlayStart() {
        }
    }

    private enum PlayState {
        Playing,
        Paused,
        Stop,
        Preload
    }

    public VideoPlayer(Context context, String str, Surface surface, boolean z) {
        this.assetsPath = str;
        this.surface = surface;
        this.async = z;
        initHandler();
        initProtectedHandler();
        initMediaCodec(context);
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public int getVideoWidth() {
        MediaExtractorWrapper mediaExtractorWrapper = this.mediaExtractorWrapper;
        if (mediaExtractorWrapper != null) {
            return mediaExtractorWrapper.getVideoWidth();
        }
        return 1080;
    }

    public int getVideoHeight() {
        MediaExtractorWrapper mediaExtractorWrapper = this.mediaExtractorWrapper;
        if (mediaExtractorWrapper != null) {
            return mediaExtractorWrapper.getVideoHeight();
        }
        return 1920;
    }

    private void initMediaCodec(Context context) {
        this.mediaExtractorWrapper = new MediaExtractorWrapper(context);
        MediaCodecWrapper mediaCodecWrapper = new MediaCodecWrapper(this.mediaExtractorWrapper);
        this.mediaCodecWrapper = mediaCodecWrapper;
        mediaCodecWrapper.setOnPreloadListener(new MediaCodecWrapper.OnPreloadListener() { // from class: com.huawei.superwallpaper.engine.videoplayer.-$$Lambda$VideoPlayer$2bWbcTwNpQhA24ymeUHem3eOwQ4
            @Override // com.huawei.superwallpaper.engine.videoplayer.MediaCodecWrapper.OnPreloadListener
            public final void onPreloadFinish() {
                this.f$0.lambda$initMediaCodec$0$VideoPlayer();
            }
        });
    }

    public /* synthetic */ void lambda$initMediaCodec$0$VideoPlayer() {
        LogUtil.i(TAG, "preload finish", new Object[0]);
        this.handlerWrapper.sendEmptyMessage(7);
    }

    private void initProtectedHandler() {
        this.protectedHandler = new Handler(Looper.getMainLooper());
        this.protectedRunnable = new Runnable() { // from class: com.huawei.superwallpaper.engine.videoplayer.-$$Lambda$VideoPlayer$xl6PbEdBr92uDDnn9ArMXJCtoQw
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$initProtectedHandler$1$VideoPlayer();
            }
        };
    }

    public /* synthetic */ void lambda$initProtectedHandler$1$VideoPlayer() {
        LogUtil.i(TAG, "protectedRunnable effective, releasePlayer right now!", new Object[0]);
        if (isStop()) {
            return;
        }
        releasePlayer(0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initMediaExtractorWrapper() {
        if (this.mediaExtractorWrapper.getMediaExtractor() == null) {
            this.mediaExtractorWrapper.setDataSource(this.assetsPath);
            this.videoDuration = this.mediaExtractorWrapper.getDuration();
            LogUtil.i(TAG, "videoDuration = " + this.videoDuration, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean initPlayTimeArea(Message message) {
        long j = this.currentPts;
        long j2 = this.targetPts;
        Object obj = message.obj;
        if (obj instanceof PtsSE) {
            PtsSE ptsSE = (PtsSE) obj;
            j = ptsSE.startPts;
            j2 = ptsSE.endPts;
        }
        if (j2 == this.currentPts) {
            LogUtil.i(TAG, "currentPts == tPts = " + this.currentPts + " , do not need play", new Object[0]);
            setTargetPts(this.currentPts);
            return false;
        }
        long jMax = Math.max(0L, Math.min(j, this.videoDuration - this.videoFrameIntervalUs));
        long jMax2 = Math.max(0L, Math.min(j2, this.videoDuration - this.videoFrameIntervalUs));
        long j3 = this.videoDuration;
        long j4 = this.videoFrameIntervalUs;
        if (jMax2 == j3 - j4 && jMax == jMax2 && this.currentPts != jMax) {
            jMax = jMax2 - j4;
        }
        setCurrentPtsHope(jMax);
        setTargetPts(jMax2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initMediaCodecWrapper(boolean z) {
        if (this.mediaCodecWrapper.getDecoder() == null) {
            this.mediaCodecWrapper.setSurface(this.surface);
            this.mediaCodecWrapper.setInitSampleTime(this.currentPtsHope);
            this.mediaCodecWrapper.prepare();
            return;
        }
        boolean z2 = false;
        if (z) {
            LogUtil.i(TAG, "hasPreload, do do not flush mediaCodec", new Object[0]);
            return;
        }
        if (!DeviceUtil.isKirin() || this.mediaCodecWrapper.hasBufferCache()) {
            z2 = true;
        } else {
            LogUtil.i(TAG, "isKirin, mediaCodec has no buffer, no not need flush", new Object[0]);
        }
        if (z2) {
            try {
                this.mediaCodecWrapper.flush();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.getMessage(), e);
            }
        }
        this.mediaCodecWrapper.setInitSampleTime(this.currentPtsHope);
        this.mediaCodecWrapper.startToSampleTime();
    }

    private void initHandler() {
        Looper mainLooper = Looper.getMainLooper();
        if (this.async) {
            HandlerThread handlerThread = new HandlerThread("videoPlayer");
            this.videoThread = handlerThread;
            handlerThread.start();
            Looper looper = this.videoThread.getLooper();
            if (looper != null) {
                LogUtil.i(TAG, "videoThread start", new Object[0]);
                mainLooper = looper;
            }
        }
        Handler handler = new Handler(mainLooper) { // from class: com.huawei.superwallpaper.engine.videoplayer.VideoPlayer.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                OutputBuffer outputBufferPopSample = null;
                switch (message.what) {
                    case 1:
                        boolean z = VideoPlayer.this.playState == PlayState.Preload && VideoPlayer.this.mediaCodecWrapper.getDecoder() != null;
                        VideoPlayer.this.setPlayState(PlayState.Playing);
                        VideoPlayer.this.destroy = false;
                        VideoPlayer.this.startDecode = false;
                        VideoPlayer.this.startRender = false;
                        VideoPlayer.this.initMediaExtractorWrapper();
                        if (!VideoPlayer.this.initPlayTimeArea(message)) {
                            VideoPlayer.this.dealArrivedTargetFrame();
                            VideoPlayer.this.releasePlayer(VideoPlayer.RELEASE_DELAY_TIME);
                        } else {
                            if (VideoPlayer.this.currentPtsHope != 0) {
                                z = false;
                            }
                            VideoPlayer videoPlayer = VideoPlayer.this;
                            if (!videoPlayer.arrivedTargetFrame(videoPlayer.currentPtsHope, VideoPlayer.this.targetPts)) {
                                VideoPlayer.this.initMediaCodecWrapper(z);
                                LogUtil.i(VideoPlayer.TAG, "start Play", new Object[0]);
                                if (VideoPlayer.this.playListener != null) {
                                    VideoPlayer.this.playListener.onVideoPlayStart();
                                }
                                VideoPlayer.this.handlerWrapper.sendEmptyMessage(4);
                                VideoPlayer.this.handlerWrapper.sendEmptyMessage(5);
                            } else {
                                VideoPlayer.this.dealArrivedTargetFrame();
                            }
                        }
                        break;
                    case 2:
                        LogUtil.i(VideoPlayer.TAG, "pauseVideo", new Object[0]);
                        VideoPlayer.this.setPlayState(PlayState.Paused);
                        VideoPlayer.this.releasePlayer(VideoPlayer.RELEASE_DELAY_TIME);
                        break;
                    case 3:
                        LogUtil.i(VideoPlayer.TAG, "releaseVideo", new Object[0]);
                        VideoPlayer.this.setPlayState(PlayState.Stop);
                        if (VideoPlayer.this.mediaCodecWrapper != null) {
                            VideoPlayer.this.mediaCodecWrapper.release();
                        }
                        if (VideoPlayer.this.mediaExtractorWrapper != null) {
                            VideoPlayer.this.mediaExtractorWrapper.release();
                        }
                        if (VideoPlayer.this.playListener != null) {
                            VideoPlayer.this.playListener.onVideoPlayEnd();
                        }
                        if (VideoPlayer.this.destroy && VideoPlayer.this.playListener != null) {
                            VideoPlayer.this.mediaCodecWrapper.setOnPreloadListener(null);
                            VideoPlayer.this.playListener.onDestroy();
                            VideoPlayer.this.playListener = null;
                            break;
                        }
                        break;
                    case 4:
                        if (VideoPlayer.this.mediaCodecWrapper != null) {
                            long jElapsedRealtime = SystemClock.elapsedRealtime();
                            try {
                                if (VideoPlayer.this.mediaCodecWrapper.writeSample()) {
                                    if (!VideoPlayer.this.startDecode) {
                                        VideoPlayer.this.startDecode = true;
                                        if (VideoPlayer.this.playListener != null) {
                                            VideoPlayer.this.playListener.onDecodeStart();
                                        }
                                    }
                                    VideoPlayer.this.decodeNextFrame();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.e(VideoPlayer.TAG, e.getMessage(), e);
                            }
                            if (!VideoPlayer.this.startRender) {
                                VideoPlayer.this.handlerWrapper.sendEmptyMessage(4);
                            } else {
                                VideoPlayer.this.handlerWrapper.sendEmptyMessageDelayed(4, Math.max((VideoPlayer.this.frameRenderIntervalUs / 1000) - (SystemClock.elapsedRealtime() - jElapsedRealtime), 0L));
                            }
                            break;
                        }
                        break;
                    case 5:
                        if (VideoPlayer.this.mediaCodecWrapper != null) {
                            long jElapsedRealtime2 = SystemClock.elapsedRealtime();
                            try {
                                outputBufferPopSample = VideoPlayer.this.mediaCodecWrapper.popSample();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            if (outputBufferPopSample != null) {
                                if (!VideoPlayer.this.startRender) {
                                    VideoPlayer.this.startRender = true;
                                    if (VideoPlayer.this.playListener != null) {
                                        VideoPlayer.this.playListener.onRenderStart();
                                    }
                                    VideoPlayer.this.videoJumpFrameLogger.reset();
                                }
                                VideoPlayer.this.setCurrentPts(outputBufferPopSample.presentationTimeUs);
                                VideoPlayer.this.videoJumpFrameLogger.updateValue(VideoPlayer.this.currentPts);
                                if (!VideoPlayer.this.looping) {
                                    VideoPlayer videoPlayer2 = VideoPlayer.this;
                                    if (videoPlayer2.arrivedTargetFrame(videoPlayer2.currentPts, VideoPlayer.this.targetPts)) {
                                        VideoPlayer.this.dealArrivedTargetFrame();
                                        VideoPlayer.this.videoJumpFrameLogger.finish();
                                    }
                                }
                                VideoPlayer.this.handlerWrapper.sendEmptyMessageDelayed(5, Math.max((VideoPlayer.this.frameRenderIntervalUs / 1000) - (SystemClock.elapsedRealtime() - jElapsedRealtime2), 0L));
                            } else {
                                VideoPlayer.this.handlerWrapper.sendEmptyMessageDelayed(5, 0L);
                            }
                            break;
                        }
                        break;
                    case 6:
                        if (VideoPlayer.this.playState == PlayState.Preload) {
                            LogUtil.i(VideoPlayer.TAG, "preloading , do not preload again", new Object[0]);
                        } else {
                            VideoPlayer.this.setPlayState(PlayState.Preload);
                            VideoPlayer.this.destroy = false;
                            VideoPlayer.this.startDecode = false;
                            VideoPlayer.this.startRender = false;
                            VideoPlayer.this.mediaCodecWrapper.preloadReady();
                            LogUtil.i(VideoPlayer.TAG, "reset currentPts before preload", new Object[0]);
                            VideoPlayer.this.setCurrentPts(0L);
                            VideoPlayer.this.initMediaExtractorWrapper();
                            if (!VideoPlayer.this.initPlayTimeArea(message)) {
                                VideoPlayer.this.releasePlayer(VideoPlayer.RELEASE_DELAY_TIME);
                            } else {
                                VideoPlayer.this.initMediaCodecWrapper(false);
                                LogUtil.i(VideoPlayer.TAG, "start Preload", new Object[0]);
                                VideoPlayer.this.handlerWrapper.sendEmptyMessage(4);
                            }
                        }
                        break;
                    case 7:
                        if (VideoPlayer.this.playState == PlayState.Preload) {
                            VideoPlayer.this.releasePlayer(VideoPlayer.RELEASE_DELAY_TIME);
                        } else {
                            LogUtil.i(VideoPlayer.TAG, "preload finish, but video is playing", new Object[0]);
                        }
                        break;
                }
            }
        };
        this.handlerWrapper = new HandlerWrapper(handler);
        if (!this.async || handler.getLooper() == Looper.getMainLooper()) {
            return;
        }
        handler.post(new Runnable() { // from class: com.huawei.superwallpaper.engine.videoplayer.-$$Lambda$VideoPlayer$rYBQj1JS7JqUD2z9of2FkiM0oBI
            @Override // java.lang.Runnable
            public final void run() {
                Process.setThreadPriority(-20);
            }
        });
    }

    protected void dealArrivedTargetFrame() {
        LogUtil.e(TAG, "dPts < mFrameInterval , so break. currentPts = " + this.currentPts + " targetPts = " + this.targetPts);
        releasePlayer(RELEASE_DELAY_TIME);
        if (this.playListener != null && Math.abs(this.targetPts) < this.videoFrameIntervalUs / 2) {
            this.playListener.onArriveFirstFrame();
            LogUtil.i(TAG, "onArriveFirstFrame", new Object[0]);
        }
        if (this.playListener == null || Math.abs(this.targetPts - (this.videoDuration - this.videoFrameIntervalUs)) >= this.videoFrameIntervalUs / 2) {
            return;
        }
        this.playListener.onArriveLastFrame();
        LogUtil.i(TAG, "onArriveLastFrame", new Object[0]);
    }

    protected void decodeNextFrame() {
        boolean z = true;
        if (this.startRender ? this.currentPts >= this.targetPts : this.currentPtsHope >= this.targetPts) {
            z = false;
        }
        long sampleTime = this.mediaExtractorWrapper.getMediaExtractor().getSampleTime();
        if (!z) {
            if (this.looping) {
                long j = this.videoFrameIntervalUs;
                if (sampleTime < j / 2) {
                    this.mediaExtractorWrapper.seekTo(this.videoDuration - j);
                    LogUtil.i(TAG, "loop play", new Object[0]);
                    return;
                }
            }
            this.mediaExtractorWrapper.seekTo(Math.max(0L, sampleTime - this.videoFrameIntervalUs));
            return;
        }
        if (this.looping && Math.abs(sampleTime - (this.videoDuration - this.videoFrameIntervalUs)) < this.videoFrameIntervalUs / 2) {
            this.mediaExtractorWrapper.seekTo(0L);
            LogUtil.i(TAG, "loop play 2", new Object[0]);
        } else {
            long j2 = this.videoFrameIntervalUs;
            this.mediaExtractorWrapper.seekTo(Math.min(sampleTime + j2, this.videoDuration - j2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean arrivedTargetFrame(long j, long j2) {
        return Math.abs(j - j2) < this.videoFrameIntervalUs / 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPlayState(PlayState playState) {
        this.playState = playState;
    }

    public void setPlayListener(OnPlayListener onPlayListener) {
        this.playListener = onPlayListener;
    }

    public void preloadFirstFrame() {
        if (this.handlerWrapper.hasMessages(4) || this.handlerWrapper.hasMessages(5)) {
            return;
        }
        this.handlerWrapper.removeCallbacksAndMessages();
        Message messageObtain = Message.obtain();
        messageObtain.what = 6;
        messageObtain.obj = new PtsSE(0L, LocationRequestCompat.PASSIVE_INTERVAL);
        this.handlerWrapper.sendMessage(messageObtain);
        this.protectedHandler.removeCallbacksAndMessages(null);
    }

    public void play(long j) {
        playTo(this.currentPts + (j * 1000));
    }

    public void playTo(long j) {
        playIn(this.currentPts, j);
    }

    public void playIn(long j, long j2) {
        this.handlerWrapper.removeCallbacksAndMessages();
        Message messageObtain = Message.obtain();
        messageObtain.what = 1;
        messageObtain.obj = new PtsSE(j, j2);
        this.handlerWrapper.sendMessage(messageObtain);
        startProtect();
    }

    private void startProtect() {
        LogUtil.i(TAG, "startProtect", new Object[0]);
        this.protectedHandler.removeCallbacksAndMessages(null);
        this.protectedHandler.postDelayed(this.protectedRunnable, PROTECTED_DELAY_TIME);
    }

    public void pause() {
        this.handlerWrapper.removeCallbacksAndMessages();
        this.handlerWrapper.sendEmptyMessage(2);
    }

    public void resume() {
        if (isPaused() || isStop()) {
            this.handlerWrapper.removeCallbacksAndMessages();
            this.handlerWrapper.sendEmptyMessage(1);
        }
    }

    public void stop() {
        releasePlayer(0L);
    }

    public void destroy() {
        this.destroy = true;
        releasePlayer(0L);
        HandlerThread handlerThread = this.videoThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            try {
                try {
                    this.videoThread.join();
                    this.videoThread = null;
                    LogUtil.i(TAG, "videoThread quit", new Object[0]);
                } catch (InterruptedException e) {
                    LogUtil.e(TAG, "videoThread join ", e);
                    LogUtil.i(TAG, "videoThread quit", new Object[0]);
                }
            } catch (Throwable th) {
                LogUtil.i(TAG, "videoThread quit", new Object[0]);
                throw th;
            }
        }
    }

    public void destroySync() {
        this.destroy = true;
        this.protectedHandler.removeCallbacksAndMessages(null);
        this.handlerWrapper.removeCallbacksAndMessages();
        MediaCodecWrapper mediaCodecWrapper = this.mediaCodecWrapper;
        if (mediaCodecWrapper != null) {
            mediaCodecWrapper.release();
            this.mediaCodecWrapper.setOnPreloadListener(null);
        }
        MediaExtractorWrapper mediaExtractorWrapper = this.mediaExtractorWrapper;
        if (mediaExtractorWrapper != null) {
            mediaExtractorWrapper.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releasePlayer(long j) {
        this.handlerWrapper.removeCallbacksAndMessages();
        this.handlerWrapper.sendEmptyMessageDelayed(3, j);
    }

    public void releasePlayerSync() {
        this.protectedHandler.removeCallbacksAndMessages(null);
        this.handlerWrapper.removeCallbacksAndMessages();
        MediaCodecWrapper mediaCodecWrapper = this.mediaCodecWrapper;
        if (mediaCodecWrapper != null) {
            mediaCodecWrapper.release();
        }
        MediaExtractorWrapper mediaExtractorWrapper = this.mediaExtractorWrapper;
        if (mediaExtractorWrapper != null) {
            mediaExtractorWrapper.release();
        }
    }

    public void initState(String str, boolean z) {
        if (str == null) {
            return;
        }
        if (!z) {
            if (Constant.STATE_AOD.equals(str)) {
                playIn(this.videoFrameIntervalUs, 0L);
                return;
            }
            if (Constant.STATE_LAUNCHER.equals(str)) {
                long j = this.videoDuration;
                if (j <= 0) {
                    playIn(LocationRequestCompat.PASSIVE_INTERVAL, LocationRequestCompat.PASSIVE_INTERVAL);
                    return;
                } else {
                    long j2 = this.videoFrameIntervalUs;
                    playIn(j - (2 * j2), j - j2);
                    return;
                }
            }
            return;
        }
        if (Constant.STATE_AOD.equals(str)) {
            setCurrentPts(0L);
        } else if (Constant.STATE_LAUNCHER.equals(str)) {
            setCurrentPts(LocationRequestCompat.PASSIVE_INTERVAL);
        }
        releasePlayer(0L);
    }

    public void setLooping(boolean z) {
        this.looping = z;
    }

    public boolean isPlaying() {
        return this.playState == PlayState.Playing;
    }

    public boolean isPaused() {
        return this.playState == PlayState.Paused;
    }

    public boolean isStop() {
        return this.playState == PlayState.Stop;
    }

    protected void setCurrentPtsHope(long j) {
        synchronized (this.lock) {
            this.currentPtsHope = j;
            LogUtil.i(TAG, "setCurrentPtsHope = " + j, new Object[0]);
        }
    }

    protected void setCurrentPts(long j) {
        synchronized (this.lock) {
            this.currentPts = j;
            LogUtil.i(TAG, "setCurrentPts = " + j, new Object[0]);
            if (this.playListener != null) {
                this.playListener.onFrameUpdate(j);
            }
        }
    }

    protected void setTargetPts(long j) {
        synchronized (this.lock) {
            this.targetPts = j;
            LogUtil.i(TAG, "setTargetPts = " + j, new Object[0]);
        }
    }

    private static class PtsSE {
        long endPts;
        long startPts;

        public PtsSE(long j, long j2) {
            this.startPts = 0L;
            this.endPts = 0L;
            this.startPts = j;
            this.endPts = j2;
        }
    }
}
