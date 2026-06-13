package com.huawei.superwallpaper.engine.animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class KeyframeSet {
    private String mEndState;
    private final List<Keyframe> mKeyframes = new ArrayList();
    private String mStartState;

    public KeyframeSet(String str, String str2) {
        this.mStartState = "unknown";
        this.mEndState = "unknown";
        this.mStartState = str;
        this.mEndState = str2;
    }

    public void addKeyframe(Keyframe keyframe) {
        this.mKeyframes.add(keyframe);
    }

    public void addKeyframes(Keyframe... keyframeArr) {
        this.mKeyframes.addAll(Arrays.asList(keyframeArr));
    }

    public List<Keyframe> getKeyframes() {
        return this.mKeyframes;
    }

    public String getStartState() {
        return this.mStartState;
    }

    public String getEndState() {
        return this.mEndState;
    }
}
