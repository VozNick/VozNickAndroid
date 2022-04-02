package com.vmm408.voznickandroid.ui.main.nav2

import android.os.Bundle
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment

class Nav2Host : BaseFragment() {
    companion object {
        fun newInstance() = Nav2Host()
    }

    override val layoutRes = R.layout.host_nav_2
    override val TAG = "Nav2Host"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}