package com.vmm408.voznickandroid.ui.global

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.vmm408.voznickandroid.R
import kotlinx.android.synthetic.main.view_base_alert.view.*

interface AlertListener {
    fun actionNegative() {}
    fun actionPositive() {}
}

object DialogHelper {
    fun showBaseAlert(
        context: Activity,
        icon: String? = null,
        title: String? = null,
        subtitle: String? = null,
        negativeText: String? = null,
        positiveText: String? = null,
        alertListener: AlertListener? = null
    ) {
        LayoutInflater.from(context).inflate(R.layout.view_base_alert, null).apply {
            if (icon == null) this?.icon?.visibility = View.GONE
            else this?.icon?.text = icon

            if (title == null) this?.title?.visibility = View.GONE
            else this?.title?.text = title

            if (subtitle == null) this?.subtitle?.visibility = View.GONE
            else this?.subtitle?.text = subtitle

            if (negativeText == null) this?.negative?.visibility = View.GONE
            else this?.negative?.text = negativeText

            if (positiveText == null) this?.positive?.visibility = View.GONE
            else this?.positive?.text = positiveText

            this?.negative?.setOnClickListener { alertListener?.actionNegative() }
            this?.positive?.setOnClickListener { alertListener?.actionPositive() }
        }.let {
            AlertDialog.Builder(context).create().apply {
                setView(it)
                setCancelable(false)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                show()
            }
        }
    }
}