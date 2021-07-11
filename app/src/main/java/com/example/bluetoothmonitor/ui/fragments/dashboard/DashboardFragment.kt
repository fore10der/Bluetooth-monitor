package com.example.bluetoothmonitor.ui.fragments.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bluetoothmonitor.R
import com.example.bluetoothmonitor.databinding.FragmentDashboardBinding
import com.example.bluetoothmonitor.databinding.FragmentScanActionBinding

class DashboardFragment : Fragment() {

    private val binding by lazy { FragmentDashboardBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    val moveToDevice =
        { findNavController().navigate(R.id.action_dashboardFragment_to_deviceFragment) }
}