package com.vmm408.voznickandroid.ui.main.nav2

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vmm408.voznickandroid.R
import kotlinx.android.synthetic.main.view_number_increase_picker.*

class NumberIncreaseModalBottomSheet : BottomSheetDialogFragment() {
    val TAG = "NumberIncreaseModalBottomSheet"

    var valueListRange = 0..0
    var lastValue = 0
    var callback: ((value: Int) -> Unit)? = null

    private var newValue = 0
        @SuppressLint("SetTextI18n")
        set(value) {
            valueText?.setTextColor(
                Color.parseColor(
                    when {
                        value > field -> "#17B16A"
                        value < field -> "#EF4045"
                        else -> "#161518"
                    }
                )
            )
            field = value
            valueText?.text = "${value}v"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.view_number_increase_picker, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).behavior.apply {
            isHideable = false
            isDraggable = false
            isCancelable = false
        }
        newValue = lastValue
        cancelButton?.setOnClickListener { dismiss() }
        doneButton?.setOnClickListener {
            callback?.invoke(newValue)
            dismiss()
        }
        downButtonContainer?.apply {
            setOnClickListener { if (newValue > valueListRange.first) newValue-- }
            setOnLongClickListener {
                executeRecursively({ newValue-- }, { this.isPressed }, 100L)
                true
            }
        }
        upButtonContainer?.apply {
            setOnClickListener { if (newValue < valueListRange.last) newValue++ }
            setOnLongClickListener {
                executeRecursively({ newValue++ }, { this.isPressed }, 100L)
//                executeRecursivelyWithDecreasingDelay(
//                    { newValue++ },
//                    { this.isPressed },
//                    100L,
//                    20L,
//                    5L
//                )
                true
            }
        }
    }

    /**
     * Execute given function, and if condition is met, re-execute recursively after delay
     * @param function: function to be executed
     * @param conditionToRepeat: condition to re-execute function
     * @param delayMillis: delay after which function should be re-executed (if condition was met)
     */
    private fun executeRecursively(
        function: () -> Unit,
        conditionToRepeat: () -> Boolean,
        delayMillis: Long
    ) {
        if (newValue !in valueListRange.last downTo valueListRange.first) return
        function()
        if (conditionToRepeat())
            Handler(Looper.getMainLooper()).postDelayed(
                { executeRecursively(function, conditionToRepeat, delayMillis) },
                delayMillis
            )

//        if (newValue > valueListRange.first && newValue < valueListRange.last) {
//            function()
//            if (conditionToRepeat())
//                Handler(Looper.getMainLooper()).postDelayed(
//                    { executeRecursively(function, conditionToRepeat, delayMillis) },
//                    delayMillis
//                )
//        } else return
    }

    /**
     * Execute given function, and if condition is met, re-execute recursively after delay
     * @param function: function to be executed
     * @param conditionToRepeat: condition to re-execute function
     * @param delayMillis: delay after which function should be re-executed (if condition was met)
     * @param minDelayMillis: minimal delay in milliseconds
     * @param decreasingDelayMillis: amount to decrease delay for next re-execution (if minimal delay has not been reached)
     */
    private fun executeRecursivelyWithDecreasingDelay(
        function: () -> Unit,
        conditionToRepeat: () -> Boolean,
        delayMillis: Long,
        minDelayMillis: Long,
        decreasingDelayMillis: Long
    ) {
        if (newValue !in valueListRange.last downTo valueListRange.first) return
        function()
        if (conditionToRepeat()) {
            val delay =
                if (delayMillis <= minDelayMillis) minDelayMillis else delayMillis - decreasingDelayMillis
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    executeRecursivelyWithDecreasingDelay(
                        function,
                        conditionToRepeat,
                        delay,
                        minDelayMillis,
                        decreasingDelayMillis
                    )
                },
                delayMillis
            )
        }
    }
}