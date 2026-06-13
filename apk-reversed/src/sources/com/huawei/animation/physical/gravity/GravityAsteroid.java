package com.huawei.animation.physical.gravity;

import android.animation.TimeInterpolator;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class GravityAsteroid {
    private static final String TAG = GravityAsteroid.class.getName();
    private float abscissa;
    private long delay;
    private long duration;
    private TimeInterpolator interpolator;
    private boolean isEnd;
    private float motionX;
    private float motionY;
    private float ordinate;
    private boolean isAffected = true;
    private GravityForce force = new GravityForce();

    public void onEnd(float f, float f2) {
    }

    public void onUpdate(float f, float f2) {
    }

    public GravityAsteroid(float f, float f2) {
        this.abscissa = f;
        this.ordinate = f2;
    }

    public float getAbscissa() {
        return this.abscissa;
    }

    public void setAbscissa(float f) {
        this.abscissa = f;
    }

    public float getOrdinate() {
        return this.ordinate;
    }

    public void setOrdinate(float f) {
        this.ordinate = f;
    }

    public long getDelay() {
        return this.delay;
    }

    public void setDelay(long j) {
        this.delay = j;
    }

    public boolean isEnd() {
        return this.isEnd;
    }

    public void setEnd(boolean z) {
        this.isEnd = z;
    }

    public boolean isAffected() {
        return this.isAffected;
    }

    public void setAffected(boolean z) {
        this.isAffected = z;
    }

    public GravityForce getForce() {
        return this.force;
    }

    public TimeInterpolator getInterpolator() {
        return this.interpolator;
    }

    public GravityAsteroid setInterpolator(TimeInterpolator timeInterpolator) {
        this.interpolator = timeInterpolator;
        return this;
    }

    public GravityAsteroid setForce(GravityForce gravityForce) {
        this.force = gravityForce;
        return this;
    }

    public void update(long j) {
        long j2 = this.delay;
        if (j < j2) {
            return;
        }
        if (this.interpolator != null) {
            updateByIntepolator(j - j2);
        } else {
            updateByForce(j - j2);
        }
    }

    private void updateByIntepolator(long j) {
        long j2 = this.duration;
        if (j2 == 0) {
            Log.e(TAG, "motion duration is not set.");
            return;
        }
        this.isEnd = j >= j2;
        float f = j;
        float interpolation = this.motionX * this.interpolator.getInterpolation(f / this.duration);
        float interpolation2 = this.motionY * this.interpolator.getInterpolation(f / this.duration);
        this.abscissa += interpolation;
        this.ordinate += interpolation2;
        onUpdate(interpolation, interpolation2);
        if (this.isEnd) {
            onEnd(interpolation, interpolation2);
        }
    }

    private void updateByForce(long j) {
        this.force.calculate(j);
        this.isEnd = this.force.isAtEquilibrium();
        this.abscissa += this.force.getValueX();
        this.ordinate += this.force.getValueY();
        onUpdate(this.force.getValueX(), this.force.getValueY());
        if (this.isEnd) {
            onEnd(this.force.getValueX(), this.force.getValueY());
        }
    }

    public GravityAsteroid setDistance(float f) {
        this.force.setRadius(f);
        return this;
    }

    public GravityAsteroid setDestination(float f, float f2) {
        this.motionX = f;
        this.motionY = f2;
        this.force.setEndValueX(f);
        this.force.setEndValueY(f2);
        return this;
    }

    public GravityAsteroid setDuration(long j) {
        this.duration = j;
        this.force.setDuration(j);
        return this;
    }

    public GravityAsteroid setGravityCoefficient(float f) {
        this.force.setGravityCoefficient(f);
        return this;
    }

    public void cancel() {
        this.isEnd = true;
        onEnd(this.force.getValueX(), this.force.getValueY());
    }
}
