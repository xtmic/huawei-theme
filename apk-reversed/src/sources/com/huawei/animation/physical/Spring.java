package com.huawei.animation.physical;

/* JADX INFO: loaded from: classes.dex */
public class Spring {
    private CalcSpring calc;
    private float damping;
    private float endValue;
    private float mass;
    private float startValue;
    private float startVelocity;
    private float stiffness;
    private float timeEstimateSpan;
    private float valueAccuracy;
    private float velocityAccuracy;

    interface CalcSpring {
        float getValue(float f);

        float getVelocity(float f);
    }

    public Spring() {
        this.startValue = 0.0f;
        this.startVelocity = 0.0f;
        this.endValue = 1.0f;
        this.valueAccuracy = 0.001f;
        this.stiffness = 228.0f;
        this.damping = 30.0f;
        this.mass = 1.0f;
        this.timeEstimateSpan = 0.001f;
        this.calc = new DefaultCalcSpring();
        this.velocityAccuracy = this.valueAccuracy * 62.5f;
    }

    public Spring(float f, float f2) {
        this.startValue = 0.0f;
        this.startVelocity = 0.0f;
        this.endValue = 1.0f;
        this.valueAccuracy = 0.001f;
        this.stiffness = 228.0f;
        this.damping = 30.0f;
        this.mass = 1.0f;
        this.timeEstimateSpan = 0.001f;
        this.calc = new DefaultCalcSpring();
        this.velocityAccuracy = this.valueAccuracy * 62.5f;
        this.stiffness = f;
        this.damping = f2;
    }

    public boolean isAtEquilibrium(float f, float f2) {
        return ((double) Math.abs(f2)) < ((double) this.velocityAccuracy) && ((double) Math.abs(f - this.endValue)) < ((double) this.valueAccuracy);
    }

    public Spring initialize() {
        this.calc = getCalc();
        return this;
    }

    public float getValue(long j) {
        return this.calc.getValue(j / 1000.0f) + this.endValue;
    }

    public float getVelocity(long j) {
        return this.calc.getVelocity(j / 1000.0f);
    }

    private CalcSpring getCalc() {
        CalcSpring calcUnderdamped;
        float f = this.startValue - this.endValue;
        float f2 = this.startVelocity;
        float f3 = this.damping;
        float f4 = this.mass;
        float f5 = f3 * f3;
        float f6 = 4.0f * f4 * this.stiffness;
        float f7 = f5 - f6;
        int iCompare = Float.compare(f5, f6);
        if (iCompare == 0) {
            float f8 = (-f3) / (f4 * 2.0f);
            return new CalcCriticalDamping(f, f2 - (f8 * f), f8);
        }
        if (iCompare > 0) {
            double d = -f3;
            double d2 = f7;
            double d3 = f4 * 2.0f;
            float fSqrt = (float) ((d - Math.sqrt(d2)) / d3);
            float fSqrt2 = (float) ((d + Math.sqrt(d2)) / d3);
            float f9 = (f2 - (fSqrt * f)) / (fSqrt2 - fSqrt);
            calcUnderdamped = new CalcOverDamping(f - f9, f9, fSqrt, fSqrt2);
        } else {
            float f10 = f4 * 2.0f;
            float fSqrt3 = (float) (Math.sqrt(f6 - f5) / ((double) f10));
            float f11 = (-f3) / f10;
            calcUnderdamped = new CalcUnderdamped(f, (f2 - (f11 * f)) / fSqrt3, fSqrt3, f11);
        }
        return calcUnderdamped;
    }

    public float getEstimatedDuration() {
        return doEstimateTime(this.calc);
    }

    private float doEstimateTime(CalcSpring calcSpring) {
        float f = this.timeEstimateSpan;
        float value = calcSpring.getValue(f);
        float velocity = calcSpring.getVelocity(f);
        while (!isAtEquilibrium(value, velocity)) {
            f += 200 * this.timeEstimateSpan;
            value = calcSpring.getValue(f);
            velocity = calcSpring.getVelocity(f);
        }
        return getDuration(calcSpring, f - (200 * this.timeEstimateSpan), f) * 1000.0f;
    }

    private float getDuration(CalcSpring calcSpring, float f, float f2) {
        if (Float.compare(Math.abs(f2 - f), 0.005f) < 0) {
            return f;
        }
        float f3 = (f + f2) / 2.0f;
        if (isAtEquilibrium(calcSpring.getValue(f3), calcSpring.getVelocity(f3))) {
            return getDuration(calcSpring, f, f3);
        }
        return getDuration(calcSpring, f3, f2);
    }

    public float getStartValue() {
        return this.startValue;
    }

    public Spring setStartValue(float f) {
        this.startValue = f;
        return this;
    }

    public float getStartVelocity() {
        return this.startVelocity;
    }

    public Spring setStartVelocity(float f) {
        this.startVelocity = f;
        return this;
    }

    public float getEndValue() {
        return this.endValue;
    }

    public Spring setEndValue(float f) {
        this.endValue = f;
        return this;
    }

    public float getValueAccuracy() {
        return this.valueAccuracy;
    }

    public Spring setValueAccuracy(float f) {
        this.valueAccuracy = f;
        this.velocityAccuracy = f * 62.5f;
        return this;
    }

    public float getStiffness() {
        return this.stiffness;
    }

    public Spring setStiffness(float f) {
        this.stiffness = f;
        return this;
    }

    public float getDamping() {
        return this.damping;
    }

    public Spring setDamping(float f) {
        this.damping = f;
        return this;
    }

    public float getMass() {
        return this.mass;
    }

    public Spring setMass(float f) {
        this.mass = f;
        return this;
    }

    class CalcCriticalDamping implements CalcSpring {
        float mC1;
        float mC2;
        float mR;

        CalcCriticalDamping(float f, float f2, float f3) {
            this.mC1 = f;
            this.mC2 = f2;
            this.mR = f3;
        }

        @Override // com.huawei.animation.physical.Spring.CalcSpring
        public float getValue(float f) {
            return (float) (((double) (this.mC1 + (this.mC2 * f))) * Math.pow(2.718281828459045d, this.mR * f));
        }

        @Override // com.huawei.animation.physical.Spring.CalcSpring
        public float getVelocity(float f) {
            float fPow = (float) Math.pow(2.718281828459045d, this.mR * f);
            float f2 = this.mR;
            float f3 = this.mC1;
            float f4 = this.mC2;
            return (f2 * (f3 + (f * f4)) * fPow) + (f4 * fPow);
        }
    }

    class CalcOverDamping implements CalcSpring {
        float mC1;
        float mC2;
        float mR1;
        float mR2;

        CalcOverDamping(float f, float f2, float f3, float f4) {
            this.mC1 = f;
            this.mC2 = f2;
            this.mR1 = f3;
            this.mR2 = f4;
        }

        @Override // com.huawei.animation.physical.Spring.CalcSpring
        public float getValue(float f) {
            return (this.mC1 * ((float) Math.pow(2.718281828459045d, this.mR1 * f))) + (this.mC2 * ((float) Math.pow(2.718281828459045d, this.mR2 * f)));
        }

        @Override // com.huawei.animation.physical.Spring.CalcSpring
        public float getVelocity(float f) {
            return (this.mC1 * this.mR1 * ((float) Math.pow(2.718281828459045d, r1 * f))) + (this.mC2 * this.mR2 * ((float) Math.pow(2.718281828459045d, r2 * f)));
        }
    }

    class CalcUnderdamped implements CalcSpring {
        float mC1;
        float mC2;
        float mR;
        float mW;

        CalcUnderdamped(float f, float f2, float f3, float f4) {
            this.mC1 = f;
            this.mC2 = f2;
            this.mW = f3;
            this.mR = f4;
        }

        @Override // com.huawei.animation.physical.Spring.CalcSpring
        public float getValue(float f) {
            return ((float) Math.pow(2.718281828459045d, this.mR * f)) * ((this.mC1 * ((float) Math.cos(this.mW * f))) + (this.mC2 * ((float) Math.sin(this.mW * f))));
        }

        @Override // com.huawei.animation.physical.Spring.CalcSpring
        public float getVelocity(float f) {
            float fPow = (float) Math.pow(2.718281828459045d, this.mR * f);
            float fCos = (float) Math.cos(this.mW * f);
            float fSin = (float) Math.sin(this.mW * f);
            float f2 = this.mC2;
            float f3 = this.mW;
            float f4 = this.mC1;
            return ((((f2 * f3) * fCos) - ((f3 * f4) * fSin)) * fPow) + (this.mR * fPow * ((f2 * fSin) + (f4 * fCos)));
        }
    }

    class DefaultCalcSpring implements CalcSpring {
        @Override // com.huawei.animation.physical.Spring.CalcSpring
        public float getValue(float f) {
            return 0.0f;
        }

        @Override // com.huawei.animation.physical.Spring.CalcSpring
        public float getVelocity(float f) {
            return 0.0f;
        }

        DefaultCalcSpring() {
        }
    }

    public String toString() {
        return "Spring{startValue=" + this.startValue + ", startVelocity=" + this.startVelocity + ", endValue=" + this.endValue + ", valueAccuracy=" + this.valueAccuracy + ", stiffness=" + this.stiffness + ", damping=" + this.damping + ", mass=" + this.mass + ", timeEstimateSpan=" + this.timeEstimateSpan + ", calc=" + this.calc + ", velocityAccuracy=" + this.velocityAccuracy + '}';
    }
}
