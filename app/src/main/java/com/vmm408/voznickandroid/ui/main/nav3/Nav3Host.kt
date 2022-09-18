package com.vmm408.voznickandroid.ui.main.nav3

import android.os.Bundle
import android.view.View
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseFragment

class Nav3Host : BaseFragment() {
    override val layoutRes = R.layout.host_nav_3
    override val TAG = "Nav3Host"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replace(R.id.nav3Host, Screens.Nav3.getCollapsingToolbarSampleOneScreen())
    }
}