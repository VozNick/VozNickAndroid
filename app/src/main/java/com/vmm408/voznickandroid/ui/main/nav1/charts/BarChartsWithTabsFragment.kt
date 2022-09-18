package com.vmm408.voznickandroid.ui.main.nav1.charts

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.card.MaterialCardView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import com.vmm408.voznickandroid.ui.global.dp
import com.vmm408.voznickandroid.ui.global.widgets.charts.RoundedBarChart
import kotlinx.android.synthetic.main.fragment_bar_charts_with_tabs.*
import kotlinx.android.synthetic.main.view_charts_tabs.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

enum class TabFlow { DAY, WEEK, MONTH, YEAR }

private const val BAR_CHART_VIEW_ID = 12345

private var labelYMin = 0
private var labelYMAx = 0
private var labelYAvg = 0

class BarChartsWithTabsFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_bar_charts_with_tabs
    override val TAG = "BarChartsWithTabsFragment"

    private var tabFlow: TabFlow = TabFlow.DAY

    private var timeSlotList: ArrayList<Date>? = null
    private var list: ArrayList<HeartRateStats>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list = generateHeartRateData()

        arrayListOf(
            dayButton,
            weekButton,
            monthButton,
            yearButton
        ).forEachIndexed { index, textView ->
            textView?.setOnClickListener {
                tabFlow = TabFlow.values()[index]

                (tabFlow == TabFlow.values()[index]).let { isSelected ->
                    textView.isSelected = isSelected
                    (textView.parent as MaterialCardView).isSelected = isSelected
                }

                list?.let { prepareDataForCharts(it) }
            }
        }
    }

    private fun prepareDataForCharts(heartRateStats: ArrayList<HeartRateStats>) {
        timeSlotList = ArrayList<Date>().apply {
            val from = Calendar.getInstance().apply {
                when (tabFlow) {
                    TabFlow.DAY -> add(Calendar.DAY_OF_MONTH, -1)
                    TabFlow.WEEK -> add(Calendar.WEEK_OF_YEAR, -1)
                    TabFlow.MONTH -> add(Calendar.MONTH, -1)
                    TabFlow.YEAR -> {
                        add(Calendar.YEAR, -1)
                        set(Calendar.DAY_OF_MONTH, 1)
                    }
                }
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val to = Calendar.getInstance().apply {
                if (tabFlow == TabFlow.YEAR) set(
                    Calendar.DAY_OF_MONTH,
                    getActualMaximum(Calendar.DAY_OF_MONTH)
                )
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 0)
            }
            val calendarFieldWithAmount = when (tabFlow) {
                TabFlow.DAY -> arrayOf(Calendar.MINUTE, 15)
                TabFlow.WEEK -> arrayOf(Calendar.DAY_OF_MONTH, 1)
                TabFlow.MONTH -> arrayOf(Calendar.DAY_OF_MONTH, 1)
                TabFlow.YEAR -> arrayOf(Calendar.MONTH, 1)
            }
            while (from < to) {
                add(from.time)
                from.add(calendarFieldWithAmount[0], calendarFieldWithAmount[1])
            }
        }
        val groupedData = heartRateStats.groupBy {
            Calendar.getInstance().apply {
                timeInMillis = it.timestamp ?: 0

                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                when (tabFlow) {
                    TabFlow.DAY -> {
                        when {
                            (0..14).contains(get(Calendar.MINUTE)) -> set(Calendar.MINUTE, 0)
                            (15..29).contains(get(Calendar.MINUTE)) -> set(Calendar.MINUTE, 15)
                            (30..44).contains(get(Calendar.MINUTE)) -> set(Calendar.MINUTE, 30)
                            (45..59).contains(get(Calendar.MINUTE)) -> set(Calendar.MINUTE, 45)
                        }
                    }
                    TabFlow.WEEK, TabFlow.MONTH -> {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                    }
                    TabFlow.YEAR -> {
                        set(Calendar.DAY_OF_MONTH, 1)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                    }
                }
            }.time
        }
        val barEntryList = ArrayList<BarEntry>().apply {
            timeSlotList?.forEachIndexed { index, date ->
                val value = groupedData[date]?.sumOf { it.heartRate } ?: 0
                add(BarEntry(index.toFloat(), value.toFloat()))
            }
        }
        createChartsWithData(barEntryList)
    }

    private fun createChartsWithData(barEntryList: ArrayList<BarEntry>) {
        /** init DataSet with data **/
        val barDataSet = BarDataSet(barEntryList, "Data Set").apply {
            setDrawValues(false)
            color = Color.parseColor("#84D26A")
            setGradientColor(Color.parseColor("#84D26A"), Color.parseColor("#84D26A"))
        }

        /** show DataSet in charts widgets **/
        val dataSets = ArrayList<IBarDataSet>().apply { add(barDataSet) }

        /** init chartView **/
        chartContainer?.removeAllViews()

        RoundedBarChart(context ?: return).apply {
            id = BAR_CHART_VIEW_ID

            description?.isEnabled = false
            legend?.isEnabled = false
            isDoubleTapToZoomEnabled = false
            isHorizontalScrollBarEnabled = true
            isVerticalScrollBarEnabled = true
            setScaleEnabled(true)
            setDrawBorders(true)
            setBorderColor(Color.parseColor("#E8EAF0"))
            setRadius(50)
            marker = ChartMarker(context, this)

            viewPortHandler?.setMaximumScaleX(9f)
            viewPortHandler?.setMaximumScaleY(9f)

            axisLeft?.apply {
                isEnabled = false
                axisMinimum = 0f
            }
            axisRight?.apply {
                isEnabled = true
                typeface = Typeface.DEFAULT
                textColor = Color.parseColor("#A2A3B3")
                spaceTop = 0f
                isGranularityEnabled = true
                granularity = 1f
                setDrawZeroLine(false)
                setDrawGridLines(true)
                setDrawAxisLine(false)
                setDrawLabels(true)
                setLabelCount(3, true)
                axisMaximum = barEntryList.maxOfOrNull { it.x } ?: 0f
                axisMinimum = barEntryList.filter { 0f != it.x }.minOfOrNull { it.x } ?: 0f
            }
            xAxis?.apply {
                setDrawLimitLinesBehindData(true)
                position = XAxis.XAxisPosition.BOTTOM
                typeface = Typeface.DEFAULT
                textColor = Color.parseColor("#A2A3B3")
                setDrawGridLines(true)
                gridColor = Color.parseColor("#E8EAF0")
                valueFormatter = object : IndexAxisValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return try {
                            SimpleDateFormat(
                                when (tabFlow) {
                                    TabFlow.DAY -> "HH:mm"
                                    TabFlow.WEEK -> "EEE"
                                    TabFlow.MONTH -> "dd"
                                    TabFlow.YEAR -> "MMM"
                                }, Locale.ENGLISH
                            ).format(timeSlotList?.get(value.toInt())?.time)
                        } catch (e: Exception) {
                            ""
                        }
                    }
                }
            }
            data = BarData(dataSets)

            zoom(0f, 0f, 0f, 0f)
            barEntryList.lastOrNull { p -> p.y != 0F && !p.y.isNaN() }?.let {
                data?.barWidth = 0.6f
                when (tabFlow) {
                    TabFlow.DAY, TabFlow.WEEK -> zoom(0f, 0f, it.x, it.y)
                    TabFlow.MONTH, TabFlow.YEAR -> zoom(3.7f, 0f, it.x, it.y)
                }
                moveViewToX(it.x)
                setFitBars(true)
            }

            chartContainer?.addView(this)
        }

        (chartContainer?.findViewById<BarChart>(BAR_CHART_VIEW_ID)
            ?.layoutParams as? LinearLayout.LayoutParams)?.height = 180.dp

    }

    inner class ChartMarker(
        context: Context?,
        barChart: BarChart,
//        private val xAxisValueFormatter: IndexAxisValueFormatter
    ) : MarkerView(context, R.layout.view_chart_marker) {

        init {
            chartView = barChart
        }

        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            view?.findViewById<TextView>(R.id.label)?.apply {
                text = StringBuilder()
                    .append("Value - ")
//                    .append(xAxisValueFormatter.values?.get(e?.x?.toInt() ?: 0))
                    .append(e?.x?.toInt() ?: 0)
            }
            view?.findViewById<TextView>(R.id.dateLabel)?.apply {
                text = SimpleDateFormat(
                    when (this@BarChartsWithTabsFragment.tabFlow) {
                        TabFlow.DAY -> "MMM d, HH:mm"
                        TabFlow.WEEK -> "d EEE"
                        TabFlow.MONTH -> "d EEE, MMM"
                        TabFlow.YEAR -> "yyyy-MM-dd HH:mm:ss"
                    }, Locale.ENGLISH
                ).format(timeSlotList?.get(e?.x?.toInt() ?: 0)?.time ?: 0)
            }
            super.refreshContent(e, highlight)
        }
    }
}

data class HeartRateStats(var heartRate: Int = 0, var timestamp: Long? = null)

private fun generateHeartRateData() = ArrayList<HeartRateStats>().apply {
    val from = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -2) }
    val to = Calendar.getInstance()

    val listOfValues = ArrayList<Int>()

    while (from < to) {
        val random = (40..300).random()

        if (labelYMAx < random) labelYMAx = random
        if (labelYMin > random) labelYMin = random
        listOfValues.add(random)

        add(HeartRateStats(random, from.timeInMillis))
        from.add(Calendar.SECOND, 13)
    }

    labelYAvg = listOfValues.sum().div(listOfValues.size)
}