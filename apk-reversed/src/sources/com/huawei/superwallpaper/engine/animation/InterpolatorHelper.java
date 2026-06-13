package com.huawei.superwallpaper.engine.animation;

import android.text.TextUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import java.util.regex.PatternSyntaxException;

/* JADX INFO: loaded from: classes.dex */
public class InterpolatorHelper {
    private InterpolatorHelper() {
    }

    public static Interpolator genInterpolator(String str) {
        if (!TextUtils.isEmpty(str)) {
            switch (str) {
                case "AccelerateDecelerateInterpolator":
                    return new AccelerateDecelerateInterpolator();
                case "AccelerateInterpolator":
                    return new AccelerateInterpolator();
                case "AnticipateInterpolator":
                    return new AnticipateInterpolator();
                case "AnticipateOvershootInterpolator":
                    return new AnticipateOvershootInterpolator();
                case "BounceInterpolator":
                    return new BounceInterpolator();
                case "DecelerateInterpolator":
                    return new DecelerateInterpolator();
                case "FastOutLinearInInterpolator":
                    return new FastOutLinearInInterpolator();
                case "FastOutSlowInInterpolator":
                    return new FastOutSlowInInterpolator();
                case "LinearInterpolator":
                    return new LinearInterpolator();
                case "LinearOutSlowInInterpolator":
                    return new LinearOutSlowInInterpolator();
                case "OvershootInterpolator":
                    return new OvershootInterpolator();
                default:
                    try {
                        String[] strArrSplit = str.split(",");
                        if (strArrSplit.length >= 4) {
                            float[] fArr = new float[4];
                            for (int i = 0; i < 4; i++) {
                                if (TextUtils.isEmpty(strArrSplit[i])) {
                                    fArr[i] = 0.0f;
                                } else {
                                    fArr[i] = Float.parseFloat(strArrSplit[i]);
                                }
                            }
                            return new PathInterpolator(fArr[0], fArr[1], fArr[2], fArr[3]);
                        }
                    } catch (NumberFormatException | PatternSyntaxException e) {
                        e.printStackTrace();
                        break;
                    }
                    break;
            }
        }
        return new AccelerateDecelerateInterpolator();
    }
}
