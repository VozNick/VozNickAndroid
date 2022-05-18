package com.vmm408.voznickandroid.ui.global

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.di.DI.APP_SCOPE
import com.vmm408.voznickandroid.network.response.Base
import com.vmm408.voznickandroid.ui.global.mvp.BaseView
import io.realm.Realm
import kotlinx.android.synthetic.main.view_base_alert.*
import kotlinx.android.synthetic.main.view_base_alert.view.*
import toothpick.Scope
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

open class BaseBottomSheetFragment : BottomSheetDialogFragment(), BaseView {

    protected lateinit var scope: Scope
        private set
    protected open val parentScopeName: String = APP_SCOPE
    protected val realm: Realm by inject()
    private var isAlertShowing = false
    override fun onCreate(savedInstanceState: Bundle?) {
        scope = createScope()
        installModules(scope)
        Toothpick.inject(this, scope)
        super.onCreate(savedInstanceState)
        this.isCancelable = false
    }

    protected fun replace(@IdRes hostId: Int, fragment: BaseFragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(hostId, fragment, fragment.TAG)
            ?.addToBackStack(fragment.TAG)
            ?.commit()
    }

    protected fun add(@IdRes hostId: Int, fragment: BaseFragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(hostId, fragment, fragment.TAG)
            ?.addToBackStack(fragment.TAG)
            ?.commit()
    }

    protected open fun createScope(): Scope {
        return Toothpick.openScopes(parentScopeName, objectScopeName())
    }

    protected open fun installModules(scope: Scope) {}

    protected fun hideKeyboard() {
        try {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnKeyListener { _: DialogInterface, keyCode: Int, keyEvent: KeyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {

                    dismiss()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        hideKeyboard()
        super.onDestroy()
    }

    override fun apiError(error: String, apiException: Base?, function: (() -> Unit)?) {
        if (isAlertShowing) return
        val listener = object : AlertListener {
            override fun actionPositive() {
                isAlertShowing = false
                function?.invoke()
            }

            override fun actionNegative() {
                isAlertShowing = false
            }
        }
        activity?.let { activity ->
            when (error) {
                API_INTERNET_ERROR -> DialogHelper.showBaseAlert(
                    activity,
                    title = apiException?.title,
                    subtitle = getString(R.string.check_internet_connection),
                    positiveText = getString(R.string.re_try),
                    alertListener = listener
                )
                API_SERVER_ERROR -> {
                    if (null == apiException?.message) {
                        DialogHelper.showBaseAlert(
                            activity,
                            subtitle = apiException?.title,
                            positiveText = getString(R.string.ok_label),
                            alertListener = listener
                        )
                    } else {
                        DialogHelper.showBaseAlert(
                            activity,
                            title = apiException.title,
                            subtitle = apiException.message,
                            positiveText = getString(R.string.ok_label),
                            alertListener = listener
                        )
                    }
                }
            }
            isAlertShowing = true
        }
    }
}