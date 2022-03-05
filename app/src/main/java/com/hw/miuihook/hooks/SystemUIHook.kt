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
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*

class SystemUIHook {

    fun updateTime(lpparam: XC_LoadPackage.LoadPackageParam?) {
        var context: Context? = null
//            val clazz = try {
//                XposedHelpers.findClass(
//                    )
//            } catch (e: Exception) {
//                null
//            }
        val clazz = XposedHelpers.findClassIfExists("com.android.systemui.statusbar.views.MiuiClock", lpparam?.classLoader)
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
                    class T: TimerTask() {
                        override fun run() {
                            android.os.Handler(textView.context.mainLooper).post(runnable)
                        }
                    }
                    if (textView.resources.getResourceEntryName(textView.id) == "clock") {
                        Timer().scheduleAtFixedRate(T(), 1000 - System.currentTimeMillis() % 1000, 1000)
                    }
                }
            })

        // Set the clock format
        XposedHelpers.findAndHookMethod(clazz, "updateTime", object : XC_MethodHook() {
            @SuppressLint("SimpleDateFormat", "SetTextI18n")
            override fun afterHookedMethod(param: MethodHookParam?) {
                val textView = param?.thisObject as TextView
                if (textView.resources.getResourceEntryName(textView.id) == "clock") {
                    val t: String = Settings.System.getString(context!!.contentResolver, Settings.System.TIME_12_24)
                    if (t == "24") {
                        textView.text = SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().time)
                    }
                    else {
                        textView.text = textView.text.toString() + SimpleDateFormat(":ss").format(Calendar.getInstance().time)
                    }
                }
            }
        })
    }

    // Remove the Remove the notification icon restriction
    fun notificationIconRestriction(lpparam: XC_LoadPackage.LoadPackageParam?) {
        try {
            val clazz = XposedHelpers.findClass(
                "com.android.systemui.statusbar.phone.NotificationIconContainer", lpparam?.classLoader
            )
            XposedHelpers.findAndHookMethod(clazz, "miuiShowNotificationIcons", Boolean::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        val boolean = param!!.args[0] as Boolean
                        //val fields: ArrayList<Field> = ArrayList()
                        val fieldNames: List<String> = listOf("MAX_DOTS", "MAX_STATIC_ICONS", "MAX_VISIBLE_ICONS_ON_LOCK")
                        //for (fieldName: String in fieldNames) {
                        //    fields.add(XposedHelpers.findField(clazz, fieldName))
                        //}
                        if (boolean) {
                            for (fieldName: String in fieldNames) {
                                XposedHelpers.setStaticIntField(clazz, fieldName, 7)
                            }
                        }
                        else {
                            for (fieldName: String in fieldNames) {
                                XposedHelpers.setStaticIntField(clazz, fieldName, 0)
                            }
                        }
                        XposedHelpers.callMethod(param.thisObject, "updateState")
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }

    }

}