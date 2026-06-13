package com.huawei.animation.physical;

import android.os.Build;
import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.view.View;
import com.huawei.animation.physical.AnimationHandler;
import com.huawei.animation.physical.DynamicAnimation;
import com.huawei.superwallpaper.engine.animation.Property;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public abstract class DynamicAnimation<T extends DynamicAnimation<T>> implements AnimationHandler.AnimationFrameCallback {
    public static final int ANDROID_LOLLIPOP = 21;
    public static final float MIN_VISIBLE_CHANGE_ALPHA = 0.00390625f;
    public static final float MIN_VISIBLE_CHANGE_PIXELS = 1.0f;
    public static final float MIN_VISIBLE_CHANGE_ROTATION_DEGREES = 0.1f;
    public static final float MIN_VISIBLE_CHANGE_SCALE = 0.002f;
    private static final float THRESHOLD_MULTIPLIER = 0.75f;
    private static final float UNSET = Float.MAX_VALUE;
    boolean isStartImmediate;
    IChainValueListener mChainValueListener;
    private final ArrayList<OnAnimationEndListener> mEndListeners;
    private long mLastFrameTime;
    float mMaxValue;
    float mMinValue;
    private float mMinVisibleChange;
    FloatPropertyCompat mProperty;
    boolean mRunning;
    private final ArrayList<OnAnimationStartListener> mStartListeners;
    boolean mStartValueIsSet;
    Object mTarget;
    private final ArrayList<OnAnimationUpdateListener> mUpdateListeners;
    float mValue;
    float mVelocity;
    public static final ViewProperty TRANSLATION_X = new ViewProperty("translationX") { // from class: com.huawei.animation.physical.DynamicAnimation.1
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationX(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationX();
        }
    };
    public static final ViewProperty TRANSLATION_Y = new ViewProperty("translationY") { // from class: com.huawei.animation.physical.DynamicAnimation.2
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationY(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationY();
        }
    };
    public static final ViewProperty TRANSLATION_Z = new ViewProperty("translationZ") { // from class: com.huawei.animation.physical.DynamicAnimation.3
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(f);
            }
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            if (Build.VERSION.SDK_INT >= 21) {
                return view.getTranslationZ();
            }
            return 0.0f;
        }
    };
    public static final ViewProperty SCALE_X = new ViewProperty(Property.SCALE_X) { // from class: com.huawei.animation.physical.DynamicAnimation.4
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScaleX(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScaleX();
        }
    };
    public static final ViewProperty SCALE_Y = new ViewProperty(Property.SCALE_Y) { // from class: com.huawei.animation.physical.DynamicAnimation.5
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScaleY(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScaleY();
        }
    };
    public static final ViewProperty ROTATION = new ViewProperty("rotation") { // from class: com.huawei.animation.physical.DynamicAnimation.6
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotation(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotation();
        }
    };
    public static final ViewProperty ROTATION_X = new ViewProperty(Property.ROTATION_X) { // from class: com.huawei.animation.physical.DynamicAnimation.7
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotationX(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotationX();
        }
    };
    public static final ViewProperty ROTATION_Y = new ViewProperty(Property.ROTATION_Y) { // from class: com.huawei.animation.physical.DynamicAnimation.8
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotationY(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotationY();
        }
    };
    public static final ViewProperty X = new ViewProperty(Property.X) { // from class: com.huawei.animation.physical.DynamicAnimation.9
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setX(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getX();
        }
    };
    public static final ViewProperty Y = new ViewProperty(Property.Y) { // from class: com.huawei.animation.physical.DynamicAnimation.10
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setY(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getY();
        }
    };
    public static final ViewProperty Z = new ViewProperty(Property.Z) { // from class: com.huawei.animation.physical.DynamicAnimation.11
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setZ(f);
            }
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            if (Build.VERSION.SDK_INT >= 21) {
                return view.getZ();
            }
            return 0.0f;
        }
    };
    public static final ViewProperty ALPHA = new ViewProperty(Property.ALPHA) { // from class: com.huawei.animation.physical.DynamicAnimation.12
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setAlpha(f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getAlpha();
        }
    };
    public static final ViewProperty SCROLL_X = new ViewProperty("scrollX") { // from class: com.huawei.animation.physical.DynamicAnimation.13
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScrollX((int) f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScrollX();
        }
    };
    public static final ViewProperty SCROLL_Y = new ViewProperty("scrollY") { // from class: com.huawei.animation.physical.DynamicAnimation.14
        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScrollY((int) f);
        }

        @Override // com.huawei.animation.physical.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScrollY();
        }
    };

    interface IChainValueListener {
        void onChainValue(DynamicAnimation dynamicAnimation, float f, float f2, boolean z);
    }

    public interface OnAnimationEndListener {
        void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2);
    }

    public interface OnAnimationStartListener {
        void onAnimationStart(DynamicAnimation dynamicAnimation, float f, float f2);
    }

    public interface OnAnimationUpdateListener {
        void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2);
    }

    abstract float getAcceleration(float f, float f2);

    abstract boolean isAtEquilibrium(float f, float f2);

    abstract void setValueThreshold(float f);

    abstract boolean updateValueAndVelocity(long j);

    public static abstract class ViewProperty extends FloatPropertyCompat<View> {
        private ViewProperty(String str) {
            super(str);
        }
    }

    public static class MassState {
        public float mValue;
        public float mVelocity;

        public String toString() {
            return "MassState{mValue=" + this.mValue + ", mVelocity=" + this.mVelocity + '}';
        }
    }

    DynamicAnimation(final FloatValueHolder floatValueHolder) {
        this.isStartImmediate = false;
        this.mVelocity = 0.0f;
        this.mValue = Float.MAX_VALUE;
        this.mStartValueIsSet = false;
        this.mRunning = false;
        this.mMaxValue = Float.MAX_VALUE;
        this.mMinValue = -Float.MAX_VALUE;
        this.mLastFrameTime = 0L;
        this.mStartListeners = new ArrayList<>();
        this.mEndListeners = new ArrayList<>();
        this.mUpdateListeners = new ArrayList<>();
        this.mTarget = null;
        this.mProperty = new FloatPropertyCompat("FloatValueHolder") { // from class: com.huawei.animation.physical.DynamicAnimation.15
            @Override // com.huawei.animation.physical.FloatPropertyCompat
            public float getValue(Object obj) {
                return floatValueHolder.getValue();
            }

            @Override // com.huawei.animation.physical.FloatPropertyCompat
            public void setValue(Object obj, float f) {
                floatValueHolder.setValue(f);
            }
        };
        this.mMinVisibleChange = 1.0f;
    }

    <K> DynamicAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat) {
        this.isStartImmediate = false;
        this.mVelocity = 0.0f;
        this.mValue = Float.MAX_VALUE;
        this.mStartValueIsSet = false;
        this.mRunning = false;
        this.mMaxValue = Float.MAX_VALUE;
        this.mMinValue = -Float.MAX_VALUE;
        this.mLastFrameTime = 0L;
        this.mStartListeners = new ArrayList<>();
        this.mEndListeners = new ArrayList<>();
        this.mUpdateListeners = new ArrayList<>();
        setObj(k, floatPropertyCompat);
    }

    public <K> T setObj(K k, FloatPropertyCompat<K> floatPropertyCompat) {
        this.mTarget = k;
        this.mProperty = floatPropertyCompat;
        if (floatPropertyCompat == ROTATION || floatPropertyCompat == ROTATION_X || floatPropertyCompat == ROTATION_Y) {
            this.mMinVisibleChange = 0.1f;
        } else if (floatPropertyCompat == ALPHA || floatPropertyCompat == SCALE_X || floatPropertyCompat == SCALE_Y) {
            this.mMinVisibleChange = 0.00390625f;
        } else {
            this.mMinVisibleChange = 1.0f;
        }
        return this;
    }

    public T setStartValue(float f) {
        this.mValue = f;
        this.mStartValueIsSet = true;
        return this;
    }

    public T setStartVelocity(float f) {
        this.mVelocity = f;
        return this;
    }

    public T setMaxValue(float f) {
        this.mMaxValue = f;
        return this;
    }

    public T setMinValue(float f) {
        this.mMinValue = f;
        return this;
    }

    public T addStartListener(OnAnimationStartListener onAnimationStartListener) {
        if (onAnimationStartListener != null && !this.mStartListeners.contains(onAnimationStartListener)) {
            this.mStartListeners.add(onAnimationStartListener);
        }
        return this;
    }

    public T addEndListener(OnAnimationEndListener onAnimationEndListener) {
        if (onAnimationEndListener != null && !this.mEndListeners.contains(onAnimationEndListener)) {
            this.mEndListeners.add(onAnimationEndListener);
        }
        return this;
    }

    public void removeStartListener(OnAnimationStartListener onAnimationStartListener) {
        removeEntry(this.mStartListeners, onAnimationStartListener);
    }

    public void removeEndListener(OnAnimationEndListener onAnimationEndListener) {
        removeEntry(this.mEndListeners, onAnimationEndListener);
    }

    public T addUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        if (onAnimationUpdateListener == null) {
            return this;
        }
        if (isRunning()) {
            throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
        }
        if (!this.mUpdateListeners.contains(onAnimationUpdateListener)) {
            this.mUpdateListeners.add(onAnimationUpdateListener);
        }
        return this;
    }

    public void removeUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        removeEntry(this.mUpdateListeners, onAnimationUpdateListener);
    }

    public T clearListeners() {
        this.mStartListeners.clear();
        this.mUpdateListeners.clear();
        this.mEndListeners.clear();
        return this;
    }

    public T setMinimumVisibleChange(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Minimum visible change must be positive.");
        }
        this.mMinVisibleChange = f;
        setValueThreshold(f * THRESHOLD_MULTIPLIER);
        return this;
    }

    public float getMinimumVisibleChange() {
        return this.mMinVisibleChange;
    }

    private static <T> void removeEntry(ArrayList<T> arrayList, T t) {
        int iIndexOf = arrayList.indexOf(t);
        if (iIndexOf >= 0) {
            arrayList.set(iIndexOf, null);
        }
    }

    public void start() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be started on the main thread");
        }
        if (this.mRunning) {
            return;
        }
        this.isStartImmediate = false;
        startAnimationInternal();
    }

    public void cancel() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be canceled on the main thread");
        }
        if (this.mRunning) {
            endAnimationInternal(true);
        }
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    private void startAnimationInternal() {
        if (this.mRunning) {
            return;
        }
        this.mRunning = true;
        if (!this.mStartValueIsSet) {
            this.mValue = getPropertyValue();
        }
        AnimationHandler.getInstance().addAnimationFrameCallback(this, 0L);
        for (OnAnimationStartListener onAnimationStartListener : this.mStartListeners) {
            if (onAnimationStartListener != null) {
                onAnimationStartListener.onAnimationStart(this, this.mValue, this.mVelocity);
            }
        }
        removeNullEntries(this.mStartListeners);
    }

    public void startImmediately() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be started on the main thread");
        }
        if (this.mRunning) {
            return;
        }
        this.isStartImmediate = true;
        startAnimationInternal();
    }

    @Override // com.huawei.animation.physical.AnimationHandler.AnimationFrameCallback
    public boolean doAnimationFrame(long j) {
        long j2 = this.mLastFrameTime;
        if (j2 == 0) {
            this.mLastFrameTime = j;
            if (!this.isStartImmediate) {
                setValue(this.mValue);
                return false;
            }
            j2 = j - 16;
        }
        this.mLastFrameTime = j;
        boolean zUpdateValueAndVelocity = updateValueAndVelocity(j - j2);
        float fMin = Math.min(this.mValue, this.mMaxValue);
        this.mValue = fMin;
        float fMax = Math.max(fMin, this.mMinValue);
        this.mValue = fMax;
        setValue(fMax);
        if (zUpdateValueAndVelocity) {
            endAnimationInternal(false);
        }
        return zUpdateValueAndVelocity;
    }

    private void endAnimationInternal(boolean z) {
        this.mRunning = false;
        AnimationHandler.getInstance().removeCallback(this);
        this.mLastFrameTime = 0L;
        this.mStartValueIsSet = false;
        for (OnAnimationEndListener onAnimationEndListener : this.mEndListeners) {
            if (onAnimationEndListener != null) {
                onAnimationEndListener.onAnimationEnd(this, z, this.mValue, this.mVelocity);
            }
        }
        removeNullEntries(this.mEndListeners);
    }

    public void setPropertyValue(float f) {
        setValue(f);
        IChainValueListener iChainValueListener = this.mChainValueListener;
        if (iChainValueListener != null) {
            iChainValueListener.onChainValue(this, f, this.mVelocity, true);
        }
    }

    public void setAnimateValue(float f) {
        this.mProperty.setValue(this.mTarget, f);
    }

    protected void setValue(float f) {
        setAnimateValue(f);
        IChainValueListener iChainValueListener = this.mChainValueListener;
        if (iChainValueListener != null) {
            iChainValueListener.onChainValue(this, f, this.mVelocity, false);
        }
        for (OnAnimationUpdateListener onAnimationUpdateListener : this.mUpdateListeners) {
            if (onAnimationUpdateListener != null) {
                onAnimationUpdateListener.onAnimationUpdate(this, f, this.mVelocity);
            }
        }
        removeNullEntries(this.mUpdateListeners);
    }

    float getValueThreshold() {
        return this.mMinVisibleChange * THRESHOLD_MULTIPLIER;
    }

    public float getPropertyValue() {
        return this.mProperty.getValue(this.mTarget);
    }

    void removeNullEntries(ArrayList arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size) == null) {
                arrayList.remove(size);
            }
        }
    }

    DynamicAnimation<T> setChainValueListener(IChainValueListener iChainValueListener) {
        this.mChainValueListener = iChainValueListener;
        return this;
    }
}
