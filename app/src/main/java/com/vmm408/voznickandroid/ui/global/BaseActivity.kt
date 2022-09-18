package com.vmm408.voznickandroid.ui.global

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vmm408.voznickandroid.App
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.di.DI
import com.vmm408.voznickandroid.helper.AlertListener
import com.vmm408.voznickandroid.helper.RealmHelper
import com.vmm408.voznickandroid.helper.showBaseAlert
import com.vmm408.voznickandroid.lifecycle.LifecycleObserver
import com.vmm408.voznickandroid.model.LocalizationEvent
import com.vmm408.voznickandroid.model.LogoutEvent
import com.vmm408.voznickandroid.model.NetworkStateEvent
import com.vmm408.voznickandroid.network.response.Base
import com.vmm408.voznickandroid.ui.SplashActivity
import com.vmm408.voznickandroid.ui.global.mvp.BaseView
import com.vmm408.voznickandroid.ui.objectScopeName
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import toothpick.Scope
import toothpick.Toothpick

abstract class BaseActivity : AppCompatActivity(), LifecycleObserver, BaseView {
    abstract val layoutRes: Int

    protected open val parentScopeName: String = DI.APP_SCOPE
    protected lateinit var scope: Scope
        private set

    private val delegate = object : LayoutChangeListener.Delegate {
        private val uiHandler = Handler(Looper.getMainLooper())

        override fun layoutDidChange(oldHeight: Int, newHeight: Int, tempBottom: Int) {
            uiHandler.post {
                resources?.getDimension(R.dimen.navigation_height)?.toInt()?.let {
                    if (tempBottom <= it) return@post
                }
                keyboardStateChanged(newHeight > oldHeight)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scope = Toothpick.openScopes(parentScopeName, objectScopeName()).apply { }
        installModules(scope)
        Toothpick.inject(this, scope)

        LayoutInflater.from(this).inflate(layoutRes, null).apply {
            setContentView(this)
            this?.addOnLayoutChangeListener(LayoutChangeListener().also { it.delegate = delegate })
        }

        lifecycle.addObserver(this)
        hideKeyboard()

        supportFragmentManager.addOnBackStackChangedListener {
            println("** start **")
            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                println(supportFragmentManager.getBackStackEntryAt(i).name.toString())
            }
            println("** end **")
            println()
        }
    }

    protected open fun installModules(scope: Scope) {}

    protected fun replace(
        @IdRes hostId: Int,
        fragment: BaseFragment,
        isAddToBackStack: Boolean = true
    ) = supportFragmentManager.beginTransaction()
        .replace(hostId, fragment, fragment.TAG)
        .apply { if (isAddToBackStack) addToBackStack(fragment.TAG) }
        .commit()

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

    open fun setLocalization() {}

    override fun disconnectListener() {
        hideKeyboard()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: LogoutEvent) {
        logOutUser()
    }

    internal fun logOutUser() {
        RealmHelper.removeSensitiveDataFromBase()
        (application as App).updateAppScope()
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: NetworkStateEvent) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: LocalizationEvent) {
        setLocalization()
    }

    override fun apiError(error: String, apiException: Base?, function: (() -> Unit)?) {
        val listener = object : AlertListener {
            override fun actionPositive() {
                function?.invoke()
            }

            override fun actionNegative() {}
        }
        when (error) {
            API_INTERNET_ERROR -> showBaseAlert(
                this,
                title = apiException?.title,
                subtitle = getString(R.string.check_internet_connection),
                positiveText = getString(R.string.re_try),
                alertListener = listener
            )
            API_SERVER_ERROR -> showBaseAlert(
                this,
                title = apiException?.title,
                subtitle = apiException?.message,
                positiveText = getString(R.string.ok_label),
                alertListener = listener
            )
        }
    }
}