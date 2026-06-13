package com.huawei.animation.physical;

import android.os.SystemClock;
import android.util.Log;
import android.view.Choreographer;
import com.huawei.animation.physical.SpringNode;
import java.util.Iterator;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public abstract class SimpleSpringNode extends SpringNode implements Choreographer.FrameCallback {
    private static final String TAG = SimpleSpringNode.class.getName();
    private Spring spring;
    private float value;
    private float valueAccuracy;
    private float velocity;

    public SimpleSpringNode(int i, float f) {
        super(i);
        this.velocity = 0.0f;
        this.valueAccuracy = 1.0f;
        this.value = f;
        this.spring = new Spring();
    }

    public SimpleSpringNode(int i) {
        this(i, 0.0f);
    }

    @Override // com.huawei.animation.physical.SpringNode
    void setValue(float f) {
        super.setValue(f);
        this.value = f;
        onUpdate(f, this.velocity);
        Iterator<SpringNode.Listener> it = this.listenerList.iterator();
        while (it.hasNext()) {
            it.next().onAnimationUpdate(f, this.velocity);
        }
        notifyNext(f, this.velocity);
    }

    @Override // com.huawei.animation.physical.SpringNode
    void endToValue(float f, float f2) {
        super.endToValue(f, f2);
        if (this.isRunning) {
            if (this.adapter.getControlNode().isAnimToEnd()) {
                this.startTime = SystemClock.uptimeMillis() - 16;
            } else {
                this.startTime = SystemClock.uptimeMillis() - ((long) ((int) (getFrameDelta() * 16.0f)));
            }
            this.spring.setStartValue(this.value).setEndValue(f).setStartVelocity(this.velocity).setValueAccuracy(this.valueAccuracy).initialize();
        } else {
            this.startTime = SystemClock.uptimeMillis();
            this.isRunning = true;
            this.spring.setStartValue(this.value).setEndValue(f).setStartVelocity(f2).setValueAccuracy(this.valueAccuracy).initialize();
            Choreographer.getInstance().postFrameCallback(this);
        }
        notifyNext(f, f2);
    }

    @Override // com.huawei.animation.physical.SpringNode
    public void cancel() {
        Choreographer.getInstance().removeFrameCallback(this);
        this.isRunning = false;
        onEnd(this.value);
    }

    @Override // com.huawei.animation.physical.SpringNode
    void transferParams(float f, float f2) {
        this.spring.setStiffness(f).setDamping(f2);
    }

    private void notifyNext(float f, float f2) {
        if (this != this.adapter.getControlNode()) {
            return;
        }
        SpringNode next = this.adapter.getNext(this);
        while (next != null) {
            next.endToValue(f, f2);
            next = this.adapter.getNext(next);
        }
    }

    @Override // android.view.Choreographer.FrameCallback
    public void doFrame(long j) {
        long jUptimeMillis = SystemClock.uptimeMillis() - this.startTime;
        this.value = this.spring.getValue(jUptimeMillis);
        this.velocity = this.spring.getVelocity(jUptimeMillis);
        Log.d(TAG, String.format(Locale.ROOT, "doFrame: index=%s,value=%s", Integer.valueOf(getIndex()), Float.valueOf(this.value)));
        if (this.spring.isAtEquilibrium(this.value, this.velocity)) {
            float endValue = this.spring.getEndValue();
            this.value = endValue;
            this.velocity = 0.0f;
            onUpdate(endValue, 0.0f);
            Iterator<SpringNode.Listener> it = this.listenerList.iterator();
            while (it.hasNext()) {
                it.next().onAnimationUpdate(this.value, this.velocity);
            }
            this.isRunning = false;
            onEnd(this.value);
            Log.w(TAG, String.format(Locale.ROOT, "doFrame: index=%s is at equilibrium value=%s.", Integer.valueOf(getIndex()), Float.valueOf(this.value)));
            return;
        }
        onUpdate(this.value, this.velocity);
        Iterator<SpringNode.Listener> it2 = this.listenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onAnimationUpdate(this.value, this.velocity);
        }
        Choreographer.getInstance().postFrameCallback(this);
    }

    public SimpleSpringNode setValueAccuracy(float f) {
        this.valueAccuracy = f;
        return this;
    }

    public float getValue() {
        return this.value;
    }

    @Override // com.huawei.animation.physical.SpringNode
    public void resetValue(float f) {
        this.value = f;
    }
}
