package com.example.bug;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    static {
        System.loadLibrary("dexkit");
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!"com.xingin.xhs".equals(loadPackageParam.packageName)) {
            return;
        }
        Dexkit.init(loadPackageParam.appInfo.sourceDir);
        VideoWatermark.hook(loadPackageParam);//去除视频水印
        RemoveDiandian.hook(loadPackageParam);//移除首页左上角点点，恢复原来菜单
        ImageWatermark.hook(loadPackageParam);//图片去水印
    }
}
