package com.huawei.animation.physical;

import com.huawei.animation.physical.SpringAdapter;

/* JADX INFO: loaded from: classes.dex */
public class SimpleSpringChain implements SpringAdapter.AdapterObserve {
    private SpringAdapter springAdapter;
    private float controlStiffness = 228.0f;
    private float controlDamping = 30.0f;
    private IParamTransfer<Float> stiffnessTransfer = new ParamsTransferImpl(1.0f);
    private IParamTransfer<Float> dampingTransfer = new ParamsTransferImpl();
    private float frameDelta = 1.0f;

    public SimpleSpringChain(SpringAdapter springAdapter) {
        this.springAdapter = springAdapter;
        springAdapter.setObserve(this);
        transferParamsInternal();
    }

    @Override // com.huawei.animation.physical.SpringAdapter.AdapterObserve
    public void onControlNodeChange() {
        transferParamsInternal();
    }

    @Override // com.huawei.animation.physical.SpringAdapter.AdapterObserve
    public void onNodeAdd(SpringNode springNode) {
        if (springNode == null) {
            return;
        }
        setParams(springNode);
    }

    @Override // com.huawei.animation.physical.SpringAdapter.AdapterObserve
    public void onNodesDelete(SpringNode springNode, int i) {
        if (springNode == null) {
            return;
        }
        SpringNode next = this.springAdapter.getNext(springNode);
        while (next != null) {
            next.setIndex(next.getIndex() - i);
            setParams(next);
            next = this.springAdapter.getNext(next);
        }
    }

    private void transferParamsInternal() {
        SpringNode controlNode = this.springAdapter.getControlNode();
        for (int i = 0; i < this.springAdapter.getSize(); i++) {
            if (controlNode != null) {
                setParams(controlNode);
                controlNode = this.springAdapter.getNext(controlNode);
            }
        }
    }

    private void setParams(SpringNode springNode) {
        int index = springNode.getIndex();
        SpringNode controlNode = this.springAdapter.getControlNode();
        if (controlNode == null) {
            controlNode = springNode;
        }
        int iAbs = Math.abs(index - controlNode.getIndex());
        springNode.transferParams(this.stiffnessTransfer.transfer(Float.valueOf(this.controlStiffness), iAbs).floatValue(), this.dampingTransfer.transfer(Float.valueOf(this.controlDamping), iAbs).floatValue());
        springNode.setFrameDelta(this.frameDelta);
        if (springNode.getAdapter() == null) {
            springNode.setAdapter(this.springAdapter);
        }
    }

    public SimpleSpringChain setValue(float f) {
        SpringNode controlNode = this.springAdapter.getControlNode();
        if (controlNode != null) {
            controlNode.setValue(f);
        }
        return this;
    }

    public SimpleSpringChain setValue(float f, float f2) {
        SpringNode controlNode = this.springAdapter.getControlNode();
        if (controlNode != null) {
            controlNode.setValue(f, f2);
        }
        return this;
    }

    public SimpleSpringChain endToPosition(float f, float f2) {
        SpringNode controlNode = this.springAdapter.getControlNode();
        if (controlNode != null) {
            controlNode.endToValue(f, f2);
        }
        return this;
    }

    public SimpleSpringChain endToPosition(float f, float f2, float f3, float f4) {
        SpringNode controlNode = this.springAdapter.getControlNode();
        if (controlNode != null) {
            controlNode.endToValue(f, f2, f3, f4);
        }
        return this;
    }

    public SimpleSpringChain cancel() {
        SpringNode controlNode = this.springAdapter.getControlNode();
        for (int i = 0; i < this.springAdapter.getSize(); i++) {
            if (controlNode != null) {
                controlNode.cancel();
                controlNode = this.springAdapter.getNext(controlNode);
            }
        }
        return this;
    }

    public SimpleSpringChain transferParams() {
        transferParamsInternal();
        return this;
    }

    public SpringNode getControlNode() {
        return this.springAdapter.getControlNode();
    }

    public int getSize() {
        return this.springAdapter.getSize();
    }

    public float getControlStiffness() {
        return this.controlStiffness;
    }

    public SimpleSpringChain setControlStiffness(float f) {
        this.controlStiffness = f;
        return this;
    }

    public float getControlDamping() {
        return this.controlDamping;
    }

    public SimpleSpringChain setControlDamping(float f) {
        this.controlDamping = f;
        return this;
    }

    public IParamTransfer<Float> getStiffnessTransfer() {
        return this.stiffnessTransfer;
    }

    public SimpleSpringChain setStiffnessTransfer(IParamTransfer<Float> iParamTransfer) {
        this.stiffnessTransfer = iParamTransfer;
        return this;
    }

    public IParamTransfer<Float> getDampingTransfer() {
        return this.dampingTransfer;
    }

    public SimpleSpringChain setDampingTransfer(IParamTransfer<Float> iParamTransfer) {
        this.dampingTransfer = iParamTransfer;
        return this;
    }

    public SpringAdapter getSpringAdapter() {
        return this.springAdapter;
    }

    public SimpleSpringChain setSpringAdapter(SpringAdapter springAdapter) {
        this.springAdapter = springAdapter;
        return this;
    }

    public float getFrameDelta() {
        return this.frameDelta;
    }

    public SimpleSpringChain setFrameDelta(float f) {
        this.frameDelta = f;
        return this;
    }
}
