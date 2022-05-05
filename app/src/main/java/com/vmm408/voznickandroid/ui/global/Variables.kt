package com.vmm408.voznickandroid.ui.global

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.view.Display
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

fun String.sha256(): String =
    BigInteger(1, MessageDigest.getInstance("SHA256").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

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

fun TextView.setLineSpacingCustom(activity: Activity?) =
    setLineSpacing((getScreenSize(activity).y * 0.001).toFloat(), 1.0f)

fun EditText.setLineSpacingCustom(activity: Activity?) =
    setLineSpacing((getScreenSize(activity).y * 0.001).toFloat(), 1.0f)

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

val Int.sp: Int
    get() = (this / Resources.getSystem().displayMetrics.scaledDensity).toInt()

val Float.sp: Int
    get() = (this / Resources.getSystem().displayMetrics.scaledDensity).toInt()


fun showToast(context: Context?, text: String) =
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()

fun String.setToDate(inFormat: String? = null): Date? =
    formatter(inFormat).parse(this)

fun Date.setToStringFormat(outFormat: String? = null): String =
    formatter(outFormat).format(this)

fun Long.setToStringFormat(outFormat: String? = null): String =
    formatter(outFormat).format(Date(this))

fun String?.changeFormat(outFormat: String, inFormat: String? = null): String =
    formatter(outFormat).format(
        formatter(inFormat).parse(this ?: "") ?: ""
    )

fun formatter(pattern: String? = "yyyy-MM-dd HH:mm:ss") =
    SimpleDateFormat(pattern, Locale.getDefault())

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

