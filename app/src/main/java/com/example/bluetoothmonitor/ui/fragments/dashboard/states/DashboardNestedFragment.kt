package com.example.bluetoothmonitor.ui.fragments.dashboard.states

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.bluetoothmonitor.ui.fragments.dashboard.DashboardFragment

sealed class DashboardNestedFragment @JvmOverloads constructor(@LayoutRes contentLayoutId: Int = 0): Fragment(contentLayoutId) {
    val parent: DashboardFragment
        get() = requireParentFragment().requireParentFragment() as DashboardFragment
}