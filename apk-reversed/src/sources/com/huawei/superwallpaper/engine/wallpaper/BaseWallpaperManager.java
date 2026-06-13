package com.huawei.superwallpaper.engine.wallpaper;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Choreographer;
import com.huawei.superwallpaper.engine.ImageList;
import com.huawei.superwallpaper.engine.Node;
import com.huawei.superwallpaper.engine.NodeClear;
import com.huawei.superwallpaper.engine.NodeColor;
import com.huawei.superwallpaper.engine.VideoImageNode;
import com.huawei.superwallpaper.engine.animation.AnimatorController;
import com.huawei.superwallpaper.engine.animation.Constant;
import com.huawei.superwallpaper.engine.animation.Listener;
import com.huawei.superwallpaper.engine.animation.Property;
import com.huawei.superwallpaper.engine.animation.PropertySet;
import com.huawei.superwallpaper.engine.math.Vector3;
import com.huawei.superwallpaper.engine.scene.Scene;
import com.huawei.superwallpaper.engine.util.LogUtil;
import com.huawei.superwallpaper.engine.util.RefreshRateUtil;
import com.huawei.superwallpaper.engine.util.VipThreadUtil;
import java.util.List;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseWallpaperManager {
    protected static final long ANIMATION_START_TIME = -1;
    protected static final String TAG = "WallpaperManager";
    protected static final Object lock = new Object();
    protected AnimatorController animatorController;
    protected float aodDeltaY;
    protected float aodInitY;
    private final Context context;
    protected NodeColor darkColor;
    protected Choreographer.FrameCallback frameCallback;
    protected Listener listener;
    protected OnEventListener onEventListener;
    protected PropertySet propertySet;
    protected Scene scene;
    protected String currentAction = "unknown";
    protected long animateStartTime = -1;
    protected long animateCurrentTime = -1;
    protected boolean animateRunning = false;
    protected boolean inNightMode = false;
    protected float aodDefaultY = 0.0f;
    protected boolean aodMoved = false;
    protected String aodNodeFlagName = "aod";
    protected boolean visible = false;
    protected boolean aodAlignment = true;

    public interface OnEventListener {
        default void onEventPause() {
        }

        default void onEventRender() {
        }

        default void onEventResume() {
        }
    }

    public void beforeSurfaceCreated(CommandData commandData) {
    }

    protected abstract void initAod2LauncherAnimatorSet();

    protected abstract void initAod2LockAnimatorSet();

    protected abstract void initItem();

    protected abstract void initLauncher2AodAnimatorSet();

    protected abstract void initLock2AodAnimatorSet();

    protected abstract void initLock2LauncherAnimatorSet();

    protected void onAnimationUpdate(long j) {
    }

    public void onPropertyUpdateFinish() {
    }

    public void setOnEventListener(OnEventListener onEventListener) {
        this.onEventListener = onEventListener;
    }

    public void setAnimationListener(Listener listener) {
        this.listener = listener;
    }

    public BaseWallpaperManager(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        this.scene = new Scene(this.context);
        AnimatorController animatorControllerCreateAnimatorController = createAnimatorController();
        this.animatorController = animatorControllerCreateAnimatorController;
        this.scene.setAnimatorController(animatorControllerCreateAnimatorController);
        initItem();
        this.propertySet = this.animatorController.getPropertySet();
        initAod2LockAnimatorSet();
        initAod2LauncherAnimatorSet();
        initLock2AodAnimatorSet();
        initLock2LauncherAnimatorSet();
        initLauncher2AodAnimatorSet();
        initNightMode();
        if (this.frameCallback == null) {
            this.frameCallback = initFrameCallback();
        }
        if (shouldFixedLauncherState()) {
            this.currentAction = Constant.STATE_LAUNCHER;
        } else {
            this.currentAction = initState();
        }
        this.animatorController.setState(this.currentAction);
        LogUtil.i(TAG, "init currentAction : %s", this.currentAction);
        this.aodInitY = this.animatorController.getAodData(this.aodNodeFlagName, Property.Y);
    }

    public AnimatorController createAnimatorController() {
        return new AnimatorController();
    }

    public AnimatorController getAnimatorController() {
        return this.animatorController;
    }

    public void setAodInitY(float f) {
        this.aodInitY = f;
    }

    public float getAodInitY() {
        return this.aodInitY;
    }

    public float getAodDefaultY() {
        return this.aodDefaultY;
    }

    public String getCurrentAction() {
        return this.currentAction;
    }

    private void initNightMode() {
        this.inNightMode = 32 == (this.context.getResources().getConfiguration().uiMode & 48);
    }

    protected String initState() {
        PowerManager powerManager = (PowerManager) this.context.getSystemService("power");
        boolean zIsInteractive = powerManager != null ? powerManager.isInteractive() : true;
        KeyguardManager keyguardManager = (KeyguardManager) this.context.getSystemService("keyguard");
        return !zIsInteractive ? Constant.STATE_AOD : keyguardManager != null ? keyguardManager.isKeyguardLocked() : false ? Constant.STATE_LOCK : Constant.STATE_LAUNCHER;
    }

    protected Choreographer.FrameCallback initFrameCallback() {
        return new Choreographer.FrameCallback() { // from class: com.huawei.superwallpaper.engine.wallpaper.-$$Lambda$BaseWallpaperManager$jQStCOPq0x2Rlj3C4BzGkaa8FlQ
            @Override // android.view.Choreographer.FrameCallback
            public final void doFrame(long j) {
                this.f$0.lambda$initFrameCallback$0$BaseWallpaperManager(j);
            }
        };
    }

    public /* synthetic */ void lambda$initFrameCallback$0$BaseWallpaperManager(long j) {
        boolean zStepAnimatorFrame;
        VipThreadUtil.setPriority();
        this.animateRunning = true;
        long j2 = j / 1000000;
        if (this.animateStartTime == -1) {
            this.animateStartTime = j2;
            Listener listener = this.listener;
            if (listener != null) {
                listener.onAnimationStart();
            }
            RefreshRateUtil.setRefreshRate(this.context, 1);
        }
        long j3 = j2 - this.animateStartTime;
        this.animateCurrentTime = j3;
        onAnimationUpdate(j3);
        synchronized (lock) {
            zStepAnimatorFrame = this.animatorController.stepAnimatorFrame(this.animateCurrentTime);
        }
        if (zStepAnimatorFrame) {
            Choreographer.getInstance().postFrameCallback(this.frameCallback);
            Listener listener2 = this.listener;
            if (listener2 != null) {
                listener2.onAnimationUpdate();
            }
        } else {
            this.animateStartTime = -1L;
            this.animateRunning = false;
            Listener listener3 = this.listener;
            if (listener3 != null) {
                listener3.onAnimationEnd();
                this.listener.onAnimationRelease();
            }
            RefreshRateUtil.setRefreshRate(this.context, 0);
        }
        OnEventListener onEventListener = this.onEventListener;
        if (onEventListener != null) {
            onEventListener.onEventRender();
        }
    }

    public final void onCommand(final String str, final String str2, int i, final int i2, int i3, final int i4) {
        LogUtil.i(TAG, "oldState : %s,action : %s,x : %d,y : %d,aodDefaultX : %d,aodDefaultY : %d,", str, str2, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
        String str3 = this.currentAction;
        if (str3 != null && str != null && !str.equals(str3)) {
            LogUtil.e(TAG, "error command!! currentAction = %s, command old = %s, command new = %s", this.currentAction, str, str2);
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            lambda$onCommand$1$BaseWallpaperManager(str, str2, i2, i4);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.huawei.superwallpaper.engine.wallpaper.-$$Lambda$BaseWallpaperManager$OSiGW4xfPWqB-QWWi0QsT8PLon8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCommand$1$BaseWallpaperManager(str, str2, i2, i4);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX INFO: renamed from: onCommandInMainThread, reason: merged with bridge method [inline-methods] */
    public final void lambda$onCommand$1$BaseWallpaperManager(String str, String str2, int i, int i2) {
        dispatchCommand(str, str2, i, i2);
        OnEventListener onEventListener = this.onEventListener;
        if (onEventListener != null) {
            onEventListener.onEventRender();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00b6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchCommand(String str, String str2, int i, int i2) {
        byte b;
        byte b2;
        this.animatorController.addAodData(this.aodNodeFlagName, Property.Y, this.aodInitY);
        if (i2 != 0) {
            float f = i2;
            if (f != this.aodDefaultY) {
                this.aodDefaultY = f;
            }
        }
        if (TextUtils.isEmpty(str)) {
            int iHashCode = str2.hashCode();
            if (iHashCode != -940906782) {
                if (iHashCode != 359703243) {
                    b2 = (iHashCode == 1365320683 && str2.equals(Constant.STATE_LAUNCHER)) ? (byte) 2 : (byte) -1;
                } else if (str2.equals(Constant.STATE_AOD)) {
                    b2 = 0;
                }
            } else if (str2.equals(Constant.STATE_LOCK)) {
                b2 = 1;
            }
            if (b2 != 0) {
                if (b2 == 1) {
                    lock();
                    return;
                } else {
                    if (b2 != 2) {
                        return;
                    }
                    launcher();
                    return;
                }
            }
            if (this.aodAlignment) {
                if (i != 0) {
                    this.aodDeltaY = i - this.aodDefaultY;
                    this.aodMoved = true;
                } else {
                    this.aodDeltaY = 0.0f;
                    this.aodMoved = false;
                }
            }
            this.animatorController.addAodData(this.aodNodeFlagName, Property.Y, this.aodInitY + this.aodDeltaY);
            aod();
            return;
        }
        int iHashCode2 = str.hashCode();
        if (iHashCode2 != -940906782) {
            if (iHashCode2 != 359703243) {
                b = (iHashCode2 == 1365320683 && str.equals(Constant.STATE_LAUNCHER)) ? (byte) 2 : (byte) -1;
            } else if (str.equals(Constant.STATE_AOD)) {
                b = 0;
            }
        } else if (str.equals(Constant.STATE_LOCK)) {
            b = 1;
        }
        if (b == 0) {
            if (this.aodAlignment) {
                if (i != 0) {
                    this.aodDeltaY = i - this.aodDefaultY;
                    this.aodMoved = true;
                }
                if (!this.aodMoved) {
                    this.aodDeltaY = 0.0f;
                }
            }
            if (!this.animateRunning) {
                float f2 = this.aodInitY + this.aodDeltaY;
                this.animatorController.addAodData(this.aodNodeFlagName, Property.Y, f2);
                LogUtil.i(TAG, "reset aodNode Y : " + f2, new Object[0]);
                this.animatorController.setState(Constant.STATE_AOD);
            }
            if (Constant.STATE_AOD.equals(str2)) {
                if (!this.animateRunning) {
                    aod();
                }
            } else if (Constant.STATE_LOCK.equals(str2)) {
                aod2Lock();
            } else if (Constant.STATE_LAUNCHER.equals(str2)) {
                aod2Launcher();
            }
        } else if (b != 1) {
            if (b == 2) {
                if (Constant.STATE_AOD.equals(str2)) {
                    launcher2Aod();
                } else if (Constant.STATE_LAUNCHER.equals(str2) && !this.animateRunning) {
                    launcher();
                }
            }
        } else if (Constant.STATE_AOD.equals(str2)) {
            lock2Aod();
        } else if (Constant.STATE_LOCK.equals(str2)) {
            if (!this.animateRunning) {
                lock();
            }
        } else if (Constant.STATE_LAUNCHER.equals(str2)) {
            lock2Launcher();
        }
        if (this.aodAlignment) {
            this.aodMoved = false;
        }
    }

    public void onSurfaceCreate() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            LogUtil.e(TAG, "onSurfaceCreate should not in mainLooper");
        } else {
            this.scene.getRootNode().initGLContext();
        }
    }

    public void onPause() {
        this.scene.getRootNode().onPause();
    }

    public void onConfigurationChanged(Configuration configuration) {
        int i = configuration.uiMode & 48;
        if (i == 32) {
            this.inNightMode = true;
        } else if (i == 16) {
            this.inNightMode = false;
        }
    }

    public void onDrawFrame() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            LogUtil.e(TAG, "onDrawFrame should not in mainLooper");
            return;
        }
        synchronized (lock) {
            applyPropertyValue(this.scene.getRootNode());
            if (this.darkColor == null) {
                this.darkColor = getDarkColor();
            }
            if (this.darkColor != null) {
                this.darkColor.setIsDark(this.inNightMode);
            }
            onPropertyUpdateFinish();
        }
        this.scene.getRootNode().update();
        this.scene.getRootNode().draw();
    }

    public final void onDetachedFromWindow() {
        Choreographer.getInstance().removeFrameCallback(this.frameCallback);
        this.animateStartTime = -1L;
        this.animateRunning = false;
        this.frameCallback = null;
        if (this.onEventListener != null) {
            this.onEventListener = null;
        }
        if (this.listener != null) {
            this.listener = null;
        }
        this.scene.getRootNode().onDetachedFromWindow();
    }

    public void onVisibleChange(boolean z) {
        this.visible = z;
    }

    protected void aod() {
        stateChange(Constant.STATE_AOD, Constant.STATE_AOD);
    }

    protected void lock() {
        stateChange(Constant.STATE_LOCK, Constant.STATE_LOCK);
    }

    protected void launcher() {
        stateChange(Constant.STATE_LAUNCHER, Constant.STATE_LAUNCHER);
    }

    protected void aod2Lock() {
        stateChange(Constant.STATE_AOD, Constant.STATE_LOCK);
    }

    protected void aod2Launcher() {
        stateChange(Constant.STATE_AOD, Constant.STATE_LAUNCHER);
    }

    protected void lock2Aod() {
        stateChange(Constant.STATE_LOCK, Constant.STATE_AOD);
    }

    protected void lock2Launcher() {
        stateChange(Constant.STATE_LOCK, Constant.STATE_LAUNCHER);
    }

    protected void launcher2Aod() {
        stateChange(Constant.STATE_LAUNCHER, Constant.STATE_AOD);
    }

    protected void stateChange(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogUtil.e(TAG, "stateChange with empty cAction");
            return;
        }
        Choreographer.getInstance().removeFrameCallback(this.frameCallback);
        synchronized (lock) {
            boolean z = this.animateRunning;
            this.animateRunning = false;
            this.animateStartTime = -1L;
            if (this.listener != null) {
                this.listener.onAnimationChange();
            }
            if (str.equals(str2)) {
                if (this.listener != null) {
                    this.listener.onAnimationStart();
                }
                this.animatorController.setState(str2);
                if (this.listener != null) {
                    this.listener.onAnimationEnd();
                }
            } else {
                this.animatorController.stateChange(str, str2, z, this.animateCurrentTime);
            }
            this.currentAction = str2;
        }
        if (str.equals(str2)) {
            return;
        }
        Choreographer.getInstance().postFrameCallback(this.frameCallback);
    }

    private boolean shouldFixedLauncherState() {
        if (isSimpleMode()) {
            return true;
        }
        UserManager userManager = (UserManager) this.context.getSystemService("user");
        if (userManager == null) {
            return false;
        }
        boolean zIsSystemUser = userManager.isSystemUser();
        LogUtil.i(TAG, "is system user : %b", Boolean.valueOf(zIsSystemUser));
        return !zIsSystemUser;
    }

    private boolean isSimpleMode() {
        int i;
        try {
            i = Settings.System.getInt(this.context.getContentResolver(), "new_simple_mode");
            LogUtil.i(TAG, "new_simple_mode : %d", Integer.valueOf(i));
        } catch (Settings.SettingNotFoundException unused) {
            LogUtil.i(TAG, "get new_simple_mode err", new Object[0]);
        }
        return i != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyPropertyValue(Node node) {
        List<Node> children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            children.forEach(new Consumer() { // from class: com.huawei.superwallpaper.engine.wallpaper.-$$Lambda$BaseWallpaperManager$DUrbBp9ZRWqMiEqc2YTDcDdiEeI
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.applyPropertyValue((Node) obj);
                }
            });
        }
        setPropertyValue2Node(node);
    }

    private void setPropertyValue2Node(Node node) {
        Property property;
        Property property2 = this.propertySet.get(node.getName(), Property.ALPHA);
        Property property3 = this.propertySet.get(node.getName(), Property.X);
        Property property4 = this.propertySet.get(node.getName(), Property.Y);
        Property property5 = this.propertySet.get(node.getName(), Property.Z);
        Property property6 = this.propertySet.get(node.getName(), Property.SCALE_X);
        Property property7 = this.propertySet.get(node.getName(), Property.SCALE_Y);
        Property property8 = this.propertySet.get(node.getName(), Property.SCALE_Z);
        Property property9 = this.propertySet.get(node.getName(), Property.ROTATION_X);
        Property property10 = this.propertySet.get(node.getName(), Property.ROTATION_Y);
        Property property11 = this.propertySet.get(node.getName(), Property.ROTATION_Z);
        if (property2 != null) {
            node.setAlpha(property2.getValue());
        }
        if (property3 != null) {
            node.setX(property3.getValue());
        }
        if (property4 != null) {
            node.setY(-property4.getValue());
        }
        if (property5 != null) {
            node.setZ(property5.getValue());
        }
        if (property6 != null) {
            node.setScaleX(property6.getValue());
        }
        if (property7 != null) {
            node.setScaleY(property7.getValue());
        }
        if (property8 != null) {
            node.setScaleZ(property8.getValue());
        }
        if (property9 != null) {
            node.setRotation(Vector3.Axis.X, property9.getValue());
        }
        if (property10 != null) {
            node.setRotation(Vector3.Axis.Y, property10.getValue());
        }
        if (property11 != null) {
            node.setRotation(Vector3.Axis.Z, property11.getValue());
        }
        if (node instanceof NodeClear) {
            Property property12 = this.propertySet.get(node.getName(), Property.R);
            Property property13 = this.propertySet.get(node.getName(), Property.G);
            Property property14 = this.propertySet.get(node.getName(), Property.B);
            if (property12 != null) {
                ((NodeClear) node).setColorRed(property12.getValue());
            }
            if (property13 != null) {
                ((NodeClear) node).setColorGreen(property13.getValue());
            }
            if (property14 != null) {
                ((NodeClear) node).setColorBlue(property14.getValue());
            }
        }
        if ((node instanceof VideoImageNode) && (property = this.propertySet.get(node.getName(), Property.EFFECT)) != null) {
            ((VideoImageNode) node).setMaskAnimateParams(property.getValue());
        }
        if (node instanceof ImageList) {
            Property property15 = this.propertySet.get(node.getName(), Property.FRAME_INDEX);
            if (property15 != null) {
                ((ImageList) node).setFrameIndex(property15.getValue());
            }
            Property property16 = this.propertySet.get(node.getName(), Property.EFFECT);
            if (property16 != null) {
                ((ImageList) node).setNormedTime(property16.getValue());
            }
        }
    }

    protected NodeColor getDarkColor() {
        return (NodeColor) this.scene.getRootNode().getChildByName("darkcolor");
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getAodNodeFlagName() {
        return this.aodNodeFlagName;
    }
}
