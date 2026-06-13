package com.huawei.animation.physical;

import android.util.Log;
import android.view.Choreographer;
import androidx.core.view.inputmethod.EditorInfoCompat;
import com.huawei.animation.physical.DynamicAnimation;
import com.huawei.animation.physical.util.Pools;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class SpringChain implements DynamicAnimation.IChainValueListener, DynamicAnimation.OnAnimationStartListener {
    private static final String TAG = "SpringChain";
    private Pools.SimplePool<HWSpringAnimation> mAnimationObjPools;
    private Pools.SimplePool<ChainTransferCallback> mCallbackPools;
    private AbstractChainAdapter mChainAdapter;
    private List<HWSpringAnimation> mModelList = new ArrayList();
    private int mControlModelIndex = EditorInfoCompat.IME_FLAG_FORCE_ASCII;
    private AtomicBoolean isDirty = new AtomicBoolean(true);
    private boolean mDelayed = true;
    private long mDelay = 0;
    private float mControlStiffness = 228.0f;
    private float mControlDamping = 30.0f;
    private boolean mIsUsePixelFix = false;
    private float mValueThresHold = Float.MAX_VALUE;
    private List<ChainTransferCallback> callbackList = new ArrayList();
    private IParamTransfer<Float> mStiffnessTransfer = new ParamsTransferImpl();
    private IParamTransfer<Float> mDampingTransfer = new ParamsTransferImpl();

    public SpringChain removeObject(ChainListener chainListener) {
        return this;
    }

    SpringChain(int i) {
        this.mCallbackPools = new Pools.SimplePool<>(i * 2);
        this.mAnimationObjPools = new Pools.SimplePool<>(i);
        this.mChainAdapter = new AbstractChainAdapter(i, this) { // from class: com.huawei.animation.physical.SpringChain.1
            @Override // com.huawei.animation.physical.AbstractChainAdapter
            protected int getMaxVisibleCount() {
                return 0;
            }

            @Override // com.huawei.animation.physical.AbstractChainAdapter
            public boolean isObjVisible(int i2) {
                return true;
            }
        };
    }

    public SpringChain addObject(ChainListener chainListener) {
        Log.i(TAG, "addObject: listener=" + chainListener);
        return addObject(-1, chainListener);
    }

    public SpringChain addObject(int i, ChainListener chainListener) {
        HWSpringAnimation hWSpringAnimationAcquire = this.mAnimationObjPools.acquire();
        if (hWSpringAnimationAcquire == null) {
            hWSpringAnimationAcquire = createAnimationObj();
        } else {
            reUseAnimationObj(hWSpringAnimationAcquire);
        }
        hWSpringAnimationAcquire.addUpdateListener(chainListener).setChainValueListener(this);
        if (i < 0) {
            i = this.mModelList.size();
        }
        this.mModelList.add(i, hWSpringAnimationAcquire);
        reConfig(hWSpringAnimationAcquire, Math.abs(i - this.mControlModelIndex));
        return this;
    }

    public SpringChain removeObject(int i) {
        if (!isIndexValid(i)) {
            return this;
        }
        this.mAnimationObjPools.release(this.mModelList.remove(i));
        return this;
    }

    public void reParamsTransfer() {
        reConfig(this.mModelList.get(this.mControlModelIndex), 0);
        int i = this.mControlModelIndex;
        int size = this.mModelList.size();
        while (true) {
            i++;
            if (i >= size) {
                break;
            }
            reConfig(this.mModelList.get(i), i - this.mControlModelIndex);
        }
        int i2 = this.mControlModelIndex;
        while (true) {
            i2--;
            if (i2 < 0) {
                return;
            }
            reConfig(this.mModelList.get(i2), this.mControlModelIndex - i2);
        }
    }

    @Override // com.huawei.animation.physical.DynamicAnimation.OnAnimationStartListener
    public void onAnimationStart(DynamicAnimation dynamicAnimation, float f, float f2) {
        if (this.mModelList.size() > 0 && this.isDirty.compareAndSet(true, false)) {
            reParamsTransfer();
        }
    }

    @Override // com.huawei.animation.physical.DynamicAnimation.IChainValueListener
    public void onChainValue(DynamicAnimation dynamicAnimation, float f, float f2, boolean z) {
        int i;
        int i2;
        int iIndexOf = this.mModelList.indexOf((HWSpringAnimation) dynamicAnimation);
        int i3 = this.mControlModelIndex;
        if (iIndexOf == i3) {
            i2 = iIndexOf - 1;
            i = iIndexOf + 1;
        } else if (iIndexOf < i3) {
            i2 = iIndexOf - 1;
            i = -1;
        } else if (iIndexOf > i3) {
            i = iIndexOf + 1;
            i2 = -1;
        } else {
            i = -1;
            i2 = -1;
        }
        if (i > -1 && i < this.mModelList.size()) {
            if (z) {
                onTransfer(this.mModelList.get(i), f, f2, i);
            } else {
                this.mModelList.get(i).setMinValue(f);
            }
        }
        if (i2 <= -1 || i2 >= this.mModelList.size()) {
            return;
        }
        if (z) {
            onTransfer(this.mModelList.get(i2), f, f2, i2);
        } else {
            this.mModelList.get(i2).setMaxValue(f);
        }
    }

    public void cancel() {
        if (isIndexValid(getControlModelIndex())) {
            this.mModelList.get(this.mControlModelIndex).cancel();
        }
    }

    public void reset() {
        for (int i = 0; i < this.mModelList.size(); i++) {
            HWSpringAnimation hWSpringAnimation = this.mModelList.get(i);
            hWSpringAnimation.setMinValue(-3.4028235E38f);
            hWSpringAnimation.setMaxValue(Float.MAX_VALUE);
        }
        if (this.callbackList.size() > 0) {
            Log.i(TAG, "remain chain frame callback:" + this.callbackList.size());
            Iterator<ChainTransferCallback> it = this.callbackList.iterator();
            while (it.hasNext()) {
                Choreographer.getInstance().removeFrameCallback(it.next());
            }
        }
        this.callbackList.clear();
    }

    public void cancelAll() {
        for (int i = 0; i < this.mModelList.size(); i++) {
            HWSpringAnimation hWSpringAnimation = this.mModelList.get(i);
            hWSpringAnimation.cancel();
            hWSpringAnimation.setMinValue(-3.4028235E38f);
            hWSpringAnimation.setMaxValue(Float.MAX_VALUE);
            hWSpringAnimation.setStartVelocity(0.0f);
        }
        if (this.callbackList.size() > 0) {
            Log.i(TAG, "remain chain frame callback:" + this.callbackList.size());
            Iterator<ChainTransferCallback> it = this.callbackList.iterator();
            while (it.hasNext()) {
                Choreographer.getInstance().removeFrameCallback(it.next());
            }
        }
        this.callbackList.clear();
    }

    private void onTransfer(HWSpringAnimation hWSpringAnimation, float f, float f2, int i) {
        if (!this.mDelayed) {
            onChainTransfer(hWSpringAnimation, f, f2, i);
            return;
        }
        ChainTransferCallback chainTransferCallbackAcquire = this.mCallbackPools.acquire();
        if (chainTransferCallbackAcquire == null) {
            chainTransferCallbackAcquire = new ChainTransferCallback();
        }
        if (this.mDelay <= 0) {
            Choreographer.getInstance().postFrameCallback(chainTransferCallbackAcquire.setObj(hWSpringAnimation).setValue(f).setVelocity(f2).setIndex(i));
        } else {
            Choreographer.getInstance().postFrameCallbackDelayed(chainTransferCallbackAcquire.setObj(hWSpringAnimation).setValue(f).setVelocity(f2).setIndex(i), this.mDelay);
        }
        this.callbackList.add(chainTransferCallbackAcquire);
    }

    class ChainTransferCallback implements Choreographer.FrameCallback {
        private int index;
        private HWSpringAnimation obj;
        private float value;
        private float velocity;

        public ChainTransferCallback setModelValue(float f) {
            return this;
        }

        public ChainTransferCallback() {
        }

        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long j) {
            SpringChain.this.onChainTransfer(this.obj, this.value, this.velocity, this.index);
            SpringChain.this.mCallbackPools.release(this);
            SpringChain.this.callbackList.remove(this);
        }

        public ChainTransferCallback setObj(HWSpringAnimation hWSpringAnimation) {
            this.obj = hWSpringAnimation;
            return this;
        }

        public ChainTransferCallback setValue(float f) {
            this.value = f;
            return this;
        }

        public ChainTransferCallback setVelocity(float f) {
            this.velocity = f;
            return this;
        }

        public int getIndex() {
            return this.index;
        }

        public ChainTransferCallback setIndex(int i) {
            this.index = i;
            return this;
        }
    }

    protected HWSpringAnimation createAnimationObj() {
        HWSpringAnimation hWSpringAnimation;
        if (this.mIsUsePixelFix) {
            hWSpringAnimation = new HwSpringPixelAnimation(new FloatValueHolder(0.0f), this.mControlStiffness, this.mControlDamping, 1.0f, 0.0f);
        } else {
            hWSpringAnimation = new HWSpringAnimation(new FloatValueHolder(0.0f), this.mControlStiffness, this.mControlDamping, 1.0f, 0.0f);
        }
        if (Float.compare(this.mValueThresHold, Float.MAX_VALUE) != 0) {
            hWSpringAnimation.setValueThreshold(this.mValueThresHold);
        }
        return hWSpringAnimation;
    }

    protected HWSpringAnimation reUseAnimationObj(HWSpringAnimation hWSpringAnimation) {
        return hWSpringAnimation.setObj(null, null, this.mControlStiffness, this.mControlDamping, 1.0f, 0.0f);
    }

    protected HWSpringAnimation resetAnimationObj(HWSpringAnimation hWSpringAnimation) {
        return hWSpringAnimation.reset();
    }

    public SpringChain setControlSpringIndex(int i) {
        setControlModelIndex(i);
        return this;
    }

    public List<HWSpringAnimation> getModelList() {
        return this.mModelList;
    }

    public int getControlModelIndex() {
        return this.mControlModelIndex;
    }

    protected IParamTransfer diffMember(IParamTransfer iParamTransfer, IParamTransfer iParamTransfer2) {
        if (iParamTransfer == iParamTransfer2) {
            return iParamTransfer;
        }
        if (iParamTransfer != null && iParamTransfer.equals(iParamTransfer2)) {
            return iParamTransfer;
        }
        this.isDirty.compareAndSet(false, true);
        return iParamTransfer2;
    }

    protected float diffMember(float f, float f2) {
        if (Float.compare(f, f2) == 0) {
            return f;
        }
        this.isDirty.compareAndSet(false, true);
        return f2;
    }

    public SpringChain setControlModelIndex(int i) {
        int i2;
        if (!isIndexValid(i) || (i2 = this.mControlModelIndex) == i) {
            return this;
        }
        if (i2 != Integer.MIN_VALUE) {
            this.mModelList.get(i2).removeStartListener(this);
        }
        this.mControlModelIndex = i;
        this.mModelList.get(i).addStartListener(this);
        this.isDirty.set(true);
        return this;
    }

    private boolean isIndexValid(int i) {
        return i >= 0 && i < this.mModelList.size();
    }

    public boolean isDelayed() {
        return this.mDelayed;
    }

    public SpringChain setDelayed(boolean z) {
        this.mDelayed = z;
        return this;
    }

    public long getDelay() {
        return this.mDelay;
    }

    public SpringChain setDelay(long j) {
        this.mDelay = j;
        return this;
    }

    public HWSpringAnimation getControlSpring() {
        int i;
        if (this.mModelList.size() != 0 && (i = this.mControlModelIndex) >= 0 && i < this.mModelList.size()) {
            return this.mModelList.get(this.mControlModelIndex);
        }
        return null;
    }

    protected void reConfig(HWSpringAnimation hWSpringAnimation, int i) {
        hWSpringAnimation.getSpringModel().setStiffness(this.mStiffnessTransfer.transfer(Float.valueOf(getControlStiffness()), i).floatValue()).setDamping(this.mDampingTransfer.transfer(Float.valueOf(getControlDamping()), i).floatValue());
    }

    protected void onChainTransfer(HWSpringAnimation hWSpringAnimation, float f, float f2, int i) {
        if (this.mChainAdapter.isObjVisible(i)) {
            hWSpringAnimation.endToPosition(f, f2);
        } else {
            if (this.mChainAdapter.isTouchDown()) {
                return;
            }
            hWSpringAnimation.setPropertyValue(f);
        }
    }

    public static SpringChain create(int i, float f, float f2) {
        return create(i).setControlStiffness(f).setControlDamping(f2);
    }

    public static SpringChain create(int i) {
        return new SpringChain(i);
    }

    public float getControlStiffness() {
        return this.mControlStiffness;
    }

    public SpringChain setControlStiffness(float f) {
        this.mControlStiffness = diffMember(this.mControlStiffness, f);
        return this;
    }

    public float getControlDamping() {
        return this.mControlDamping;
    }

    public SpringChain setControlDamping(float f) {
        this.mControlDamping = diffMember(this.mControlDamping, f);
        return this;
    }

    public IParamTransfer<Float> getStiffnessTransfer() {
        return this.mStiffnessTransfer;
    }

    public SpringChain setStiffnessTransfer(IParamTransfer<Float> iParamTransfer) {
        this.mStiffnessTransfer = diffMember(this.mStiffnessTransfer, iParamTransfer);
        return this;
    }

    public IParamTransfer<Float> getDampingTransfer() {
        return this.mDampingTransfer;
    }

    public SpringChain setDampingTransfer(IParamTransfer<Float> iParamTransfer) {
        this.mDampingTransfer = diffMember(this.mDampingTransfer, iParamTransfer);
        return this;
    }

    public List<HWSpringAnimation> getAllSpringAnimation() {
        return this.mModelList;
    }

    public boolean isUsePixelFix() {
        return this.mIsUsePixelFix;
    }

    public SpringChain setUsePixelFix(boolean z) {
        this.mIsUsePixelFix = z;
        return this;
    }

    public float getValueThresHold() {
        return this.mValueThresHold;
    }

    public SpringChain setValueThresHold(float f) {
        this.mValueThresHold = f;
        for (int i = 0; i < this.mModelList.size(); i++) {
            this.mModelList.get(i).setValueThreshold(f);
        }
        return this;
    }

    public AbstractChainAdapter getChainAdapter() {
        return this.mChainAdapter;
    }

    public SpringChain setChainAdapter(AbstractChainAdapter abstractChainAdapter) {
        this.mChainAdapter = abstractChainAdapter;
        return this;
    }
}
