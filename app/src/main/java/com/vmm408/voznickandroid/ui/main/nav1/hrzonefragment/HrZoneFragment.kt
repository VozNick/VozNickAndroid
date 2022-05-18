package com.vmm408.voznickandroid.ui.main.nav1.hrzonefragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import com.vmm408.voznickandroid.ui.global.dp
import com.vmm408.voznickandroid.ui.global.widgets.HrZone
import kotlinx.android.synthetic.main.fragment_hr_zone.*

class HrZoneFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_hr_zone
    override val TAG = "HrZoneFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hrZoneProgressBar?.initSizeOfCells(20)

        HrZone.values().forEachIndexed { index, hrZone ->
            TextView(context).apply {
                setBackgroundColor(Color.parseColor(hrZone.zoneColor))
                setOnClickListener { this@HrZoneFragment.hrZoneProgressBar?.redrawCurrentCell(hrZone) }

                this@HrZoneFragment.buttonContainer?.addView(this)

                (layoutParams as? LinearLayout.LayoutParams)?.apply {
                    setMargins(40.dp, if (index == 0) 0.dp else 10.dp, 40.dp, 0)
                    width = RelativeLayout.LayoutParams.MATCH_PARENT
                    height = 48.dp
                }
            }
        }

        nextCellButton?.setOnClickListener { hrZoneProgressBar?.moveToNextCell() }
        clearAllCellsButton?.setOnClickListener {
            hrZoneProgressBar?.clearAllViews()
            hrZoneProgressBar?.initSizeOfCells(20)
        }
    }
}