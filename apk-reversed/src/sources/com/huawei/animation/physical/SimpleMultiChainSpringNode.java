package com.huawei.animation.physical;

import android.view.Choreographer;

/* JADX INFO: loaded from: classes.dex */
public abstract class SimpleMultiChainSpringNode extends SimpleMultiSpringNode {
    public SimpleMultiChainSpringNode(int i) {
        super(i);
    }

    @Override // com.huawei.animation.physical.SimpleMultiSpringNode
    protected void notifyNext(final float f, final float f2, final float f3, final float f4) {
        final SpringNode next = this.adapter.getNext(this);
        if (next != null) {
            if (this == this.adapter.getControlNode()) {
                next.endToValue(f, f2, f3, f4);
            } else {
                Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() { // from class: com.huawei.animation.physical.SimpleMultiChainSpringNode.1
                    @Override // android.view.Choreographer.FrameCallback
                    public void doFrame(long j) {
                        next.endToValue(f, f2, f3, f4);
                    }
                });
            }
        }
    }
}
