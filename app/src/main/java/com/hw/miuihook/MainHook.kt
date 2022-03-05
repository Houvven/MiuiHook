package com.hw.miuihook

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
                XposedHelpers.findAndHookMethod("com.hw.miuihook.Encapsulation", lpparam.classLoader, "isActivated",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam?) {
                            param?.result = true
                        }
                    })
            }

            "com.android.systemui" -> {

            }

            "com.miui.securitycenter" -> {

            }

            "com.android.updater" -> {

            }

            "com.miui.powerkeeper" -> {

            }

            "" -> {

            }
        }
    }

}