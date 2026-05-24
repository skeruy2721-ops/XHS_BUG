# XposedHook Environment Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Configure this Android project as a legacy Xposed module using API 82 and add one minimal working hook example.

**Architecture:** Keep the project as a single `app` module. Wire Xposed through manifest metadata plus `assets/xposed_init`, and keep the example logic in one focused `MainHook` class with a tiny unit-testable package filter.

**Tech Stack:** Android Gradle Plugin, Java 11, XposedBridge API 82, JUnit 4

---

### Task 1: Wire Xposed module metadata

**Files:**
- Modify: `app/build.gradle.kts`
- Modify: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/assets/xposed_init`
- Modify: `app/src/main/res/values/strings.xml`

- [ ] Add `compileOnly("de.robv.android.xposed:api:82")` so Xposed classes are available at compile time but not packaged.
- [ ] Add `xposedmodule`, `xposeddescription`, and `xposedminversion` manifest metadata.
- [ ] Register `com.example.bug.MainHook` in `assets/xposed_init`.
- [ ] Add string resources used by the manifest metadata.

### Task 2: Add the sample hook entry

**Files:**
- Modify: `app/src/main/java/com/example/bug/MainHook.java`
- Test: `app/src/test/java/com/example/bug/MainHookTest.java`

- [ ] Write a failing unit test for the package filter helper.
- [ ] Implement `IXposedHookLoadPackage` in `MainHook`.
- [ ] Restrict the sample hook to `com.xingin.xhs`.
- [ ] Hook `Application.attach(Context)` and log success through `android.util.Log`.
- [ ] Re-run the unit test and confirm it passes.

### Task 3: Verify the project

**Files:**
- No source changes required

- [ ] Run `.\gradlew.bat testDebugUnitTest`.
- [ ] Run `.\gradlew.bat assembleDebug`.
- [ ] Review the final diff and report assumptions or follow-up items.
