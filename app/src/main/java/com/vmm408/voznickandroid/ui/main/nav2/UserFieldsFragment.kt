package com.vmm408.voznickandroid.ui.main.nav2

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import com.google.android.material.card.MaterialCardView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.model.CheckSampleOne
import com.vmm408.voznickandroid.model.fillListWithData
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseFragment
import com.vmm408.voznickandroid.ui.global.dp
import com.vmm408.voznickandroid.ui.global.randomInt
import com.vmm408.voznickandroid.ui.global.setToStringFormat
import kotlinx.android.synthetic.main.fragment_user_fields.*
import kotlinx.android.synthetic.main.fragment_user_fields.rootView
import kotlinx.android.synthetic.main.item_row_two_text_view_48.view.*
import java.util.*
import kotlin.collections.ArrayList

enum class UserFieldCardType { LABEL, CARD, SPACE }

enum class UserFieldCards(val label: String, val type: UserFieldCardType) {
    CALENDARS_LABEL("Calendars", UserFieldCardType.LABEL),
    CALENDAR("Calendar", UserFieldCardType.CARD),
    TIME("Time", UserFieldCardType.CARD),
    CALENDAR_AND_TIME("Calendar and time", UserFieldCardType.CARD),
    ALERTS_LABEL("ALERTS", UserFieldCardType.LABEL),
    SIMPLE_LIST_DROPDOWN("Simple list dropdown", UserFieldCardType.CARD),
    ONE_SELECTION_REMEMBER("One selection remember", UserFieldCardType.CARD),
    MULTI_SELECTION_REMEMBER("Multi Selection Remember", UserFieldCardType.CARD),
    SPACE_ONE("", UserFieldCardType.SPACE),
    FRAGMENT_SELECTION("Fragment selection", UserFieldCardType.CARD),
    FRAGMENT_SELECTION_WITH_SAVE("Fragment selection with save", UserFieldCardType.CARD),
    FRAGMENT_SELECTION_WITH_SAVE_BOTTOM_SHEET(
        "Fragment selection with save bottom sheet",
        UserFieldCardType.CARD
    ),
    SPACE_TWO("", UserFieldCardType.SPACE),
    NUMBER_PICKER_ONE("Number picker one", UserFieldCardType.CARD),
}

class UserFieldsFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_user_fields
    override val TAG = "UserFieldsFragment"

    private var calendarMin = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -5) }
    private var calendarMax = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 5) }
    private var calendar = Calendar.getInstance()
    private var calendarAndTime = Calendar.getInstance()

    private var simpleList = arrayOf("Male", "Female")
    private var simpleListSelected = -1

    private var singleSelectionList = Array(20) { "String $it" }
    private var singleListSelected = -1

    private var multiSelectionList = Array(20) { "String $it" }
    private var multiListSelected = BooleanArray(multiSelectionList.size)

    private val checkSampleOneList = fillListWithData()
    private val checkSampleOneWithSaveList = fillListWithData()
    private val checkSampleOneWithSaveBottomSheetList = fillListWithData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserFieldCards.values().forEach { card ->
            when (card.type) {
                UserFieldCardType.LABEL -> {
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_row_label_sample_one, rootView, false)
                        ?.apply {
                            id = card.ordinal
                            label?.text = card.label
                            (layoutParams as? FrameLayout.LayoutParams)?.setMargins(
                                18.dp, 30.dp, 18.dp, 8.dp
                            )
                        }.also { mainContainer?.addView(it) }
                }
                UserFieldCardType.CARD -> {
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_row_two_text_view_48, rootView, false)
                        ?.apply {
                            id = card.ordinal
                            label?.text = card.label
                            addClickListener(card)
                            (layoutParams as? FrameLayout.LayoutParams)?.setMargins(
                                18.dp,
                                if (UserFieldCards.values()[card.ordinal - 1].type == UserFieldCardType.SPACE) 16.dp else 8.dp,
                                18.dp,
                                0
                            )
                        }?.also { mainContainer?.addView(it) }
                }
                else -> {
                }
            }
        }

//        testField?.setOnClickListener {
//            testField?.text = ArrayList<String>().apply {
//                checkSampleOneList.forEach { s ->
//                    if (null != s.checkList?.firstOrNull { it.isChecked }) {
//                        s.name?.let { n -> add(n) }
//                    }
//                }
//            }.joinToString()
//        }
    }

    @SuppressLint("SetTextI18n")
    private fun View.addClickListener(card: UserFieldCards) {
        this.setOnClickListener {
            when (card) {
                UserFieldCards.CALENDAR -> {
                    showCalendar(
                        context,
                        calendar,
                        calendarMin.timeInMillis,
                        calendarMax.timeInMillis
                    ) { c ->
                        calendar = c
                        field?.text = c.time.setToStringFormat("dd MMMM yyyy")
                    }
                }
                UserFieldCards.TIME -> {
                    showTime(
                        context,
                        calendar
                    ) { c ->
                        calendar = c
                        field?.text = c.time.setToStringFormat("HH:mm")
                    }
                }
                UserFieldCards.CALENDAR_AND_TIME -> {
                    showCalendarAndTime(
                        context,
                        calendarAndTime
                    ) { c ->
                        calendarAndTime = c
                        field?.text = c.time.setToStringFormat("HH:mm  dd MMMM yyyy")
                    }
                }
                UserFieldCards.SIMPLE_LIST_DROPDOWN -> {
                    showSimpleListDialog(
                        context,
                        simpleList
                    ) { _, p1 ->
                        simpleListSelected = p1
                        this.field?.text = simpleList[p1]
                    }
                }
                UserFieldCards.ONE_SELECTION_REMEMBER -> {
                    showSingleRememberDialog(
                        context,
                        null,
                        singleSelectionList,
                        singleListSelected
                    ) { p0 ->
//                    this.field?.text = singleSelectionList[p1]
////                    p0.dismiss()
                    }
                }
                UserFieldCards.MULTI_SELECTION_REMEMBER -> {
                    showMultiRememberDialog(
                        context,
                        "Multi selection",
                        multiSelectionList,
                        multiListSelected
                    ) {
                        multiListSelected = it
                        field?.text = ArrayList<String>().apply {
                            multiSelectionList.forEachIndexed { index, s ->
                                if (multiListSelected[index]) add(s)
                            }
                        }.joinToString()
                    }
                }
                UserFieldCards.FRAGMENT_SELECTION -> {
                    add(android.R.id.content, Screens.Nav2Host.getCheckSampleOneScreen(checkSampleOneList) {
                        field?.text = ArrayList<String>().apply {
                            checkSampleOneList.forEach { s ->
                                if (null != s.checkList?.firstOrNull { it.isChecked }) {
                                    s.name?.let { n -> add(n) }
                                }
                            }
                        }.joinToString()
                    })
                }
                UserFieldCards.FRAGMENT_SELECTION_WITH_SAVE -> {
                    add(
                        android.R.id.content,
                        Screens.Nav2Host.getCheckSampleOneWithSaveScreen(checkSampleOneWithSaveList) {
                            field?.text = ArrayList<String>().apply {
                                checkSampleOneWithSaveList.forEach { s ->
                                    if (null != s.checkList?.firstOrNull { it.isChecked }) {
                                        s.name?.let { n -> add(n) }
                                    }
                                }
                            }.joinToString()
                        }
                    )
                }
                UserFieldCards.NUMBER_PICKER_ONE -> {

                }
                else -> {
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
        context: Context,
        list: Array<String>,
        listener: DialogInterface.OnClickListener,
    ) = AlertDialog.Builder(context).setItems(list, listener).create().show()


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