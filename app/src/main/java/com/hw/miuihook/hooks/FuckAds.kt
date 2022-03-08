package com.hw.miuihook.hooks

import android.content.Context
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class FuckAds {

    fun remoteController(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            val clazz = XposedHelpers.findClass(
                "com.duokan.phone.remotecontroller.operation.BaseOperationProvider", lpparam.classLoader
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

    fun mms(lpparam: XC_LoadPackage.LoadPackageParam) {
        val clazz = XposedHelpers.findClass("com.miui.smsextra.ui.BottomMenu", lpparam.classLoader)
        XposedHelpers.findAndHookMethod(clazz, "allowMenuMode", Context::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = false
                }
            })
    }

    fun theme(lpparam: XC_LoadPackage.LoadPackageParam) {
        val clazz = XposedHelpers.findClass("", lpparam.classLoader)
        XposedHelpers.findAndHookMethod(clazz, "", object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {

            }
        })
    }
}