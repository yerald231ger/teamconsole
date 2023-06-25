package com.example.teamconsole.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamconsole.R
import com.example.teamconsole.infrastructure.models.SppDevice

class DeviceAdapter(private val devices: List<SppDevice>) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {
    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDevice : TextView

        init {
            tvDevice = itemView.findViewById(R.id.tvDevice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.itemView.apply {
            holder.tvDevice.text = devices[position].name
        }
    }
}