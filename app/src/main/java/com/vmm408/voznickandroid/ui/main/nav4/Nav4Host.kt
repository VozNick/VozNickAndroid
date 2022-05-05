package com.vmm408.voznickandroid.ui.main.nav4

import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment

class Nav4Host : BaseFragment() {
    companion object {
        fun newInstance() = Nav4Host()
    }

    override val layoutRes = R.layout.host_nav_4
    override val TAG = "Nav4Host"
}