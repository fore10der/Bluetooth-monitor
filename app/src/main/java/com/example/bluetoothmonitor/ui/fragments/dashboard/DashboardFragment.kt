package com.example.bluetoothmonitor.ui.fragments.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bluetoothmonitor.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private val binding by lazy { FragmentDashboardBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}