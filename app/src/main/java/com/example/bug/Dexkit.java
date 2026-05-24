package com.example.bug;

import org.luckypray.dexkit.DexKitBridge;

public class Dexkit {
    private static volatile DexKitBridge bridge;
    public static DexKitBridge init(final String apkPath) {
        DexKitBridge localBridge = bridge;
        if (localBridge != null) {
            return localBridge;
        }
        synchronized (Dexkit.class) {
            localBridge = bridge;
            if (localBridge == null) {
                localBridge = DexKitBridge.create(apkPath);
                bridge = localBridge;
            }
            return localBridge;
        }
    }
    public static DexKitBridge get() {
        return bridge;
    }
}
