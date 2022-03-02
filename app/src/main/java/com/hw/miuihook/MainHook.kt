package com.hw.miuihook

import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        TODO("Not yet implemented")
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam?) {

    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        when (lpparam?.packageName) {
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