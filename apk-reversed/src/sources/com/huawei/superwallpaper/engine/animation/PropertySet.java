package com.huawei.superwallpaper.engine.animation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
public class PropertySet {
    private final Map<String, Property> mProperties = new HashMap();

    private void add(Property property) {
        this.mProperties.put(property.getFlag().concat(property.getProperty()), property);
    }

    public Property get(String str, String str2) {
        return this.mProperties.get(str.concat(str2));
    }

    public Property createOrGet(String str, String str2) {
        Property property = get(str, str2);
        if (property != null) {
            return property;
        }
        Property property2 = new Property(str, str2);
        add(property2);
        return property2;
    }

    public void clearAllPropertyKeyframes() {
        if (this.mProperties.isEmpty()) {
            return;
        }
        this.mProperties.forEach(new BiConsumer() { // from class: com.huawei.superwallpaper.engine.animation.-$$Lambda$PropertySet$uUTL_OcQe4sWJTPe4F3Q6NAPmK4
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((Property) obj2).clearKeyframes();
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0034  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setState(String str) {
        byte b;
        int iHashCode = str.hashCode();
        if (iHashCode != -940906782) {
            if (iHashCode != 359703243) {
                b = (iHashCode == 1365320683 && str.equals(Constant.STATE_LAUNCHER)) ? (byte) 2 : (byte) -1;
            } else if (str.equals(Constant.STATE_AOD)) {
                b = 0;
            }
        } else if (str.equals(Constant.STATE_LOCK)) {
            b = 1;
        }
        if (b == 0) {
            this.mProperties.forEach(new BiConsumer() { // from class: com.huawei.superwallpaper.engine.animation.-$$Lambda$PropertySet$NCTy-Gy_OQcBLTEOmuapnsKj4PQ
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    Property property = (Property) obj2;
                    property.setValue(property.getAodValue());
                }
            });
        } else if (b == 1) {
            this.mProperties.forEach(new BiConsumer() { // from class: com.huawei.superwallpaper.engine.animation.-$$Lambda$PropertySet$_oarGFoCb0gLEGIh1aUKh_3y2ss
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    Property property = (Property) obj2;
                    property.setValue(property.getLockValue());
                }
            });
        } else {
            if (b != 2) {
                return;
            }
            this.mProperties.forEach(new BiConsumer() { // from class: com.huawei.superwallpaper.engine.animation.-$$Lambda$PropertySet$WR2aI7KTHsa6_FG9zeD0-BzC_Uw
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    Property property = (Property) obj2;
                    property.setValue(property.getLauncherValue());
                }
            });
        }
    }
}
