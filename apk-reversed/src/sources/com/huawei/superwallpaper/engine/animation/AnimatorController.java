package com.huawei.superwallpaper.engine.animation;

import com.huawei.superwallpaper.engine.util.LogUtil;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class AnimatorController {
    private static final String TAG = AnimatorController.class.getSimpleName();
    protected long mAnimationDuration;
    protected AnimatorSet mAnimatorSet;
    protected AnimatorSet mAod2LauncherAnimatorSet;
    protected AnimatorSet mAod2LockAnimatorSet;
    protected AnimatorSet mLauncher2AodAnimatorSet;
    protected AnimatorSet mLock2AodAnimatorSet;
    protected AnimatorSet mLock2LauncherAnimatorSet;
    protected PropertySet mProperties;
    protected String mPreviousAction = "unknown";
    protected String mCurrentAction = "unknown";

    public AnimatorController() {
        init();
    }

    private void init() {
        this.mAnimatorSet = new AnimatorSet();
        this.mProperties = new PropertySet();
    }

    public long getAnimationDuration() {
        return this.mAnimationDuration;
    }

    public boolean stepAnimatorFrame(long j) {
        return this.mAnimatorSet.step(j);
    }

    public void setStateAnimatorSet(AnimatorSet animatorSet, String str, String str2) {
        if (str.equals(Constant.STATE_AOD) && str2.equals(Constant.STATE_LOCK)) {
            this.mAod2LockAnimatorSet = animatorSet;
            return;
        }
        if (str.equals(Constant.STATE_AOD) && str2.equals(Constant.STATE_LAUNCHER)) {
            this.mAod2LauncherAnimatorSet = animatorSet;
            return;
        }
        if (str.equals(Constant.STATE_LOCK) && str2.equals(Constant.STATE_AOD)) {
            this.mLock2AodAnimatorSet = animatorSet;
            return;
        }
        if (str.equals(Constant.STATE_LOCK) && str2.equals(Constant.STATE_LAUNCHER)) {
            this.mLock2LauncherAnimatorSet = animatorSet;
        } else if (str.equals(Constant.STATE_LAUNCHER) && str2.equals(Constant.STATE_AOD)) {
            this.mLauncher2AodAnimatorSet = animatorSet;
        }
    }

    public float getAodData(String str, String str2) {
        return this.mProperties.createOrGet(str, str2).getAodValue();
    }

    public void addAodData(String str, String str2, float f) {
        this.mProperties.createOrGet(str, str2).setAodValue(f);
    }

    public float getLockData(String str, String str2) {
        return this.mProperties.createOrGet(str, str2).getLockValue();
    }

    public float getLauncherData(String str, String str2) {
        return this.mProperties.createOrGet(str, str2).getLauncherValue();
    }

    public void addAodData(JSONObject jSONObject) {
        String str;
        String str2;
        try {
            String string = jSONObject.getString("name");
            if (jSONObject.has(Property.ALPHA)) {
                str = Property.B;
                str2 = Property.G;
                addAodData(string, Property.ALPHA, (float) jSONObject.getDouble(Property.ALPHA));
            } else {
                str = Property.B;
                str2 = Property.G;
            }
            if (jSONObject.has(Property.X)) {
                addAodData(string, Property.X, (float) jSONObject.getDouble(Property.X));
            }
            if (jSONObject.has(Property.Y)) {
                addAodData(string, Property.Y, (float) jSONObject.getDouble(Property.Y));
            }
            if (jSONObject.has(Property.Z)) {
                addAodData(string, Property.Z, (float) jSONObject.getDouble(Property.Z));
            }
            if (jSONObject.has(Property.SCALE_X)) {
                addAodData(string, Property.SCALE_X, (float) jSONObject.getDouble(Property.SCALE_X));
            }
            if (jSONObject.has(Property.SCALE_Y)) {
                addAodData(string, Property.SCALE_Y, (float) jSONObject.getDouble(Property.SCALE_Y));
            }
            if (jSONObject.has(Property.SCALE_Z)) {
                addAodData(string, Property.SCALE_Z, (float) jSONObject.getDouble(Property.SCALE_Z));
            }
            if (jSONObject.has(Property.ROTATION_X)) {
                addAodData(string, Property.ROTATION_X, (float) jSONObject.getDouble(Property.ROTATION_X));
            }
            if (jSONObject.has(Property.ROTATION_Y)) {
                addAodData(string, Property.ROTATION_Y, (float) jSONObject.getDouble(Property.ROTATION_Y));
            }
            if (jSONObject.has(Property.ROTATION_Z)) {
                addAodData(string, Property.ROTATION_Z, (float) jSONObject.getDouble(Property.ROTATION_Z));
            }
            if (jSONObject.has(Property.R)) {
                addAodData(string, Property.R, (float) jSONObject.getDouble(Property.R));
            }
            String str3 = str2;
            if (jSONObject.has(str3)) {
                addAodData(string, str3, (float) jSONObject.getDouble(str3));
            }
            String str4 = str;
            if (jSONObject.has(str4)) {
                addAodData(string, str4, (float) jSONObject.getDouble(str4));
            }
            if (jSONObject.has(Property.EFFECT)) {
                addAodData(string, Property.EFFECT, (float) jSONObject.getDouble(Property.EFFECT));
            }
            if (jSONObject.has(Property.FRAME_INDEX)) {
                addAodData(string, Property.FRAME_INDEX, (float) jSONObject.getDouble(Property.FRAME_INDEX));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addLockData(JSONObject jSONObject) {
        String str;
        String str2;
        try {
            String string = jSONObject.getString("name");
            if (jSONObject.has(Property.ALPHA)) {
                str = Property.B;
                str2 = Property.G;
                addLockData(string, Property.ALPHA, (float) jSONObject.getDouble(Property.ALPHA));
            } else {
                str = Property.B;
                str2 = Property.G;
            }
            if (jSONObject.has(Property.X)) {
                addLockData(string, Property.X, (float) jSONObject.getDouble(Property.X));
            }
            if (jSONObject.has(Property.Y)) {
                addLockData(string, Property.Y, (float) jSONObject.getDouble(Property.Y));
            }
            if (jSONObject.has(Property.Z)) {
                addLockData(string, Property.Z, (float) jSONObject.getDouble(Property.Z));
            }
            if (jSONObject.has(Property.SCALE_X)) {
                addLockData(string, Property.SCALE_X, (float) jSONObject.getDouble(Property.SCALE_X));
            }
            if (jSONObject.has(Property.SCALE_Y)) {
                addLockData(string, Property.SCALE_Y, (float) jSONObject.getDouble(Property.SCALE_Y));
            }
            if (jSONObject.has(Property.SCALE_Z)) {
                addLockData(string, Property.SCALE_Z, (float) jSONObject.getDouble(Property.SCALE_Z));
            }
            if (jSONObject.has(Property.ROTATION_X)) {
                addLockData(string, Property.ROTATION_X, (float) jSONObject.getDouble(Property.ROTATION_X));
            }
            if (jSONObject.has(Property.ROTATION_Y)) {
                addLockData(string, Property.ROTATION_Y, (float) jSONObject.getDouble(Property.ROTATION_Y));
            }
            if (jSONObject.has(Property.ROTATION_Z)) {
                addLockData(string, Property.ROTATION_Z, (float) jSONObject.getDouble(Property.ROTATION_Z));
            }
            if (jSONObject.has(Property.R)) {
                addLockData(string, Property.R, (float) jSONObject.getDouble(Property.R));
            }
            String str3 = str2;
            if (jSONObject.has(str3)) {
                addLockData(string, str3, (float) jSONObject.getDouble(str3));
            }
            String str4 = str;
            if (jSONObject.has(str4)) {
                addLockData(string, str4, (float) jSONObject.getDouble(str4));
            }
            if (jSONObject.has(Property.EFFECT)) {
                addLockData(string, Property.EFFECT, (float) jSONObject.getDouble(Property.EFFECT));
            }
            if (jSONObject.has(Property.FRAME_INDEX)) {
                addLockData(string, Property.FRAME_INDEX, (float) jSONObject.getDouble(Property.FRAME_INDEX));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addLauncherData(JSONObject jSONObject) {
        String str;
        String str2;
        try {
            String string = jSONObject.getString("name");
            if (jSONObject.has(Property.ALPHA)) {
                str = Property.B;
                str2 = Property.G;
                addLauncherData(string, Property.ALPHA, (float) jSONObject.getDouble(Property.ALPHA));
            } else {
                str = Property.B;
                str2 = Property.G;
            }
            if (jSONObject.has(Property.X)) {
                addLauncherData(string, Property.X, (float) jSONObject.getDouble(Property.X));
            }
            if (jSONObject.has(Property.Y)) {
                addLauncherData(string, Property.Y, (float) jSONObject.getDouble(Property.Y));
            }
            if (jSONObject.has(Property.Z)) {
                addLauncherData(string, Property.Z, (float) jSONObject.getDouble(Property.Z));
            }
            if (jSONObject.has(Property.SCALE_X)) {
                addLauncherData(string, Property.SCALE_X, (float) jSONObject.getDouble(Property.SCALE_X));
            }
            if (jSONObject.has(Property.SCALE_Y)) {
                addLauncherData(string, Property.SCALE_Y, (float) jSONObject.getDouble(Property.SCALE_Y));
            }
            if (jSONObject.has(Property.SCALE_Z)) {
                addLauncherData(string, Property.SCALE_Z, (float) jSONObject.getDouble(Property.SCALE_Z));
            }
            if (jSONObject.has(Property.ROTATION_X)) {
                addLauncherData(string, Property.ROTATION_X, (float) jSONObject.getDouble(Property.ROTATION_X));
            }
            if (jSONObject.has(Property.ROTATION_Y)) {
                addLauncherData(string, Property.ROTATION_Y, (float) jSONObject.getDouble(Property.ROTATION_Y));
            }
            if (jSONObject.has(Property.ROTATION_Z)) {
                addLauncherData(string, Property.ROTATION_Z, (float) jSONObject.getDouble(Property.ROTATION_Z));
            }
            if (jSONObject.has(Property.R)) {
                addLauncherData(string, Property.R, (float) jSONObject.getDouble(Property.R));
            }
            String str3 = str2;
            if (jSONObject.has(str3)) {
                addLauncherData(string, str3, (float) jSONObject.getDouble(str3));
            }
            String str4 = str;
            if (jSONObject.has(str4)) {
                addLauncherData(string, str4, (float) jSONObject.getDouble(str4));
            }
            if (jSONObject.has(Property.EFFECT)) {
                addLauncherData(string, Property.EFFECT, (float) jSONObject.getDouble(Property.EFFECT));
            }
            if (jSONObject.has(Property.FRAME_INDEX)) {
                addLauncherData(string, Property.FRAME_INDEX, (float) jSONObject.getDouble(Property.FRAME_INDEX));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addLockData(String str, String str2, float f) {
        this.mProperties.createOrGet(str, str2).setLockValue(f);
    }

    public void addLockData(String str, String[] strArr, float[] fArr) {
        for (int i = 0; i < strArr.length; i++) {
            addLockData(str, strArr[i], fArr[i]);
        }
    }

    public void addLauncherData(String str, String str2, float f) {
        this.mProperties.createOrGet(str, str2).setLauncherValue(f);
    }

    public void addLauncherData(String str, String[] strArr, float[] fArr) {
        for (int i = 0; i < strArr.length; i++) {
            addLauncherData(str, strArr[i], fArr[i]);
        }
    }

    public PropertySet getPropertySet() {
        return this.mProperties;
    }

    public void stateChange(String str, String str2, boolean z, long j) {
        long j2;
        String str3 = str;
        LogUtil.i(TAG, "previousAction : %s, currentAction : %s, isRunning : %b, time : %d", str3, str2, Boolean.valueOf(z), Long.valueOf(j));
        if (("unknown".equals(this.mPreviousAction) || "unknown".equals(this.mCurrentAction) || this.mPreviousAction.equals(this.mCurrentAction)) ? false : z) {
            long delay = 0;
            for (Animator animator : chooseAnimatorSet(this.mPreviousAction, this.mCurrentAction).getAnimators()) {
                if (animator.getDelay() + animator.getDuration() > delay) {
                    delay = animator.getDelay() + animator.getDuration();
                }
            }
            long delay2 = 0;
            for (Animator animator2 : this.mAnimatorSet.getAnimators()) {
                if (animator2.getDelay() + animator2.getDuration() > delay2) {
                    delay2 = animator2.getDelay() + animator2.getDuration();
                }
            }
            this.mAnimatorSet.clearAnimators();
            this.mProperties.clearAllPropertyKeyframes();
            if (Constant.STATE_LOCK.equals(str3) && Constant.STATE_LAUNCHER.equals(str2)) {
                str3 = Constant.STATE_AOD;
            }
            if (Constant.STATE_LOCK.equals(this.mPreviousAction) && Constant.STATE_LAUNCHER.equals(this.mCurrentAction) && Constant.STATE_AOD.equals(str3)) {
                str3 = Constant.STATE_LOCK;
            }
            AnimatorSet animatorSetChooseAnimatorSet = chooseAnimatorSet(str3, str2);
            long delay3 = 0;
            for (Animator animator3 : animatorSetChooseAnimatorSet.getAnimators()) {
                if (animator3.getDelay() + animator3.getDuration() > delay3) {
                    delay3 = animator3.getDelay() + animator3.getDuration();
                }
            }
            AnimatorSet animatorSet = new AnimatorSet();
            if (Constant.STATE_LOCK.equals(this.mPreviousAction) && Constant.STATE_LAUNCHER.equals(this.mCurrentAction)) {
                j2 = 0;
            } else {
                j2 = ((Constant.STATE_AOD.equals(this.mPreviousAction) && Constant.STATE_AOD.equals(str3)) || (Constant.STATE_AOD.equals(this.mCurrentAction) && Constant.STATE_AOD.equals(str2))) ? j + (delay - delay2) : (delay3 - j) - (delay - delay2);
            }
            if (delay3 - j2 < 200) {
                j2 = delay3 - 200;
            }
            delay = j2 >= 0 ? j2 : 0L;
            this.mAnimationDuration = delay3 - delay;
            LogUtil.i(TAG, "deltaTime : %d", Long.valueOf(delay));
            for (Animator animator4 : animatorSetChooseAnimatorSet.getAnimators()) {
                Animator animator5 = new Animator();
                if (animator4.getDelay() + animator4.getDuration() <= delay) {
                    animator5.setDuration(100L);
                    animator5.addProperty(animator4.getProperties());
                    Iterator<Property> it = animator5.getProperties().iterator();
                    while (it.hasNext()) {
                        it.next().buildSimpleKeyframes(str2);
                    }
                } else if (animator4.getDelay() <= delay) {
                    long delay4 = (animator4.getDelay() + animator4.getDuration()) - delay;
                    long j3 = delay4 >= 100 ? delay4 : 100L;
                    float duration = (animator4.getDuration() - j3) / animator4.getDuration();
                    if (duration < 0.0f) {
                        duration = 0.0f;
                    }
                    animator5.setDuration(j3);
                    animator5.addProperty(animator4.getProperties());
                    Iterator<Property> it2 = animator5.getProperties().iterator();
                    while (it2.hasNext()) {
                        it2.next().buildKeyframes(str3, str2, duration);
                    }
                } else {
                    animator5.setDelay(animator4.getDelay() - delay);
                    animator5.setDuration(animator4.getDuration());
                    animator5.addProperty(animator4.getProperties());
                    Iterator<Property> it3 = animator5.getProperties().iterator();
                    while (it3.hasNext()) {
                        it3.next().buildKeyframes(str3, str2, 0.0f);
                    }
                }
                animatorSet.addAnimator(animator5);
            }
            this.mAnimatorSet.addAllAnimator(animatorSet.getAnimators());
        } else {
            this.mAnimatorSet.clearAnimators();
            this.mProperties.clearAllPropertyKeyframes();
            AnimatorSet animatorSetChooseAnimatorSet2 = chooseAnimatorSet(this.mCurrentAction, str2);
            List<Animator> animators = animatorSetChooseAnimatorSet2.getAnimators();
            if (animators.isEmpty()) {
                this.mAnimationDuration = 0L;
                LogUtil.i(TAG, "mAnimators is empty", new Object[0]);
            } else {
                for (Animator animator6 : animatorSetChooseAnimatorSet2.getAnimators()) {
                    if (animator6.getDelay() + animator6.getDuration() > delay) {
                        delay = animator6.getDelay() + animator6.getDuration();
                    }
                }
                this.mAnimationDuration = delay;
            }
            this.mAnimatorSet.addAllAnimator(animators);
            Iterator<Animator> it4 = this.mAnimatorSet.getAnimators().iterator();
            while (it4.hasNext()) {
                Iterator<Property> it5 = it4.next().getProperties().iterator();
                while (it5.hasNext()) {
                    it5.next().buildKeyframes(this.mCurrentAction, str2, 0.0f);
                }
            }
        }
        this.mPreviousAction = str3;
        this.mCurrentAction = str2;
    }

    protected AnimatorSet chooseAnimatorSet(String str, String str2) {
        LogUtil.i(TAG, "chooseAnimatorSet .... pAction : %s, cAction : %s", str, str2);
        if (str.equals(Constant.STATE_AOD) && str2.equals(Constant.STATE_LOCK)) {
            return this.mAod2LockAnimatorSet;
        }
        if (str.equals(Constant.STATE_AOD) && str2.equals(Constant.STATE_LAUNCHER)) {
            return this.mAod2LauncherAnimatorSet;
        }
        if (str.equals(Constant.STATE_LOCK) && str2.equals(Constant.STATE_AOD)) {
            return this.mLock2AodAnimatorSet;
        }
        if (str.equals(Constant.STATE_LOCK) && str2.equals(Constant.STATE_LAUNCHER)) {
            return this.mLock2LauncherAnimatorSet;
        }
        if (str.equals(Constant.STATE_LAUNCHER) && str2.equals(Constant.STATE_AOD)) {
            return this.mLauncher2AodAnimatorSet;
        }
        return this.mLock2LauncherAnimatorSet;
    }

    public void setState(String str) {
        this.mCurrentAction = str;
        this.mProperties.setState(str);
    }
}
