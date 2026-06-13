package com.huawei.animation.physical.interpolator;

import android.view.View;
import android.view.animation.Interpolator;
import com.huawei.animation.physical.FloatPropertyCompat;
import com.huawei.animation.physical.FloatValueHolder;
import com.huawei.animation.physical.PhysicalModelBase;
import com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase;
import com.huawei.animation.physical.util.ViewCompat;
import com.huawei.superwallpaper.engine.animation.Property;

/* JADX INFO: loaded from: classes.dex */
public abstract class PhysicalInterpolatorBase<T extends PhysicalInterpolatorBase<T>> implements Interpolator {
    private static final float CURRENT_TIME = -1.0f;
    private static final long DEFAULT_DURATION = 300;
    public static final float MIN_VISIBLE_CHANGE_ALPHA = 0.00390625f;
    public static final float MIN_VISIBLE_CHANGE_PIXELS = 1.0f;
    public static final float MIN_VISIBLE_CHANGE_ROTATION_DEGREES = 0.1f;
    public static final float MIN_VISIBLE_CHANGE_SCALE = 0.002f;
    public static final float MIN_VISIBLE_CHANGE_STANDARD_INTERPOLATE = 0.001f;
    private static final float ONE_SECOND = 1000.0f;
    protected static final float THRESHOLD_MULTIPLIER = 0.75f;
    private static final float UNSET = Float.MAX_VALUE;
    private InterpolatorDataUpdateListener mDataUpdateListener;
    private long mDuration;
    float mMaxValue;
    float mMinValue;
    private float mMinVisibleChange;
    private PhysicalModelBase mModel;
    final FloatPropertyCompat mProperty;
    protected float mTimeScale;
    public static final ViewProperty TRANSLATION_X = new ViewProperty("translationX") { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.1
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationX(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationX();
        }
    };
    public static final ViewProperty TRANSLATION_Y = new ViewProperty("translationY") { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.2
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationY(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationY();
        }
    };
    public static final ViewProperty TRANSLATION_Z = new ViewProperty("translationZ") { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.3
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            ViewCompat.setTranslationZ(view, f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return ViewCompat.getTranslationZ(view);
        }
    };
    public static final ViewProperty SCALE_X = new ViewProperty(Property.SCALE_X) { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.4
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScaleX(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScaleX();
        }
    };
    public static final ViewProperty SCALE_Y = new ViewProperty(Property.SCALE_Y) { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.5
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScaleY(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScaleY();
        }
    };
    public static final ViewProperty ROTATION = new ViewProperty("rotation") { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.6
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotation(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotation();
        }
    };
    public static final ViewProperty ROTATION_X = new ViewProperty(Property.ROTATION_X) { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.7
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotationX(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotationX();
        }
    };
    public static final ViewProperty ROTATION_Y = new ViewProperty(Property.ROTATION_Y) { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.8
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotationY(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotationY();
        }
    };
    public static final ViewProperty X = new ViewProperty(Property.X) { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.9
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setX(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getX();
        }
    };
    public static final ViewProperty Y = new ViewProperty(Property.Y) { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.10
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setY(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getY();
        }
    };
    public static final ViewProperty Z = new ViewProperty(Property.Z) { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.11
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            ViewCompat.setZ(view, f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return ViewCompat.getZ(view);
        }
    };
    public static final ViewProperty ALPHA = new ViewProperty(Property.ALPHA) { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.12
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setAlpha(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getAlpha();
        }
    };
    public static final ViewProperty SCROLL_X = new ViewProperty("scrollX") { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.13
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScrollX((int) f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScrollX();
        }
    };
    public static final ViewProperty SCROLL_Y = new ViewProperty("scrollY") { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.14
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScrollY((int) f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScrollY();
        }
    };

    public interface InterpolatorDataUpdateListener {
        void onDataUpdate(float f, float f2, float f3, float f4);
    }

    abstract T setValueThreshold(float f);

    public static abstract class ViewProperty extends FloatPropertyCompat<View> {
        private ViewProperty(String str) {
            super(str);
        }
    }

    PhysicalInterpolatorBase(final FloatValueHolder floatValueHolder, PhysicalModelBase physicalModelBase) {
        this.mMaxValue = Float.MAX_VALUE;
        this.mMinValue = -Float.MAX_VALUE;
        this.mDuration = 300L;
        this.mModel = physicalModelBase;
        this.mProperty = new FloatPropertyCompat("FloatValueHolder") { // from class: com.huawei.animation.physical.interpolator.PhysicalInterpolatorBase.15
            @Override // com.huawei.animation.physical.FloatPropertyCompat
            public float getValue(Object obj) {
                return floatValueHolder.getValue();
            }

            @Override // com.huawei.animation.physical.FloatPropertyCompat
            public void setValue(Object obj, float f) {
                floatValueHolder.setValue(f);
            }
        };
        this.mMinVisibleChange = 0.001f;
    }

    <K> PhysicalInterpolatorBase(FloatPropertyCompat<K> floatPropertyCompat, PhysicalModelBase physicalModelBase) {
        this.mMaxValue = Float.MAX_VALUE;
        this.mMinValue = -Float.MAX_VALUE;
        this.mDuration = 300L;
        this.mModel = physicalModelBase;
        this.mProperty = floatPropertyCompat;
        if (floatPropertyCompat == ROTATION || floatPropertyCompat == ROTATION_X || floatPropertyCompat == ROTATION_Y) {
            this.mMinVisibleChange = 0.1f;
            return;
        }
        if (floatPropertyCompat == ALPHA) {
            this.mMinVisibleChange = 0.00390625f;
        } else if (floatPropertyCompat == SCALE_X || floatPropertyCompat == SCALE_Y) {
            this.mMinVisibleChange = 0.002f;
        } else {
            this.mMinVisibleChange = 1.0f;
        }
    }

    public T setModel(PhysicalModelBase physicalModelBase) {
        this.mModel = physicalModelBase;
        return this;
    }

    /* JADX WARN: Incorrect return type in method signature: <T:Lcom/huawei/animation/physical/PhysicalModelBase;>()TT; */
    public final PhysicalModelBase getModel() {
        return this.mModel;
    }

    public T setMaxValue(float f) {
        this.mMaxValue = f;
        return this;
    }

    public T setMinValue(float f) {
        this.mMinValue = f;
        return this;
    }

    public T setMinimumVisibleChange(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Minimum visible change must be positive.");
        }
        this.mMinVisibleChange = f;
        setValueThreshold(f);
        return this;
    }

    final float getValueThreshold() {
        return this.mMinVisibleChange * THRESHOLD_MULTIPLIER;
    }

    public T setDuration(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " + j);
        }
        this.mDuration = j;
        return this;
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f) {
        float duration = (f * getDuration()) / ONE_SECOND;
        float x = getModel().getX(duration);
        if (this.mDataUpdateListener != null) {
            this.mDataUpdateListener.onDataUpdate(duration, x, getModel().getDX(duration), getModel().getDDX(duration));
        }
        return x / getDeltaX();
    }

    protected float getDeltaX() {
        return Math.abs(getModel().getEndPosition() - getModel().getStartPosition());
    }

    public float getInterpolation2(float f) {
        if (this.mDuration < 0 || f < this.mModel.getStartTime() || f > this.mModel.getStartTime() + this.mDuration || getDuration() == 0.0f || getDuration() == CURRENT_TIME) {
            return 0.0f;
        }
        float startTime = (((f - this.mModel.getStartTime()) / this.mDuration) * getDuration()) / ONE_SECOND;
        float x = getModel().getX(startTime);
        this.mDataUpdateListener.onDataUpdate(startTime, x, getModel().getDX(startTime), getModel().getDDX(startTime));
        return x / Math.abs(getModel().getEndPosition());
    }

    public float getDuration() {
        return getModel().getEstimatedDuration();
    }

    public float getEndOffset() {
        return getModel().getEndPosition();
    }

    public T setDataUpdateListener(InterpolatorDataUpdateListener interpolatorDataUpdateListener) {
        this.mDataUpdateListener = interpolatorDataUpdateListener;
        return this;
    }
}
