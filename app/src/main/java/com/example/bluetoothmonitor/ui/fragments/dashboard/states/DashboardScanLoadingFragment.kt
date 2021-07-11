package com.example.bluetoothmonitor.ui.fragments.dashboard.states

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bluetoothmonitor.R

class DashboardScanLoadingFragment : DashboardNestedFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_scan_loading, container, false)
    }
}