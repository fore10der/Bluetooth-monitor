package com.example.bluetoothmonitor.ui.fragments.dashboard.states

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bluetoothmonitor.R
import com.example.bluetoothmonitor.databinding.FragmentScanActionBinding

class DashboardScanActionFragment : DashboardNestedFragment() {
    private val binding by lazy { FragmentScanActionBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dashboardScanActionButton.setOnClickListener { findNavController().navigate(R.id.action_global_dashboardScanLoadingFragment) }
    }
}