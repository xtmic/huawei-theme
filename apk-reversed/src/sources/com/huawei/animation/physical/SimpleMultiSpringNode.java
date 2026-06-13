package com.huawei.animation.physical;

import android.os.SystemClock;
import android.util.Log;
import android.view.Choreographer;
import com.huawei.animation.physical.SpringNode;
import java.util.Iterator;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public abstract class SimpleMultiSpringNode extends SpringNode implements Choreographer.FrameCallback {
    private static final String TAG = SimpleMultiSpringNode.class.getName();
    private Spring springX;
    private Spring springY;
    private float valueAccuracy;
    private float valueX;
    private float valueY;
    private float velocityX;
    private float velocityY;

    public SimpleMultiSpringNode(int i) {
        super(i);
        this.valueX = 0.0f;
        this.velocityX = 0.0f;
        this.valueY = 0.0f;
        this.velocityY = 0.0f;
        this.valueAccuracy = 1.0f;
        this.springX = new Spring();
        this.springY = new Spring();
    }

    @Override // com.huawei.animation.physical.SpringNode
    void setValue(float f, float f2) {
        super.setValue(f, f2);
        this.valueX = f;
        this.valueY = f2;
        onUpdate(f, f2, this.velocityX, this.velocityY);
        Iterator<SpringNode.Listener> it = this.listenerList.iterator();
        while (it.hasNext()) {
            it.next().onAnimationUpdate(this.valueX, this.valueY, this.velocityX, this.velocityY);
        }
        notifyNext(this.valueX, this.valueY, this.velocityX, this.velocityY);
    }

    @Override // com.huawei.animation.physical.SpringNode
    void endToValue(float f, float f2, float f3, float f4) {
        super.endToValue(f, f2, f3, f4);
        if (this.isRunning) {
            if (this.adapter.getControlNode().isAnimToEnd()) {
                this.startTime = SystemClock.uptimeMillis() - 16;
            } else {
                this.startTime = SystemClock.uptimeMillis() - ((long) ((int) (getFrameDelta() * 16.0f)));
            }
            this.springX.setStartValue(this.valueX).setEndValue(f).setStartVelocity(this.velocityX).setValueAccuracy(this.valueAccuracy).initialize();
            this.springY.setStartValue(this.valueY).setEndValue(f2).setStartVelocity(this.velocityY).setValueAccuracy(this.valueAccuracy).initialize();
        } else {
            this.startTime = SystemClock.uptimeMillis();
            this.isRunning = true;
            this.springX.setStartValue(this.valueX).setEndValue(f).setStartVelocity(f3).setValueAccuracy(this.valueAccuracy).initialize();
            this.springY.setStartValue(this.valueY).setEndValue(f2).setStartVelocity(f4).setValueAccuracy(this.valueAccuracy).initialize();
            Choreographer.getInstance().postFrameCallback(this);
        }
        notifyNext(f, f2, f3, f4);
    }

    @Override // com.huawei.animation.physical.SpringNode
    public void cancel() {
        Choreographer.getInstance().removeFrameCallback(this);
        this.isRunning = false;
        onEnd(this.valueX, this.valueY);
    }

    @Override // com.huawei.animation.physical.SpringNode
    void transferParams(float f, float f2) {
        this.springX.setStiffness(f).setDamping(f2);
        this.springY.setStiffness(f).setDamping(f2);
    }

    protected void notifyNext(float f, float f2, float f3, float f4) {
        if (this != this.adapter.getControlNode()) {
            return;
        }
        SpringNode next = this.adapter.getNext(this);
        while (next != null) {
            next.endToValue(f, f2, f3, f4);
            next = this.adapter.getNext(next);
        }
    }

    @Override // android.view.Choreographer.FrameCallback
    public void doFrame(long j) {
        long jUptimeMillis = SystemClock.uptimeMillis() - this.startTime;
        this.valueX = this.springX.getValue(jUptimeMillis);
        this.velocityX = this.springX.getVelocity(jUptimeMillis);
        this.valueY = this.springY.getValue(jUptimeMillis);
        this.velocityY = this.springY.getVelocity(jUptimeMillis);
        Log.d(TAG, String.format(Locale.ROOT, "doFrame: index=%s,value=(%s,%s)", Integer.valueOf(getIndex()), Float.valueOf(this.valueX), Float.valueOf(this.valueY)));
        if (this.springX.isAtEquilibrium(this.valueX, this.velocityX) && this.springY.isAtEquilibrium(this.valueY, this.velocityY)) {
            this.valueX = this.springX.getEndValue();
            this.velocityX = 0.0f;
            float endValue = this.springY.getEndValue();
            this.valueY = endValue;
            this.velocityY = 0.0f;
            onUpdate(this.valueX, endValue, this.velocityX, 0.0f);
            Iterator<SpringNode.Listener> it = this.listenerList.iterator();
            while (it.hasNext()) {
                it.next().onAnimationUpdate(this.valueX, this.valueY, this.velocityX, this.velocityY);
            }
            this.isRunning = false;
            onEnd(this.valueX, this.valueY);
            Log.w(TAG, String.format(Locale.ROOT, "doFrame: index=%s is at equilibrium value=(%s,%s).", Integer.valueOf(getIndex()), Float.valueOf(this.valueX), Float.valueOf(this.valueY)));
            return;
        }
        onUpdate(this.valueX, this.valueY, this.velocityX, this.velocityY);
        Iterator<SpringNode.Listener> it2 = this.listenerList.iterator();
        while (it2.hasNext()) {
            it2.next().onAnimationUpdate(this.valueX, this.valueY, this.velocityX, this.velocityY);
        }
        Choreographer.getInstance().postFrameCallback(this);
    }

    public SimpleMultiSpringNode setValueAccuracy(float f) {
        this.valueAccuracy = f;
        return this;
    }

    public float getValueX() {
        return this.valueX;
    }

    public void setValueX(float f) {
        this.valueX = f;
    }

    public float getValueY() {
        return this.valueY;
    }

    public void setValueY(float f) {
        this.valueY = f;
    }

    @Override // com.huawei.animation.physical.SpringNode
    public void resetValue(float f, float f2) {
        this.valueX = f;
        this.valueY = f2;
    }
}
