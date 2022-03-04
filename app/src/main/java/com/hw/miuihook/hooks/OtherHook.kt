package com.hw.miuihook.hooks

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class OtherHook {

    fun forceMaxFps(lpparam: XC_LoadPackage.LoadPackageParam?) {
        var clazz: Class<*>? = XposedHelpers.findClassIfExists(
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

}