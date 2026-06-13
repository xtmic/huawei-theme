package com.huawei.animation.physical.press;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase;
import com.huawei.animation.physical.interpolator.SpringInterpolator;
import com.huawei.superwallpaper.engine.animation.Property;

/* JADX INFO: loaded from: classes.dex */
public class PressTilt {
    private static final String TAG = "PressTilt";
    private SpringInterpolator interpolatorX;
    private SpringInterpolator interpolatorY;
    private View mView;
    private ValueAnimator rotateAnimatorX;
    private ValueAnimator rotateAnimatorY;
    private final PressParams setting;
    private float lastX = 0.0f;
    private float lastY = 0.0f;
    private int centerX = -1;
    private int centerY = -1;
    private boolean isSupportZ = false;
    private long duration = 300;

    public PressTilt(View view) {
        setView(view);
        this.setting = new PressParams();
        this.rotateAnimatorX = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.rotateAnimatorY = ValueAnimator.ofFloat(0.0f, 1.0f);
    }

    public void setView(View view) {
        this.mView = view;
        initCoordinate(view);
    }

    public PressParams getSetting() {
        return this.setting;
    }

    public void setGravityPoint(int i, int i2) {
        if (this.mView == null) {
            Log.e(TAG, "Set the target view first. ");
        }
        int[] iArr = new int[2];
        this.mView.getLocationOnScreen(iArr);
        int i3 = iArr[0];
        int i4 = iArr[1];
        if (i4 <= i2 && this.mView.getMeasuredHeight() + i4 >= i2 && i3 <= i && this.mView.getMeasuredWidth() + i3 >= i) {
            this.centerX = i;
            this.centerY = i2;
        }
        this.mView.setPivotX(this.centerX - i3);
        this.mView.setPivotY(this.centerY - i4);
    }

    public boolean isSupportZ() {
        return this.isSupportZ;
    }

    public PressTilt setSupportZ(boolean z) {
        this.isSupportZ = z;
        if (z) {
            this.mView.setTranslationZ(18.0f);
        } else {
            this.mView.setTranslationZ(0.0f);
        }
        return this;
    }

    public PressTilt setDuration(long j) {
        this.duration = j;
        return this;
    }

    private void initCoordinate(View view) {
        if (view == null) {
            return;
        }
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        this.centerX = i + (view.getMeasuredWidth() / 2);
        this.centerY = i2 + (view.getMeasuredHeight() / 2);
    }

    public void init() {
        this.lastX = 0.0f;
        this.lastY = 0.0f;
    }

    public void addMovement(float f, float f2) {
        int i = f2 > ((float) this.centerY) ? -1 : 1;
        int i2 = f >= ((float) this.centerX) ? 1 : -1;
        int iAbs = Math.abs(((int) f) - this.centerX);
        int iAbs2 = Math.abs((int) (f - this.lastX));
        if (iAbs > 50 && iAbs2 > 15) {
            this.interpolatorY = new SpringInterpolator((this.setting.getStiffness() * iAbs) / this.setting.getMomentCoeffi(), this.setting.getDamping(), this.setting.getEndPosition(), this.setting.getStartVelocity(), this.setting.getValueAccuracy());
            startRotationVerticalAnimation(i2 * this.setting.getMaxDegree());
            this.lastX = f;
        }
        int iAbs3 = Math.abs(((int) f2) - this.centerY);
        int iAbs4 = Math.abs((int) (f2 - this.lastY));
        if (iAbs3 <= 50 || iAbs4 <= 15) {
            return;
        }
        this.interpolatorX = new SpringInterpolator((this.setting.getStiffness() * iAbs3) / this.setting.getMomentCoeffi(), this.setting.getDamping(), this.setting.getEndPosition(), this.setting.getStartVelocity(), this.setting.getValueAccuracy());
        startRotationHorizonAnimation(i * this.setting.getMaxDegree());
        this.lastY = f2;
    }

    public void reset() {
        startRotationHorizonAnimation(0.0f);
        startRotationVerticalAnimation(0.0f);
        this.lastX = 0.0f;
        this.lastY = 0.0f;
    }

    private void startRotationVerticalAnimation(float f) {
        ValueAnimator valueAnimator = this.rotateAnimatorY;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.rotateAnimatorY.cancel();
            Log.e(TAG, "Rotation Y animation cancel.");
        }
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mView, PropertyValuesHolder.ofFloat(Property.ROTATION_Y, this.mView.getRotationY(), f));
        this.rotateAnimatorY = objectAnimatorOfPropertyValuesHolder;
        objectAnimatorOfPropertyValuesHolder.setInterpolator(this.interpolatorY);
        long j = this.duration;
        if (j != -1) {
            this.rotateAnimatorY.setDuration(j);
        } else {
            SpringInterpolator springInterpolator = this.interpolatorY;
            if (springInterpolator instanceof PhysicalInterpolatorBase) {
                this.rotateAnimatorY.setDuration((long) springInterpolator.getDuration());
            } else {
                this.rotateAnimatorY.setDuration(300L);
            }
        }
        this.rotateAnimatorY.start();
    }

    private void startRotationHorizonAnimation(float f) {
        ValueAnimator valueAnimator = this.rotateAnimatorX;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.rotateAnimatorX.cancel();
            Log.e(TAG, "Rotation X animation cancel.");
        }
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mView, PropertyValuesHolder.ofFloat(Property.ROTATION_X, this.mView.getRotationX(), f));
        this.rotateAnimatorX = objectAnimatorOfPropertyValuesHolder;
        objectAnimatorOfPropertyValuesHolder.setInterpolator(this.interpolatorX);
        long j = this.duration;
        if (j != -1) {
            this.rotateAnimatorX.setDuration(j);
        } else {
            SpringInterpolator springInterpolator = this.interpolatorX;
            if (springInterpolator instanceof PhysicalInterpolatorBase) {
                this.rotateAnimatorX.setDuration((long) springInterpolator.getDuration());
            } else {
                this.rotateAnimatorX.setDuration(300L);
            }
        }
        this.rotateAnimatorX.start();
    }

    public ValueAnimator getRotateAnimatorX() {
        return this.rotateAnimatorX;
    }

    public void setRotateAnimatorX(ValueAnimator valueAnimator) {
        this.rotateAnimatorX = valueAnimator;
    }

    public ValueAnimator getRotateAnimatorY() {
        return this.rotateAnimatorY;
    }

    public void setRotateAnimatorY(ValueAnimator valueAnimator) {
        this.rotateAnimatorY = valueAnimator;
    }
}
