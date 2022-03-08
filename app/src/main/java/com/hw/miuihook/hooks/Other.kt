package com.hw.miuihook.hooks

import android.content.pm.ApplicationInfo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Other {

    fun forceMaxFps(lpparam: XC_LoadPackage.LoadPackageParam?) {
        var clazz = XposedHelpers.findClassIfExists(
            "com.miui.powerkeeper.statemachine.DisplayFrameSetting", lpparam?.classLoader
        )
        if (clazz != null) {
            XposedHelpers.findAndHookMethod(
                clazz, "setScreenEffect", String::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        param?.result = null
                    }
                })
            try {
                XposedHelpers.findAndHookMethod(
                    clazz, "setScreenEffect", Int::class.java, Int::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam?) {
                            param?.result = null
                        }
                    })
            } catch (e: Exception) {
                XposedBridge.log(e)
            }
        }
        try {
            clazz = XposedHelpers.findClass("com.miui.powerkeeper.statemachine.DisplayFrameSetting",
                lpparam?.classLoader)
            XposedHelpers.findAndHookMethod(clazz, "isFeatureOn", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = false
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    // Remove the restriction of "Cannot install system applications from official channels"
    fun removeInstallAppRestriction(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            var clazz: Class<*>?
            var letter = 'a'
            for (i in 0..25) {
                clazz = XposedHelpers.findClassIfExists("com.android.packageinstaller.$letter", lpparam?.classLoader)
                if (clazz == null) {
                    continue
                }
                if (clazz.fields.isEmpty() && clazz.declaredFields.size == 1 && clazz.declaredMethods.size >= 10) {
                    XposedHelpers.findAndHookMethod(clazz, "a", ApplicationInfo::class.java,
                        object : XC_MethodHook() {
                            override fun beforeHookedMethod(param: MethodHookParam?) {
                                param?.result = false
                            }
                        })
                    return
                }
                letter++
            }
            XposedBridge.log("tag: com.hw.miuihook, Remove the restriction of \"Cannot install system applications from official channels\" can't find class")
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }





}