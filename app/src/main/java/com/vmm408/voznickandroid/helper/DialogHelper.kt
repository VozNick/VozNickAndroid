package com.vmm408.voznickandroid.helper

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.vmm408.voznickandroid.R

var isAlertShowing = false

interface AlertListener {
    fun actionNegative() {}
    fun actionPositive() {}
}

//object DialogHelper {
fun showBaseAlert(
    context: Context?,
    icon: String? = null,
    title: String? = null,
    subtitle: String? = null,
    negativeText: String? = null,
    positiveText: String? = null,
    alertListener: AlertListener? = null
) {
    if (isAlertShowing || null == context) return

    val alert = AlertDialog.Builder(context).create()

    val view = LayoutInflater.from(context).inflate(R.layout.view_base_alert, null)?.apply {

        findViewById<TextView>(R.id.iconField)?.apply {
            text = icon ?: run { isVisible = false; "" }
        }
        findViewById<TextView>(R.id.titleField)?.apply {
            text = title ?: run { isVisible = false; "" }
        }
        findViewById<TextView>(R.id.subtitleField)?.apply {
            text = subtitle ?: run { isVisible = false; "" }
        }
        findViewById<TextView>(R.id.negativeButton)?.apply {
            text = negativeText ?: run { isVisible = false; "" }
            setOnClickListener {
                alert.dismiss()
                isAlertShowing = false
                alertListener?.actionNegative()
            }
        }
        findViewById<TextView>(R.id.positiveButton)?.apply {
            text = positiveText ?: run { isVisible = false; "" }
            setOnClickListener {
                alert.dismiss()
                isAlertShowing = false
                alertListener?.actionPositive()
            }
        }
    }

    alert.setView(view)
    alert.setCancelable(false)
    alert.create()
    alert.window?.setBackgroundDrawableResource(android.R.color.transparent)
    alert.show()

    isAlertShowing = true
}
//}