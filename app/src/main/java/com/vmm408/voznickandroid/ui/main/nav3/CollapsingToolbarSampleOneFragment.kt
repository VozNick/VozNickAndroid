package com.vmm408.voznickandroid.ui.main.nav3

import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_collapsing_toolbar_sample_one.*
import kotlin.math.abs

class CollapsingToolbarSampleOneFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_collapsing_toolbar_sample_one
    override val TAG = "CollapsingToolbarSampleOneFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            println("${verticalOffset}_____${appBarLayout.totalScrollRange}")
//            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
//                toolbar?.setBackgroundColor(context?.getColor(R.color.white) ?: 0)
//                titleCollapsed?.text = "Tittle collapsed"
//                extendedButton?.setTextColor(context?.getColor(R.color.black) ?: 0)
//
//            } else {
//                toolbar?.setBackgroundColor(0)
//                titleCollapsed?.text = ""
//                extendedButton?.setTextColor(context?.getColor(R.color.white) ?: 0)
//            }

            (abs(verticalOffset) - appBarLayout.totalScrollRange == 0).let {
                toolbar?.setBackgroundColor(if (it) context?.getColor(R.color.white) ?: 0 else 0)
                titleCollapsed?.text = if (it) "Tittle collapsed" else ""
                extendedButton?.setTextColor(
                    if (it) context?.getColor(R.color.black) ?: 0
                    else context?.getColor(R.color.white) ?: 0
                )
            }
        })
        extendedButton?.setOnClickListener {
            replace(android.R.id.content, Screens.Nav3Host.getCollapsingToolbarSampleTwoScreen())
        }
        titleExpanded?.text = "Title Expanded"
    }
}