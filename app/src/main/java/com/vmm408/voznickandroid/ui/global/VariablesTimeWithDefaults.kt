package com.vmm408.voznickandroid.ui.global

import java.text.SimpleDateFormat
import java.util.*

/** date presented as String **/

//fun String.changeFormat(
//    outFormat: String,
//    outTimeZoneId: String,
//    inFormat: String? = null,
//    inTimeZoneId: String? = null
//): String = this.parse(inFormat, inTimeZoneId)?.format(outFormat, outTimeZoneId) ?: ""
//
//fun String.setToTimeInMillis(
//    inFormat: String? = null,
//    inTimeZoneId: String? = null
//): Long = this.setToCalendar(inFormat, inTimeZoneId)?.timeInMillis ?: 0
//
//fun String.setToCalendar(
//    inFormat: String? = null,
//    inTimeZoneId: String? = null
//): Calendar? {
//    val date = this.setToDate(inFormat, inTimeZoneId)
//
//    return if (null == date) null
//    else Calendar.getInstance().apply { time = date }
//}
//
//fun String.setToDate(
//    inFormat: String? = null,
//    inTimeZoneId: String? = null
//): Date? = this.parse(inFormat, inTimeZoneId)
//
//
///** date presented as Long **/
//
//fun Long.setToStringFormat(
//    outFormat: String? = null,
//    outTimeZoneId: String? = null
//): String = Date(this).format(outFormat, outTimeZoneId) ?: ""
//
//fun Long.setToCalendar(): Calendar =
//    Calendar.getInstance().apply { timeInMillis = this@setToCalendar }
//
//fun Long.setToDate(): Date = Date(this)


/** date presented as Date **/

//fun Date.setToStringFormat(
//    outFormat: String? = null,
//    outTimeZoneId: String? = null
//): String = this.format(outFormat, outTimeZoneId) ?: ""
//
//fun Date.setToCalendar(
//    outFormat: String? = null,
//    outTimeZoneId: String? = null
//): Calendar? = Calendar.getInstance().apply { time = this@setToCalendar }
//
//
///** SimpleDateFormat with timeZoneId, default format = "yyyy-MM-dd HH:mm:ss" **/
//
//fun Date.format(outFormat: String?, timeZoneId: String?): String? =
//    getSimpleFormat(outFormat, timeZoneId)?.format(this)
//
//fun String.parse(inFormat: String?, timeZoneId: String?): Date? =
//    getSimpleFormat(inFormat, timeZoneId)?.parse(this)
//
//
///** SimpleDateFormat, default format = "yyyy-MM-dd HH:mm:ss" **/
//
//fun getSimpleFormat(pattern: String?, timeZoneId: String?): SimpleDateFormat? {
//    return try {
//        SimpleDateFormat(pattern ?: "yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//            .apply {
//                if (null != timeZoneId && timeZoneId.isNotEmpty()) {
//                    timeZone = TimeZone.getTimeZone(timeZoneId)
//                }
//            }
//    } catch (e: Exception) {
//        null
//    }
//}
//
//
///** Seconds to Hours, Minutes, Seconds **/
//
//fun Long.toStringHoursMinutesSeconds() =
//    this.toHoursMinutesSeconds().let { String.format("%02d:%02d:%02d", it[0], it[1], it[2]) }
//
//fun Long.toHoursMinutesSeconds() = arrayOf(this / 3600, (this % 3600) / 60, this % 60)


/** TEMP **/

//package com.vmm408.voznickandroid.ui.global
//
//import java.text.SimpleDateFormat
//import java.util.*
//
///** date presented as String **/
//
//fun String.changeFormat(outFormat: String, inFormat: String? = null): String? =
//    this.parse(inFormat)?.format(outFormat)
//
//fun String.setToTimeInMillis(inFormat: String? = null): Long? =
//    this.setToCalendar(inFormat)?.timeInMillis
//
//fun String.setToCalendar(inFormat: String? = null): Calendar? =
//    this.setToDate(inFormat)?.let { date -> Calendar.getInstance().apply { time = date } }
//
//fun String.setToDate(inFormat: String? = null): Date? = this.parse(inFormat)
//
//
///** date presented as Long **/
//
//fun Long.setToStringFormat(outFormat: String? = null): String = Date(this).format(outFormat)
//
//fun Long.setToCalendar(): Calendar =
//    Calendar.getInstance().apply { time = this@setToCalendar.setToDate() }
//
//fun Long.setToDate(): Date = Date(this)
//
//
///** date presented as Date **/
//
//fun Date.setToStringFormat(outFormat: String? = null): String = this.format(outFormat)
//
//
///** SimpleDateFormat with timeZoneId, default format = "yyyy-MM-dd HH:mm:ss" **/
//
//fun Date.format(outFormat: String?, timeZoneId: String): String = outFormat.simpleFormat()
//    .apply { timeZone = TimeZone.getTimeZone(timeZoneId) }
//    .format(this)
//
//fun String.parse(inFormat: String?, timeZoneId: String): Date? = inFormat.simpleFormat()
//    .apply { timeZone = TimeZone.getTimeZone(timeZoneId) }
//    .parse(this)
//
//
///** SimpleDateFormat, default format = "yyyy-MM-dd HH:mm:ss" **/
//
//fun Date.format(outFormat: String?): String = outFormat.simpleFormat().format(this)
//fun String.parse(inFormat: String?): Date? = inFormat.simpleFormat().parse(this)
//
//fun String?.simpleFormat() = SimpleDateFormat(this ?: "yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//
//fun getSimpleFormat(pattern: String?, timeZoneId: String?): SimpleDateFormat? {
//    return try {
//        SimpleDateFormat(pattern ?: "yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//            .apply {
//                if (null != timeZoneId && timeZoneId.isNotEmpty()) {
//                    timeZone = TimeZone.getTimeZone(timeZoneId)
//                }
//            }
//    } catch (e: Exception) {
//        null
//    }
//}
//
///** Seconds to Hours, Minutes, Seconds **/
//
//fun Long.toStringHoursMinutesSeconds() =
//    this.toHoursMinutesSeconds().let { String.format("%02d:%02d:%02d", it[0], it[1], it[2]) }
//
//fun Long.toHoursMinutesSeconds() = arrayOf(this / 3600, (this % 3600) / 60, this % 60)
//
///** convert time **/
//
//fun String.convertTime(fromLocalId: String, toLocalId: String): String? =
//    this.parse(null, fromLocalId)?.format(null, toLocalId)
//
//fun convertDate(dateCarent: String, idLocation: String): String? {
//    val sourceFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    val parsed = sourceFormat.parse(dateCarent)
//    val destFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    destFormat.timeZone = TimeZone.getTimeZone(idLocation)
//
//    return destFormat.format(parsed)
//}