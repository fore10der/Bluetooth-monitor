package com.example.bluetoothmonitor.ui.fragments.dashboard

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow<DashboardState>(DashboardState.Initial)
    val state: StateFlow<DashboardState> = _state
    val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)

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
        } else if (!adapter.startDiscovery()) {
            Toast.makeText(
                app.applicationContext,
                "Can't discovery",
                Toast.LENGTH_SHORT
            )
            _state.value = DashboardState.Initial
        } else {
            Toast.makeText(
                app.applicationContext,
                "Discovery started",
                Toast.LENGTH_SHORT
            )
        }
    }

    val deviceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("device", "got")
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    when (state.value) {
                        is DashboardState.Loading -> {
                            _state.value =
                                DashboardState.DevicesScanHasResults(mutableSetOf(device))
                        }
                        is DashboardState.DevicesScanHasResults -> {
                            val copySet =
                                (state.value as DashboardState.DevicesScanHasResults).devices.toMutableSet()
                            _state.value = DashboardState.DevicesScanHasResults(copySet)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    override fun onCleared() {
        app.unregisterReceiver(deviceReceiver)
        super.onCleared()
    }

    sealed class DashboardState {
        data class DevicesScanHasResults(val devices: MutableSet<BluetoothDevice>) :
            DashboardState()

        object Initial : DashboardState()
        object Loading : DashboardState()
    }
}