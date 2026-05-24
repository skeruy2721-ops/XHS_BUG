package com.example.bug;

import android.util.Log;
import org.luckypray.dexkit.DexKitBridge;
import org.luckypray.dexkit.query.FindClass;
import org.luckypray.dexkit.query.FindMethod;
import org.luckypray.dexkit.query.enums.StringMatchType;
import org.luckypray.dexkit.query.matchers.AnnotationElementMatcher;
import org.luckypray.dexkit.query.matchers.AnnotationMatcher;
import org.luckypray.dexkit.query.matchers.ClassMatcher;
import org.luckypray.dexkit.query.matchers.MethodMatcher;
import org.luckypray.dexkit.result.ClassData;
import org.luckypray.dexkit.result.MethodData;
import java.lang.reflect.Method;
import java.util.List;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/*
* 原理：
* 用DexKit 匹配 kotlin.Deprecated 注解和注解里的message文本，找到对应类
* 然后在用callers筛出这个类里面被HomeNavigationImpl.enableShareToDianDianApp调用的方法
*/
public class RemoveDiandian {
    public static final String TAG = "XHS_BUG";
    public static void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        DexKitBridge bridge = Dexkit.get();
        if (bridge == null) {
            return;
        }
        List<ClassData> classes = bridge.findClass(
                FindClass.create().matcher(
                        ClassMatcher.create().addAnnotation(
                                AnnotationMatcher.create()
                                        // 匹配 kotlin.Deprecated 注解
                                        .type("kotlin.Deprecated", StringMatchType.Equals, false)
                                        .addElement(
                                                AnnotationElementMatcher.create()
                                                        // 匹配注解里的 message 字段
                                                        .name("message", StringMatchType.Equals, false)
                                                        // 指定 message 的文本
                                                        .stringValue("请使用 BuildConfigsUtilV2 工具类，进行BuildConfig信息获取", StringMatchType.Equals, false)
                                        )
                        )
                )
        );
        for (ClassData classData : classes) {
            String className = classData.getName();
            if (className == null || className.isEmpty()) {
                continue;
            }
            List<MethodData> methods = bridge.findMethod(
                    FindMethod.create().matcher(
                            MethodMatcher.create()
                                    .declaredClass(className)
                                    .paramCount(0)
                    )
            );
            for (MethodData methodData : methods) {
                boolean HomeDiandian = false;
                try {
                    List<MethodData> callers = methodData.getCallers();
                    for (MethodData caller : callers) {
                        String callerClassName = caller.getClassName();
                        String callerMethodName = caller.getName();
                        // 只保留被 HomeNavigationImpl.enableShareToDianDianApp 调用的方法
                        if ("com.xingin.xhs.homepage.spi.HomeNavigationImpl".equals(callerClassName)
                                && "enableShareToDianDianApp".equals(callerMethodName)) {
                            HomeDiandian = true;
                            break;
                        }
                    }
                } catch (Throwable ignored) {
                }
                if (!HomeDiandian) {
                    continue;
                }
                Method method;
                try {
                    method = methodData.getMethodInstance(loadPackageParam.classLoader);
                } catch (Throwable throwable) {
                    continue;
                }
                Class<?> returnType = method.getReturnType();
                if (returnType != boolean.class && returnType != Boolean.class) {
                    continue;
                }
                Log.i(TAG,methodData.getDescriptor());//拿完整方法签名
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        param.setResult(true);
                    }
                });
            }
        }
    }
}
