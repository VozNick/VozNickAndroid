package com.vmm408.voznickandroid.ui.global.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.card.MaterialCardView
import com.vmm408.voznickandroid.ui.global.dp
import kotlinx.android.synthetic.main.fragment_custom_action_button.view.*

class ActionButton : MaterialCardView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private val actionButtonText: ActionButtonText by lazy { ActionButtonText(context) }

    fun setText(text: String) {
        actionButtonText.text = text

        arrayListOf<String>().forEach {

        }
    }
    
    inner class ActionButtonText : AppCompatTextView {
        constructor(context: Context) : super(context)
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
                super(context, attrs, defStyleAttr)

        init {
            gravity = Gravity.CENTER_VERTICAL
        }

        override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }
}

