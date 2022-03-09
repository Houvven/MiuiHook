package com.hw.miuihook.hooks

import android.content.Intent
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class StatusBarIcon constructor(lpparam: XC_LoadPackage.LoadPackageParam?) {

    private var classLoader: ClassLoader? = null

    init {
        this.classLoader = lpparam!!.classLoader
        hideVolteIcon()
        ringerIcon()
    }

    private fun hideVolteIcon() {
        try {
            val clazz = XposedHelpers.findClass(
                "com.android.systemui.MiuiOperatorCustomizedPolicy",
                classLoader
            )
            XposedHelpers.findAndHookMethod(clazz, "hideVolteIcon", Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        param?.result = true
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    private fun ringerIcon() {
        try {
            val clazz = XposedHelpers.findClass(
                "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy",
                classLoader
            )
            volumeZen(clazz)
            alarmChanged(clazz)
            bluetooth(clazz)
            bluetoothFreeBattery(clazz)
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    private fun volumeZen(clazz: Class<*>) {
        try {
            XposedHelpers.findAndHookMethod(clazz, "updateVolumeZen", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = null
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    private fun alarmChanged(clazz: Class<*>) {
        try {
            XposedHelpers.findAndHookMethod(clazz, "onMiuiAlarmChanged", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = null
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    private fun bluetooth(clazz: Class<*>) {
        try {
            XposedHelpers.findAndHookMethod(clazz, "updateBluetooth", String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        param?.result = null
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    private fun bluetoothFreeBattery(clazz: Class<*>) {
        try {
            XposedHelpers.findAndHookMethod(clazz, "updateBluetoothHandsfreeBattery", Intent::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        param?.result = null
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }




}