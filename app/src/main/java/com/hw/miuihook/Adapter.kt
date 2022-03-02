package com.hw.miuihook

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val itemList: List<Function>) : RecyclerView.Adapter<Adapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemSubtitle: TextView = view.findViewById(R.id.item_subtitle)

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val itemSwitch: Switch = view.findViewById(R.id.item_switch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemName.text = item.name
    }

    override fun getItemCount() = itemList.size

}