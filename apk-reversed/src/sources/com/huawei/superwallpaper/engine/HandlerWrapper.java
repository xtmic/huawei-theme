package com.huawei.superwallpaper.engine;

import android.os.Handler;
import android.os.Message;

/* JADX INFO: loaded from: classes.dex */
public class HandlerWrapper {
    private final Handler handler;

    public HandlerWrapper(Handler handler) {
        this.handler = handler;
    }

    public void sendEmptyMessage(int i) {
        sendEmptyMessageDelayed(i, 0L);
    }

    public void sendEmptyMessageDelayed(int i, long j) {
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeMessages(i);
            this.handler.sendEmptyMessageDelayed(i, j);
        }
    }

    public void sendMessage(Message message) {
        sendMessageDelayed(message, 0L);
    }

    public void sendMessageDelayed(Message message, long j) {
        Handler handler = this.handler;
        if (handler == null || message == null) {
            return;
        }
        handler.removeMessages(message.what);
        this.handler.sendMessageDelayed(message, j);
    }

    public void removeMessages(int i) {
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeMessages(i);
        }
    }

    public void removeCallbacksAndMessages() {
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public boolean hasMessages(int i) {
        Handler handler = this.handler;
        if (handler != null) {
            return handler.hasMessages(i);
        }
        return false;
    }
}
