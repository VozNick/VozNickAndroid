package com.vmm408.voznickandroid.ui.global

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.di.DI.APP_SCOPE
import com.vmm408.voznickandroid.helper.AlertListener
import com.vmm408.voznickandroid.helper.showBaseAlert
import com.vmm408.voznickandroid.lifecycle.LifecycleObserver
import com.vmm408.voznickandroid.model.LocalizationEvent
import com.vmm408.voznickandroid.model.NetworkStateEvent
import com.vmm408.voznickandroid.network.response.Base
import com.vmm408.voznickandroid.ui.getStatusBarHeight
import com.vmm408.voznickandroid.ui.global.mvp.BaseView
import com.vmm408.voznickandroid.ui.global.widgets.ActionButtonFragment
import com.vmm408.voznickandroid.ui.objectScopeName
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import toothpick.Scope
import toothpick.Toothpick
import toothpick.ktp.KTP

abstract class BaseFragment : Fragment(), BaseView, LifecycleObserver {
    abstract val layoutRes: Int
    abstract val TAG: String

    protected open val parentScopeName: String by lazy {
        (parentFragment as? BaseFragment)?.fragmentScopeName ?: APP_SCOPE
    }
    private lateinit var fragmentScopeName: String
    protected lateinit var scope: Scope
        private set

    private var instanceStateSaved: Boolean = false

    private val delegate = object : LayoutChangeListener.Delegate {
        private val uiHandler = Handler(Looper.getMainLooper())

        override fun layoutDidChange(oldHeight: Int, newHeight: Int, tempBottom: Int) {
            uiHandler.post {
                context?.resources?.getDimension(R.dimen.navigation_height)?.toInt()?.let {
                    if (tempBottom <= it) return@post
                }
                keyboardStateChanged(newHeight > oldHeight)
            }
        }
    }

    open fun keyboardStateChanged(isShown: Boolean) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()
        if (Toothpick.isScopeOpen(fragmentScopeName)) {
            scope = Toothpick.openScope(fragmentScopeName)
        } else {
            scope = Toothpick.openScopes(parentScopeName, fragmentScopeName)
            installModules(scope)
        }
        Toothpick.inject(this, scope)

        lifecycle.addObserver(this)
        super.onCreate(savedInstanceState)
    }

    protected open fun installModules(scope: Scope) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addOnLayoutChangeListener(LayoutChangeListener().also { it.delegate = delegate })
        view.findViewById<ViewGroup>(R.id.rootView)?.setOnClickListener { hideKeyboard() }
    }

    override fun onStart() {
        super.onStart()
//        EventBus.getDefault().register(this)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun connectListener() {
        setLocalization()
    }

    open fun setLocalization() {}

    override fun onPause() {
        super.onPause()
    }

    override fun disconnectListener() {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) KTP.closeScope(scope.name)
    }

    private fun needCloseScope(): Boolean = when {
        activity?.isChangingConfigurations == true -> false
        activity?.isFinishing == true -> true
        else -> isRealRemoving()
    }

    private fun isRealRemoving(): Boolean =
        isRemoving || (parentFragment as? BaseFragment)?.isRealRemoving() ?: false

    fun addTopMarginForFragment() {
        context?.let { c ->
            (view?.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin =
                getStatusBarHeight(c)
        }
    }

    fun Activity.setStatusBarNormal() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    fun Activity.setFullScreen() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    protected fun replace(
        @IdRes hostId: Int,
        fragment: BaseFragment,
        isAddToBackStack: Boolean = true
    ) = activity?.supportFragmentManager?.beginTransaction()
        ?.replace(hostId, fragment, fragment.TAG)
        ?.setReorderingAllowed(true)
        ?.apply { if (isAddToBackStack) addToBackStack(fragment.TAG) }
        ?.commit()

    protected fun add(
        @IdRes hostId: Int,
        fragment: BaseFragment
    ) = activity?.supportFragmentManager?.beginTransaction()
        ?.add(hostId, fragment, fragment.TAG)
        ?.setReorderingAllowed(true)
        ?.addToBackStack(fragment.TAG)
        ?.commit()

    protected fun finishFragment() = activity?.supportFragmentManager?.popBackStack()

    protected fun findFragment(fragmentTag: String): Fragment? =
        activity?.supportFragmentManager?.findFragmentByTag(fragmentTag)

    protected fun findWidgetFragment(@IdRes fragmentTag: Int): Fragment? =
        childFragmentManager.findFragmentById(fragmentTag)

    protected fun findButton(@IdRes id: Int): ActionButtonFragment? =
        childFragmentManager.findFragmentById(id) as? ActionButtonFragment

//    protected fun findToolbar(@IdRes id: Int = R.id.toolbarView): ToolbarFragment? =
//        childFragmentManager.findFragmentById(id) as? ToolbarFragment

    private fun hideKeyboard() {
        try {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
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

            override fun actionNegative() {
            }
        }
        when (error) {
            API_INTERNET_ERROR -> showBaseAlert(
                context,
                title = apiException?.title,
                subtitle = getString(R.string.check_internet_connection),
                positiveText = getString(R.string.re_try),
                alertListener = listener
            )
            API_SERVER_ERROR -> showBaseAlert(
                context,
                title = apiException?.title,
                subtitle = apiException?.message,
                positiveText = getString(R.string.ok_label),
                alertListener = listener
            )
        }
    }
}