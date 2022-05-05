package com.vmm408.voznickandroid.ui.global.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.vmm408.voznickandroid.ui.global.dp

enum class HrZone(
    val value: Int,
    val zoneText: String,
    val percentText: String,
    val zoneColor: String,
    val percentRange: IntRange
) {
    RED(1, "ZONE 1", "above 90% of MAX HR", "#B80F0A", 0..59),
    YELLOW(2, "ZONE 2", "83-89% of MAX HR", "#FED103", 60..69),
    GREEN(3, "ZONE 3", "70-82% of MAX HR", "#4CBB17", 70..81),
    BLUE(4, "ZONE 4", "60-69% of MAX HR", "#0E8C9B", 83..88),
    GREY(5, "ZONE 5", "under 60% of MAX HR", "#585960", 85..200)
}

private var itemCount = 0
private var currentCell = 0
private var currentHrZone: HrZone? = null

class HrZoneProgressBarFragment : LinearLayout {

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet) : super(context!!, attrs)

    fun initSizeOfCells(count: Int) {
        clearAllViews()

        itemCount = count
        for (i in 0 until itemCount) {
            addCell()
        }
    }

    fun drawAllCells(list: ArrayList<HrZone>) {
        clearAllViews()
        list.forEach { addCell(it) }
    }

    fun clearAllViews() {
        itemCount = 0
        currentCell = 0
        currentHrZone = null
        removeAllViews()
    }

    fun moveToNextCell(hrZone: HrZone? = null) {
        if (currentCell <= itemCount) currentCell += 1 else return
        redrawCurrentCell(hrZone ?: currentHrZone ?: return)
    }

    fun redrawCurrentCell(hrZone: HrZone) {
        currentHrZone = hrZone
        this@HrZoneProgressBarFragment.getChildAt(currentCell)
            ?.setBackgroundColor(Color.parseColor(hrZone.zoneColor))
    }

    private fun addCell(hrZone: HrZone? = null) {
        View(context)
            .apply {
                if (null != hrZone) setBackgroundColor(Color.parseColor(hrZone.zoneColor))
                else setBackgroundColor(Color.BLACK)

                this@HrZoneProgressBarFragment.addView(this)

                (layoutParams as? LayoutParams)?.apply {
                    height = LayoutParams.MATCH_PARENT
                    width = 0
                    weight = 1f
                    if (this@HrZoneProgressBarFragment.childCount != 1) marginStart = 2.dp
                }
            }
    }
}