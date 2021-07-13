package com.example.bluetoothmonitor.ui.fragments.dashboard.adapters

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothmonitor.R
import com.example.bluetoothmonitor.databinding.LayoutDeviceItemBinding

class BluetoothDeviceAdapter(val devices: Set<BluetoothDevice>): RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceHolder>() {

    class DeviceHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = LayoutDeviceItemBinding.bind(item)
        fun bind(device: BluetoothDevice) = with(binding) {
            label.text = device.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_device_item, parent, false)
        return DeviceHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceHolder, position: Int) {
        holder.bind(devices.elementAt(position))
    }

    override fun getItemCount(): Int {
        return devices.size
    }
}