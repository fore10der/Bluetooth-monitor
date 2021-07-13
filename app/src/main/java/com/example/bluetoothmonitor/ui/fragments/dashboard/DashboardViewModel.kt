package com.example.bluetoothmonitor.ui.fragments.dashboard

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow<DashboardState>(DashboardState.Initial)
    val state: StateFlow<DashboardState> = _state
    var hasResults = false

    val app = getApplication<Application>()

    fun startScan() = viewModelScope.launch {
        _state.value = DashboardState.Loading
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            Toast.makeText(
                app.applicationContext,
                "No bluetooth adapter available",
                Toast.LENGTH_SHORT
            )
                .show()
            _state.value = DashboardState.Initial
        } else if (!adapter.isEnabled) {
            Toast.makeText(
                app.applicationContext,
                "Enable bluetooth",
                Toast.LENGTH_SHORT
            )
                .show()
            _state.value = DashboardState.Initial
        } else {
            adapter.startDiscovery()

            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            app.applicationContext.registerReceiver(deviceReceiver, filter)
        }
    }

    private val deviceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    when (state.value) {
                        is DashboardState.Loading -> {
                            _state.value =
                                DashboardState.DevicesScanHasResults(mutableSetOf(device))
                            Log.d("devices", (state.value as DashboardState.DevicesScanHasResults).devices.toString())
                        }
                        is DashboardState.DevicesScanHasResults -> {
                            Log.d("device", device.toString())
                            Log.d("devices", "loaded")
                            val devices = ((state.value as DashboardState.DevicesScanHasResults).devices)
                            devices.add(device)
                            _state.value = DashboardState.GotDevice
                            _state.value = DashboardState.DevicesScanHasResults(devices)
                            Log.d("devices", (state.value as DashboardState.DevicesScanHasResults).devices.toString())
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        app.applicationContext.unregisterReceiver(deviceReceiver)
    }

    sealed class DashboardState {
        data class DevicesScanHasResults(val devices: MutableSet<BluetoothDevice>) :
            DashboardState()
        object GotDevice: DashboardState()
        object Initial : DashboardState()
        object Loading : DashboardState()
    }
}