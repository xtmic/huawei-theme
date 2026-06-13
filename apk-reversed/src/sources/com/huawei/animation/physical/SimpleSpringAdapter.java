package com.huawei.animation.physical;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SimpleSpringAdapter extends SpringAdapter<SpringNode> {
    List<SpringNode> nodeList = new ArrayList();

    public int getControlIndex() {
        return 0;
    }

    public void addNode(SpringNode springNode) {
        this.nodeList.add(springNode);
        notifyNodeAdd(springNode);
    }

    @Override // com.huawei.animation.physical.SpringAdapter
    public SpringNode getNext(SpringNode springNode) {
        int index = springNode.getIndex() + 1;
        if (index >= this.nodeList.size()) {
            return null;
        }
        return this.nodeList.get(index);
    }

    @Override // com.huawei.animation.physical.SpringAdapter
    public int getSize() {
        return this.nodeList.size();
    }

    @Override // com.huawei.animation.physical.SpringAdapter
    public SpringNode getControlNode() {
        if (this.nodeList.isEmpty()) {
            return null;
        }
        return this.nodeList.get(getControlIndex());
    }
}
