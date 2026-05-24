# XposedHook Environment Design

## Goal

Turn this Android app into a classic Xposed module that compiles against API 82 and exposes one working sample hook entry.

## Scope

- Add the legacy Xposed API 82 as a compile-time-only dependency.
- Mark the APK as an Xposed module through manifest metadata.
- Register the hook entry class with `assets/xposed_init`.
- Implement a minimal `IXposedHookLoadPackage` example in `MainHook`.
- Add one small unit test around the package filter logic.

## Design

This project will use the legacy XposedBridge integration path because the user explicitly asked for API 82. The hook entry will implement `IXposedHookLoadPackage`, filter to the XiaoHongShu package, and then attach one simple `Application.attach(Context)` hook for verification logging.

The sample will stay intentionally small. It is only meant to prove that the module is wired correctly and to provide a clean place for future app-specific hooks.

## Assumptions

- The intended target package is `com.xingin.xhs`.
- The module should be installable as a normal APK with no launcher activity.
- Legacy Xposed metadata is preferred over the newer LSPosed metadata format for this task.

## Sources

- https://github.com/rovo89/XposedBridge/wiki/development-tutorial
- https://github.com/rovo89/XposedBridge/wiki/using-the-xposed-framework-api
