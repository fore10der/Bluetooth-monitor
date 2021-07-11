package com.example.bluetoothmonitor.ui.fragments.dashboard.states

import androidx.fragment.app.Fragment
import com.example.bluetoothmonitor.ui.fragments.dashboard.DashboardFragment

sealed class DashboardNestedFragment: Fragment() {
    val parent: DashboardFragment
        get() = requireParentFragment().requireParentFragment() as DashboardFragment
}