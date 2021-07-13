package com.example.bluetoothmonitor.ui.fragments.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.bluetoothmonitor.R
import com.example.bluetoothmonitor.databinding.FragmentDashboardBinding
import com.example.bluetoothmonitor.utils.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class DashboardFragment : Fragment() {

    private val binding by lazy { FragmentDashboardBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<DashboardViewModel>()
    private val scanNavController by lazy { binding.dashboardStatesContainerView.findNavController() }

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
            is DashboardViewModel.DashboardState.Initial -> {
                binding.dashboardDescription.text = resources.getString(R.string.dashboard_start_scan_text)
            }
            is DashboardViewModel.DashboardState.Started -> {
                binding.dashboardDescription.text = resources.getString(R.string.dashboard_await_scan_text)
                scanNavController.navigate(R.id.action_dashboardScanActionFragment_to_dashboardScanStartedFragment2)
            }
        }
    }
}