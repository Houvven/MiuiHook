package com.hw.miuihook

import android.graphics.Color
import android.view.View
import android.view.Window
import android.view.WindowManager
import de.robv.android.xposed.XSharedPreferences

class Encapsulation {

    val mainItem: List<String> = listOf("系统界面", "手机管家", "系统更新", "七七八八", "凑四个字")

    fun isActivated(): Boolean {
        return false
    }

    fun getBoolean(key: String): Boolean {
        val xSharedPreferences = XSharedPreferences("com.hw.miuihook", "function")
        return xSharedPreferences.getBoolean(key, false)
    }

    fun hideStatusBar(window: Window, isLight: Boolean) {
        if (isLight) {
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }


}