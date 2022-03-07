package com.hw.miuihook.hooks

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SecurityCenterHook {

    // 手机管家分数锁定100
    fun setExaminationScore100(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            val clazz = XposedHelpers.findClass("com.miui.securityscan.scanner.ScoreManager", lpparam?.classLoader)
            XposedHelpers.findAndHookMethod(clazz, "i", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = 100
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    // 去除自动连招黑名单
    fun removeMacroBlacklist(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            var letter = 'a'
            for (i in 0..25) {
                val clazz = XposedHelpers.findClassIfExists(
                        "com.miui.gamebooster.v.$letter" + "0", lpparam?.classLoader
                ) ?: continue
                if (clazz.declaredMethods.size in 6..12 && clazz.fields.isEmpty() && clazz.declaredFields.size >= 2) {
                    XposedHelpers.findAndHookMethod(clazz, "c", String::class.java,
                        object : XC_MethodHook() {
                            override fun beforeHookedMethod(param: MethodHookParam?) {
                                param?.result = false
                            }
                        })
                    return
                }
                letter++
            }
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    //
}