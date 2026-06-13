package com.huawei.animation.physical;

import com.huawei.animation.physical.SpringNode;

/* JADX INFO: loaded from: classes.dex */
public abstract class SpringAdapter<VH extends SpringNode> {
    private AdapterObserve observe;

    interface AdapterObserve {
        void onControlNodeChange();

        void onNodeAdd(SpringNode springNode);

        void onNodesDelete(SpringNode springNode, int i);
    }

    public abstract VH getControlNode();

    public abstract VH getNext(VH vh);

    public abstract int getSize();

    public void notifyControlIndexChange() {
        AdapterObserve adapterObserve = this.observe;
        if (adapterObserve != null) {
            adapterObserve.onControlNodeChange();
        }
    }

    public void notifyNodeAdd(SpringNode springNode) {
        AdapterObserve adapterObserve = this.observe;
        if (adapterObserve != null) {
            adapterObserve.onNodeAdd(springNode);
        }
    }

    private void notifyNodesDelete(SpringNode springNode, int i) {
        AdapterObserve adapterObserve = this.observe;
        if (adapterObserve != null) {
            adapterObserve.onNodesDelete(springNode, i);
        }
    }

    public void notifyNodeDelete(SpringNode springNode) {
        notifyNodesDelete(springNode, 1);
    }

    void setObserve(AdapterObserve adapterObserve) {
        this.observe = adapterObserve;
    }
}
