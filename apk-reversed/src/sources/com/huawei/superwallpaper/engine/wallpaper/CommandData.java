package com.huawei.superwallpaper.engine.wallpaper;

/* JADX INFO: loaded from: classes.dex */
public class CommandData {
    public int aodDefaultX;
    public int aodDefaultY;
    public String newAction;
    public String oldAction;
    public int x;
    public int y;

    public CommandData(String str, String str2, int i, int i2, int i3, int i4) {
        this.oldAction = "unknown";
        this.newAction = "unknown";
        this.x = 0;
        this.y = 0;
        this.aodDefaultX = 0;
        this.aodDefaultY = 0;
        this.oldAction = str;
        this.newAction = str2;
        this.x = i;
        this.y = i2;
        this.aodDefaultX = i3;
        this.aodDefaultY = i4;
    }
}
