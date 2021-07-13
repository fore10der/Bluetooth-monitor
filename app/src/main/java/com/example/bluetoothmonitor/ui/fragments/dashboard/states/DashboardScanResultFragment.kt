package com.example.bluetoothmonitor.ui.fragments.dashboard.states

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bluetoothmonitor.R
import com.example.bluetoothmonitor.databinding.FragmentDashboardScanResultBinding
import com.example.bluetoothmonitor.databinding.FragmentScanActionBinding
import com.example.bluetoothmonitor.ui.fragments.dashboard.DashboardViewModel
import com.example.bluetoothmonitor.ui.fragments.dashboard.adapters.BluetoothDeviceAdapter
import com.example.bluetoothmonitor.utils.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class DashboardScanResultFragment : DashboardNestedFragment() {

    private val binding by lazy { FragmentDashboardScanResultBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.onEach {
            mapViewModelState(it)
        }.launchWhenStarted(lifecycleScope)
    }

    private fun mapViewModelState(state: DashboardViewModel.DashboardState) {
        when (state) {
            is DashboardViewModel.DashboardState.DevicesScanHasResults -> {
                if (binding.devicesRecycler.adapter != null) {
                    binding.devicesRecycler.adapter!!.notifyDataSetChanged()
                } else {
                    Log.d("status", "init")
                    val adapter = BluetoothDeviceAdapter(state.devices)
                    val layoutManager = LinearLayoutManager(context)
                    binding.devicesRecycler.layoutManager = layoutManager
                    binding.devicesRecycler.adapter = adapter
                }
            }
            else -> Unit
        }
    }
}