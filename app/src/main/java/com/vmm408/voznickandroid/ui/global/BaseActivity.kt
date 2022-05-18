package com.vmm408.voznickandroid.ui.global

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.di.DI
import com.vmm408.voznickandroid.lifecycle.LifecycleObserver
import com.vmm408.voznickandroid.network.response.Base
import com.vmm408.voznickandroid.ui.global.mvp.BaseView
import io.realm.Realm
import kotlinx.android.synthetic.main.view_base_alert.*
import kotlinx.android.synthetic.main.view_base_alert.view.*
import toothpick.Scope
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

abstract class BaseActivity : AppCompatActivity(), LifecycleObserver, BaseView {
    abstract val layoutRes: Int
    protected val realm: Realm by inject()
    protected open val parentScopeName: String = DI.APP_SCOPE
    protected lateinit var scope: Scope
        private set
    private var isAlertShowing = false

    private val delegate = object : LayoutChangeListener.Delegate {
        private val uiHandler = Handler(Looper.getMainLooper())

        override fun layoutDidChange(oldHeight: Int, newHeight: Int, tempBottom: Int) {
            uiHandler.post {
                if (tempBottom <= resources?.getDimension(R.dimen.navigation_height)?.toInt() ?: 82
                ) {
                    return@post
                }
                keyboardStateChanged(newHeight > oldHeight)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scope = createScope()
        installModules(scope)
        Toothpick.inject(this, scope)
        LayoutInflater.from(this).inflate(layoutRes, null).apply {
            setContentView(this)
            this?.addOnLayoutChangeListener(LayoutChangeListener().also { it.delegate = delegate })
        }
        lifecycle.addObserver(this)
        hideKeyboard()
    }

    protected open fun createScope(): Scope {
        return Toothpick.openScopes(parentScopeName, objectScopeName())
    }

    protected open fun installModules(scope: Scope) {}

    protected fun replace(
        @IdRes hostId: Int,
        fragment: BaseFragment,
        isAddToBackStack: Boolean = true
    ) {
        if (supportFragmentManager?.findFragmentByTag(fragment.TAG) == null) {
            val trans = supportFragmentManager.beginTransaction()
                .replace(hostId, fragment, fragment.TAG)
            if (isAddToBackStack) {
                trans.addToBackStack(fragment.TAG)
            }
            trans.commit()
        }
    }

    protected fun findFragment(fragmentTag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(fragmentTag)

    private fun hideKeyboard() {
        try {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(window?.decorView?.windowToken, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    open fun keyboardStateChanged(isShown: Boolean) {}
    fun setStatusBarColor(@ColorRes colorId: Int) {
        window.statusBarColor = resources.getColor(colorId)
    }

    override fun connectListener() {
        setLocalization()
    }

    override fun disconnectListener() {
        hideKeyboard()
    }

    open fun setLocalization() {}

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
        when (error) {
            API_INTERNET_ERROR -> DialogHelper.showBaseAlert(
                this,
                title = apiException?.title,
                subtitle = getString(R.string.check_internet_connection),
                positiveText = getString(R.string.re_try),
                alertListener = listener
            )
            API_SERVER_ERROR -> {
                if (null == apiException?.message) {
                    DialogHelper.showBaseAlert(
                        this,
                        subtitle = apiException?.title,
                        positiveText = getString(R.string.ok_label),
                        alertListener = listener
                    )
                } else {
                    DialogHelper.showBaseAlert(
                        this,
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