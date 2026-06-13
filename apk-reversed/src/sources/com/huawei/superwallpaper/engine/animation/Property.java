package com.huawei.superwallpaper.engine.animation;

import android.view.animation.Interpolator;
import com.huawei.superwallpaper.engine.util.LogUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Property {
    public static final String ALPHA = "alpha";
    public static final String B = "b";
    public static final String EFFECT = "effect";
    public static final String FRAME_INDEX = "frameIndex";
    public static final String G = "g";
    public static final String R = "r";
    public static final String ROTATION_X = "rotationX";
    public static final String ROTATION_Y = "rotationY";
    public static final String ROTATION_Z = "rotationZ";
    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";
    public static final String SCALE_Z = "scaleZ";
    private static final String TAG = Property.class.getSimpleName();
    public static final String X = "x";
    public static final String Y = "y";
    public static final String Z = "z";
    private float mAodValue;
    private String mFlag;
    private float mLauncherValue;
    private float mLockValue;
    private String mProperty;
    private float mValue;
    private float mAodOffset = 0.0f;
    private List<Keyframe> mKeyframes = null;
    private List<KeyframeSet> mKeyframeSets = null;

    public Property(String str, String str2) {
        this.mFlag = str;
        this.mProperty = str2;
    }

    public String getProperty() {
        return this.mProperty;
    }

    public void setProperty(String str) {
        this.mProperty = str;
    }

    public float getValue() {
        return this.mValue;
    }

    public void setValue(float f) {
        this.mValue = f;
    }

    public String getFlag() {
        return this.mFlag;
    }

    public void setFlag(String str) {
        this.mFlag = str;
    }

    public void clearKeyframes() {
        List<Keyframe> list = this.mKeyframes;
        if (list != null) {
            list.clear();
        }
    }

    public float getAodOffset() {
        return this.mAodOffset;
    }

    public void setAodOffset(float f) {
        this.mAodOffset = f;
    }

    public float getAodValue() {
        return this.mAodValue + this.mAodOffset;
    }

    public void setAodValue(float f) {
        this.mAodValue = f;
    }

    public float getLockValue() {
        return this.mLockValue;
    }

    public void setLockValue(float f) {
        this.mLockValue = f;
    }

    public float getLauncherValue() {
        return this.mLauncherValue;
    }

    public void setLauncherValue(float f) {
        this.mLauncherValue = f;
    }

    public void addKeyframe(Keyframe keyframe) {
        if (this.mKeyframes == null) {
            this.mKeyframes = new ArrayList();
        }
        this.mKeyframes.add(keyframe);
    }

    public void addKeyframes(Keyframe... keyframeArr) {
        if (this.mKeyframes == null) {
            this.mKeyframes = new ArrayList();
        }
        this.mKeyframes.addAll(Arrays.asList(keyframeArr));
    }

    public void addKeyframes(List<Keyframe> list) {
        if (this.mKeyframes == null) {
            this.mKeyframes = new ArrayList();
        }
        this.mKeyframes.addAll(list);
    }

    public List<Keyframe> getKeyframes() {
        return this.mKeyframes;
    }

    void calculateValue(float f) {
        this.mValue = getValue(f);
    }

    public void buildSimpleKeyframes(String str) {
        addKeyframes(new Keyframe(0.0f, this.mValue), new Keyframe(1.0f, chooseValue(str)));
    }

    public void buildKeyframes(String str, String str2, float f) {
        KeyframeSet keyframeSetFindMatchKeyframes = findMatchKeyframes(str, str2);
        if (keyframeSetFindMatchKeyframes != null) {
            List<Keyframe> keyframes = keyframeSetFindMatchKeyframes.getKeyframes();
            addKeyframe(new Keyframe(0.0f, this.mValue));
            for (Keyframe keyframe : keyframes) {
                if (Float.compare(keyframe.getFraction(), f) >= 1) {
                    addKeyframe(new Keyframe((keyframe.getFraction() - f) / (1.0f - f), keyframe.getValue(), keyframe.getInterpolator()));
                }
            }
            return;
        }
        buildSimpleKeyframes(str2);
    }

    public void buildKeyframes(String str) {
        addKeyframes(new Keyframe(0.0f, chooseValue(str)), new Keyframe(1.0f, chooseValue(str)));
    }

    public KeyframeSet findMatchKeyframes(String str, String str2) {
        List<KeyframeSet> list = this.mKeyframeSets;
        if (list != null && !list.isEmpty()) {
            for (KeyframeSet keyframeSet : this.mKeyframeSets) {
                if (keyframeSet.getStartState().equals(str) && keyframeSet.getEndState().equals(str2)) {
                    return keyframeSet;
                }
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0033  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private float chooseValue(String str) {
        byte b;
        int iHashCode = str.hashCode();
        if (iHashCode != -940906782) {
            if (iHashCode != 359703243) {
                b = (iHashCode == 1365320683 && str.equals(Constant.STATE_LAUNCHER)) ? (byte) 2 : (byte) -1;
            } else if (str.equals(Constant.STATE_AOD)) {
                b = 0;
            }
        } else if (str.equals(Constant.STATE_LOCK)) {
            b = 1;
        }
        if (b == 0) {
            return getAodValue();
        }
        if (b == 1) {
            return getLockValue();
        }
        return getLauncherValue();
    }

    public void addKeyframeSet(KeyframeSet keyframeSet) {
        if (this.mKeyframeSets == null) {
            this.mKeyframeSets = new ArrayList();
        }
        this.mKeyframeSets.add(keyframeSet);
    }

    public float getValue(float f) {
        float value;
        float value2;
        float fraction;
        float fraction2;
        if (this.mKeyframes.isEmpty()) {
            LogUtil.i(TAG, "mKeyframes is empty", new Object[0]);
            return 0.0f;
        }
        if (f <= 0.0f) {
            Keyframe keyframe = this.mKeyframes.get(0);
            Keyframe keyframe2 = this.mKeyframes.get(1);
            value = keyframe.getValue();
            value2 = keyframe2.getValue();
            fraction = keyframe.getFraction();
            fraction2 = keyframe2.getFraction();
            Interpolator interpolator = keyframe2.getInterpolator();
            if (interpolator != null) {
                f = interpolator.getInterpolation(f);
            }
        } else if (f >= 1.0f) {
            Keyframe keyframe3 = this.mKeyframes.get(r0.size() - 2);
            List<Keyframe> list = this.mKeyframes;
            Keyframe keyframe4 = list.get(list.size() - 1);
            value = keyframe3.getValue();
            value2 = keyframe4.getValue();
            fraction = keyframe3.getFraction();
            fraction2 = keyframe4.getFraction();
            Interpolator interpolator2 = keyframe4.getInterpolator();
            if (interpolator2 != null) {
                f = interpolator2.getInterpolation(f);
            }
        } else {
            Keyframe keyframe5 = this.mKeyframes.get(0);
            int i = 1;
            while (i < this.mKeyframes.size()) {
                Keyframe keyframe6 = this.mKeyframes.get(i);
                if (f < keyframe6.getFraction()) {
                    Interpolator interpolator3 = keyframe6.getInterpolator();
                    float fraction3 = (f - keyframe5.getFraction()) / (keyframe6.getFraction() - keyframe5.getFraction());
                    float value3 = keyframe5.getValue();
                    float value4 = keyframe6.getValue();
                    if (interpolator3 != null) {
                        fraction3 = interpolator3.getInterpolation(fraction3);
                    }
                    return value3 + (fraction3 * (value4 - value3));
                }
                i++;
                keyframe5 = keyframe6;
            }
            List<Keyframe> list2 = this.mKeyframes;
            return list2.get(list2.size() - 1).getValue();
        }
        return value + (((f - fraction) / (fraction2 - fraction)) * (value2 - value));
    }
}
