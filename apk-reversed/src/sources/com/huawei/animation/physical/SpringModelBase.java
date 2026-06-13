package com.huawei.animation.physical;

import android.os.SystemClock;

/* JADX INFO: loaded from: classes.dex */
public class SpringModelBase extends PhysicalModelBase {
    private static final float CURRENT_TIME = -1.0f;
    public static final float DEFAULT_DAMPING = 15.0f;
    private static final float DEFAULT_ESTIMATE_DURATION = 500.0f;
    public static final float DEFAULT_MASS = 1.0f;
    public static final float DEFAULT_STIFFNESS = 800.0f;
    public static final float DEFAULT_VALUE_THRESHOLD = 0.001f;
    private static final int DIST_NUM = 16;
    public static final float MAXIMUM_DAMPING = 99.0f;
    public static final float MAXIMUM_MASS = 1.0f;
    public static final float MAXIMUM_STIFFNESS = 999.0f;
    private static final float MAX_ITERATION_NUM = 999.0f;
    public static final float MINIMUM_DAMPING = 1.0f;
    public static final float MINIMUM_MASS = 1.0f;
    public static final float MINIMUM_STIFFNESS = 1.0f;
    private static final float ONE_SECODN_F = 1000.0f;
    private static final double ONE_SECOND_D = 1000.0d;
    private float mDamping;
    private float mMass;
    private Solution mSolution;
    private float mStiffness;

    private boolean almostEqual(float f, float f2, float f3) {
        return f > f2 - f3 && f < f2 + f3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean almostGreaterThan(float f, float f2, float f3) {
        return f > f2 - f3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean almostLessThan(float f, float f2, float f3) {
        return f < f2 - f3;
    }

    public SpringModelBase(float f, float f2, float f3) {
        this.mMass = 1.0f;
        this.mStiffness = 800.0f;
        this.mDamping = 15.0f;
        super.setValueThreshold(f3);
        this.mMass = 1.0f;
        this.mStiffness = Math.min(Math.max(1.0f, f), 999.0f);
        this.mDamping = Math.min(Math.max(1.0f, f2), 99.0f);
        this.mSolution = null;
        this.mStartPosition = 0.0f;
        this.mEndPosition = 0.0f;
        this.mStartVelocity = 0.0f;
        this.mStartTime = 0L;
    }

    public SpringModelBase(float f, float f2, float f3, float f4) {
        this.mMass = 1.0f;
        this.mStiffness = 800.0f;
        this.mDamping = 15.0f;
        super.setValueThreshold(f4);
        this.mMass = f;
        this.mStiffness = Math.min(Math.max(1.0f, f2), 999.0f);
        this.mDamping = Math.min(Math.max(1.0f, f3), 99.0f);
        this.mSolution = null;
        this.mStartPosition = 0.0f;
        this.mEndPosition = 0.0f;
        this.mStartVelocity = 0.0f;
        this.mStartTime = 0L;
    }

    public SpringModelBase(float f, float f2) {
        this.mMass = 1.0f;
        this.mStiffness = 800.0f;
        this.mDamping = 15.0f;
        super.setValueThreshold(0.001f);
        this.mMass = 1.0f;
        this.mStiffness = Math.min(Math.max(1.0f, f), 999.0f);
        this.mDamping = Math.min(Math.max(1.0f, f2), 99.0f);
        this.mSolution = null;
        this.mStartPosition = 0.0f;
        this.mEndPosition = 0.0f;
        this.mStartVelocity = 0.0f;
        this.mStartTime = 0L;
    }

    private abstract class Solution {
        protected float mDDX;
        protected float mDX;
        protected float mDuration;
        protected float mX;
        private float[] mXDist;

        private float getIterate(float f, float f2) {
            return f <= 999.0f ? f2 : SpringModelBase.CURRENT_TIME;
        }

        protected abstract void doEstimateDuration();

        protected abstract float getDDX(float f);

        protected abstract float getDX(float f);

        protected abstract float getFirstExtremumX();

        protected abstract float getMaxAbsX();

        protected abstract float getX(float f);

        protected Solution() {
            this.mDuration = SpringModelBase.CURRENT_TIME;
            this.mXDist = new float[17];
            this.mX = 0.0f;
            this.mDX = 0.0f;
            this.mDDX = 0.0f;
            this.mDuration = 0.0f;
        }

        protected Solution(float f, float f2, float f3) {
            this.mDuration = SpringModelBase.CURRENT_TIME;
            this.mXDist = new float[17];
            this.mX = f;
            this.mDX = f2;
            this.mDDX = f3;
            this.mDuration = 0.0f;
        }

        protected float estimateDuration() {
            if (Float.compare(this.mDuration, 0.0f) <= 0) {
                doEstimateDuration();
            }
            return this.mDuration;
        }

        private float getStartTForX(float f, float f2, float f3) {
            float f4 = (f3 - f2) / 16.0f;
            boolean z = getDX((f3 + f2) / 2.0f) > 0.0f;
            for (int i = 1; i < 17; i++) {
                float[] fArr = this.mXDist;
                int i2 = i - 1;
                float f5 = fArr[i] - fArr[i2];
                if (z && fArr[i] >= f) {
                    return f5 == 0.0f ? f2 + (i2 * f4) : f2 + ((i2 + ((f - fArr[i2]) / f5)) * f4);
                }
                if (!z) {
                    float[] fArr2 = this.mXDist;
                    if (fArr2[i] <= f) {
                        return f5 == 0.0f ? f2 + (i2 * f4) : f2 + ((i - ((fArr2[i] - f) / f5)) * f4);
                    }
                }
            }
            return f3;
        }

        protected float doIterate(float f, float f2) {
            float f3 = (f2 - f) / 16.0f;
            float f4 = SpringModelBase.this.mValueThreshold;
            for (int i = 0; i < 17; i++) {
                this.mXDist[i] = getX((i * f3) + f);
            }
            boolean z = true;
            int i2 = 1;
            while (true) {
                if (i2 >= 17) {
                    z = false;
                    break;
                }
                int i3 = i2 - 1;
                if ((this.mXDist[i3] - SpringModelBase.this.mValueThreshold) * (this.mXDist[i2] - SpringModelBase.this.mValueThreshold) < 0.0f) {
                    f4 = SpringModelBase.this.mValueThreshold;
                    break;
                }
                if ((this.mXDist[i3] + SpringModelBase.this.mValueThreshold) * (this.mXDist[i2] + SpringModelBase.this.mValueThreshold) < 0.0f) {
                    f4 = -SpringModelBase.this.mValueThreshold;
                    break;
                }
                i2++;
            }
            if (!z) {
                return f;
            }
            float startTForX = getStartTForX(f4, f, f2);
            while (true) {
                float f5 = startTForX;
                float f6 = f2;
                f2 = f5;
                if (Math.abs(getX(f2)) >= SpringModelBase.this.mValueThreshold || f6 - f2 < 0.0625f) {
                    break;
                }
                float f7 = (f2 - f) / 16.0f;
                for (int i4 = 0; i4 < 17; i4++) {
                    this.mXDist[i4] = getX((i4 * f7) + f);
                }
                startTForX = getStartTForX(f4, f, f2);
            }
            float x = getX(f2);
            float dx = getDX(f2);
            float f8 = 0.0f;
            while (true) {
                if (!SpringModelBase.this.almostGreaterThan(Math.abs(x), SpringModelBase.this.mValueThreshold, 0.0f)) {
                    break;
                }
                float f9 = 1.0f + f8;
                if (f8 >= 999.0f) {
                    f8 = f9;
                    break;
                }
                f2 -= x / dx;
                x = getX(f2);
                dx = getDX(f2);
                f8 = f9;
            }
            return getIterate(f8, f2);
        }
    }

    private class Solution0 extends Solution {
        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected void doEstimateDuration() {
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected float getFirstExtremumX() {
            return 0.0f;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected float getMaxAbsX() {
            return 0.0f;
        }

        private Solution0() {
            super();
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getX(float f) {
            return this.mX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getDX(float f) {
            return this.mDX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getDDX(float f) {
            return this.mDDX;
        }
    }

    private class Solution1 extends Solution {
        float mC1;
        float mC2;
        float mR;

        Solution1(float f, float f2, float f3) {
            super();
            this.mC1 = f;
            this.mC2 = f2;
            this.mR = f3;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getX(float f) {
            this.mX = (float) (((double) (this.mC1 + (this.mC2 * f))) * Math.pow(2.718281828459045d, this.mR * f));
            return this.mX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getDX(float f) {
            float fPow = (float) Math.pow(2.718281828459045d, this.mR * f);
            float f2 = this.mR;
            float f3 = this.mC1;
            float f4 = this.mC2;
            this.mDX = (f2 * (f3 + (f * f4)) * fPow) + (f4 * fPow);
            return this.mDX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getDDX(float f) {
            float fPow = (float) Math.pow(2.718281828459045d, this.mR * f);
            float f2 = this.mR;
            float f3 = this.mC1;
            float f4 = this.mC2;
            this.mDDX = (f2 * f2 * (f3 + (f * f4)) * fPow) + (f4 * 2.0f * f2 * fPow);
            return this.mDDX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected final void doEstimateDuration() {
            float f = this.mC2;
            float f2 = (-(((f * 2.0f) / this.mR) + this.mC1)) / f;
            int i = 0;
            if (f2 < 0.0f || Float.isInfinite(f2) || Float.isNaN(f2)) {
                f2 = 0.0f;
            } else {
                float x = getX(f2);
                int i2 = 0;
                while (SpringModelBase.this.almostLessThan(Math.abs(x), SpringModelBase.this.mValueThreshold, 0.0f)) {
                    i2++;
                    if (i2 > 999.0f) {
                        break;
                    }
                    f2 = (f2 + 0.0f) / 2.0f;
                    x = getX(f2);
                }
                if (i2 > 999.0f) {
                    this.mDuration = f2;
                    return;
                }
            }
            float x2 = getX(f2);
            float dx = getDX(f2);
            while (SpringModelBase.this.almostGreaterThan(Math.abs(x2), SpringModelBase.this.mValueThreshold, 0.0f)) {
                i++;
                if (i > 999.0f) {
                    break;
                }
                f2 -= x2 / dx;
                if (f2 < 0.0f || Float.isNaN(f2) || Float.isInfinite(f2)) {
                    this.mDuration = 0.0f;
                    return;
                } else {
                    x2 = getX(f2);
                    dx = getDX(f2);
                }
            }
            if (i > 999.0f) {
                this.mDuration = SpringModelBase.CURRENT_TIME;
            } else {
                this.mDuration = f2;
            }
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected float getMaxAbsX() {
            return Math.abs(getFirstExtremumX());
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected float getFirstExtremumX() {
            float f = this.mC2;
            float f2 = (-((f / this.mR) + this.mC1)) / f;
            if (f2 < 0.0f || Float.isInfinite(f2)) {
                f2 = 0.0f;
            }
            return getX(f2);
        }
    }

    private class Solution2 extends Solution {
        float mC1;
        float mC2;
        float mR1;
        float mR2;

        Solution2(float f, float f2, float f3, float f4) {
            super();
            this.mC1 = f;
            this.mC2 = f2;
            this.mR1 = f3;
            this.mR2 = f4;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getX(float f) {
            this.mX = (this.mC1 * ((float) Math.pow(2.718281828459045d, this.mR1 * f))) + (this.mC2 * ((float) Math.pow(2.718281828459045d, this.mR2 * f)));
            return this.mX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getDX(float f) {
            this.mDX = (this.mC1 * this.mR1 * ((float) Math.pow(2.718281828459045d, r1 * f))) + (this.mC2 * this.mR2 * ((float) Math.pow(2.718281828459045d, r2 * f)));
            return this.mDX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getDDX(float f) {
            float f2 = this.mC1;
            float f3 = this.mR1;
            float fPow = f2 * f3 * f3 * ((float) Math.pow(2.718281828459045d, f3 * f));
            float f4 = this.mC2;
            float f5 = this.mR2;
            this.mDDX = fPow + (f4 * f5 * f5 * ((float) Math.pow(2.718281828459045d, f5 * f)));
            return this.mDDX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected final void doEstimateDuration() {
            float f = this.mC1;
            float f2 = this.mR1;
            float fLog = (float) Math.log(Math.abs(f * f2 * f2));
            float f3 = -this.mC2;
            float f4 = this.mR2;
            float fLog2 = (fLog - ((float) Math.log(Math.abs((f3 * f4) * f4)))) / (this.mR2 - this.mR1);
            int i = 0;
            if (fLog2 < 0.0f || Float.isInfinite(fLog2) || Float.isNaN(fLog2)) {
                fLog2 = 0.0f;
            } else {
                float x = getX(fLog2);
                int i2 = 0;
                while (SpringModelBase.this.almostLessThan(Math.abs(x), SpringModelBase.this.mValueThreshold, 0.0f)) {
                    i2++;
                    if (i2 > 999.0f) {
                        break;
                    }
                    fLog2 = (fLog2 + 0.0f) / 2.0f;
                    x = getX(fLog2);
                }
                if (i2 > 999.0f) {
                    this.mDuration = fLog2;
                    return;
                }
            }
            float x2 = getX(fLog2);
            float dx = getDX(fLog2);
            while (SpringModelBase.this.almostGreaterThan(Math.abs(x2), SpringModelBase.this.mValueThreshold, 0.0f)) {
                i++;
                if (i > 999.0f) {
                    break;
                }
                fLog2 -= x2 / dx;
                if (fLog2 < 0.0f || Float.isNaN(fLog2) || Float.isInfinite(fLog2)) {
                    this.mDuration = 0.0f;
                    return;
                } else {
                    x2 = getX(fLog2);
                    dx = getDX(fLog2);
                }
            }
            if (i > 999.0f) {
                this.mDuration = SpringModelBase.CURRENT_TIME;
            } else {
                this.mDuration = fLog2;
            }
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected float getMaxAbsX() {
            return Math.abs(getFirstExtremumX());
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected float getFirstExtremumX() {
            float fLog = (((float) Math.log(Math.abs(this.mC1 * this.mR1))) - ((float) Math.log(Math.abs((-this.mC2) * this.mR2)))) / (this.mR2 - this.mR1);
            if (fLog < 0.0f || Float.isInfinite(fLog)) {
                fLog = 0.0f;
            }
            return getX(fLog);
        }
    }

    private class Solution3 extends Solution {
        float mC1;
        float mC2;
        float mR;
        float mW;

        Solution3(float f, float f2, float f3, float f4) {
            super();
            this.mC1 = f;
            this.mC2 = f2;
            this.mW = f3;
            this.mR = f4;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getX(float f) {
            this.mX = ((float) Math.pow(2.718281828459045d, this.mR * f)) * ((this.mC1 * ((float) Math.cos(this.mW * f))) + (this.mC2 * ((float) Math.sin(this.mW * f))));
            return this.mX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getDX(float f) {
            float fPow = (float) Math.pow(2.718281828459045d, this.mR * f);
            float fCos = (float) Math.cos(this.mW * f);
            float fSin = (float) Math.sin(this.mW * f);
            float f2 = this.mC2;
            float f3 = this.mW;
            float f4 = this.mC1;
            this.mDX = ((((f2 * f3) * fCos) - ((f3 * f4) * fSin)) * fPow) + (this.mR * fPow * ((f2 * fSin) + (f4 * fCos)));
            return this.mDX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        public float getDDX(float f) {
            float fPow = (float) Math.pow(2.718281828459045d, this.mR * f);
            float fCos = (float) Math.cos(this.mW * f);
            float fSin = (float) Math.sin(this.mW * f);
            float f2 = this.mR;
            float f3 = this.mC2;
            float f4 = this.mW;
            float f5 = this.mC1;
            this.mDDX = (f2 * fPow * (((f3 * f4) * fCos) - ((f5 * f4) * fSin))) + ((((((-f3) * f4) * f4) * fSin) - (((f5 * f4) * f4) * fCos)) * fPow) + (f2 * f2 * fPow * ((f3 * fSin) + (f5 * fCos))) + (f2 * fPow * (((f3 * f4) * fCos) - ((f5 * f4) * fSin)));
            return this.mDDX;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected final void doEstimateDuration() {
            float fSqrt = (float) Math.sqrt((SpringModelBase.this.mDamping * SpringModelBase.this.mDamping) / ((SpringModelBase.this.mMass * 4.0f) * SpringModelBase.this.mStiffness));
            float fSqrt2 = ((float) Math.sqrt(1.0f - (fSqrt * fSqrt))) * ((float) Math.sqrt(SpringModelBase.this.mStiffness / SpringModelBase.this.mMass));
            float f = (6.2831855f / fSqrt2) / 2.0f;
            float fAtan = (float) Math.atan(this.mC2 / this.mC1);
            if (Float.isNaN(fAtan)) {
                this.mDuration = 0.0f;
                return;
            }
            float fAcos = ((((float) Math.acos(0.0d)) + fAtan) % 3.1415927f) / this.mW;
            float dx = getDX(fAcos);
            float fAcos2 = (((((float) Math.acos(0.0d)) + ((float) Math.atan(fSqrt2 / (fSqrt * r1)))) + fAtan) % 3.1415927f) / fSqrt2;
            int i = 0;
            float f2 = 0.0f;
            while (true) {
                if (!SpringModelBase.this.almostGreaterThan(Math.abs(dx), SpringModelBase.this.mVelocityThreshold, 0.0f)) {
                    break;
                }
                int i2 = i + 1;
                if (i >= 999.0f) {
                    i = i2;
                    break;
                }
                fAcos += f;
                dx = getDX(fAcos);
                f2 += f;
                fAcos2 += f;
                i = i2;
            }
            float f3 = i;
            float fDoIterate = SpringModelBase.CURRENT_TIME;
            if (f3 >= 999.0f) {
                this.mDuration = SpringModelBase.CURRENT_TIME;
                return;
            }
            if ((f2 <= fAcos2 && fAcos2 < fAcos) || f2 == fAcos) {
                fDoIterate = doIterate(fAcos2, f + fAcos2);
            } else if (f2 < fAcos && fAcos < fAcos2) {
                fDoIterate = doIterate(Math.max(0.0f, fAcos2 - f), fAcos2);
            }
            this.mDuration = fDoIterate;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected float getMaxAbsX() {
            float fSqrt = (float) Math.sqrt((SpringModelBase.this.mDamping * SpringModelBase.this.mDamping) / ((SpringModelBase.this.mMass * 4.0f) * SpringModelBase.this.mStiffness));
            float fSqrt2 = (float) (((double) ((float) Math.sqrt(SpringModelBase.this.mStiffness / SpringModelBase.this.mMass))) * Math.sqrt(1.0f - (fSqrt * fSqrt)));
            float fAcos = (float) (((Math.acos(0.0d) + ((double) ((float) Math.atan(fSqrt2 / (fSqrt * r1))))) + ((double) ((float) Math.atan(this.mC2 / this.mC1)))) % 3.141592653589793d);
            float fAbs = Math.abs(getX(fAcos / fSqrt2));
            int i = 0;
            do {
                float f = (float) (((double) fAcos) + ((((double) i) * 3.141592653589793d) / ((double) fSqrt2)));
                float fAbs2 = Math.abs(getX(f));
                if (fAbs < fAbs2) {
                    fAbs = fAbs2;
                }
                if (f >= estimateDuration()) {
                    break;
                }
                i++;
            } while (i < 999.0f);
            return ((float) i) >= 999.0f ? SpringModelBase.CURRENT_TIME : fAbs;
        }

        @Override // com.huawei.animation.physical.SpringModelBase.Solution
        protected float getFirstExtremumX() {
            float fSqrt = (float) Math.sqrt((SpringModelBase.this.mDamping * SpringModelBase.this.mDamping) / ((SpringModelBase.this.mMass * 4.0f) * SpringModelBase.this.mStiffness));
            float fSqrt2 = (float) (((double) ((float) Math.sqrt(SpringModelBase.this.mStiffness / SpringModelBase.this.mMass))) * Math.sqrt(1.0f - (fSqrt * fSqrt)));
            return getX((float) ((((Math.acos(0.0d) + ((double) ((float) Math.atan(fSqrt2 / (fSqrt * r1))))) + ((double) ((float) Math.atan(this.mC2 / this.mC1)))) % 3.141592653589793d) / ((double) fSqrt2)));
        }
    }

    private boolean almostZero(float f, float f2) {
        return almostEqual(f, 0.0f, f2);
    }

    public Solution solve(float f, float f2) {
        float f3 = this.mDamping;
        float f4 = this.mMass;
        float f5 = f3 * f3;
        float f6 = 4.0f * f4 * this.mStiffness;
        float f7 = f5 - f6;
        int iCompare = Float.compare(f5, f6);
        if (iCompare == 0) {
            float f8 = (-f3) / (f4 * 2.0f);
            return new Solution1(f, f2 - (f8 * f), f8);
        }
        if (iCompare > 0) {
            double d = -f3;
            double d2 = f7;
            double d3 = f4 * 2.0f;
            float fSqrt = (float) ((d - Math.sqrt(d2)) / d3);
            float fSqrt2 = (float) ((d + Math.sqrt(d2)) / d3);
            float f9 = (f2 - (fSqrt * f)) / (fSqrt2 - fSqrt);
            return new Solution2(f - f9, f9, fSqrt, fSqrt2);
        }
        float f10 = f4 * 2.0f;
        float fSqrt3 = (float) (Math.sqrt(f6 - f5) / ((double) f10));
        float f11 = (-f3) / f10;
        return new Solution3(f, (f2 - (f11 * f)) / fSqrt3, fSqrt3, f11);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getX(float f) {
        if (f < 0.0f) {
            f = (float) ((SystemClock.elapsedRealtime() - this.mStartTime) / ONE_SECOND_D);
        }
        if (this.mSolution != null) {
            return this.mEndPosition + this.mSolution.getX(f);
        }
        return 0.0f;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getX() {
        return getX(CURRENT_TIME);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDX(float f) {
        if (f < 0.0f) {
            f = (float) ((SystemClock.elapsedRealtime() - this.mStartTime) / ONE_SECOND_D);
        }
        Solution solution = this.mSolution;
        if (solution != null) {
            return solution.getDX(f);
        }
        return 0.0f;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDX() {
        return getDX(CURRENT_TIME);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDDX(float f) {
        if (f < 0.0f) {
            f = (float) ((SystemClock.elapsedRealtime() - this.mStartTime) / ONE_SECOND_D);
        }
        Solution solution = this.mSolution;
        if (solution != null) {
            return solution.getDDX(f);
        }
        return 0.0f;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getDDX() {
        return getDDX(CURRENT_TIME);
    }

    public SpringModelBase setEndPosition(float f, float f2, long j) {
        float fMin = Math.min(99999.0f, Math.max(-99999.0f, f));
        float fMin2 = Math.min(99999.0f, Math.max(-99999.0f, f2));
        if (j <= 0) {
            j = SystemClock.elapsedRealtime();
        }
        if (fMin == this.mEndPosition && almostZero(fMin2, this.mValueThreshold)) {
            return this;
        }
        float f3 = this.mEndPosition;
        if (this.mSolution != null) {
            if (almostZero(fMin2, this.mValueThreshold)) {
                fMin2 = this.mSolution.getDX((j - this.mStartTime) / ONE_SECODN_F);
            }
            float x = this.mSolution.getX((j - this.mStartTime) / ONE_SECODN_F);
            if (almostZero(fMin2, this.mValueThreshold)) {
                fMin2 = 0.0f;
            }
            if (almostZero(x, this.mValueThreshold)) {
                x = 0.0f;
            }
            f3 = x + this.mEndPosition;
            if (almostZero(f3 - fMin, this.mValueThreshold) && almostZero(fMin2, this.mValueThreshold)) {
                return this;
            }
        }
        this.mEndPosition = fMin;
        this.mStartPosition = f3;
        this.mStartVelocity = fMin2;
        this.mSolution = solve(f3 - this.mEndPosition, fMin2);
        this.mStartTime = j;
        return this;
    }

    public SpringModelBase setEndValue(float f, float f2) {
        if (f == this.mEndPosition && almostZero(f2, this.mValueThreshold)) {
            return this;
        }
        this.mStartTime = SystemClock.elapsedRealtime();
        this.mStartPosition = 0.0f;
        this.mEndPosition = f;
        this.mStartVelocity = f2;
        this.mSolution = solve(this.mStartPosition - this.mEndPosition, f2);
        return this;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium(float f) {
        if (f < 0.0f) {
            f = SystemClock.elapsedRealtime() - (getStartTime() / ONE_SECODN_F);
        }
        return almostEqual(getX(f), this.mEndPosition, this.mValueThreshold) && almostZero(getDX(f), this.mValueThreshold);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium() {
        return isAtEquilibrium(CURRENT_TIME);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public boolean isAtEquilibrium(float f, float f2) {
        return ((double) Math.abs(f2)) < ((double) this.mVelocityThreshold) && ((double) Math.abs(f - this.mEndPosition)) < ((double) this.mValueThreshold);
    }

    public SpringModelBase snap(float f) {
        float fMin = Math.min(0.0f, Math.max(0.0f, f));
        this.mStartTime = SystemClock.elapsedRealtime();
        this.mStartPosition = 0.0f;
        this.mEndPosition = fMin;
        this.mStartVelocity = 0.0f;
        this.mSolution = new Solution0();
        return this;
    }

    public SpringModelBase reconfigure(float f, float f2, float f3, float f4) {
        super.setValueThreshold(f4);
        this.mMass = Math.min(Math.max(1.0f, f), 1.0f);
        this.mStiffness = Math.min(Math.max(1.0f, f2), 999.0f);
        this.mDamping = Math.min(Math.max(1.0f, f3), 99.0f);
        this.mStartPosition = getX(CURRENT_TIME);
        this.mStartVelocity = getDX(CURRENT_TIME);
        this.mSolution = solve(this.mStartPosition - this.mEndPosition, this.mStartVelocity);
        this.mStartTime = SystemClock.elapsedRealtime();
        return this;
    }

    public float getStiffness() {
        return this.mStiffness;
    }

    public float getDamping() {
        return this.mDamping;
    }

    public void setMass(float f) {
        reconfigure(f, this.mStiffness, this.mDamping, this.mValueThreshold);
    }

    public SpringModelBase setStiffness(float f) {
        return reconfigure(this.mMass, f, this.mDamping, this.mValueThreshold);
    }

    public SpringModelBase setDamping(float f) {
        return reconfigure(this.mMass, this.mStiffness, f, this.mValueThreshold);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public SpringModelBase setValueThreshold(float f) {
        return reconfigure(this.mMass, this.mStiffness, this.mDamping, f);
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getEstimatedDuration() {
        float fEstimateDuration = this.mSolution.estimateDuration();
        return Float.compare(fEstimateDuration, CURRENT_TIME) == 0 ? DEFAULT_ESTIMATE_DURATION : fEstimateDuration * ONE_SECODN_F;
    }

    @Override // com.huawei.animation.physical.PhysicalModelBase, com.huawei.animation.physical.IPhysicalModel
    public float getMaxAbsX() {
        Solution solution = this.mSolution;
        if (solution != null) {
            return solution.getMaxAbsX();
        }
        return 0.0f;
    }

    public float getFirstExtremumX() {
        Solution solution = this.mSolution;
        if (solution != null) {
            return solution.getFirstExtremumX();
        }
        return 0.0f;
    }
}
