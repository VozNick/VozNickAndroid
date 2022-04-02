package com.vmm408.voznickandroid.ui.global.widgets

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.vmm408.voznickandroid.R
import kotlinx.android.synthetic.main.widget_toolbar.*

class ToolbarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.widget_toolbar, container, false)

    private var titleText: String = ""
    private var rightButtonText: String = ""

    fun showTitle(
        text: String,
        @ColorRes color: Int,
        @FontRes font: Int,
        isVisible: Boolean = true,
        onClickEvent: (() -> Unit)? = null
    ) {
        with(toolbarTitle) {
            this?.isVisible = isVisible
            this?.text = text
            this?.setTextColor(context.getColor(color))
            this?.typeface = ResourcesCompat.getFont(context, font)
            this?.setOnClickListener { onClickEvent?.invoke() }
        }
    }

    fun setBackgroundColor(@ColorRes color: Int) {
        context?.getColor(color)?.let { mainToolbarContainer?.setBackgroundColor(it) }
    }

    fun setBackgroundResource(@DrawableRes drawable: Int) {
        mainToolbarContainer?.setBackgroundResource(drawable)
    }

    fun showStrokeView(isVisible: Boolean = true, @ColorRes color: Int) {
        with(strokeView) {
            this?.isVisible = isVisible
            context?.getColor(color)?.let { this?.setBackgroundColor(it) }
        }
    }

    fun showCenterImage(@DrawableRes drawable: Int) {
        centerImage?.isVisible = true
        centerImage?.setImageResource(drawable)
    }

    fun showLeftButton(
        text: String,
        @ColorRes color: Int,
        @FontRes font: Int,
        onClickEvent: (() -> Unit)? = null,
    ) {
        with(leftTextButton) {
            this?.isVisible = true
            this?.text = text
            this?.setTextColor(context.getColor(color))
            this?.typeface = ResourcesCompat.getFont(context, font)
            this?.setOnClickListener { buttonClick(onClickEvent) }
        }
    }

    fun showLeftButton(@DrawableRes drawable: Int, onClickEvent: (() -> Unit)? = null) {
        with(leftImageButton) {
            this?.isVisible = true
            this?.setImageResource(drawable)
            this?.setOnClickListener { buttonClick(onClickEvent) }
        }
    }

    fun showRightButton(
        text: String,
        @ColorRes color: Int,
        @FontRes font: Int,
        onClickEvent: (() -> Unit)? = null,
    ) {
        with(rightTextButton) {
            rightImageContainer?.isVisible = true
            this?.isVisible = true
            this?.text = text
            this?.setTextColor(context.getColor(color))
            this?.typeface = ResourcesCompat.getFont(context, font)
            this?.setOnClickListener { buttonClick(onClickEvent) }
        }
    }

    fun showRightButton(drawable: IntArray, vararg onClickEvent: (() -> Unit)?) {
        rightImageContainer?.isVisible = true
        drawable.forEachIndexed { index, i ->
            ImageView(context).apply {
                setImageResource(i)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }.apply {
                setOnClickListener { buttonClick(onClickEvent[index]) }
                rightImageContainer?.addView(this)
            }
        }
    }

    private fun buttonClick(onClickEvent: (() -> Unit)?) {
        if (null == onClickEvent) {
            hideKeyboard()
            activity?.supportFragmentManager?.popBackStack()
        } else onClickEvent()
    }

    private fun hideKeyboard() {
        try {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun startLoadingRightButton() {
        view?.isEnabled = false
        rightButtonText = rightTextButton?.text.toString()
        rightTextContainer?.isEnabled = false
        rightTextButton?.text = ""
        rightButtonLottie?.playAnimation()
        rightButtonLottie?.visibility = View.VISIBLE
        rightButtonLottie?.addValueCallback(
            KeyPath("**"), LottieProperty.COLOR_FILTER, {
                rightTextButton?.currentTextColor?.let { it1 ->
                    PorterDuffColorFilter(it1, PorterDuff.Mode.SRC_ATOP)
                }
            })
    }

    fun stopLoadingRightButton() {
        view?.isEnabled = true
        rightTextContainer?.isEnabled = true
        rightButtonLottie?.cancelAnimation()
        rightButtonLottie?.visibility = View.GONE
        rightTextButton?.text = rightButtonText
    }

    override fun onDestroyView() {
        rightButtonLottie?.cancelAnimation()
        rightButtonLottie?.visibility = View.GONE
        super.onDestroyView()
    }
}
