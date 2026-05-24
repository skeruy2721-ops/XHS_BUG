package com.example.bug;

import android.util.Log;
import org.luckypray.dexkit.DexKitBridge;
import org.luckypray.dexkit.query.FindMethod;
import org.luckypray.dexkit.query.matchers.MethodMatcher;
import org.luckypray.dexkit.result.FieldData;
import org.luckypray.dexkit.result.MethodData;
import java.lang.reflect.Method;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
/*
* 去除视频水印，disableWaterMark是抓包获取的
* */
public class VideoWatermark {
    public static final String TAG = "XHS_BUG";

    public static void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        DexKitBridge bridge = Dexkit.get();
        if (bridge == null) {
            return;
        }
        FieldData fieldData = bridge.getFieldData("Lcom/xingin/entities/MediaSaveConfig;->disableWaterMark:Z");//找到com.xingin.entities.MediaSaveConfig里面的disableWaterMark字段
        if (fieldData == null) {
            return;
        }
        MethodData methodData = fieldData.getReaders().findMethod(
                FindMethod.create().matcher(
                        MethodMatcher.create()
                                .paramTypes()//无参
                                .returnType("boolean")//返回布尔类型
                )
        ).firstOrNull();
        if (methodData == null) {
            return;
        }
        try {
            Method method = methodData.getMethodInstance(loadPackageParam.classLoader);
            Log.i(TAG,methodData.getDescriptor());//拿完整方法签名
            XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(true));
        } catch (Throwable throwable) {
            return;
        }
    }
}
