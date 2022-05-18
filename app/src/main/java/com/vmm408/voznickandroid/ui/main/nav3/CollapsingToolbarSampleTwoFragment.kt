package com.vmm408.voznickandroid.ui.main.nav3

import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_collapsing_toolbar_sample_one.*
import kotlin.math.abs

class CollapsingToolbarSampleTwoFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_collapsing_toolbar_sample_two
    override val TAG = "CollapsingToolbarSampleTwoFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            println("${verticalOffset}_____${appBarLayout.totalScrollRange}")

            (abs(verticalOffset) - appBarLayout.totalScrollRange == 0).let {
                toolbar?.setBackgroundColor(if (it) context?.getColor(R.color.white) ?: 0 else 0)
                titleCollapsed?.text = if (it) "Tittle collapsed" else ""
            }
        })
    }
}