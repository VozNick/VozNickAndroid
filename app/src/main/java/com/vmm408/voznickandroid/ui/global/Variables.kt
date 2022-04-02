package com.vmm408.voznickandroid.ui.global

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.view.Display
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.inSpans
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.random.Random

const val STATE_SCOPE_NAME = "state_scope_name"

const val API_INTERNET_ERROR = "api_internet_error"
const val API_SERVER_ERROR = "api_server_error"
var userToken: String? = null
fun String.sha256(): String {
    val md = MessageDigest.getInstance("SHA256")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun String?.isPasswordValid(): Boolean = Pattern.compile(
    "^" +
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[0-9])" +         //at least 1 digit
//            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$"
).matcher(this ?: "").matches()

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"

fun getStatusBarHeight(context: Context): Int {
    var result = 0
    val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) result = context.resources.getDimensionPixelSize(resourceId)
    return result
}

fun getScreenHeight(activity: Activity?): Int = getScreenSize(activity).y
fun getScreenWidth(activity: Activity?): Int = getScreenSize(activity).x

fun TextView.setLineSpacingCustom(activity: Activity?) {
    setLineSpacing((getScreenSize(activity).y * 0.001).toFloat(), 1.0f)
}

fun EditText.setLineSpacingCustom(activity: Activity?) {
    setLineSpacing((getScreenSize(activity).y * 0.001).toFloat(), 1.0f)
}

private fun getScreenSize(activity: Activity?): Point {
    val display: Display? = activity?.windowManager?.defaultDisplay
    val size = Point()
    display?.getSize(size)
    return size
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()


val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()


fun showToast(context: Context?, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

fun String?.setFormat(outFormat: String, inFormat: String = "yyyy-MM-dd HH:mm:ss"): String =
    SimpleDateFormat(outFormat, Locale.getDefault()).format(
        SimpleDateFormat(inFormat, Locale.getDefault()).parse(this ?: "") ?: ""
    )

fun String?.setToDate(inFormat: String = "yyyy-MM-dd HH:mm:ss"): Date? =
    SimpleDateFormat(inFormat, Locale.getDefault()).parse(this ?: "")

fun Date?.setToSimpleFormat(outFormat: String = "yyyy-MM-dd HH:mm:ss"): String =
    SimpleDateFormat(outFormat, Locale.getDefault()).format(this ?: Date()) ?: ""

fun Long.setToSimpleFormat(outFormat: String = "yyyy-MM-dd HH:mm:ss"): String =
    SimpleDateFormat(outFormat, Locale.getDefault()).format(Date(this)) ?: ""

fun randomInt() = Random(Calendar.getInstance().timeInMillis).nextInt()

//fun EditText?.setOnFocusListener() {
//    this?.onFocusChangeListener = View.OnFocusChangeListener { view, p1 ->
//        (view?.parent as? MaterialCardView)?.isSelected = p1
//        if (p1)
//            (view?.parent?.parent as? ShadowLayout)?.shadowConfig?.setShadowColorRes(R.color.input_shadow_231F20_10)
//        else (view?.parent?.parent as? ShadowLayout)?.shadowConfig?.setShadowColorRes(android.R.color.transparent)
//    }
//}

//fun TextInputEditText?.setOnFocusListener() {
//    this?.onFocusChangeListener = View.OnFocusChangeListener { view, p1 ->
//        (view?.parent as? TextInputLayout)?.isSelected = p1
//    }
//}

fun TextView?.initPrivacyText() {
    this?.text = SpannableStringBuilder()
        .append("By signing in, you accept to our ")
        .inSpans(
            URLSpan("https://limebee.pino.pp.ua/terms-and-conditions"),
            UnderlineSpan(),
            ForegroundColorSpan(Color.parseColor("#65B120"))
        ) { append("Privacy Policy") }
        .append(" and ")
        .inSpans(
            URLSpan("https://limebee.pino.pp.ua/privacy-policy"),
            UnderlineSpan(),
            ForegroundColorSpan(Color.parseColor("#65B120"))
        ) { append("Terms of Services") }
        .append(".")
    this?.linksClickable = true
    this?.movementMethod = LinkMovementMethod.getInstance()
}
