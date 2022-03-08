package com.hw.miuihook.hooks

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class UpdaterHook constructor(lpparam: XC_LoadPackage.LoadPackageParam) {


    fun mian(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            var letter = 'a'
            for (i in 1..25) {
                val clazz = XposedHelpers.findClassIfExists(
                    "com.android.updater.common.utils.$letter", lpparam.classLoader
                ) ?: continue
                if (clazz.declaredFields.size >= 9 && clazz.declaredMethods.size > 60) {
                    fuckOtaValidate(clazz)
                    versionImg(clazz)
                    return
                }
                letter++
            }
            XposedBridge.log("tag: com.hw.miuihook, fuck miui ota validate can't find clazz")
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    private fun fuckOtaValidate(clazz: Class<*>) {
        try {
            XposedHelpers.findAndHookMethod(clazz, "T", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = false
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    private fun versionImg(clazz: Class<*>) {
        try {
            XposedHelpers.findAndHookMethod(clazz, "n", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = "13"
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }
}