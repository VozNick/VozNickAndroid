package com.vmm408.voznickandroid.ui.main.nav1

import android.os.Bundle
import android.view.View
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseFragment

class Nav1Host : BaseFragment() {
    override val layoutRes = R.layout.host_nav_1
    override val TAG = "Nav1Host"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replace(R.id.nav1Host, Screens.Nav1.getHomeScreen())
    }
}