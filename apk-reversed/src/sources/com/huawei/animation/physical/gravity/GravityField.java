package com.huawei.animation.physical.gravity;

import android.os.SystemClock;
import android.util.Log;
import android.view.Choreographer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class GravityField implements Choreographer.FrameCallback {
    private static final float DEFAULT_AMPLITUDE_COEFFICIENT = 20.0f;
    private static final float DEFAULT_GRAVITY_COEFFICIENT = 1.0E7f;
    private static final long DEFAULT_GRAVITY_VELOCITY = 30000000;
    private static final long DEFAULT_IMPACT_DURATION = 500;
    private static final float DEFAULT_IMPACT_MIN_RADIUS = 0.0f;
    private static final float DEFAULT_IMPACT_RADIUS = 500.0f;
    private static final long NANOS_TO_MILLS = 1000000;
    private static final String TAG = GravityField.class.getName();
    private float amplitudeCoefficient;
    private List<GravityAsteroid> asteroidsList;
    private long duration;
    private float firstMotionDistance;
    private float gravityCoefficient;
    private GravityType gravityType;
    private float gravityVelocity;
    private float impactMinimumRadius;
    private float impactRadius;
    private boolean isStart;
    private float sourceAbscissa;
    private float sourceOrdinate;
    private long startTime;

    public enum GravityType {
        GRAVITATION,
        REPULSION
    }

    public GravityField() {
        this(0.0f, 0.0f);
    }

    public GravityField(float f, float f2) {
        this.gravityVelocity = 3.0E7f;
        this.amplitudeCoefficient = DEFAULT_AMPLITUDE_COEFFICIENT;
        this.impactRadius = DEFAULT_IMPACT_RADIUS;
        this.impactMinimumRadius = 0.0f;
        this.duration = DEFAULT_IMPACT_DURATION;
        this.gravityCoefficient = DEFAULT_GRAVITY_COEFFICIENT;
        this.firstMotionDistance = DEFAULT_IMPACT_RADIUS * DEFAULT_IMPACT_RADIUS;
        this.sourceAbscissa = f;
        this.sourceOrdinate = f2;
        init();
    }

    public GravityField addAsteroid(GravityAsteroid gravityAsteroid) {
        if (gravityAsteroid == null) {
            return this;
        }
        Log.d(TAG, String.format(Locale.ROOT, "asteroid(%s,%s) add.", Float.valueOf(gravityAsteroid.getAbscissa()), Float.valueOf(gravityAsteroid.getOrdinate())));
        float distanceToSource = getDistanceToSource(gravityAsteroid);
        float f = this.impactMinimumRadius;
        if (distanceToSource > f * f && distanceToSource < this.firstMotionDistance) {
            this.firstMotionDistance = distanceToSource;
        }
        this.asteroidsList.add(gravityAsteroid);
        return this;
    }

    private float getDistanceToSource(GravityAsteroid gravityAsteroid) {
        float abscissa = this.sourceAbscissa - gravityAsteroid.getAbscissa();
        float ordinate = this.sourceOrdinate - gravityAsteroid.getOrdinate();
        return (abscissa * abscissa) + (ordinate * ordinate);
    }

    public GravityField start() {
        if (!this.isStart) {
            Log.e(TAG, String.format(Locale.ROOT, "GravityField start:%s, source:(%s,%s)", Long.valueOf(SystemClock.uptimeMillis()), Float.valueOf(this.sourceAbscissa), Float.valueOf(this.sourceOrdinate)));
            double dSqrt = Math.sqrt(this.firstMotionDistance);
            for (GravityAsteroid gravityAsteroid : this.asteroidsList) {
                float abscissa = this.sourceAbscissa - gravityAsteroid.getAbscissa();
                float ordinate = this.sourceOrdinate - gravityAsteroid.getOrdinate();
                float f = (abscissa * abscissa) + (ordinate * ordinate);
                if (gravityAsteroid.isAffected()) {
                    float f2 = this.impactRadius;
                    if (f <= f2 * f2) {
                        float f3 = this.impactMinimumRadius;
                        if (f > f3 * f3) {
                            float amplitude = getAmplitude(f);
                            gravityAsteroid.setDestination(abscissa * amplitude, amplitude * ordinate).setDistance(f).setDuration(this.duration).setGravityCoefficient(this.gravityCoefficient);
                            gravityAsteroid.setAffected(true);
                            gravityAsteroid.setEnd(false);
                            if (this.gravityVelocity > 0.0f) {
                                gravityAsteroid.setDelay((long) ((Math.sqrt(f) - dSqrt) / ((double) this.gravityVelocity)));
                                Log.i(TAG, String.format(Locale.ROOT, "asteroid(%s,%s), delay:%s", Float.valueOf(gravityAsteroid.getAbscissa()), Float.valueOf(gravityAsteroid.getOrdinate()), Long.valueOf(gravityAsteroid.getDelay())));
                            }
                        }
                    }
                }
                gravityAsteroid.setAffected(false);
            }
            this.isStart = true;
            long jUptimeMillis = SystemClock.uptimeMillis();
            this.startTime = jUptimeMillis;
            doFrame(jUptimeMillis * NANOS_TO_MILLS);
        }
        return this;
    }

    /* JADX INFO: renamed from: com.huawei.animation.physical.gravity.GravityField$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$huawei$animation$physical$gravity$GravityField$GravityType;

        static {
            int[] iArr = new int[GravityType.values().length];
            $SwitchMap$com$huawei$animation$physical$gravity$GravityField$GravityType = iArr;
            try {
                iArr[GravityType.GRAVITATION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$huawei$animation$physical$gravity$GravityField$GravityType[GravityType.REPULSION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private float getAmplitude(float f) {
        int i = AnonymousClass1.$SwitchMap$com$huawei$animation$physical$gravity$GravityField$GravityType[this.gravityType.ordinal()];
        return (((i != 1 ? i != 2 ? 0.0f : -1.0f : 1.0f) * this.amplitudeCoefficient) * this.impactRadius) / f;
    }

    public void cancel() {
        Choreographer.getInstance().removeFrameCallback(this);
        this.isStart = false;
        Iterator<GravityAsteroid> it = this.asteroidsList.iterator();
        while (it.hasNext()) {
            it.next().cancel();
        }
    }

    private void end() {
        Choreographer.getInstance().removeFrameCallback(this);
        this.isStart = false;
    }

    public void init() {
        this.asteroidsList = new ArrayList();
        this.isStart = false;
        this.gravityType = GravityType.GRAVITATION;
    }

    public float getSourceAbscissa() {
        return this.sourceAbscissa;
    }

    public GravityField setSourceAbscissa(float f) {
        this.sourceAbscissa = f;
        return this;
    }

    public float getSourceOrdinate() {
        return this.sourceOrdinate;
    }

    public GravityField setSourceOrdinate(float f) {
        this.sourceOrdinate = f;
        return this;
    }

    public boolean isStart() {
        return this.isStart;
    }

    public float getImpactRadius() {
        return this.impactRadius;
    }

    public GravityField setImpactRadius(float f) {
        if (f < 0.0f) {
            return this;
        }
        this.impactRadius = f;
        return this;
    }

    public float getImpactMinimumRadius() {
        return this.impactMinimumRadius;
    }

    public GravityField setImpactMinimumRadius(float f) {
        if (f < 0.0f) {
            return this;
        }
        this.impactMinimumRadius = f;
        return this;
    }

    public long getDuration() {
        return this.duration;
    }

    public GravityField setDuration(long j) {
        this.duration = j;
        return this;
    }

    public float getAmplitudeCoefficient() {
        return this.amplitudeCoefficient;
    }

    public GravityField setAmplitudeCoefficient(float f) {
        this.amplitudeCoefficient = f;
        return this;
    }

    public GravityType getGravityType() {
        return this.gravityType;
    }

    public GravityField setGravityType(GravityType gravityType) {
        this.gravityType = gravityType;
        return this;
    }

    public float getGravityVelocity() {
        return this.gravityVelocity;
    }

    public GravityField setGravityVelocity(float f) {
        this.gravityVelocity = f;
        return this;
    }

    public float getGravityCoefficient() {
        return this.gravityCoefficient;
    }

    public GravityField setGravityCoefficient(float f) {
        this.gravityCoefficient = f;
        return this;
    }

    @Override // android.view.Choreographer.FrameCallback
    public void doFrame(long j) {
        long j2 = (j / NANOS_TO_MILLS) - this.startTime;
        Log.d(TAG, String.format(Locale.ROOT, "doFrame: frameTime:%s", Long.valueOf(j2)));
        boolean zIsEnd = true;
        for (GravityAsteroid gravityAsteroid : this.asteroidsList) {
            if (!gravityAsteroid.isAffected() || gravityAsteroid.isEnd()) {
                Log.d(TAG, String.format("doFrame: asteroid(%s,%s).isAffected:%s,asteroid.isEnd:%s", Float.valueOf(gravityAsteroid.getAbscissa()), Float.valueOf(gravityAsteroid.getOrdinate()), Boolean.valueOf(gravityAsteroid.isAffected()), Boolean.valueOf(gravityAsteroid.isEnd())));
            } else if (j2 < gravityAsteroid.getDelay()) {
                Log.d(TAG, String.format(Locale.ROOT, "doFrame:asteroid delay:%s, frameTime:%s", Long.valueOf(gravityAsteroid.getDelay()), Long.valueOf(j2)));
                zIsEnd = false;
            } else {
                gravityAsteroid.update(j2);
                gravityAsteroid.setDistance(getDistanceToSource(gravityAsteroid));
                zIsEnd &= gravityAsteroid.isEnd();
            }
        }
        if (zIsEnd) {
            end();
        } else if (this.isStart) {
            Choreographer.getInstance().postFrameCallback(this);
        }
    }
}
