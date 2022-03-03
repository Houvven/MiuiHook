package com.hw.miuihook.hooks

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SecurityCenterHook {

    fun setExaminationScore100(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            val clazz: Class<*> = XposedHelpers.findClass("com.miui.securityscan.scanner.ScoreManager", lpparam?.classLoader)
            XposedHelpers.findAndHookMethod(clazz, "i", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = 100
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    fun removeMacroBlacklist(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            val clazz: Class<*> = XposedHelpers.findClass("com.miui.gamebooster.v.i0", lpparam?.classLoader)
            XposedHelpers.findAndHookMethod(clazz, "c", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = false
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }
}