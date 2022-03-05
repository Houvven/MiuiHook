package com.hw.miuihook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val itemList: List<Function>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemSubtitle: TextView = view.findViewById(R.id.item_subtitle)
        val itemRightImage: ImageView = view.findViewById(R.id.item_right_image)

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val itemSwitch: Switch = view.findViewById(R.id.item_switch)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        val editor: SharedPreferences.Editor? =
            parent.context.getSharedPreferences("temporary", AppCompatActivity.MODE_PRIVATE).edit()
        editor!!.clear()
        editor.apply()
        // 注册点击事件
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            // 判断mod是否激活
            if (Encapsulation().isActivated()) {
                val position = viewHolder.adapterPosition
                // 判断点击的item是否具有二级目录
                if (Encapsulation().mainItem.indexOf(viewHolder.itemName.text.toString()) != -1) {

                    // add checked item
                    editor.putString("checked_item", (viewHolder.itemName.text).toString())
                    editor.apply()

                    // Start a new MainActivity
                    val intent = Intent(parent.context, MainActivity::class.java)
                    parent.context.startActivity(intent)

                }



            }
            // 未激活,注册Toast提示
            else {
                Toast.makeText(parent.context, "模块未激活,请先激活模块", Toast.LENGTH_SHORT).show()
            }
        }
        return ViewHolder(view)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val sharedPreferences: SharedPreferences =
//            holder.itemView.context.getSharedPreferences("temporary", AppCompatActivity.MODE_PRIVATE)
//        val checkedItem = sharedPreferences.getString("checked_item", null)

        val item = itemList[position]
        holder.itemName.text = item.name
//        holder.itemSubtitle.visibility = View.GONE

        for (itemName in Encapsulation().mainItem) {
            if (holder.itemName.text == itemName) {
                holder.itemSwitch.visibility = View.GONE
            }
        }



    }

    override fun getItemCount() = itemList.size

}