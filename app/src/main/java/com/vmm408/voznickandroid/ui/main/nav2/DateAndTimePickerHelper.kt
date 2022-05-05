package com.vmm408.voznickandroid.ui.main.nav2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import java.util.*

fun showCalendarAndTime(
    context: Context,
    listener: (calendar: Calendar) -> Unit,
    calendar: Calendar = Calendar.getInstance(),
    is24HourView: Boolean = true,
    datePickerMinDateInMillis: Long? = null,
    datePickerMaxDateInMillis: Long? = null
) {
    showCalendar(
        context,
        { c ->
            showTime(
                context,
                { time -> listener(time) },
                c,
                is24HourView
            )
        },
        calendar,
        datePickerMinDateInMillis,
        datePickerMaxDateInMillis
    )
}

fun showCalendar(
    context: Context,
    listener: (calendar: Calendar) -> Unit,
    calendar: Calendar = Calendar.getInstance(),
    datePickerMinDateInMillis: Long? = null,
    datePickerMaxDateInMillis: Long? = null,
) {
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            listener(Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                set(Calendar.MONTH, month)
                set(Calendar.YEAR, year)
            })
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    ).apply {
        datePickerMinDateInMillis?.let { min -> datePicker.minDate = min }
        datePickerMaxDateInMillis?.let { max -> datePicker.maxDate = max }
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        show()
    }
}

fun showTime(
    context: Context,
    listener: (calendar: Calendar) -> Unit,
    calendar: Calendar = Calendar.getInstance(),
    is24HourView: Boolean = true
) {
    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            listener(calendar)
        },
        calendar[Calendar.HOUR_OF_DAY],
        calendar[Calendar.MINUTE],
        is24HourView
    ).apply {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        show()
    }
}