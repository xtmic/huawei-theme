package com.huawei.animation.physical;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public abstract class SpringNode {
    protected SpringAdapter adapter;
    private int index;
    protected long startTime;
    protected List<Listener> listenerList = new CopyOnWriteArrayList();
    protected boolean isRunning = false;
    private float frameDelta = 1.0f;
    private boolean isAnimToEnd = false;

    public interface Listener {
        void onAnimationUpdate(float f, float f2);

        void onAnimationUpdate(float f, float f2, float f3, float f4);
    }

    public static abstract class ListenerAdapter implements Listener {
        @Override // com.huawei.animation.physical.SpringNode.Listener
        public void onAnimationUpdate(float f, float f2) {
        }

        @Override // com.huawei.animation.physical.SpringNode.Listener
        public void onAnimationUpdate(float f, float f2, float f3, float f4) {
        }
    }

    public abstract void cancel();

    public void onEnd(float f) {
    }

    public void onEnd(float f, float f2) {
    }

    public void onUpdate(float f, float f2) {
    }

    public void onUpdate(float f, float f2, float f3, float f4) {
    }

    public void resetValue(float f) {
    }

    public void resetValue(float f, float f2) {
    }

    abstract void transferParams(float f, float f2);

    public SpringNode(int i) {
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    public SpringNode setIndex(int i) {
        this.index = i;
        return this;
    }

    public float getFrameDelta() {
        return this.frameDelta;
    }

    public SpringNode setFrameDelta(float f) {
        this.frameDelta = f;
        return this;
    }

    public SpringNode addListener(Listener listener) {
        if (listener != null) {
            this.listenerList.add(listener);
        }
        return this;
    }

    public SpringNode removeListener(Listener listener) {
        this.listenerList.remove(listener);
        return this;
    }

    public SpringAdapter getAdapter() {
        return this.adapter;
    }

    public SpringNode setAdapter(SpringAdapter springAdapter) {
        this.adapter = springAdapter;
        return this;
    }

    public boolean isAnimToEnd() {
        return this.isAnimToEnd;
    }

    public SpringNode setAnimToEnd(boolean z) {
        this.isAnimToEnd = z;
        return this;
    }

    void setValue(float f) {
        this.isAnimToEnd = false;
    }

    void setValue(float f, float f2) {
        this.isAnimToEnd = false;
    }

    void endToValue(float f, float f2) {
        this.isAnimToEnd = true;
    }

    void endToValue(float f, float f2, float f3, float f4) {
        this.isAnimToEnd = true;
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
