package com.huawei.superwallpaper.engine.scene;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.superwallpaper.engine.Image;
import com.huawei.superwallpaper.engine.ImageColor;
import com.huawei.superwallpaper.engine.ImageList;
import com.huawei.superwallpaper.engine.Node;
import com.huawei.superwallpaper.engine.NodeClear;
import com.huawei.superwallpaper.engine.NodeColor;
import com.huawei.superwallpaper.engine.RootNode;
import com.huawei.superwallpaper.engine.VideoImageNode;
import com.huawei.superwallpaper.engine.VideoLowMemoryNode;
import com.huawei.superwallpaper.engine.animation.Animator;
import com.huawei.superwallpaper.engine.animation.AnimatorController;
import com.huawei.superwallpaper.engine.animation.AnimatorSet;
import com.huawei.superwallpaper.engine.animation.Constant;
import com.huawei.superwallpaper.engine.animation.InterpolatorHelper;
import com.huawei.superwallpaper.engine.animation.Keyframe;
import com.huawei.superwallpaper.engine.animation.KeyframeSet;
import com.huawei.superwallpaper.engine.animation.Property;
import com.huawei.superwallpaper.engine.animation.PropertySet;
import com.huawei.superwallpaper.engine.math.Vector3;
import com.huawei.superwallpaper.engine.util.FileUtil;
import com.huawei.superwallpaper.engine.util.LogUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class Scene {
    private static final String KEY_CHILDREN = "mChildren";
    private static final String KEY_END_IMAGE_PATH = "mEndImagePath";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_NAME = "mName";
    private static final String KEY_PATH = "mPath";
    private static final String KEY_START_IMAGE_PATH = "mStartImagePath";
    private static final String KEY_TYPE = "type";
    private static final String KEY_WIDTH = "width";
    private static final String TAG = Scene.class.getSimpleName();
    private static final String TYPE_CLEAR_COLOR = "ClearColor";
    private static final String TYPE_IMAGE = "Image";
    private static final String TYPE_IMAGE_COLOR = "ImageColor";
    private static final String TYPE_IMAGE_LIST = "ImageList";
    private static final String TYPE_NODE = "Node";
    private static final String TYPE_NODE_COLOR = "NodeColor";
    private static final String TYPE_ROOT = "RootNode";
    private static final String TYPE_VIDEO_IMAGE_NODE = "VideoImageNode";
    private static final String TYPE_VIDEO_LOW_MEMORY_NODE = "VideoLowMemoryNode";
    private AnimatorController mAnimatorController;
    private Vector3 mCameraPosition;
    private double mCameraSceneHeight;
    private double mCameraSceneWidth;
    private Vector3 mCameraTarget;
    private double mCameraZoom;
    private final Context mContext;
    protected RootNode mRootNode;
    private float mTimeRatio = 1.0f;

    public Scene(Context context) {
        this.mContext = context;
    }

    public void setTimeRatio(float f) {
        this.mTimeRatio = f;
    }

    public float getTimeRatio() {
        return this.mTimeRatio;
    }

    public void setCameraInfo(String str) {
        String stringFromAssets = FileUtil.readStringFromAssets(this.mContext, str);
        if (TextUtils.isEmpty(stringFromAssets)) {
            LogUtil.i(TAG, "setSceneElement json is null", new Object[0]);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(stringFromAssets);
            this.mCameraSceneWidth = jSONObject.getDouble("cameraSceneWidth");
            this.mCameraSceneHeight = jSONObject.getDouble("cameraSceneHeight");
            this.mCameraZoom = jSONObject.getDouble("cameraZoom");
            JSONArray jSONArray = jSONObject.getJSONArray("cameraPosition");
            this.mCameraPosition = new Vector3(jSONArray.getDouble(0), jSONArray.getDouble(1), jSONArray.getDouble(2));
            JSONArray jSONArray2 = jSONObject.getJSONArray("cameraTarget");
            this.mCameraTarget = new Vector3(jSONArray2.getDouble(0), jSONArray2.getDouble(1), jSONArray2.getDouble(2));
            LogUtil.i(TAG, "width : %f, height : %f, zoom : %f, position : %s, target : %s", Double.valueOf(this.mCameraSceneWidth), Double.valueOf(this.mCameraSceneHeight), Double.valueOf(this.mCameraZoom), this.mCameraPosition, this.mCameraTarget);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setSceneElement(String str) {
        String stringFromAssets = FileUtil.readStringFromAssets(this.mContext, str);
        if (TextUtils.isEmpty(stringFromAssets)) {
            LogUtil.i(TAG, "setSceneElement json is null", new Object[0]);
        } else {
            initializeScene(stringFromAssets);
        }
    }

    public void initializeScene(String str) {
        LogUtil.e(TAG, "initializeScene " + str);
        try {
            Node nodes = parseNodes(new JSONObject(str), null);
            if (nodes instanceof RootNode) {
                this.mRootNode = (RootNode) nodes;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Node parseNodes(JSONObject jSONObject, Node node) {
        String strOptString = jSONObject.optString(KEY_TYPE);
        String strOptString2 = jSONObject.optString(KEY_PATH);
        String strOptString3 = jSONObject.optString(KEY_NAME);
        int iOptInt = jSONObject.optInt(KEY_WIDTH);
        int iOptInt2 = jSONObject.optInt(KEY_HEIGHT);
        String strOptString4 = jSONObject.optString(KEY_START_IMAGE_PATH);
        String strOptString5 = jSONObject.optString(KEY_END_IMAGE_PATH);
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(KEY_CHILDREN);
        Node nodeFromData = getNodeFromData(strOptString, strOptString3, strOptString2);
        LogUtil.e(TAG, "getNodeFromData : " + strOptString);
        if (nodeFromData instanceof VideoImageNode) {
            VideoImageNode videoImageNode = (VideoImageNode) nodeFromData;
            videoImageNode.setVideoSize(iOptInt, iOptInt2);
            videoImageNode.setStartImagePath(strOptString4);
            videoImageNode.setEndImagePath(strOptString5);
        }
        if (jSONArrayOptJSONArray != null) {
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
                if (jSONObjectOptJSONObject != null) {
                    parseNodes(jSONObjectOptJSONObject, nodeFromData);
                }
            }
        }
        if (node != null) {
            node.addChild(nodeFromData);
        }
        return nodeFromData;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0063  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Node getNodeFromData(String str, String str2, String str3) {
        switch (str) {
            case "Node":
                return new Node(str2);
            case "Image":
                return new Image(this.mContext, str3, str2);
            case "ImageColor":
                return new ImageColor(this.mContext, str2, (int) this.mCameraSceneWidth, (int) this.mCameraSceneHeight);
            case "RootNode":
                return new RootNode();
            case "NodeColor":
                return new NodeColor(this.mContext, str2, (int) this.mCameraSceneWidth, (int) this.mCameraSceneHeight);
            case "ClearColor":
                return new NodeClear(str2);
            case "VideoImageNode":
                return new VideoImageNode(this.mContext, str3, str2);
            case "VideoLowMemoryNode":
                return new VideoLowMemoryNode(this.mContext, str3, str2);
            case "ImageList":
                return new ImageList(this.mContext, str3, str2);
            default:
                return null;
        }
    }

    public void setAodData(String str) {
        setData(str, Constant.STATE_AOD);
    }

    public void setLockData(String str) {
        setData(str, Constant.STATE_LOCK);
    }

    public void setLauncherData(String str) {
        setData(str, Constant.STATE_LAUNCHER);
    }

    public void setData(String str, String str2) {
        String stringFromAssets = FileUtil.readStringFromAssets(this.mContext, str);
        if (TextUtils.isEmpty(stringFromAssets)) {
            LogUtil.i(TAG, "setData json is null", new Object[0]);
            return;
        }
        try {
            JSONArray jSONArray = new JSONArray(stringFromAssets);
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                byte b = -1;
                int iHashCode = str2.hashCode();
                if (iHashCode != -940906782) {
                    if (iHashCode != 359703243) {
                        if (iHashCode == 1365320683 && str2.equals(Constant.STATE_LAUNCHER)) {
                            b = 2;
                        }
                    } else if (str2.equals(Constant.STATE_AOD)) {
                        b = 0;
                    }
                } else if (str2.equals(Constant.STATE_LOCK)) {
                    b = 1;
                }
                if (b == 0) {
                    this.mAnimatorController.addAodData(jSONObject);
                } else if (b == 1) {
                    this.mAnimatorController.addLockData(jSONObject);
                } else if (b == 2) {
                    this.mAnimatorController.addLauncherData(jSONObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public AnimatorController getAnimatorController() {
        return this.mAnimatorController;
    }

    public void setAnimatorController(AnimatorController animatorController) {
        this.mAnimatorController = animatorController;
    }

    public RootNode getRootNode() {
        return this.mRootNode;
    }

    public double getCameraSceneWidth() {
        return this.mCameraSceneWidth;
    }

    public double getCameraSceneHeight() {
        return this.mCameraSceneHeight;
    }

    public double getCameraZoom() {
        return this.mCameraZoom;
    }

    public Vector3 getCameraPosition() {
        return this.mCameraPosition;
    }

    public Vector3 getCameraTarget() {
        return this.mCameraTarget;
    }

    public AnimatorSet setAnimatorSet(String str, PropertySet propertySet) {
        String stringFromAssets = FileUtil.readStringFromAssets(this.mContext, str);
        if (TextUtils.isEmpty(stringFromAssets)) {
            LogUtil.i(TAG, "setSceneElement json is null", new Object[0]);
            return null;
        }
        return parseAnimatorSet(stringFromAssets, propertySet);
    }

    public AnimatorSet parseAnimatorSet(String str, PropertySet propertySet) {
        JSONArray jSONArray;
        Scene scene = this;
        try {
            JSONObject jSONObject = new JSONObject(str);
            AnimatorSet animatorSet = new AnimatorSet();
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("mAnimators");
            if (jSONArrayOptJSONArray != null) {
                int i = 0;
                while (i < jSONArrayOptJSONArray.length()) {
                    JSONObject jSONObject2 = jSONArrayOptJSONArray.getJSONObject(i);
                    long jOptLong = (long) (jSONObject2.optLong("mDelay") * scene.mTimeRatio);
                    long jOptLong2 = (long) (jSONObject2.optLong("mDuration") * scene.mTimeRatio);
                    String strOptString = jSONObject2.optString("mInterpolator");
                    Animator animator = new Animator(jOptLong, jOptLong2, strOptString, InterpolatorHelper.genInterpolator(strOptString));
                    JSONArray jSONArrayOptJSONArray2 = jSONObject2.optJSONArray("mProperties");
                    if (jSONArrayOptJSONArray2 != null) {
                        int i2 = 0;
                        while (i2 < jSONArrayOptJSONArray2.length()) {
                            JSONObject jSONObject3 = jSONArrayOptJSONArray2.getJSONObject(i2);
                            Property property = propertySet.get(jSONObject3.optString("mFlag"), jSONObject3.optString("mProperty"));
                            if (property == null) {
                                jSONArray = jSONArrayOptJSONArray;
                            } else {
                                JSONArray jSONArrayOptJSONArray3 = jSONObject3.optJSONArray("mKeyframeSets");
                                if (jSONArrayOptJSONArray3 != null) {
                                    int i3 = 0;
                                    while (i3 < jSONArrayOptJSONArray3.length()) {
                                        JSONObject jSONObject4 = jSONArrayOptJSONArray3.getJSONObject(i3);
                                        KeyframeSet keyframeSet = new KeyframeSet(jSONObject4.optString("mStartState"), jSONObject4.optString("mEndState"));
                                        JSONArray jSONArrayOptJSONArray4 = jSONObject4.optJSONArray("mKeyframes");
                                        if (jSONArrayOptJSONArray4 != null) {
                                            int i4 = 0;
                                            while (i4 < jSONArrayOptJSONArray4.length()) {
                                                JSONObject jSONObject5 = jSONArrayOptJSONArray4.getJSONObject(i4);
                                                keyframeSet.addKeyframes(new Keyframe((float) jSONObject5.optDouble("mFraction"), (float) jSONObject5.optDouble("mValue"), InterpolatorHelper.genInterpolator(jSONObject5.optString("mInterpolator"))));
                                                i4++;
                                                jSONArrayOptJSONArray = jSONArrayOptJSONArray;
                                                jSONArrayOptJSONArray4 = jSONArrayOptJSONArray4;
                                            }
                                        }
                                        property.addKeyframeSet(keyframeSet);
                                        i3++;
                                        jSONArrayOptJSONArray = jSONArrayOptJSONArray;
                                    }
                                }
                                jSONArray = jSONArrayOptJSONArray;
                                animator.addProperty(property);
                            }
                            i2++;
                            jSONArrayOptJSONArray = jSONArray;
                        }
                    }
                    JSONArray jSONArray2 = jSONArrayOptJSONArray;
                    animatorSet.addAnimator(animator);
                    i++;
                    scene = this;
                    jSONArrayOptJSONArray = jSONArray2;
                }
            }
            return animatorSet;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
