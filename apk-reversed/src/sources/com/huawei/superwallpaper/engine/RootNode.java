package com.huawei.superwallpaper.engine;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class RootNode extends Node {
    public final List<Node> mAllNode = Collections.synchronizedList(new CopyOnWriteArrayList());
    private boolean mIsDirty;

    public RootNode() {
        this.mName = "RootNode";
        this.mIsDirty = false;
    }

    public void update() {
        if (!this.mIsDirty) {
            this.mAllNode.clear();
            int size = this.mChildren.size();
            for (int i = 0; i < size; i++) {
                tree2list(this.mChildren.get(i), this.mAllNode);
            }
            this.mIsDirty = true;
        }
        calculateModelMatrix();
        this.mWorldMatrix = this.mMMatrix;
        int size2 = this.mChildren.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.mChildren.get(i2).updateAll();
        }
        Collections.sort(this.mAllNode);
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void draw() {
        int size = this.mAllNode.size();
        for (int i = 0; i < size; i++) {
            Node node = this.mAllNode.get(i);
            if (node.couldDraw()) {
                node.draw();
            }
        }
    }

    public void tree2list(Node node, List<Node> list) {
        if (node == null) {
            return;
        }
        list.add(node);
        if (node.getNumChildren() == 0) {
            return;
        }
        Iterator<Node> it = node.mChildren.iterator();
        while (it.hasNext()) {
            tree2list(it.next(), list);
        }
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void onDetachedFromWindow() {
        int size = this.mAllNode.size();
        for (int i = 0; i < size; i++) {
            this.mAllNode.get(i).onDetachedFromWindow();
        }
    }

    @Override // com.huawei.superwallpaper.engine.Node
    public void onPause() {
        int size = this.mAllNode.size();
        for (int i = 0; i < size; i++) {
            this.mAllNode.get(i).onPause();
        }
    }
}
