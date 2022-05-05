package com.vmm408.voznickandroid.ui.main.nav3

import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment

class Nav3Host : BaseFragment() {
    companion object {
        fun newInstance() = Nav3Host()
    }

    override val layoutRes = R.layout.host_nav_3
    override val TAG = "Nav3Host"
}