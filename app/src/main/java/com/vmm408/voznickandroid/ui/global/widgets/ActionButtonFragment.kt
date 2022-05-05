package com.vmm408.voznickandroid.ui.global.widgets

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.google.android.material.card.MaterialCardView
import com.vmm408.voznickandroid.R
import kotlinx.android.synthetic.main.widget_button.*

enum class DrawablePosition { START, END }

open class ActionButtonFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.widget_button, container, false)

    private var buttonText: String = ""

    fun setText(
        text: String,
        color: Int,
        font: Int,
        isAllCaps: Boolean = false
    ) {
        buttonText = text
        with(actionButtonText) {
            this?.text = text
            this?.setTextColor(context.getColor(color))
            this?.typeface = ResourcesCompat.getFont(context, font)
            this?.isAllCaps = isAllCaps
        }
    }

    fun setBorder(
        color: Int,
        width: Int = resources.getDimension(R.dimen.stroke_width_1_dp).toInt()
    ) {
        context?.getColor(color)?.let { actionButtonCard?.setStrokeColor(it) }
        actionButtonCard?.strokeWidth = width
    }

    fun setBackgroundColor(color: Int) {
        context?.getColor(color)?.let { actionButtonCard?.setCardBackgroundColor(it) }
    }

    fun setBackground(drawable: Int) {
        actionButtonCard?.setBackgroundResource(drawable)
    }

    fun setCornersRadius(radius: Int) {
        actionButtonCard?.radius = radius.toFloat()
    }

    fun addDrawable(
        drawable: Int,
        padding: Int,
        position: DrawablePosition,
    ) {
        when (position) {
            DrawablePosition.START ->
                actionButtonText?.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
            DrawablePosition.END ->
                actionButtonText?.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
        }
        actionButtonText?.compoundDrawablePadding = resources.getDimension(padding).toInt()
    }

    fun onClick(onClick: (() -> Unit)?) {
        actionButtonCard?.setOnClickListener { onClick?.let { it() } }
    }

    fun startLoading() {
        buttonIsEnabled(false)
        lottiIsEnabled(true)
        actionButtonText?.text = ""
    }

    fun stopLoading() {
        buttonIsEnabled(true)
        lottiIsEnabled(false)
        actionButtonText?.text = buttonText
    }

    override fun onDestroyView() {
        lottiIsEnabled(false)
        actionButtonText?.text = buttonText
        super.onDestroyView()
    }

    fun buttonIsEnabled(isEnable: Boolean) {
        view?.isEnabled = isEnable
        actionButtonCard?.isEnabled = isEnable
    }

    private fun lottiIsEnabled(isEnable: Boolean) {
        if (isEnable) {
            actionButtonLottie?.playAnimation()
            actionButtonLottie?.visibility = View.VISIBLE
            actionButtonLottie?.addValueCallback(
                KeyPath("**"), LottieProperty.COLOR_FILTER
            ) {
                PorterDuffColorFilter(
                    actionButtonText.currentTextColor,
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        } else {
            actionButtonLottie?.cancelAnimation()
            actionButtonLottie?.visibility = View.GONE
        }
    }
}

class ActionButton : FrameLayout {
    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet) : super(context!!, attrs)

    private var card: MaterialCardView? = null
    private var frameLayout: FrameLayout? = null
    private var textView: TextView? = null
    private val lotti = LottieAnimationView(context)

    internal var text = ""
    internal var textColor = 0
    internal var textSize = 0
    internal var textFont = 0
    internal var textIsAllCaps = 0

    internal var strokeColor = 0
    internal var strokeWidth = 0

    internal var cardBackgroundColor = 0
    internal var cardBackgroundDrawable = 0

    internal var cardRadius = 0

    internal var drawable = 0
    internal var drawablePosition = DrawablePosition.START

    init {
        card = initCard()
        this@ActionButton.addView(card)
        frameLayout = initFrameLayout()
        card?.addView(frameLayout)
        textView = initText()
        frameLayout?.addView(textView)
    }

    private fun initCard() = MaterialCardView(context).apply {
        layoutParams?.apply {
            height = LayoutParams.MATCH_PARENT
            width = LayoutParams.MATCH_PARENT
        }
    }

    private fun initFrameLayout() = FrameLayout(context).apply {
        layoutParams?.apply {
            height = LayoutParams.MATCH_PARENT
            width = LayoutParams.MATCH_PARENT
        }
    }

    private fun initText() = TextView(context).apply {
        layoutParams?.apply {
            height = LayoutParams.MATCH_PARENT
            width = LayoutParams.WRAP_CONTENT
        }
        text = this@ActionButton.text
        setTextColor(this@ActionButton.textColor)
        textSize = this@ActionButton.textSize.toFloat()
    }
}