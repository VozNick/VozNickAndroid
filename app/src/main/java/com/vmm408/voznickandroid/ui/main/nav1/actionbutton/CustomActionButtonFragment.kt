package com.vmm408.voznickandroid.ui.main.nav1.actionbutton

import android.os.Bundle
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_custom_action_button.*

class CustomActionButtonFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_custom_action_button
    override val TAG = "CustomActionButtonFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionButtonOne?.setText("Button")
    }

}
