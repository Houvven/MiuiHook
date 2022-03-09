package com.hw.miuihook.hooks

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class FuckAds {

    fun remoteController(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            val clazz = XposedHelpers.findClass(
                "com.duokan.phone.remotecontroller.operation.BaseOperationProvider", lpparam?.classLoader
            )
            XposedHelpers.findAndHookMethod(clazz, "fromNet", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    param?.result = null
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    fun themeManager(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            val clazz = XposedHelpers.findClass(
                "com.android.thememanager.basemodule.ad.model.AdInfoResponse", lpparam?.classLoader
            )
            val valueClass: Class<*> = XposedHelpers.findClass(
                "com.android.thememanager.basemodule.ad.model.AdInfo", lpparam?.classLoader
            )
            XposedHelpers.findAndHookMethod(clazz, "isAdValid", valueClass, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = false
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }
}