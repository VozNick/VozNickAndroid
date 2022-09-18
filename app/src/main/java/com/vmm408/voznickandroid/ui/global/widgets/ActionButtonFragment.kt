package com.vmm408.voznickandroid.ui.global.widgets

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.google.android.material.card.MaterialCardView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.dp

//const val ACTION_BUTTON_CARD_VIEW = 100001
//const val ACTION_BUTTON_TEXT_VIEW = 100002
//const val ACTION_BUTTON_LOTTI = 100003

open class ActionButtonFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.widget_button, container, false)


//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
//
//        val textView = TextView(context).apply {
//            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//            ellipsize = TextUtils.TruncateAt.END
//            gravity = Gravity.CENTER
//            maxLines = 1
//
//        }
//
//
//        val view = FrameLayout(requireContext()).apply {
//            layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
//            layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
//        }
//
//    }

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

//class ActionButton : FrameLayout {
//    constructor(context: Context?) : super(context!!)
//
//    constructor(context: Context?, attrs: AttributeSet) : super(context!!, attrs)
//
////    private var card: MaterialCardView? = null
////    private var frameLayout: FrameLayout? = null
////    private var textView: TextView? = null
////    private val lotti = LottieAnimationView(context)
////
////    internal var text = ""
////    internal var textColor = 0
////    internal var textSize = 0
////    internal var textFont = 0
////    internal var textIsAllCaps = 0
////
////    internal var strokeColor = 0
////    internal var strokeWidth = 0
////
////    internal var cardBackgroundColor = 0
////    internal var cardBackgroundDrawable = 0
////
////    internal var cardRadius = 0
////
////    internal var drawable = 0
////    internal var drawablePosition = DrawablePosition.START
//
//    init {
//        layoutParams.height = LayoutParams.MATCH_PARENT
//        layoutParams.width = LayoutParams.MATCH_PARENT
//        addView(CardView(context))
//    }
//
////    inner class CardView(context: Context?) : MaterialCardView(context) {
////
////        init {
////            layoutParams.height = LayoutParams.MATCH_PARENT
////            layoutParams.width = LayoutParams.MATCH_PARENT
////            radius = 100.dp.toFloat()
////            cardElevation = 0.dp.toFloat()
////
////            addView(FrameLayout(context))
////        }
////
////        inner class FrameLayout(context: Context?) : android.widget.FrameLayout(context!!) {
////
////            init {
////                layoutParams.height = LayoutParams.MATCH_PARENT
////                layoutParams.width = LayoutParams.MATCH_PARENT
////
////                addView(ActionButtonTextView(context))
////            }
////
////            inner class ActionButtonTextView(context: Context?) :
////                androidx.appcompat.widget.AppCompatTextView(context!!) {
////
////                init {
////                    id = ACTION_BUTTON_TEXT_VIEW
////                    layoutParams.height = LayoutParams.MATCH_PARENT
////                    layoutParams.width = LayoutParams.MATCH_PARENT
////                    ellipsize = TextUtils.TruncateAt.END
////                    gravity = Gravity.CENTER
////                    maxLines = 1
////                    setPadding(16.dp, 0, 16.dp, 0)
////                    textSize = 14.sp.toFloat()
////                }
////            }
////
////            inner class ActionButtonLotti(context: Context?) : LottieAnimationView(context) {
////
////                init {
////                    id = ACTION_BUTTON_LOTTI
////                    layoutParams.height = 30.dp
////                    layoutParams.width = 30.dp
////                    (layoutParams as? LayoutParams)?.gravity = Gravity.CENTER
////                   visibility = ViewGroup.GONE
////
////                }
////            }
////        }
////
//////        android:id="@+id/actionButtonLottie"
//////        android:layout_width="30dp"
//////        android:layout_height="30dp"
//////        android:layout_gravity="center"
//////        android:visibility="gone"
//////        app:lottie_autoPlay="true"
//////        app:lottie_loop="true"
//////        app:lottie_rawRes="@raw/loading" />
////    }
//}