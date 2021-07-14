package com.example.bluetoothmonitor.ui.fragments.dashboard.adapters

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.opengl.Visibility
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothmonitor.Constants
import com.example.bluetoothmonitor.R
import com.example.bluetoothmonitor.databinding.LayoutDeviceItemBinding
import java.io.IOException
import java.lang.reflect.InvocationTargetException

class BluetoothDeviceAdapter(
    private val devices: Set<BluetoothDevice>,
    private val sockets: MutableSet<BluetoothSocket>,
    private val context: Context
) : RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceHolder>() {

    class DeviceHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = LayoutDeviceItemBinding.bind(item)
        fun bind(device: BluetoothDevice, remoteDevices: Set<BluetoothDevice>) = with(binding) {
            if (device.name == null) {
                binding.label.text = device.address
            } else {
                binding.label.text = device.name
            }
            if (remoteDevices.contains(device)) {
                binding.parryButton.visibility = View.GONE
                binding.unparryButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_device_item, parent, false)
        return DeviceHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceHolder, position: Int) {
        val device = devices.elementAt(position)
        holder.bind(device, sockets.map { return@map it.remoteDevice }.toSet())
        if (holder.binding.parryButton.visibility == View.VISIBLE) {
            holder.binding.parryButton.setOnClickListener {
                try {
                    val bluetoothSocket = device.createRfcommSocketToServiceRecord(Constants.BOX_UUID)
                    bluetoothSocket.connect()
                    sockets.add(bluetoothSocket)
                    beginListenForData(bluetoothSocket, context)
                    notifyDataSetChanged()
                } catch (e: IOException) {
                    Toast.makeText(context, "Unable to connect", Toast.LENGTH_SHORT).show()
                }
            }
        }
//        if (holder.binding.unparryButton.visibility == View.VISIBLE) {
//            holder.binding.unparryButton.setOnClickListener {
//                try {
//                    val socket = sockets.find { it.remoteDevice == device }
//                    Log.d("socket", socket.toString())
//                    socket?.close()
//                    sockets.remove(socket)
//                    notifyDataSetChanged()
//                    Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
//                } catch (e: IOException) {
//                    Toast.makeText(context, "Unable to disconnect", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    private fun beginListenForData(socket: BluetoothSocket, context: Context) {
        val handler = Handler(Looper.getMainLooper())
        val stopWorker = false
        val workerThread = Thread {
            while (!Thread.currentThread().isInterrupted && !stopWorker) {
                try {
                    val bytesAvailable: Int = socket.inputStream.available()
                    if (bytesAvailable > 0) {
                        val packetBytes = ByteArray(bytesAvailable)
                        socket.inputStream.read(packetBytes)
                        val data = String(packetBytes)
                        Log.d("bytes",String(packetBytes))
                        handler.post { Toast.makeText(context,  socket.remoteDevice.address + " " + data, Toast.LENGTH_SHORT).show() }
                    }
                } catch (ex: IOException) {

                }
            }
        }
        workerThread.start()
    }

    override fun getItemCount(): Int {
        return devices.size
    }
}