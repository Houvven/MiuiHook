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
import java.util.logging.Handler

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

}