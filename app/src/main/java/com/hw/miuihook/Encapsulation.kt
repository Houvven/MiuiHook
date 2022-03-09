package com.hw.miuihook

import android.graphics.Color
import android.view.View
import android.view.Window
import android.view.WindowManager
import de.robv.android.xposed.XSharedPreferences
import java.lang.invoke.CallSite

class Encapsulation {

    val hasSecondaryMenuItem: List<String> = listOf("系统界面", "手机管家", "系统更新", "七七八八", "凑四个字", "去广告")
    val mainItem: List<String> = listOf("系统界面", "手机管家", "系统更新", "七七八八", "凑四个字")
    val systemUIItem: List<String> = listOf("时钟显秒", "去除通知图标限制", "实时网速刷新率")
    val securityCenterItem: List<String> = listOf("分数锁定100", "去除自动连招黑名单")
    val updateItem: List<String> = listOf("去除OTA验证", "开发版logo改稳定版")
    val otherItem: List<String> = listOf("去广告", "强制使用峰值刷新率", "去除系统应用安装限制")
    val fuckAdsItem: List<String> = listOf("万能遥控", "主题壁纸")

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