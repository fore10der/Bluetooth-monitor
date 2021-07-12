package com.example.bluetoothmonitor.ui.fragments.dashboard.states

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.example.bluetoothmonitor.R
import com.example.bluetoothmonitor.databinding.FragmentScanActionBinding
import com.example.bluetoothmonitor.ui.fragments.dashboard.DashboardViewModel


class DashboardScanActionFragment : DashboardNestedFragment() {
    private val binding by lazy { FragmentScanActionBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<DashboardViewModel>()
    private val gpsDialog by lazy { AlertDialog.Builder(ContextThemeWrapper(requireActivity(), R.style.AlertDialogCustom))
        .setTitle(R.string.dashboard_dialog_location_required_title)
        .setMessage(R.string.dashboard_dialog_location_required_description)
        .setIcon(R.drawable.ic_baseline_warning_24)
        .setPositiveButton(R.string.dashboard_dialog_location_required_positive) { dialog, _ ->
            val intent = Intent(ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            dialog.cancel()
        }.setNegativeButton(
            R.string.dashboard_dialog_location_required_negative
        ) { dialog, _ ->
            dialog.cancel()
        } }

    private fun askPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_PRIVILEGED
            ), 0
        )
    }

    private fun scanDevices() {
        if (Build.VERSION.SDK_INT >= 29) {
            val locationManager =
                context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                gpsDialog.show()
                return
            }
        }
        askPermission();
        viewModel.startScan()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dashboardScanActionButton.setOnClickListener { scanDevices() }
    }
}