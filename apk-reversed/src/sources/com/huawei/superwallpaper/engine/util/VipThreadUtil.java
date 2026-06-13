package com.huawei.superwallpaper.engine.util;

import android.os.Process;
import com.huawei.android.iaware.IAwareSdkEx;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class VipThreadUtil {
    private static final String RT_THREAD_FORMAT = "pid:%d;tid:%d,%d";
    private static final int RT_THREAD_SET_EVENT_ID = 3011;
    private static final long SET_INTERVAL = 900;
    private static final String TAG = "VipThreadUtil";
    private static final String VIP_THREAD_FORMAT = "pid:%d;tid:%d,%d;uid:%d;";
    private static final int VIP_THREAD_SET_EVENT_ID = 3006;
    private static long lastSetTime;
    public static volatile int sRenderThreadId;

    private VipThreadUtil() {
    }

    private static void setVip() {
        String str = String.format(Locale.ENGLISH, VIP_THREAD_FORMAT, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()), Integer.valueOf(sRenderThreadId), Integer.valueOf(Process.myUid()));
        LogUtil.i(TAG, "setVIP_main & render : %s", str);
        IAwareSdkEx.reportData(VIP_THREAD_SET_EVENT_ID, str);
    }

    private static void setRT() {
        String str = String.format(Locale.ENGLISH, RT_THREAD_FORMAT, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()), Integer.valueOf(sRenderThreadId));
        LogUtil.i(TAG, "setRT : %s", str);
        IAwareSdkEx.reportData(RT_THREAD_SET_EVENT_ID, str);
    }

    public static void setPriority() {
        if (frequent()) {
            return;
        }
        setVip();
    }

    private static boolean frequent() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - lastSetTime < SET_INTERVAL) {
            return true;
        }
        lastSetTime = jCurrentTimeMillis;
        return false;
    }
}
