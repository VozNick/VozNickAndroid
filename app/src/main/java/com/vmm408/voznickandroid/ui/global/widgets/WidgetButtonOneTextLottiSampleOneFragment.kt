package com.vmm408.voznickandroid.ui.global.widgets

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.google.android.material.card.MaterialCardView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.dp

class ActionButtonSampleOneFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.widget_button_one_text_lotti_sample_one, container, false)

    private var buttonText: String = ""

    fun setText(text: String, color: Int, font: Int, isAllCaps: Boolean = false) {
        buttonText = text

        view?.findViewById<TextView>(R.id.actionButtonText)?.apply {
            this.text = text
            this.setTextColor(context.getColor(color))
            this.typeface = ResourcesCompat.getFont(context, font)
            this.isAllCaps = isAllCaps
        }
    }

    fun setBorder(color: Int, width: Int = 1.dp) {
        view?.findViewById<MaterialCardView>(R.id.actionButtonCard)?.apply {
            context?.getColor(color)?.let {
                strokeColor = it
                strokeWidth = width
            }
        }
    }

    fun setBackgroundColor(color: Int) {
        view?.findViewById<MaterialCardView>(R.id.actionButtonCard)?.apply {
            context?.getColor(color)?.let {
                setCardBackgroundColor(it)
            }
        }
    }

    fun setBackground(drawable: Int) {
        view?.findViewById<MaterialCardView>(R.id.actionButtonCard)?.apply {
            setBackgroundResource(drawable)
        }
    }

    fun setCornersRadius(radius: Int) {
        view?.findViewById<MaterialCardView>(R.id.actionButtonCard)?.apply {
            setRadius(radius.toFloat())
        }
    }

    enum class DrawablePosition { START, TOP, END, BOTTOM }

    fun addDrawable(drawable: Int, padding: Int, position: DrawablePosition) {
        view?.findViewById<TextView>(R.id.actionButtonText)?.apply {
            when (position) {
                DrawablePosition.START ->
                    setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
                DrawablePosition.TOP ->
                    setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)
                DrawablePosition.END ->
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
                DrawablePosition.BOTTOM ->
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable)
            }
            compoundDrawablePadding = resources.getDimension(padding).toInt()
        }
    }

    fun onClick(onClick: (() -> Unit)?) {
        view?.findViewById<MaterialCardView>(R.id.actionButtonCard)?.apply {
            setOnClickListener { onClick?.let { it() } }
        }
    }

    fun startLoading() {
        buttonIsEnabled(false)
        lottiIsEnabled(true)
        view?.findViewById<TextView>(R.id.actionButtonText)?.apply {
            this.text = ""
        }
    }

    fun stopLoading() {
        buttonIsEnabled(true)
        lottiIsEnabled(false)
        view?.findViewById<TextView>(R.id.actionButtonText)?.apply {
            this.text = buttonText
        }
    }

    override fun onDestroyView() {
        lottiIsEnabled(false)
        view?.findViewById<TextView>(R.id.actionButtonText)?.apply {
            this.text = buttonText
        }
        super.onDestroyView()
    }

    fun buttonIsEnabled(isEnable: Boolean) {
        view?.isEnabled = isEnable
        view?.findViewById<MaterialCardView>(R.id.actionButtonCard)?.apply {
            isEnabled = isEnable
        }
    }

    private fun lottiIsEnabled(isEnable: Boolean) {
        view?.findViewById<LottieAnimationView>(R.id.actionButtonLottie)?.apply {
            if (isEnable) {
                playAnimation()
                visibility = View.VISIBLE
                addValueCallback(KeyPath("**"), LottieProperty.COLOR_FILTER) {
                    view?.findViewById<TextView>(R.id.actionButtonText)?.currentTextColor?.let {
                        PorterDuffColorFilter(it, PorterDuff.Mode.SRC_ATOP)
                    }
                }
            } else {
                cancelAnimation()
                visibility = View.GONE
            }
        }
    }
}