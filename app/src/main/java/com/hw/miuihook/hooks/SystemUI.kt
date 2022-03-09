package com.hw.miuihook.hooks

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.AttributeSet
import android.widget.TextView
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SystemUI {

    fun updateTime(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            var context: Context? = null
            val clazz = XposedHelpers.findClassIfExists(
                "com.android.systemui.statusbar.views.MiuiClock",
                lpparam?.classLoader
            )
            if (clazz == null) {
                XposedBridge.log("tag:com.hw.miuihook, updateTime can't find clazz")
                return
            }
            // The clock refresh rate is set to per second
            XposedHelpers.findAndHookConstructor(clazz, Context::class.java, AttributeSet::class.java, Integer.TYPE,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        context = param!!.args[0] as Context
                        val textView = param.thisObject as TextView
                        val method: Method = textView.javaClass.getDeclaredMethod("updateTime")
                        val runnable = Runnable {
                            method.isAccessible = true
                            method.invoke(textView)
                        }

                        class T : TimerTask() {
                            override fun run() {
                                android.os.Handler(textView.context.mainLooper).post(runnable)
                            }
                        }
                        if (textView.resources.getResourceEntryName(textView.id) == "clock") {
                            Timer().scheduleAtFixedRate(
                                T(), 1000 - System.currentTimeMillis() % 1000, 1000
                            )
                        }
                    }
                })

            // Set the clock format
            XposedHelpers.findAndHookMethod(clazz, "updateTime", object : XC_MethodHook() {
                @SuppressLint("SimpleDateFormat", "SetTextI18n")
                override fun afterHookedMethod(param: MethodHookParam?) {
                    val textView = param?.thisObject as TextView
                    textView.text = SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().time)
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    // Remove the Remove the notification icon restriction
    fun notificationIconRestriction(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            val clazz = XposedHelpers.findClass(
                "com.android.systemui.statusbar.phone.NotificationIconContainer", lpparam?.classLoader
            )
            XposedHelpers.findAndHookMethod(clazz, "miuiShowNotificationIcons", Boolean::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        val boolean = param!!.args[0] as Boolean
                        val fieldNames: List<String> = listOf("MAX_DOTS", "MAX_STATIC_ICONS", "MAX_VISIBLE_ICONS_ON_LOCK")
                        val value: Int = if (boolean) 7 else 0
                        for (fieldName in fieldNames) {
                            val field = XposedHelpers.findField(clazz, fieldName)
                            field.isAccessible = true
                            field.setInt(param.thisObject, value)
                        }
                        XposedHelpers.callMethod(param.thisObject, "updateState")
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }

    }

    fun chargeAnimation(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            val clazz = XposedHelpers.findClass(
                "com.android.keyguard.charge.ChargeUtils",
                lpparam?.classLoader
            )
            XposedHelpers.findAndHookMethod(clazz, "supportWaveChargeAnimation", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = true
                }
            })


        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

    fun networkSpeed(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            val clazz =XposedHelpers.findClass(
                "com.android.systemui.statusbar.policy.NetworkSpeedController", lpparam?.classLoader
            )
            XposedHelpers.findAndHookMethod(clazz, "postUpdateNetworkSpeedDelay", Long::class.java, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        param!!.args[0] = 1000.toLong()
                }
            })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }

}