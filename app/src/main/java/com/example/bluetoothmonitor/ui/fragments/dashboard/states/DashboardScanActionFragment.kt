package com.example.bluetoothmonitor.ui.fragments.dashboard.states

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.bluetoothmonitor.databinding.FragmentScanActionBinding
import com.example.bluetoothmonitor.ui.fragments.dashboard.DashboardViewModel

class DashboardScanActionFragment : DashboardNestedFragment() {
    private val binding by lazy { FragmentScanActionBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<DashboardViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dashboardScanActionButton.setOnClickListener { viewModel.startScan() }
    }
}