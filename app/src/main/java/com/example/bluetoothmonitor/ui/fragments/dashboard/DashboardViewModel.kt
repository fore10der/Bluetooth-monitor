package com.example.bluetoothmonitor.ui.fragments.dashboard

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluetoothmonitor.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow<DashboardState>(DashboardState.Initial)
    val state: StateFlow<DashboardState> = _state
    val app = getApplication<Application>()

    private val gpsDialog by lazy { AlertDialog.Builder(ContextThemeWrapper(app.applicationContext, R.style.AlertDialogCustom))
        .setTitle(R.string.dashboard_dialog_location_required_title)
        .setMessage(R.string.dashboard_dialog_location_required_description)
        .setIcon(R.drawable.ic_baseline_warning_24)
        .setPositiveButton(R.string.dashboard_dialog_location_required_positive) { dialog, _ ->
            val intent = Intent(ACTION_LOCATION_SOURCE_SETTINGS);
            app.startActivity(intent);
            dialog.cancel()
        }.setNegativeButton(
            R.string.dashboard_dialog_location_required_negative
        ) { dialog, _ ->
            dialog.cancel()
        } }

    fun startScan() {
        if (Build.VERSION.SDK_INT >= 29) {
            val locationManager =
                app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                gpsDialog.show()
                return
            }
        }
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            Toast.makeText(
                app.applicationContext,
                "No bluetooth adapter available",
                Toast.LENGTH_SHORT
            )
                .show()
        } else if (!adapter.isEnabled) {
            Toast.makeText(
                app.applicationContext,
                "Enable bluetooth",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            _state.value = DashboardState.Started
        }
    }

    sealed class DashboardState {
        object Initial : DashboardState()
        object Started : DashboardState()
    }
}