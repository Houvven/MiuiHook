package com.hw.miuihook

import com.hw.miuihook.hooks.OtherHook
import com.hw.miuihook.hooks.SecurityCenterHook
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {

    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam?) {

    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        when (lpparam?.packageName) {
            "com.hw.miuihook" -> {
                XposedHelpers.findAndHookMethod("com.hw.miuihook.Encapsulation",
                    lpparam.classLoader,
                    "isActivated",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam?) {
                            param?.result = true
                        }
                    })
            }

            "com.android.systemui" -> {

            }

            "com.miui.securitycenter" -> {
                if (Encapsulation().getBoolean("分数锁定100")) {
                    SecurityCenterHook().setExaminationScore100(lpparam)
                }

                if (Encapsulation().getBoolean("去除自动连招黑名单")) {
                    SecurityCenterHook().removeMacroBlacklist(lpparam)
                }
            }

            "com.android.updater" -> {

            }

            "com.miui.powerkeeper" -> {
                if (Encapsulation().getBoolean("强制使用峰值刷新率")) {
                    OtherHook().forceMaxFps(lpparam)
                }

                if (Encapsulation().getBoolean("去除系统应用安装限制")) {
                    OtherHook().removeInstallAppRestriction(lpparam)
                }
            }

            "" -> {

            }
        }
    }

}