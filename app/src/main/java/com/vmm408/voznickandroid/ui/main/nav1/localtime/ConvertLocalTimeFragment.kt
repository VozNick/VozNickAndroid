package com.vmm408.voznickandroid.ui.main.nav1.localtime

import android.os.Bundle
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import com.vmm408.voznickandroid.ui.global.changeFormat
import com.vmm408.voznickandroid.ui.global.setToStringFormat
import com.vmm408.voznickandroid.ui.main.nav2.showCalendarAndTime
import com.vmm408.voznickandroid.ui.main.nav2.showSingleRememberDialog
import kotlinx.android.synthetic.main.fragment_conert_local_time.*
import java.util.*

class ConvertLocalTimeFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_conert_local_time
    override val TAG = "ConvertLocalTimeFragment"

    private var localtime = Calendar.getInstance()
    private val timeZoneList = TimeZone.getAvailableIDs()
    private var selectedTimeZoneId = TimeZone.getDefault().id

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateViews()

        localTimeField?.setOnClickListener {
            showCalendarAndTime(context, localtime) {
                localtime = it
                updateViews()
            }
        }
        chooseLocalButton?.setOnClickListener {
            showSingleRememberDialog(
                context,
                "Choose time zone",
                timeZoneList,
                timeZoneList.indexOfFirst { it == selectedTimeZoneId }
            ) {
                selectedTimeZoneId = timeZoneList[it]
                updateViews()
            }
        }
    }

    private fun updateViews() {
        localTimeField?.text = localtime.time.setToStringFormat(
            "yyyy-MM-dd HH:mm:ss",
            TimeZone.getDefault().id
        )
        convertedTimeField?.text = localtime.time.setToStringFormat("HH:mm:ss", selectedTimeZoneId)
        chooseLocalButton?.text = TimeZone.getTimeZone(selectedTimeZoneId).id
    }
}