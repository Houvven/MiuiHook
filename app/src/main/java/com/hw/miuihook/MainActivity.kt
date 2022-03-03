package com.hw.miuihook

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val itemList = ArrayList<Function>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initItems() // 初始化item
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = Adapter(itemList)
        recyclerView.adapter = adapter
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            Encapsulation().hideStatusBar(this.window, true)
        }
    }

    // item
    @SuppressLint("CommitPrefEdits")
    private fun initItems() {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("temporary", MODE_PRIVATE)
        val items = when (sharedPreferences.getString("checked_item", null)) {
            "系统界面" -> Encapsulation().mainItem
            else -> Encapsulation().mainItem
        }
        // 清除temporary
        sharedPreferences.edit().clear()
        sharedPreferences.edit().apply()

        for (name: String in items) {
            itemList.add(Function(name))
        }
    }

}