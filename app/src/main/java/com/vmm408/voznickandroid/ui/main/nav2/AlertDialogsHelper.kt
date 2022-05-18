package com.vmm408.voznickandroid.ui.main.nav2

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.vmm408.voznickandroid.R
import kotlinx.android.synthetic.main.view_alert_dialog_title_sample_one.view.*

fun showSingleRememberDialog(
    context: Context,
    title: String?,
    list: Array<String>,
    checkedPosition: Int,
    newCheckedPosition: (newCheckedPosition: Int) -> Unit
) {
    var tempCheckedPosition = checkedPosition

    val adapter = ArrayAdapter(context, R.layout.item_row_check_box_sample_one, list)
    val alert = AlertDialog.Builder(context)
//        .setCustomTitle(title)
        .setSingleChoiceItems(adapter, tempCheckedPosition) { _, which ->
            tempCheckedPosition = which
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton("Confirm") { dialog, _ ->
            newCheckedPosition(tempCheckedPosition)
            dialog.dismiss()
        }
        .create()
    alert.setCancelable(false)
    alert.setCanceledOnTouchOutside(false)
    alert.window?.setBackgroundDrawableResource(R.drawable.background_alert_dialog_rounded_corners_8)
    alert.show()
}

fun showMultiRememberDialog(
    context: Context,
    title: String?,
    list: Array<String>,
    checkedList: BooleanArray,
    selectedItemList: (checkedList: BooleanArray) -> Unit
) {
    val checkedItems = BooleanArray(list.size).apply {
        checkedList.forEachIndexed { index, b -> this[index] = b }
    }

    val alert = AlertDialog.Builder(context)
        .setCustomTitle(title)
        .setMultiChoiceItems(list, checkedItems) { dialog, which, isChecked ->
            checkedItems[which] = isChecked
        }
        .setNeutralButton("Clear") { _, _ ->
            for (i in checkedItems.indices) checkedItems[i] = false
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton("Confirm") { dialog, _ ->
            selectedItemList(checkedItems)
            dialog.dismiss()
        }
        .create()
    alert.setCancelable(false)
    alert.setCanceledOnTouchOutside(false)
    alert.window?.setBackgroundDrawableResource(R.drawable.background_alert_dialog_rounded_corners_8)
    alert.show()
}

private fun AlertDialog.Builder.setCustomTitle(text: String?): AlertDialog.Builder {
    if (!text.isNullOrEmpty()) {
        setCustomTitle(
            View.inflate(context, R.layout.view_alert_dialog_title_sample_one, null)
                ?.apply { this.title?.text = text }
        )
    }
    return this
}