package com.huawei.animation.physical.press;

/* JADX INFO: loaded from: classes.dex */
public class PressParams {
    public static final int AXIS_NUM = 2;
    public static final float DEFAULT_DAMPING = 30.0f;
    public static final long DEFAULT_DURATION = 300;
    public static final float DEFAULT_END_VALUE = 1.0f;
    public static final int DEFAULT_MASS = 1;
    public static final float DEFAULT_MAX_DEGREE = 4.5f;
    public static final float DEFAULT_MOMENT_COEFFICIENT = 50.0f;
    public static final float DEFAULT_START_VALUE = 0.0f;
    public static final float DEFAULT_START_VELOCITY = 0.0f;
    public static final float DEFAULT_STIFFNESS = 228.0f;
    public static final int DEFAULT_TRANSLATION_Z = 18;
    public static final int DELTA_THRESHOLD = 15;
    public static final int HALF_FACTOR = 2;
    public static final int INITIAL_VALUE = -1;
    public static final int NEGATIVE = -1;
    public static final int POSITIVE = 1;
    public static final int THRESHOLD = 50;
    public static final long UNSET_DURATION = -1;
    public static final float VALUE_ACCURACY = 0.001f;
    public static final float VELOCITY_THRESHOLD_MULTIPLIER = 62.5f;
    private float damping = 30.0f;
    private float stiffness = 228.0f;
    private float mass = 1.0f;
    private float endPosition = 1.0f;
    private float valueAccuracy = 0.001f;
    private float startVelocity = 0.0f;
    private float momentCoeffi = 50.0f;
    private float maxDegree = 4.5f;

    public float getDamping() {
        return this.damping;
    }

    public void setDamping(float f) {
        this.damping = f;
    }

    public float getStiffness() {
        return this.stiffness;
    }

    public void setStiffness(float f) {
        this.stiffness = f;
    }

    public float getMass() {
        return this.mass;
    }

    public void setMass(float f) {
        this.mass = f;
    }

    public float getEndPosition() {
        return this.endPosition;
    }

    public void setEndPosition(float f) {
        this.endPosition = f;
    }

    public float getValueAccuracy() {
        return this.valueAccuracy;
    }

    public void setValueAccuracy(float f) {
        this.valueAccuracy = f;
    }

    public float getStartVelocity() {
        return this.startVelocity;
    }

    public void setStartVelocity(float f) {
        this.startVelocity = f;
    }

    public float getMomentCoeffi() {
        return this.momentCoeffi;
    }

    public void setMomentCoeffi(float f) {
        this.momentCoeffi = f;
    }

    public float getMaxDegree() {
        return this.maxDegree;
    }

    public void setMaxDegree(float f) {
        this.maxDegree = f;
    }
}
