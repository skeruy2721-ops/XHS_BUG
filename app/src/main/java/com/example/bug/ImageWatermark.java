package com.example.bug;

import android.util.Log;
import org.luckypray.dexkit.DexKitBridge;
import org.luckypray.dexkit.query.FindMethod;
import org.luckypray.dexkit.query.matchers.MethodMatcher;
import org.luckypray.dexkit.result.MethodData;
import org.luckypray.dexkit.result.MethodDataList;
import java.lang.reflect.Method;
import java.util.List;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ImageWatermark {
    public static final String TAG = "XHS_BUG";

    public static void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        DexKitBridge bridge = Dexkit.get();
        if (bridge == null) {
            return;
        }
        MethodDataList methods = bridge.findMethod(
                FindMethod.create().matcher(
                        MethodMatcher.create()
                                .usingStrings("SaveImagesHelper.saveImage")
                )
        );
        MethodData methodData = methods.firstOrNull();
        if (methodData == null) {
            return;
        }
        List<String> paramTypeNames = methodData.getParamTypeNames();
        if (paramTypeNames.size() <= 5 || !"boolean".equals(paramTypeNames.get(5))) {
            return;
        }
        try {
            Method method = methodData.getMethodInstance(loadPackageParam.classLoader);
            Log.i(TAG,"图片水印方法"+methodData.getDescriptor());
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    param.args[5] = false;
                }
            });
        } catch (Throwable ignored) {
        }
    }
}
