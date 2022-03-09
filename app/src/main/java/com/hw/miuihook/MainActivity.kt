package com.hw.miuihook

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.text.TextPaint
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val itemList = ArrayList<Function>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null

        val textPaint: TextPaint = toolbar_title.paint
        textPaint.strokeWidth = 2.0f
        textPaint.style = Paint.Style.FILL_AND_STROKE

        if (!Encapsulation().isActivated()) {
            toolbar_title.text = "模块未激活"
        }

        initItems() // 初始化item
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = Adapter(itemList)
        recyclerView.adapter = adapter

        //recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            Encapsulation().hideStatusBar(this.window, true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_menu_item -> {
                Toast.makeText(this, "dujiad", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    // item
    @SuppressLint("CommitPrefEdits")
    private fun initItems() {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("temporary", MODE_PRIVATE)
        val items = when (sharedPreferences.getString("checked_item", null)) {
            "系统界面" -> Encapsulation().systemUIItem
            "手机管家" -> Encapsulation().securityCenterItem
            "系统更新" -> Encapsulation().updateItem
            "七七八八" -> Encapsulation().otherItem
            "去广告" -> Encapsulation().fuckAdsItem
            else -> Encapsulation().mainItem
        }
        // 清除temporary
        sharedPreferences.run {
            edit().clear()
            edit().apply()
        }

        for (name: String in items) {
            itemList.add(Function(name))
        }
    }

}