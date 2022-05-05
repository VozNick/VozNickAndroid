package com.vmm408.voznickandroid.ui.main.nav2

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import com.vmm408.voznickandroid.ui.global.dp
import com.vmm408.voznickandroid.ui.global.setToStringFormat
import kotlinx.android.synthetic.main.fragment_user_fields.*
import kotlinx.android.synthetic.main.fragment_user_fields.rootView
import kotlinx.android.synthetic.main.item_row_two_text_view_48.view.*
import kotlinx.android.synthetic.main.view_alert_dialog_title_sample_one.view.*
import java.util.*
import kotlin.collections.ArrayList

enum class UserFieldCards(val label: String) {
    CARD_0("Calendar"),
    CARD_1("Time"),
    CARD_2("Calendar and time"),
    CARD_3("Simple list dropdown"),
    CARD_4("One selection remember"),
    CARD_5("Multi Selection Remember"),
    CARD_6("List text"),
    CARD_7("Int"),
}

class UserFieldsFragment : BaseFragment() {
    companion object {
        fun newInstance() = UserFieldsFragment()
    }

    override val layoutRes = R.layout.fragment_user_fields
    override val TAG = "UserFieldsFragment"

    private var calendarMin = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, -5)
    }
    private var calendarMax = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, 5)
    }
    private var calendar = Calendar.getInstance()
    private var calendarAndTime = Calendar.getInstance()

    private var simpleList = arrayOf("Male", "Female")

    private var singleSelectionList = Array(20) { "String $it" }
    private var singleSelected = -1

    private var multiSelectionList = Array(20) { "String $it" }
    private var multiSelectedList = BooleanArray(multiSelectionList.size)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserFieldCards.values().forEach { card ->
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_two_text_view_48, rootView, false)
                ?.apply {
                    label?.text = card.label
                    addClickListener(card)
                    (layoutParams as? LinearLayout.LayoutParams)?.setMargins(
                        18.dp, if (card.ordinal == 0) 40.dp else 8.dp, 18.dp, 0
                    )
                }?.also {
                    mainContainer?.addView(it)
                }
        }

        testField?.setOnClickListener {
            testField?.text = ArrayList<String>().apply {
                multiSelectionList.forEachIndexed { index, s ->
                    if (multiSelectedList[index]) add(s)
                }
            }.joinToString()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun View.addClickListener(card: UserFieldCards) {
        this.setOnClickListener {
            when (card) {
                UserFieldCards.CARD_0 -> showCalendar(
                    context,
                    { c ->
                        calendar = c
                        field?.text = c.time.setToStringFormat("dd MMMM yyyy")
                    },
                    calendar,
                    datePickerMinDateInMillis = calendarMin.timeInMillis,
                    datePickerMaxDateInMillis = calendarMax.timeInMillis
                )
                UserFieldCards.CARD_1 -> showTime(
                    context,
                    { c ->
                        calendar = c
                        field?.text = c.time.setToStringFormat("HH:mm")
                    },
                    calendar
                )
                UserFieldCards.CARD_2 -> showCalendarAndTime(
                    context,
                    { c ->
                        calendarAndTime = c
                        field?.text = c.time.setToStringFormat("HH:mm  dd MMMM yyyy")
                    },
                    calendarAndTime
                )
                UserFieldCards.CARD_3 -> showSimpleListDialog(context, simpleList) { _, p1 ->
                    this.field?.text = simpleList[p1]
                }
                UserFieldCards.CARD_4 -> showSingleRememberDialog(
                    context,
                    singleSelectionList,
                    singleSelected
                ) { p0, p1 ->
                    this.field?.text = singleSelectionList[p1]
                    p0.dismiss()
                }
                UserFieldCards.CARD_5 -> showMultiRememberDialog(
                    context,
                    "Multi selection",
                    multiSelectionList,
                    multiSelectedList
                ) {
                    multiSelectedList = it
                    field?.text = ArrayList<String>().apply {
                        multiSelectionList.forEachIndexed { index, s ->
                            if (multiSelectedList[index]) add(s)
                        }
                    }.joinToString()
                }


//                UserFieldCards.CARD_4 -> {
//                    val list = Array(180) { it + 1 }
//
//                    showSingleRememberDialog(
//                        context,
//                        list.map { "$it min" } as ArrayList<String>,
//                        DialogInterface.OnClickListener { dialog, which ->
//                            field?.text = list[which].toString()
//                        })
//
//                }
//                UserFieldCards.CARD_5 -> {
//                    val list = Array(180) { it + 1 }
//                    showSingleRememberDialog(
//                        context,
//                        list.map { "$it min" } as ArrayList<String>,
//                        DialogInterface.OnMultiChoiceClickListener {
//
//                        })
//                }
//                UserFieldCards.CARD_1 -> showHeightPicker(
//                    object : NumberPickerListener {
//                        override fun selectedItem(value: Int) {
//                            this@addClickListener.field?.text = "$value cm"
//                        }
//                    }, context
//                )
//                UserFieldCards.CARD_2 -> showWeightPicker(
//                    object : NumberPickerListener {
//                        override fun selectedItem(value: Int) {
//                            this@addClickListener.field?.text = "$value kg"
//                        }
//                    }, context
//                )
            }
        }
    }

    private fun showSimpleListDialog(
        context: Context?,
        list: Array<String>,
        listener: DialogInterface.OnClickListener,
    ) {
        context?.let {
            AlertDialog.Builder(it)
                .setItems(list, listener)
                .create()
                .show()
        }
    }

    private fun showSingleRememberDialog(
        context: Context,
        list: Array<String>,
        checkedPosition: Int,
        listener: DialogInterface.OnClickListener
    ) {
        val adapter = ArrayAdapter(context, R.layout.item_row_checked_simple_text_view, list)
        val alert = AlertDialog.Builder(context)
            .setCustomTitle(
                View.inflate(
                    context,
                    R.layout.view_alert_dialog_title_sample_one,
                    null
                )
            )
            .setSingleChoiceItems(adapter, checkedPosition, listener)
            .create()
        alert.window?.setBackgroundDrawableResource(R.drawable.background_alert_dialog_rounded_corners_8)
        alert.show()
    }

//    private fun showSingleRememberWithSomeItemsDialog(
//        context: Context,
//        list: ArrayList<String>,
//        listener: DialogInterface.OnClickListener,
//        checkedPosition: Int = 0
//    ) {
//            val adapter = ArrayAdapter<String>(c, R.layout.item_row_location).apply {
//                addAll(list.map { if (it == 0) "No Duration" else "$it min" })
//            }
//            val alert = AlertDialog.Builder(c)
//                .setCustomTitle(View.inflate(c, R.layout.view_choose_duration_min, null))
//                .setSingleChoiceItems(
//                    adapter,
//                    0
//                ) { dialog, which ->
//                    freestyle.durationSec = if (which == 0) null else list[which].times(60)
//                    durationField?.text =
//                        if (which == 0) "No Duration" else "${list[which]} min"
//                    dialog.dismiss()
//                }
//                .create()
//            alert.window?.setBackgroundDrawableResource(R.drawable.background_alert_dialog_rounded_corners_8)
//            alert.show()
//
//    }

//    interface OnClickListener {
//        /**
//         * This method will be invoked when a button in the dialog is clicked.
//         *
//         * @param dialog the dialog that received the click
//         * @param which the button that was clicked (ex.
//         * [DialogInterface.BUTTON_POSITIVE]) or the position
//         * of the item clicked
//         */
//        fun onClick(dialog: DialogInterface?, which: Int)
//    }
}