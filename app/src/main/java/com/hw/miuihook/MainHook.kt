package com.hw.miuihook

import com.hw.miuihook.hooks.*
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

            // 系统界面
            "com.android.systemui" -> {
                if (Encapsulation().getBoolean("时钟显秒")) {
                    SystemUI().updateTime(lpparam)
                }

                if (Encapsulation().getBoolean("去除通知图标限制")) {
                    SystemUI().notificationIconRestriction(lpparam)
                }
            }

            // 手机管家
            "com.miui.securitycenter" -> {
                if (Encapsulation().getBoolean("分数锁定100")) {
                    SecurityCenter().setExaminationScore100(lpparam)
                }

                if (Encapsulation().getBoolean("去除自动连招黑名单")) {
                    SecurityCenter().removeMacroBlacklist(lpparam)
                    SecurityCenter().memc(lpparam)
                }
            }

            // 系统更新
            "com.android.updater" -> {
                Updater().init(lpparam)
            }

            // 电量与性能
            "com.miui.powerkeeper" -> {
                if (Encapsulation().getBoolean("强制使用峰值刷新率")) {
                    Other().forceMaxFps(lpparam)
                }
            }

            // 应用包管理组件
            "com.miui.packageinstaller" -> {
                if (Encapsulation().getBoolean("去除系统应用安装限制")) {
                    Other().removeInstallAppRestriction(lpparam)
                }
            }

            // 万能遥控
            "com.duokan.phone.remotecontroller" -> {
                if (Encapsulation().getBoolean("万能遥控")) {
                    FuckAds().remoteController(lpparam)
                }
            }


        }
    }

}