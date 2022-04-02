package com.vmm408.voznickandroid.ui.global.widgets

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
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
                KeyPath("**"), LottieProperty.COLOR_FILTER,
                {
                    PorterDuffColorFilter(
                        actionButtonText.currentTextColor,
                        PorterDuff.Mode.SRC_ATOP
                    )
                })
        } else {
            actionButtonLottie?.cancelAnimation()
            actionButtonLottie?.visibility = View.GONE
        }
    }
}
