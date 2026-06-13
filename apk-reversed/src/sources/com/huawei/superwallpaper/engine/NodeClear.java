package com.huawei.superwallpaper.engine;

import android.opengl.GLES32;

/* JADX INFO: loaded from: classes.dex */
public class NodeClear extends Node {
    private float mColorBlue;
    private float mColorGreen;
    private float mColorRed;

    public NodeClear(String str) {
        super(str);
    }

    public float getColorRed() {
        return this.mColorRed;
    }

    public void setColorRed(float f) {
        this.mColorRed = f;
    }

    public void setColorGreen(float f) {
        this.mColorGreen = f;
    }

    public void setColorBlue(float f) {
        this.mColorBlue = f;
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void draw() {
        super.draw();
        GLES32.glClearColor(this.mColorRed, this.mColorGreen, this.mColorBlue, 1.0f);
        GLES32.glClear(16640);
    }
}
