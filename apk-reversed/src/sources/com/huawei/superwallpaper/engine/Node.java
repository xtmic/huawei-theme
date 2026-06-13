package com.huawei.superwallpaper.engine;

import com.huawei.superwallpaper.engine.math.Matrix4;
import com.huawei.superwallpaper.engine.math.Vector3;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class Node extends ATransformable3D implements Comparable<Node> {
    protected float alpha;
    protected List<Node> mChildren;
    protected boolean mIsVisible;
    protected String mName;
    protected Node mParent;
    protected Matrix4 mWorldMatrix;

    public void draw() {
    }

    public void onDetachedFromWindow() {
    }

    public void onPause() {
    }

    public Node() {
        this.mChildren = Collections.synchronizedList(new CopyOnWriteArrayList());
        this.alpha = 1.0f;
        this.mIsVisible = true;
    }

    public Node(String str) {
        this();
        this.mName = str;
    }

    public void initGLContext() {
        int numChildren = getNumChildren();
        for (int i = 0; i < numChildren; i++) {
            getChildAt(i).initGLContext();
        }
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public void setVisible(boolean z) {
        this.mIsVisible = z;
    }

    public void addChild(Node node) {
        this.mChildren.add(node);
        node.setParent(this);
    }

    public void addChildren(List<Image> list) {
        list.forEach(new Consumer() { // from class: com.huawei.superwallpaper.engine.-$$Lambda$Z-9v8fBy-UZ3VZ_D81FwnWN6moE
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.addChild((Image) obj);
            }
        });
    }

    @Override // java.lang.Comparable
    public int compareTo(Node node) {
        return Double.compare(getWorldPosition().z, node.getWorldPosition().z);
    }

    public Node getParent() {
        return this.mParent;
    }

    public void updateAll() {
        calculateModelMatrix();
        if (this.mParent == null) {
            this.mWorldMatrix = this.mMMatrix;
        } else {
            this.mWorldMatrix = this.mMMatrix.leftMultiply(this.mParent.mWorldMatrix);
            Node node = this.mParent;
            if (node.alpha <= 0.0f || !node.mIsVisible) {
                this.alpha = 0.0f;
            }
        }
        int numChildren = getNumChildren();
        for (int i = 0; i < numChildren; i++) {
            getChildAt(i).updateAll();
        }
    }

    public boolean couldDraw() {
        return this.alpha > 0.0f && this.mIsVisible;
    }

    public Vector3 getWorldPosition() {
        return this.mWorldMatrix.getTranslation();
    }

    public int getNumChildren() {
        return this.mChildren.size();
    }

    public List<Node> getChildren() {
        return this.mChildren;
    }

    public Node getChildAt(int i) {
        return this.mChildren.get(i);
    }

    public Node getChildByName(String str) {
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            Node node = this.mChildren.get(i);
            if (node.getName() != null && node.getName().equals(str)) {
                return node;
            }
        }
        return null;
    }

    private void setParent(Node node) {
        this.mParent = node;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public String getName() {
        return this.mName;
    }

    public void setAlpha(float f) {
        this.alpha = f;
    }

    public void setAlpha(int i) {
        this.alpha = i / 255.0f;
    }
}
