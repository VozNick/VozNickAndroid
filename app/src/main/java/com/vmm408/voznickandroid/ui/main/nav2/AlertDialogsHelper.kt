package com.vmm408.voznickandroid.ui.main.nav2

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.vmm408.voznickandroid.R
import kotlinx.android.synthetic.main.view_alert_dialog_title_sample_one.view.*

fun showMultiRememberDialog(
    context: Context,
    title: String?,
    list: Array<String>,
    checkedList: BooleanArray,
    selectedItemsList: (checkedList: BooleanArray) -> Unit
) {
    val checkedItems = BooleanArray(list.size).apply {
        checkedList.forEachIndexed { index, b -> this[index] = b }
    }

    val alert = AlertDialog.Builder(context)
        .apply {
            if (!title.isNullOrEmpty()) {
                setCustomTitle(
                    View.inflate(context, R.layout.view_alert_dialog_title_sample_one, null)
                        ?.apply { this.title?.text = title }
                )
            }
        }
        .setMultiChoiceItems(list, checkedItems) { _, which, isChecked ->
            checkedItems[which] = isChecked
        }
        .setNeutralButton("Clear") { _, _ ->
            for (i in checkedItems.indices) checkedItems[i] = false
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton("Confirm") { dialog, _ ->
            selectedItemsList(checkedItems)
            dialog.dismiss()
        }
        .create()
    alert.setCancelable(false)
    alert.setCanceledOnTouchOutside(false)
    alert.window?.setBackgroundDrawableResource(R.drawable.background_alert_dialog_rounded_corners_8)
    alert.show()
}