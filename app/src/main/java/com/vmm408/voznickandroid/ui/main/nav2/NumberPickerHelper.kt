package com.vmm408.voznickandroid.ui.main.nav2

import android.content.Context
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog

fun showNumPicker(
    context: Context,
    list: Array<String>,
    function: (value: Int) -> Unit
) = NumberPicker(context).apply {
    displayedValues = list
    minValue = 0
    maxValue = list.size.minus(1)
}.createAlertDialog(function)

fun showNumPicker(
    context: Context,
    list: Array<String>,
    setValue: Int,
    function: (value: Int) -> Unit
) = NumberPicker(context).apply {
    displayedValues = list
    minValue = 0
    maxValue = list.size.minus(1)
    if (setValue != -1) value = setValue
}.createAlertDialog(function)

fun showNumPicker(
    context: Context,
    list: Array<Int>,
    function: (value: Int) -> Unit
) = NumberPicker(context).apply {
    displayedValues = list.map { "$it" }.toTypedArray()
    minValue = list.first()
    maxValue = list.last()
    value = 20
}.createAlertDialog(function)

fun showNumPicker(
    context: Context,
    list: Array<Int>,
    setValue: Int,
    function: (value: Int) -> Unit
) = NumberPicker(context).apply {
    displayedValues = list.map { "$it" }.toTypedArray()
    minValue = list.first()
    maxValue = list.last()
    if (setValue != -1) value = setValue
}.createAlertDialog(function)

private fun NumberPicker.createAlertDialog(function: (value: Int) -> Unit) {
    AlertDialog.Builder(context).apply {
        setView(this@createAlertDialog)
        setPositiveButton("Confirm") { p0, _ ->
            function(this@createAlertDialog.value)
            p0.dismiss()
        }
        setNegativeButton("Cancel") { p0, _ -> p0.dismiss() }
        setCancelable(false)
        create()
        show()
    }
}