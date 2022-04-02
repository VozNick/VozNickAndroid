package com.vmm408.voznickandroid.ui.main.nav1

import android.os.Bundle
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseFragment

class Nav1Host : BaseFragment() {
    companion object {
        fun newInstance() = Nav1Host()
    }

    override val layoutRes = R.layout.host_nav_1
    override val TAG = "Nav1Host"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        replace(R.id.nav1Host, Screens.getHomeScreen())
    }
}