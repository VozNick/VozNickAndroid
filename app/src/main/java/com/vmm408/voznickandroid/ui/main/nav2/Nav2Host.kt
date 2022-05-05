package com.vmm408.voznickandroid.ui.main.nav2

import android.os.Bundle
import android.view.View
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseFragment

class Nav2Host : BaseFragment() {
    companion object {
        fun newInstance() = Nav2Host()
    }

    override val layoutRes = R.layout.host_nav_2
    override val TAG = "Nav2Host"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replace(R.id.nav2Host, Screens.getUserFieldsScreen())
    }
}