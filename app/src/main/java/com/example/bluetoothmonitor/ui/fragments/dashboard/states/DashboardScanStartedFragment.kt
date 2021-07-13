package com.example.bluetoothmonitor.ui.fragments.dashboard.states

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bluetoothmonitor.databinding.FragmentDashboardScanStartedBinding
import com.example.bluetoothmonitor.ui.fragments.dashboard.adapters.BluetoothDeviceAdapter

class DashboardScanStartedFragment : Fragment() {
    val devices = mutableSetOf<BluetoothDevice>()
    private val binding by lazy { FragmentDashboardScanStartedBinding.inflate(layoutInflater) }
    private val btAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private val deviceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    if (devices.size == 0) {
                        completeLoad()
                    }
                    devices.add(device)
                    binding.devicesRecycler.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BluetoothDeviceAdapter(devices)
        val layoutManager = LinearLayoutManager(context)
        binding.devicesRecycler.layoutManager = layoutManager
        binding.devicesRecycler.adapter = adapter

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        requireActivity().registerReceiver(deviceReceiver, filter)
        btAdapter.startDiscovery()
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(deviceReceiver)
    }

    private fun completeLoad() {
        binding.devicesLoadingLayout.visibility = View.GONE
        binding.devicesResultsLayout.visibility = View.VISIBLE
    }
}