package com.huawei.animation.physical.gravity;

import java.math.BigDecimal;

/* JADX INFO: loaded from: classes.dex */
public class GravityForce {
    private static final float DEFAULT_GRAVITY_COEFFICIENT = 1.0E7f;
    private static final float HALF_FACTOR = 2.0f;
    private static final float ONE_SECOND = 1000.0f;
    private static final String TAG = GravityForce.class.getName();
    private static final float VELOCITY_THRESHOLD_MULTIPLIER = new BigDecimal("1000").divide(new BigDecimal("16")).floatValue();
    private long duration;
    private float radius;
    private float endValueX = 1.0f;
    private float endValueY = 1.0f;
    private float valueAccuracy = 1.0f;
    private float mass = 1.0f;
    private float velocityAccuracy = 1.0f * VELOCITY_THRESHOLD_MULTIPLIER;
    private float gravityCoefficient = DEFAULT_GRAVITY_COEFFICIENT;
    private long lastFrameTime = 0;
    private MassState massState = new MassState();

    public boolean isAtEquilibrium() {
        return (Math.abs(this.massState.mValueX) > Math.abs(this.endValueX) - this.valueAccuracy && Math.abs(this.massState.mValueY) > Math.abs(this.endValueY) - this.valueAccuracy) || this.lastFrameTime >= this.duration;
    }

    public void calculate(long j) {
        if (Math.abs(this.radius) <= this.valueAccuracy) {
            return;
        }
        if (this.lastFrameTime == 0) {
            this.lastFrameTime = j;
        }
        float f = (j - this.lastFrameTime) / ONE_SECOND;
        calculateValueX(f);
        calculateValueY(f);
        this.lastFrameTime = j;
    }

    private void calculateValueX(float f) {
        if (Math.abs(this.massState.mValueX - this.endValueX) < this.valueAccuracy) {
            return;
        }
        float f2 = this.massState.mVelocityX;
        float f3 = this.massState.mValueX;
        float fSignum = (((Math.signum(this.endValueX) * (this.gravityCoefficient * this.mass)) / this.radius) * f) + f2;
        this.massState.mValueX = f3 + (f2 * f) + (((((Math.signum(this.endValueX) * (this.gravityCoefficient * this.mass)) / this.radius) * f) * f) / HALF_FACTOR);
        this.massState.mVelocityX = fSignum;
    }

    private void calculateValueY(float f) {
        if (Math.abs(this.massState.mValueY - this.endValueY) < this.valueAccuracy) {
            return;
        }
        float f2 = this.massState.mVelocityY;
        float f3 = this.massState.mValueY;
        float fSignum = (((Math.signum(this.endValueY) * (this.gravityCoefficient * this.mass)) / this.radius) * f) + f2;
        this.massState.mValueY = f3 + (f2 * f) + (((((Math.signum(this.endValueY) * (this.gravityCoefficient * this.mass)) / this.radius) * f) * f) / HALF_FACTOR);
        this.massState.mVelocityY = fSignum;
    }

    public void init() {
        this.massState.mValueX = 0.0f;
        this.massState.mVelocityX = 0.0f;
        this.massState.mValueY = 0.0f;
        this.massState.mVelocityY = 0.0f;
    }

    public float getValueX() {
        return this.massState.mValueX;
    }

    public float getVelocityX() {
        return this.massState.mVelocityX;
    }

    public float getValueY() {
        return this.massState.mValueY;
    }

    public float getVelocityY() {
        return this.massState.mVelocityY;
    }

    public float getEndValueX() {
        return this.endValueX;
    }

    public void setEndValueX(float f) {
        this.endValueX = f;
    }

    public float getEndValueY() {
        return this.endValueY;
    }

    public void setEndValueY(float f) {
        this.endValueY = f;
    }

    public float getValueAccuracy() {
        return this.valueAccuracy;
    }

    public void setValueAccuracy(float f) {
        this.valueAccuracy = f;
    }

    public float getMass() {
        return this.mass;
    }

    public void setMass(float f) {
        this.mass = f;
    }

    public float getVelocityAccuracy() {
        return this.velocityAccuracy;
    }

    public void setVelocityAccuracy(float f) {
        this.velocityAccuracy = f;
    }

    public MassState getMassState() {
        return this.massState;
    }

    public void setMassState(MassState massState) {
        this.massState = massState;
    }

    public float getGravityCoefficient() {
        return this.gravityCoefficient;
    }

    public void setGravityCoefficient(float f) {
        this.gravityCoefficient = f;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float f) {
        if (f > 0.0f) {
            this.radius = f;
        }
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    protected class MassState {
        private float mValueX = 0.0f;
        private float mVelocityX = 0.0f;
        private float mValueY = 0.0f;
        private float mVelocityY = 0.0f;

        protected MassState() {
        }

        public float getValueX() {
            return this.mValueX;
        }

        public void setValueX(float f) {
            this.mValueX = f;
        }

        public float getVelocityX() {
            return this.mVelocityX;
        }

        public void setVelocityX(float f) {
            this.mVelocityX = f;
        }

        public float getValueY() {
            return this.mValueY;
        }

        public void setValueY(float f) {
            this.mValueY = f;
        }

        public float getVelocityY() {
            return this.mVelocityY;
        }

        public void setVelocityY(float f) {
            this.mVelocityY = f;
        }

        public String toString() {
            return "MassState{mValueX=" + this.mValueX + ", mVelocityX=" + this.mVelocityX + ", mValueY=" + this.mValueY + ", mVelocityY=" + this.mVelocityY + '}';
        }
    }
}
